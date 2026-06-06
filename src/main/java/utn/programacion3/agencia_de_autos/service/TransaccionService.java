package utn.programacion3.agencia_de_autos.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utn.programacion3.agencia_de_autos.dto.request.TransaccionFilterDTO;
import utn.programacion3.agencia_de_autos.dto.request.TransaccionRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.TransaccionResponseDTO;
import utn.programacion3.agencia_de_autos.exception.ResourceNotFoundException;
import utn.programacion3.agencia_de_autos.exception.TransaccionNoModificableException;
import utn.programacion3.agencia_de_autos.exception.VehiculoNoDisponibleException;
import utn.programacion3.agencia_de_autos.mapper.TransaccionMapper;
import utn.programacion3.agencia_de_autos.model.Transaccion;
import utn.programacion3.agencia_de_autos.model.Usuario;
import utn.programacion3.agencia_de_autos.model.Vehiculo;
import utn.programacion3.agencia_de_autos.model.enums.EstadoTransaccion;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;
import utn.programacion3.agencia_de_autos.repository.TransaccionRepository;
import utn.programacion3.agencia_de_autos.repository.TransaccionSpecification;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final TransaccionMapper transaccionMapper;
    private final UsuarioService usuarioService;
    private final VehiculoService vehiculoService;


    private final BigDecimal comision_vendedores = new BigDecimal("0.05");   // 5%

    //me falta listar transacciones con filtros, actualizar transaccion,

    public List<TransaccionResponseDTO> listarConFiltros(TransaccionFilterDTO filtros) {
        return transaccionRepository.findAll(TransaccionSpecification.conFiltros(filtros))
                .stream()
                .map(transaccionMapper::toResponseDTO)
                .toList();
    }


    @Transactional
    public TransaccionResponseDTO crear(TransaccionRequestDTO dto){

        Usuario cliente = usuarioService.buscarEntityPorId(dto.getCliente_id());
        Usuario vendedor = usuarioService.buscarEntityPorId(dto.getVendedor_id());
        Vehiculo vehiculo = vehiculoService.obtenerVehiculoEntityPorId(dto.getVehiculo_id());

        if (vehiculo.getEstado() != EstadoVehiculo.DISPONIBLE) throw new VehiculoNoDisponibleException("El vehiculo requerido no está disponoble");

        Transaccion transaccion = transaccionMapper.toEntity(dto);
        transaccion.setCliente(cliente);
        transaccion.setVehiculo(vehiculo);
        transaccion.setVendedor(vendedor);

        vehiculoService.cambiarEstadoEntity(dto.getVehiculo_id(), EstadoVehiculo.RESERVADO);

        return transaccionMapper.toResponseDTO(transaccionRepository.save(transaccion));
    }

    @Transactional
    public TransaccionResponseDTO actualizar(Long id, TransaccionRequestDTO dto){

        Transaccion transaccion = buscarEntityPorId(id);
        Usuario cliente = usuarioService.buscarEntityPorId(dto.getCliente_id());
        Usuario vendedor = usuarioService.buscarEntityPorId(dto.getVendedor_id());
        Vehiculo vehiculo = vehiculoService.obtenerVehiculoEntityPorId(dto.getVehiculo_id());

        transaccionMapper.updateEntityFromDto(
                dto,
                cliente,
                vendedor,
                vehiculo,
                transaccion
        );

        return transaccionMapper.toResponseDTO(transaccionRepository.save(transaccion));
    }

    @Transactional
    public TransaccionResponseDTO cambiarEstado(Long id, EstadoTransaccion estadoTransaccion){

        Transaccion transaccion = buscarEntityPorId(id);

        asignacionCambioDeEstado(transaccion, estadoTransaccion);

        return transaccionMapper.toResponseDTO(
                transaccionRepository.save(transaccion)
        );
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

    private void asignacionCambioDeEstado(Transaccion transaccion, EstadoTransaccion nuevoEstado){

        Long vehiculo_id = transaccion.getVehiculo().getId();

        if (nuevoEstado == EstadoTransaccion.VENDIDO){
            vehiculoService.cambiarEstadoEntity(vehiculo_id, EstadoVehiculo.VENDIDO);

            // Calcula automaticamente la comision del vendedor cuando se concreta la venta
            transaccion.setComision_calculada(
                    transaccion.getPrecio_final().multiply(this.comision_vendedores));

        } else if (nuevoEstado == EstadoTransaccion.CANCELADO) {
            vehiculoService.cambiarEstadoEntity(vehiculo_id, EstadoVehiculo.DISPONIBLE);

        }else {
            vehiculoService.cambiarEstadoEntity(vehiculo_id, EstadoVehiculo.RESERVADO);
        }

        transaccion.setEstadoTransaccion(nuevoEstado);
    }

    @Transactional
    public TransaccionResponseDTO cancelarTransaccion(Long id) {

        Transaccion transaccion = buscarEntityPorId(id);
        // Falta validar que solo lo pueda realizar un vendedor o Admin
        if (transaccion.getEstadoTransaccion() == EstadoTransaccion.CANCELADO){
            throw new TransaccionNoModificableException(transaccion.getId().toString());
        }

        asignacionCambioDeEstado(transaccion, EstadoTransaccion.CANCELADO);

        return  transaccionMapper.toResponseDTO(
                transaccionRepository.save(transaccion)
        );
    }

    @Transactional
    public TransaccionResponseDTO venderOSeniarTransaccion(Long id, BigDecimal precioFinal, EstadoTransaccion estado) {

        Transaccion transaccion = buscarEntityPorId(id);
        // Falta validar que solo lo pueda realizar un vendedor o Admin

        if (transaccion.getEstadoTransaccion() == EstadoTransaccion.CANCELADO){
            throw new TransaccionNoModificableException(transaccion.getId().toString());
        }

        asignacionCambioDeEstado(transaccion, estado);
        transaccion.setPrecio_final(precioFinal);

        return  transaccionMapper.toResponseDTO(
                transaccionRepository.save(transaccion)
        );
    }

}
