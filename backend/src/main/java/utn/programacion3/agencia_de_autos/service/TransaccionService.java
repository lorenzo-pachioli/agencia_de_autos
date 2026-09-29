package utn.programacion3.agencia_de_autos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utn.programacion3.agencia_de_autos.dto.request.TransaccionFilterDTO;
import utn.programacion3.agencia_de_autos.dto.request.TransaccionRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.TransaccionBalanceResponseDTO;
import utn.programacion3.agencia_de_autos.dto.response.TransaccionComisionResponseDTO;
import utn.programacion3.agencia_de_autos.dto.response.TransaccionResponseDTO;
import utn.programacion3.agencia_de_autos.exception.ResourceNotFoundException;
import utn.programacion3.agencia_de_autos.exception.TransaccionNoModificableException;
import utn.programacion3.agencia_de_autos.exception.VehiculoNoDisponibleException;
import utn.programacion3.agencia_de_autos.mapper.TransaccionMapper;
import utn.programacion3.agencia_de_autos.model.BalanceProyeccion;
import utn.programacion3.agencia_de_autos.model.Transaccion;
import utn.programacion3.agencia_de_autos.model.Usuario;
import utn.programacion3.agencia_de_autos.model.Vehiculo;
import utn.programacion3.agencia_de_autos.model.enums.EstadoTransaccion;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;
import utn.programacion3.agencia_de_autos.model.enums.Rol;
import utn.programacion3.agencia_de_autos.repository.TransaccionRepository;
import utn.programacion3.agencia_de_autos.repository.TransaccionSpecification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final TransaccionMapper transaccionMapper;
    private final UsuarioService usuarioService;
    private final VehiculoServiceImpl vehiculoService;
    private final AuditoriaTransaccionService auditoriaTransaccionService;


    public static final BigDecimal comision_vendedores = new BigDecimal("0.05");   // 5%

    public static BigDecimal calcularComision(BigDecimal precio_final){
        return precio_final.multiply(TransaccionService.comision_vendedores);
    }


    public Page<TransaccionResponseDTO> listarConFiltros(TransaccionFilterDTO filtros, Pageable pageable) {
        return transaccionRepository.findAll(TransaccionSpecification.conFiltros(filtros), pageable)
                .map(transaccionMapper::toResponseDTO);
    }


    @Transactional
    public TransaccionResponseDTO crear(TransaccionRequestDTO dto) {

        Usuario cliente = usuarioService.buscarEntityPorId(dto.getCliente_id());
        Usuario vendedor = usuarioService.buscarEntityPorId(dto.getVendedor_id());
        Vehiculo vehiculo = vehiculoService.obtenerVehiculoEntityPorId(dto.getVehiculo_id());

        if (vehiculo.getEstado() != EstadoVehiculo.DISPONIBLE)
            throw new VehiculoNoDisponibleException("El vehiculo requerido no está disponoble");

        Transaccion transaccion = transaccionMapper.toEntity(dto);
        transaccion.setCliente(cliente);
        transaccion.setVehiculo(vehiculo);
        transaccion.setVendedor(vendedor);

        vehiculoService.cambiarEstadoEntity(dto.getVehiculo_id(), EstadoVehiculo.RESERVADO);
        Transaccion transaccionGuardada = transaccionRepository.save(transaccion);
        auditoriaTransaccionService.registrarCreacion(transaccionGuardada);

        return transaccionMapper.toResponseDTO(transaccionGuardada);
    }

    @Transactional
    public TransaccionResponseDTO actualizar(Long id, TransaccionRequestDTO dto) {

        Transaccion transaccion = buscarEntityPorId(id);
        Transaccion snapshotAnterior = crearSnapshot(transaccion);

        Usuario cliente = usuarioService.buscarEntityPorId(dto.getCliente_id());
        Usuario vendedor = usuarioService.buscarEntityPorId(dto.getVendedor_id());
        Vehiculo vehiculo = vehiculoService.obtenerVehiculoEntityPorId(dto.getVehiculo_id());

        transaccionMapper.updateEntityFromDto(
                dto,
                transaccion
        );
        transaccion.setCliente(cliente);
        transaccion.setVendedor(vendedor);
        transaccion.setVehiculo(vehiculo);

        auditoriaTransaccionService.registrarCambio(snapshotAnterior, transaccion);

        return transaccionMapper.toResponseDTO(transaccionRepository.save(transaccion));
    }


    @Transactional(readOnly = true)
    public TransaccionResponseDTO buscarPorId(Long id) {
        Transaccion transaccion = transaccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaccion no encontrado con el ID: " + id));
        return transaccionMapper.toResponseDTO(transaccion);
    }

    @Transactional(readOnly = true)
    public Transaccion buscarEntityPorId(Long id) {
        return transaccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaccion no encontrado con el ID: " + id));
    }

    @Transactional
    public TransaccionResponseDTO cambiarEstado(Long id, EstadoTransaccion estadoTransaccion, BigDecimal precioFinal) {

        Transaccion transaccion = buscarEntityPorId(id);
        Transaccion snapshotAnterior = crearSnapshot(transaccion);

        // Falta validar que solo lo pueda realizar un vendedor o Admin
        if (transaccion.getEstadoTransaccion() == EstadoTransaccion.CANCELADO) {
            throw new TransaccionNoModificableException(transaccion.getId().toString());
        }

        asignacionCambioDeEstado(transaccion, estadoTransaccion);
        if (precioFinal != null ){
            // IMPORTANTE: LE QUE SE RECIBE SE SUMA O SE REEMPLAZA?
            transaccion.setPrecio_final(precioFinal);
        }

        auditoriaTransaccionService.registrarCambio(snapshotAnterior, transaccion);

        return transaccionMapper.toResponseDTO(
                transaccionRepository.save(transaccion)
        );
    }

    private void asignacionCambioDeEstado(Transaccion transaccion, EstadoTransaccion nuevoEstado) {

        Long vehiculo_id = transaccion.getVehiculo().getId();

        if (transaccion.getEstadoTransaccion() == EstadoTransaccion.VENDIDO) {
            throw new TransaccionNoModificableException("No se puede cambiar el estado de una Transaccion en VENDIDO");
        }

        if (nuevoEstado == EstadoTransaccion.VENDIDO) {
            vehiculoService.cambiarEstadoEntity(vehiculo_id, EstadoVehiculo.VENDIDO);

            // Calcula automaticamente la comision del vendedor cuando se concreta la venta
            transaccion.setComision_calculada(calcularComision(transaccion.getPrecio_final()));

        } else if (nuevoEstado == EstadoTransaccion.CANCELADO) {
            vehiculoService.cambiarEstadoEntity(vehiculo_id, EstadoVehiculo.DISPONIBLE);

        } else {
            vehiculoService.cambiarEstadoEntity(vehiculo_id, EstadoVehiculo.RESERVADO);
        }

        transaccion.setEstadoTransaccion(nuevoEstado);
    }

    public TransaccionComisionResponseDTO comisionPorVendedor(TransaccionFilterDTO filtros) {


        Usuario vendedor = usuarioService.buscarEntityPorId(filtros.getVendedor_id());
        if (vendedor.getRolUsuario() != Rol.VENDEDOR)
            throw new ResourceNotFoundException("El id solicitado no pertenece a un vendedor");

        BigDecimal totalComisiones = transaccionRepository.findAll(TransaccionSpecification.conFiltros(filtros)).stream()
                .map(Transaccion::getComision_calculada)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return TransaccionComisionResponseDTO.builder()
                .id(vendedor.getId())
                .vendedor_id(vendedor.getId())
                .nombre_completo(vendedor.getNombre() + " " + vendedor.getApellido()) // O el campo que uses
                .email(vendedor.getEmail())
                .comision_total(totalComisiones)
                .build();
    }


    @Transactional(readOnly = true)
    public TransaccionBalanceResponseDTO obtenerBalance(LocalDate desde, LocalDate hasta) {


        BalanceProyeccion resultado = transaccionRepository.calcularBalanceEntreFechas(desde, hasta)
                .orElse(null);


        if (resultado == null){
            return TransaccionBalanceResponseDTO.builder()
                    .precios_final_total(BigDecimal.ZERO)
                    .comisiones_total(BigDecimal.ZERO)
                    .costos_vehiculos_vendidos(BigDecimal.ZERO)
                    .ingreso_final(BigDecimal.ZERO)
                    .fecha_desde(desde)
                    .fecha_hasta(hasta)
                    .build();
        }

        BigDecimal precioTotal = resultado.getPrecioFinalTotal() != null ? resultado.getPrecioFinalTotal() : BigDecimal.ZERO;
        BigDecimal comisionesTotal = resultado.getComisionesTotal() != null ? resultado.getComisionesTotal() : BigDecimal.ZERO;
        BigDecimal costosVehiculos = resultado.getCostosVehiculos() != null ? resultado.getCostosVehiculos() : BigDecimal.ZERO;


        BigDecimal ingresoFinal = precioTotal.subtract(costosVehiculos).subtract(comisionesTotal);

        return TransaccionBalanceResponseDTO.builder()
                .precios_final_total(precioTotal)
                .comisiones_total(comisionesTotal)
                .costos_vehiculos_vendidos(costosVehiculos)
                .ingreso_final(ingresoFinal)
                .fecha_desde(desde)
                .fecha_hasta(hasta)
                .build();
    }

    private Transaccion crearSnapshot(Transaccion transaccion){
        return Transaccion.builder()
                .id(transaccion.getId())
                .estadoTransaccion(transaccion.getEstadoTransaccion())
                .precio_final(transaccion.getPrecio_final())
                .vendedor(transaccion.getVendedor())
                .cliente(transaccion.getCliente())
                .vehiculo(transaccion.getVehiculo())
                .build();
    }
}
