package utn.programacion3.agencia_de_autos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utn.programacion3.agencia_de_autos.dto.request.AsignarVisitaRequestDTO;
import utn.programacion3.agencia_de_autos.dto.request.SolicitudTransaccionRequestDTO;
import utn.programacion3.agencia_de_autos.dto.request.TransaccionFilterDTO;
import utn.programacion3.agencia_de_autos.dto.request.TransaccionRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.TransaccionBalanceResponseDTO;
import utn.programacion3.agencia_de_autos.dto.response.TransaccionComisionResponseDTO;
import utn.programacion3.agencia_de_autos.dto.response.TransaccionResponseDTO;
import utn.programacion3.agencia_de_autos.exception.NegocioException;
import utn.programacion3.agencia_de_autos.exception.ResourceNotFoundException;
import utn.programacion3.agencia_de_autos.exception.TransaccionNoModificableException;
import utn.programacion3.agencia_de_autos.exception.VehiculoNoDisponibleException;
import utn.programacion3.agencia_de_autos.mapper.SolicitudTransaccionMapper;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final TransaccionMapper transaccionMapper;
    private final SolicitudTransaccionMapper solicitudTransaccionMapper;
    private final UsuarioService usuarioService;
    private final VehiculoServiceImpl vehiculoService;
    private final AuditoriaTransaccionService auditoriaTransaccionService;
    private final ConfiguracionService configuracionService;


    public static final BigDecimal comision_vendedores = new BigDecimal("0.05");   // 5%
    private static final BigDecimal CIEN = new BigDecimal("100");

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

        validarVehiculoDisponible(vehiculo);

        Transaccion transaccion = transaccionMapper.toEntity(dto);
        transaccion.setCliente(cliente);
        transaccion.setVehiculo(vehiculo);
        transaccion.setVendedor(vendedor);

        EstadoTransaccion tipoOperacion = dto.getEstadoTransaccion();
        registrarOperacion(transaccion, tipoOperacion, dto.getMonto_entregado());
        transaccion.setEstadoTransaccion(tipoOperacion);

        vehiculoService.cambiarEstadoEntity(dto.getVehiculo_id(), tipoOperacion == EstadoTransaccion.VENDIDO
                ? EstadoVehiculo.VENDIDO
                : EstadoVehiculo.RESERVADO);
        Transaccion transaccionGuardada = transaccionRepository.save(transaccion);
        auditoriaTransaccionService.registrarCreacion(transaccionGuardada);
        cancelarOtrasSolicitudes(transaccionGuardada);

        return transaccionMapper.toResponseDTO(transaccionGuardada);
    }

    @Transactional
    public TransaccionResponseDTO solicitar(SolicitudTransaccionRequestDTO dto, String emailCliente) {

        Usuario cliente = usuarioService.buscarEntityPorEmail(emailCliente);
        Vehiculo vehiculo = vehiculoService.obtenerVehiculoEntityPorId(dto.getVehiculo_id());

        if (vehiculo.getEstado() != EstadoVehiculo.DISPONIBLE)
            throw new VehiculoNoDisponibleException("El vehiculo requerido no está disponible");

        if (transaccionRepository.existsByClienteEmailAndEstadoTransaccion(emailCliente, EstadoTransaccion.SOLICITAR))
            throw new NegocioException("Ya tenés una visita pendiente");

        Transaccion solicitud = solicitudTransaccionMapper.toEntity(dto);
        solicitud.setCliente(cliente);
        solicitud.setVehiculo(vehiculo);

        Transaccion solicitudGuardada = transaccionRepository.save(solicitud);
        auditoriaTransaccionService.registrarCreacion(solicitudGuardada);

        return transaccionMapper.toResponseDTO(solicitudGuardada);
    }

    @Transactional
    public TransaccionResponseDTO asignar(Long id, AsignarVisitaRequestDTO dto, String emailVendedor) {

        Transaccion transaccion = buscarEntityPorId(id);
        Transaccion snapshotAnterior = crearSnapshot(transaccion);
        Usuario vendedor = usuarioService.buscarEntityPorEmail(emailVendedor);

        if (transaccion.getEstadoTransaccion() != EstadoTransaccion.SOLICITAR)
            throw new TransaccionNoModificableException("Solo se pueden asignar transacciones en estado SOLICITAR");

        if (transaccion.getVendedor() != null && !transaccion.getVendedor().getId().equals(vendedor.getId()))
            throw new NegocioException("La solicitud ya está asignada a otro vendedor");

        transaccion.setVendedor(vendedor);
        if (dto.getFechaVisita() != null)
            transaccion.setFecha_visita(dto.getFechaVisita());

        auditoriaTransaccionService.registrarCambio(snapshotAnterior, transaccion);

        return transaccionMapper.toResponseDTO(transaccionRepository.save(transaccion));
    }

    @Transactional
    public TransaccionResponseDTO actualizar(Long id, TransaccionRequestDTO dto) {

        Transaccion transaccion = buscarEntityPorId(id);
        Transaccion snapshotAnterior = crearSnapshot(transaccion);

        Usuario cliente = usuarioService.buscarEntityPorId(dto.getCliente_id());
        Usuario vendedor = usuarioService.buscarEntityPorId(dto.getVendedor_id());
        Vehiculo vehiculo = vehiculoService.obtenerVehiculoEntityPorId(dto.getVehiculo_id());

        if (snapshotAnterior.getEstadoTransaccion() != EstadoTransaccion.SOLICITAR
                && !vehiculo.getId().equals(snapshotAnterior.getVehiculo().getId()))
            throw new TransaccionNoModificableException("No se puede cambiar el vehículo de una operación ya registrada");

        transaccionMapper.updateEntityFromDto(
                dto,
                transaccion
        );
        transaccion.setCliente(cliente);
        transaccion.setVendedor(vendedor);
        transaccion.setVehiculo(vehiculo);

        EstadoTransaccion nuevoEstado = dto.getEstadoTransaccion();

        if (nuevoEstado != snapshotAnterior.getEstadoTransaccion())
            cambiarEstadoOperacion(transaccion, nuevoEstado, dto.getMonto_entregado());
        else
            validarMontoSinCambios(transaccion, dto.getMonto_entregado());

        auditoriaTransaccionService.registrarCambio(snapshotAnterior, transaccion);

        return transaccionMapper.toResponseDTO(transaccionRepository.save(transaccion));
    }


    @Transactional(readOnly = true)
    public List<TransaccionResponseDTO> listarPorCliente(String emailCliente) {
        return transaccionRepository.findByClienteEmailOrderByIdDesc(emailCliente).stream()
                .map(transaccionMapper::toResponseDTO)
                .toList();
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

    /** Vende o cancela una transacción sin modificar el resto de sus datos. */
    @Transactional
    public TransaccionResponseDTO cambiarEstado(Long id, EstadoTransaccion nuevoEstado) {

        if (nuevoEstado != EstadoTransaccion.VENDIDO && nuevoEstado != EstadoTransaccion.CANCELADO)
            throw new NegocioException("Por esta vía solo se puede vender o cancelar. Las reservas y señas se registran actualizando la transacción");

        // Falta validar que solo lo pueda realizar un vendedor o Admin
        Transaccion transaccion = buscarEntityPorId(id);
        Transaccion snapshotAnterior = crearSnapshot(transaccion);

        if (nuevoEstado == EstadoTransaccion.VENDIDO
                && transaccion.getEstadoTransaccion() == EstadoTransaccion.SOLICITAR)
            throw new NegocioException("Una solicitud se vende registrando la operación con su vendedor y método de pago");

        cambiarEstadoOperacion(transaccion, nuevoEstado, null);

        auditoriaTransaccionService.registrarCambio(snapshotAnterior, transaccion);

        return transaccionMapper.toResponseDTO(
                transaccionRepository.save(transaccion)
        );
    }

    private void cambiarEstadoOperacion(Transaccion transaccion, EstadoTransaccion nuevoEstado, BigDecimal montoReserva) {

        EstadoTransaccion estadoActual = transaccion.getEstadoTransaccion();
        validarCambioDeEstado(estadoActual, nuevoEstado);

        if (nuevoEstado != EstadoTransaccion.CANCELADO) {
            if (estadoActual == EstadoTransaccion.SOLICITAR) {
                validarVehiculoDisponible(transaccion.getVehiculo());
                registrarOperacion(transaccion, nuevoEstado, montoReserva);
            } else {
                // Reserva o seña que se termina de pagar
                transaccion.setMonto_entregado(transaccion.getPrecio_final());
            }
        }

        asignacionCambioDeEstado(transaccion, nuevoEstado);
    }

    private void validarCambioDeEstado(EstadoTransaccion estadoActual, EstadoTransaccion nuevoEstado) {

        if (estadoActual == EstadoTransaccion.CANCELADO)
            throw new TransaccionNoModificableException("No se puede cambiar el estado de una transacción cancelada");
        if (estadoActual == EstadoTransaccion.VENDIDO)
            throw new TransaccionNoModificableException("No se puede cambiar el estado de una transacción vendida");
        if (nuevoEstado == EstadoTransaccion.SOLICITAR)
            throw new TransaccionNoModificableException("No se puede volver una transacción al estado de solicitud");
        if (estadoActual != EstadoTransaccion.SOLICITAR
                && nuevoEstado != EstadoTransaccion.VENDIDO
                && nuevoEstado != EstadoTransaccion.CANCELADO)
            throw new TransaccionNoModificableException("Una reserva o seña solo puede pasar a vendida o cancelada");
    }

    /**
     * Fija el precio y el monto entregado al registrar una reserva, una seña o una venta.
     * El precio es siempre el de venta del vehículo y queda congelado desde este momento.
     */
    private void registrarOperacion(Transaccion transaccion, EstadoTransaccion tipoOperacion, BigDecimal montoReserva) {

        BigDecimal precio = transaccion.getVehiculo().getPrecioVenta();

        BigDecimal montoEntregado = switch (tipoOperacion) {
            case RESERVA -> validarMontoReserva(montoReserva, precio);
            case SENIADO -> porcentajeDe(precio, configuracionService.obtenerPorcentajeSenia(), RoundingMode.HALF_UP);
            case VENDIDO -> precio;
            case SOLICITAR, CANCELADO ->
                    throw new NegocioException("Una operación se registra como reserva, seña o venta");
        };

        transaccion.setPrecio_final(precio);
        transaccion.setMonto_entregado(montoEntregado);
    }

    private BigDecimal validarMontoReserva(BigDecimal monto, BigDecimal precio) {

        if (monto == null || monto.signum() <= 0)
            throw new NegocioException("La reserva requiere un monto mayor a cero");

        BigDecimal maximo = porcentajeDe(precio, ConfiguracionService.PORCENTAJE_MAXIMO_RESERVA, RoundingMode.DOWN);
        if (monto.compareTo(maximo) > 0)
            throw new NegocioException("La reserva no puede superar el " + ConfiguracionService.PORCENTAJE_MAXIMO_RESERVA
                    + "% del precio del vehículo (máximo " + maximo.toPlainString() + ")");

        return monto;
    }

    private BigDecimal porcentajeDe(BigDecimal precio, BigDecimal porcentaje, RoundingMode redondeo) {
        return precio.multiply(porcentaje).divide(CIEN, 2, redondeo);
    }

    private void validarMontoSinCambios(Transaccion transaccion, BigDecimal monto) {

        BigDecimal actual = transaccion.getMonto_entregado();
        if (monto != null && (actual == null || monto.compareTo(actual) != 0))
            throw new TransaccionNoModificableException("El monto entregado solo cambia cuando se paga el vehículo");
    }

    private void validarVehiculoDisponible(Vehiculo vehiculo) {
        if (vehiculo.getEstado() != EstadoVehiculo.DISPONIBLE)
            throw new VehiculoNoDisponibleException("El vehículo requerido no está disponible");
    }

    private void asignacionCambioDeEstado(Transaccion transaccion, EstadoTransaccion nuevoEstado) {

        Long vehiculo_id = transaccion.getVehiculo().getId();

        if (nuevoEstado == EstadoTransaccion.VENDIDO) {
            vehiculoService.cambiarEstadoEntity(vehiculo_id, EstadoVehiculo.VENDIDO);

            // Calcula automaticamente la comision del vendedor cuando se concreta la venta
            transaccion.setComision_calculada(calcularComision(transaccion.getPrecio_final()));
            cancelarOtrasSolicitudes(transaccion);

        } else if (nuevoEstado == EstadoTransaccion.CANCELADO) {
            if (transaccion.getEstadoTransaccion() != EstadoTransaccion.SOLICITAR)
                vehiculoService.cambiarEstadoEntity(vehiculo_id, EstadoVehiculo.DISPONIBLE);

        } else {
            vehiculoService.cambiarEstadoEntity(vehiculo_id, EstadoVehiculo.RESERVADO);
            cancelarOtrasSolicitudes(transaccion);
        }

        transaccion.setEstadoTransaccion(nuevoEstado);
    }

    private void cancelarOtrasSolicitudes(Transaccion reservada) {
        List<Transaccion> solicitudes = transaccionRepository.findByVehiculoIdAndEstadoTransaccionAndIdNot(
                reservada.getVehiculo().getId(), EstadoTransaccion.SOLICITAR, reservada.getId());

        for (Transaccion solicitud : solicitudes) {
            Transaccion snapshotAnterior = crearSnapshot(solicitud);
            solicitud.setEstadoTransaccion(EstadoTransaccion.CANCELADO);
            auditoriaTransaccionService.registrarCambio(snapshotAnterior, solicitud);
        }

        transaccionRepository.saveAll(solicitudes);
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
                .metodoPago(transaccion.getMetodoPago())
                .vendedor(transaccion.getVendedor())
                .cliente(transaccion.getCliente())
                .vehiculo(transaccion.getVehiculo())
                .build();
    }
}
