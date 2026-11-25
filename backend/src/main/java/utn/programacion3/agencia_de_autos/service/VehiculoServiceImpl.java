package utn.programacion3.agencia_de_autos.service;

import java.util.Optional;
import utn.programacion3.agencia_de_autos.model.Transaccion;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.programacion3.agencia_de_autos.dto.request.*;
import utn.programacion3.agencia_de_autos.dto.response.ReporteGananciasResponseDTO;
import utn.programacion3.agencia_de_autos.dto.response.VehiculoGestionResponseDTO;
import utn.programacion3.agencia_de_autos.dto.response.VehiculoPublicResponseDTO;
import utn.programacion3.agencia_de_autos.dto.response.VehiculoResponseDTO;
import utn.programacion3.agencia_de_autos.exception.ModeloNoEncontradoException;
import utn.programacion3.agencia_de_autos.exception.NegocioException;
import utn.programacion3.agencia_de_autos.exception.PatenteDuplicadaException;
import utn.programacion3.agencia_de_autos.exception.VehiculoNoEncontradoException;
import utn.programacion3.agencia_de_autos.mapper.VehiculoMapper;
import utn.programacion3.agencia_de_autos.model.Marca;
import utn.programacion3.agencia_de_autos.model.Modelo;
import utn.programacion3.agencia_de_autos.model.Vehiculo;
import utn.programacion3.agencia_de_autos.model.enums.EstadoTransaccion;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;
import utn.programacion3.agencia_de_autos.model.enums.Rol;
import utn.programacion3.agencia_de_autos.repository.ModeloRepository;
import utn.programacion3.agencia_de_autos.repository.TransaccionRepository;
import utn.programacion3.agencia_de_autos.repository.VehiculoRepository;
import utn.programacion3.agencia_de_autos.repository.VehiculoSpecification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ModeloRepository modeloRepository;
    private final VehiculoMapper vehiculoMapper;
    private final ImagenVehiculoService imagenVehiculoService;
    private final TransaccionRepository transaccionRepository;
    private final AuditoriaTransaccionService auditoriaTransaccionService;

    @Override
    @Transactional
    public VehiculoResponseDTO crearVehiculo(VehiculoRequestDTO request) {

        rechazarEstadoBaja(request.getEstado());
        Optional<Vehiculo> eliminado = buscarPatenteRecuperable(request.getPatente());

        Modelo modelo = buscarModeloVigente(request.getModeloId());
        validarMarcaActiva(modelo.getMarca());
        validarModeloActivo(modelo);

        // Si la patente es de un vehículo eliminado sin vender, vuelve ese mismo con los datos nuevos
        Vehiculo vehiculo = eliminado.orElseGet(() -> vehiculoMapper.toEntity(request));
        aplicarDatos(vehiculo, request, modelo);
        vehiculo.setEliminado(false);

        Vehiculo vehiculoGuardado = vehiculoRepository.save(vehiculo);

        return vehiculoMapper.toResponse(vehiculoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public Vehiculo obtenerVehiculoEntityPorId(Long id) {

        return vehiculoRepository.findById(id)
                .orElseThrow(VehiculoNoEncontradoException::new);
    }
    @Override
    @Transactional(readOnly = true)
    public VehiculoResponseDTO obtenerVehiculoPorId(Long id, Authentication authentication){
        Vehiculo vehiculo = buscarVigente(id);
        VehiculoResponseDTO dto = vehiculoMapper.toResponse(vehiculo);
        dto.setImagenes(imagenVehiculoService.obtenerTodasLasUrls(id));
        dto.setImagenPrincipalUrl(
                imagenVehiculoService.obtenerUrlPrincipal(vehiculo.getId())
        );

        boolean autenticado = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);

        if (autenticado) {
            dto.setEsPersonalAgencia(authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(a -> a.equals("ROLE_" + Rol.VENDEDOR.name())
                            || a.equals("ROLE_" + Rol.ADMINISTRADOR.name())));
            dto.setTieneTransaccionActiva(
                    transaccionRepository.existsByVehiculoIdAndClienteEmailAndEstadoTransaccionNot(
                            id, authentication.getName(), EstadoTransaccion.CANCELADO)
            );
            dto.setTieneSolicitudPendiente(
                    transaccionRepository.existsByClienteEmailAndEstadoTransaccion(
                            authentication.getName(), EstadoTransaccion.SOLICITAR)
            );
        }
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoPublicResponseDTO> obtenerVehiculosPublicos() {

        return vehiculoRepository.findByEliminadoFalse()
                .stream()
                .map(vehiculoMapper::toPublicResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoResponseDTO> obtenerVehiculosInternos() {

        return vehiculoRepository.findByEliminadoFalse()
                .stream()
                .map(vehiculoMapper::toResponse)
                .toList();
    }

    @Override
    public VehiculoResponseDTO actualizarVehiculo(Long id, VehiculoRequestDTO request) {

        Vehiculo vehiculo = buscarVigente(id);
        rechazarEstadoBaja(request.getEstado());

        if (vehiculo.getEstado() == EstadoVehiculo.VENDIDO
                && vehiculo.getPrecioAdquisicion().compareTo(request.getPrecioAdquisicion()) != 0) {
            throw new NegocioException("No se puede modificar el precio de adquisición de un vehículo VENDIDO.");
        }

        if (!vehiculo.getPatente().equals(request.getPatente())
                && buscarPatenteRecuperable(request.getPatente()).isPresent()) {
            throw new NegocioException("La patente es de un vehículo eliminado. Cargalo como nuevo para recuperarlo.");
        }

        Modelo modelo = buscarModeloVigente(request.getModeloId());

        // Pasar un vehículo a otra marca o modelo es sumarle stock; editar los que ya tiene no lo es
        if (!modelo.getMarca().getId().equals(vehiculo.getModelo().getMarca().getId())) {
            validarMarcaActiva(modelo.getMarca());
        }
        if (!modelo.getId().equals(vehiculo.getModelo().getId())) {
            validarModeloActivo(modelo);
        }

        aplicarDatos(vehiculo, request, modelo);

        Vehiculo vehiculoActualizado = vehiculoRepository.save(vehiculo);

        return vehiculoMapper.toResponse(vehiculoActualizado);
    }

    @Override
    public VehiculoResponseDTO cambiarEstado(Long id, CambiarEstadoVehiculoDTO request) {

        Vehiculo vehiculo = buscarVigente(id);
        rechazarEstadoBaja(request.getEstado());

        vehiculo.setEstado(request.getEstado());

        Vehiculo vehiculoGuardado = vehiculoRepository.save(vehiculo);

        return vehiculoMapper.toResponse(vehiculoGuardado);
    }

    @Override
    public Vehiculo cambiarEstadoEntity(Long id, EstadoVehiculo estado) {

        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(VehiculoNoEncontradoException::new);

        vehiculo.setEstado(estado);

        return vehiculoRepository.save(vehiculo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehiculoResponseDTO> buscarConFiltros(VehiculoFilterDTO filtros, Pageable pageable) {

        return vehiculoRepository.findAll(VehiculoSpecification.conFiltros(filtros), pageable)
                .map(vehiculo -> {
                    VehiculoResponseDTO dto = vehiculoMapper.toResponse(vehiculo);
                    dto.setImagenPrincipalUrl(
                            imagenVehiculoService.obtenerUrlPrincipal(vehiculo.getId())
                    );
                    return dto;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehiculoGestionResponseDTO> listarParaGestion(Pageable pageable) {

        return vehiculoRepository.findByEliminadoFalse(pageable)
                .map(vehiculoMapper::toGestionResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ReporteGananciasResponseDTO obtenerReporteGanancias() {

        List<Vehiculo> vehiculos = vehiculoRepository.findAll()
                .stream()
                .filter(v -> v.getEstado() == EstadoVehiculo.VENDIDO)
                .toList();

        List<ReporteGananciaVehiculoDTO> detalle = vehiculos.stream()
                .map(vehiculoMapper::toReporteGanancia)
                .toList();

        BigDecimal totalInvertido = vehiculos.stream()
                .map(Vehiculo::getPrecioAdquisicion)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalVenta = vehiculos.stream()
                .map(Vehiculo::getPrecioVenta)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal gananciaTotal = totalVenta.subtract(totalInvertido);

        return ReporteGananciasResponseDTO.builder()
                .totalInvertido(totalInvertido)
                .totalVenta(totalVenta)
                .gananciaTotal(gananciaTotal)
                .vehiculos(detalle)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ReporteStockVehiculoDTO obtenerReporteStock() {

        long disponibles = vehiculoRepository.countByEstadoAndEliminadoFalse(EstadoVehiculo.DISPONIBLE);

        long reservados = vehiculoRepository.countByEstadoAndEliminadoFalse(EstadoVehiculo.RESERVADO);

        long vendidos = vehiculoRepository.countByEstadoAndEliminadoFalse(EstadoVehiculo.VENDIDO);

        long baja = vehiculoRepository.countByEliminadoTrue();

        return ReporteStockVehiculoDTO.builder()
                .disponibles(disponibles)
                .reservados(reservados)
                .vendidos(vendidos)
                .baja(baja)
                .total(disponibles + reservados + vendidos + baja)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoResponseDTO> obtenerUltimosVehiculos(Integer cantidad) {

        Pageable pageable = PageRequest.of(
                0,
                cantidad,
                Sort.by("createdAt").descending()
        );

        return vehiculoRepository.findByEliminadoFalse(pageable)
                .stream()
                .map(vehiculoMapper::toResponse)
                .toList();
    }



    /**
     * Borrado lógico: el vehículo deja de mostrarse, pero se conservan sus imágenes,
     * transacciones y favoritos. Un vendido sigue contando en las ganancias.
     */
    @Override
    @Transactional
    public void eliminarVehiculo(Long id) {

        Vehiculo vehiculo = buscarVigente(id);

        if (vehiculo.getEstado() == EstadoVehiculo.RESERVADO
                || transaccionRepository.existsByVehiculoIdAndEstadoTransaccionIn(
                        id, List.of(EstadoTransaccion.RESERVA, EstadoTransaccion.SENIADO))) {
            throw new NegocioException("No se puede eliminar un vehículo con una reserva o seña en curso.");
        }

        cancelarSolicitudesPendientes(id);
        vehiculo.setEliminado(true);
        vehiculoRepository.save(vehiculo);
    }

    /** Un vehículo eliminado se trata como inexistente. */
    private Vehiculo buscarVigente(Long id) {
        return vehiculoRepository.findById(id)
                .filter(vehiculo -> !vehiculo.getEliminado())
                .orElseThrow(VehiculoNoEncontradoException::new);
    }

    /**
     * Rechaza la patente si la tiene un vehículo vigente. Si no, devuelve el eliminado que se puede
     * recuperar. Los vendidos no se recuperan: su registro queda intacto para ganancias e historial.
     */
    private Optional<Vehiculo> buscarPatenteRecuperable(String patente) {
        List<Vehiculo> conEsaPatente = vehiculoRepository.findByPatente(patente);

        if (conEsaPatente.stream().anyMatch(vehiculo -> !vehiculo.getEliminado())) {
            throw new PatenteDuplicadaException();
        }
        return conEsaPatente.stream()
                .filter(vehiculo -> vehiculo.getEstado() != EstadoVehiculo.VENDIDO)
                .findFirst();
    }

    private void aplicarDatos(Vehiculo vehiculo, VehiculoRequestDTO request, Modelo modelo) {
        vehiculo.setPatente(request.getPatente());
        vehiculo.setAnio(request.getAnio());
        vehiculo.setPrecioAdquisicion(request.getPrecioAdquisicion());
        vehiculo.setPrecioVenta(request.getPrecioVenta());
        vehiculo.setKilometraje(request.getKilometraje());
        vehiculo.setColor(request.getColor());
        vehiculo.setEstado(request.getEstado());
        vehiculo.setModelo(modelo);
        vehiculo.setDescripcion(request.getDescripcion());
        vehiculo.setTipoCombustible(request.getTipoCombustible());
        vehiculo.setTipoTransmision(request.getTipoTransmision());
    }

    private Modelo buscarModeloVigente(Long modeloId) {
        return modeloRepository.findById(modeloId)
                .filter(modelo -> !modelo.getEliminado())
                .orElseThrow(ModeloNoEncontradoException::new);
    }

    /** Una marca o un modelo inactivos no suman stock; sus vehículos ya cargados se siguen vendiendo. */
    private void validarMarcaActiva(Marca marca) {
        if (!marca.getActivo()) {
            throw new NegocioException("La marca " + marca.getNombre()
                    + " está inactiva: no se pueden cargar vehículos nuevos.");
        }
    }

    private void validarModeloActivo(Modelo modelo) {
        if (!modelo.getActivo()) {
            throw new NegocioException("El modelo " + modelo.getNombre()
                    + " está inactivo: no se pueden cargar vehículos nuevos.");
        }
    }

    private void rechazarEstadoBaja(EstadoVehiculo estado) {
        if (estado == EstadoVehiculo.BAJA) {
            throw new NegocioException("Para dar de baja un vehículo hay que eliminarlo.");
        }
    }

    private void cancelarSolicitudesPendientes(Long vehiculoId) {
        for (Transaccion solicitud : transaccionRepository.findByVehiculoIdAndEstadoTransaccion(
                vehiculoId, EstadoTransaccion.SOLICITAR)) {
            Transaccion anterior = solicitud.copia();
            solicitud.setEstadoTransaccion(EstadoTransaccion.CANCELADO);
            auditoriaTransaccionService.registrarCambio(anterior, solicitud);
        }
    }

}