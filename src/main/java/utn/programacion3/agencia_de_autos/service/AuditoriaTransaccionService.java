package utn.programacion3.agencia_de_autos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import utn.programacion3.agencia_de_autos.dto.request.AuditoriaTransaccionCambiosFilterDTO;
import utn.programacion3.agencia_de_autos.dto.request.AuditoriaTransaccionFilterDTO;
import utn.programacion3.agencia_de_autos.dto.request.TransaccionFilterDTO;
import utn.programacion3.agencia_de_autos.dto.response.AuditoriaTransaccionResponseDTO;
import utn.programacion3.agencia_de_autos.dto.response.TransaccionResponseDTO;
import utn.programacion3.agencia_de_autos.mapper.AuditoriaTransaccionMapper;
import utn.programacion3.agencia_de_autos.model.AuditoriaTransaccion;
import utn.programacion3.agencia_de_autos.model.Transaccion;
import utn.programacion3.agencia_de_autos.repository.AuditoriaTransaccionRepository;
import utn.programacion3.agencia_de_autos.repository.AuditoriaTransaccionSpecification;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaTransaccionService {

    private final AuditoriaTransaccionRepository repository;
    private final AuditoriaTransaccionMapper mapper;

    public Page<AuditoriaTransaccionResponseDTO> listar(AuditoriaTransaccionFilterDTO filtros, Pageable pageable) {

        return repository.findAll(
                        AuditoriaTransaccionSpecification.conFiltros(filtros), pageable)
                .map(mapper::toResponseDTO);
    }

    public Page<AuditoriaTransaccionResponseDTO> listarCambiosEstado(AuditoriaTransaccionCambiosFilterDTO filtros, Pageable pageable) {

        return repository.findAll(
                        AuditoriaTransaccionSpecification.cambioEstado(filtros), pageable)
                .map(mapper::toResponseDTO);
    }

    public Page<AuditoriaTransaccionResponseDTO> listarCambiosPrecio(AuditoriaTransaccionCambiosFilterDTO filtros, Pageable pageable) {

        return repository.findAll(
                        AuditoriaTransaccionSpecification.cambioPrecio(filtros), pageable)
                .map(mapper::toResponseDTO);
    }

    public void registrarCambio(Transaccion anterior, Transaccion nueva) {

        AuditoriaTransaccion auditoria = AuditoriaTransaccion.builder()
                .transaccion_id(anterior.getId())
                .vendedor_id(anterior.getVendedor().getId())
                .precioFinalAnterior(anterior.getPrecio_final())
                .precioFinalNuevo(nueva.getPrecio_final())
                .metodoPagoAnterior(anterior.getMetodoPago())
                .metodoPagoNuevo(nueva.getMetodoPago())
                .estadoAnterior(anterior.getEstadoTransaccion())
                .estadoNuevo(nueva.getEstadoTransaccion())
                .build();

        repository.save(auditoria);
    }

    public void registrarCreacion(Transaccion nueva) {

        AuditoriaTransaccion auditoria = AuditoriaTransaccion.builder()
                .transaccion_id(nueva.getId())
                .vendedor_id(nueva.getVendedor().getId())
                .precioFinalAnterior(null)
                .precioFinalNuevo(nueva.getPrecio_final())
                .metodoPagoAnterior(null)
                .metodoPagoNuevo(nueva.getMetodoPago())
                .estadoAnterior(null)
                .estadoNuevo(nueva.getEstadoTransaccion())
                .build();

        repository.save(auditoria);
    }

}
