package utn.programacion3.agencia_de_autos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utn.programacion3.agencia_de_autos.model.AuditoriaTransaccion;
import utn.programacion3.agencia_de_autos.model.Transaccion;
import utn.programacion3.agencia_de_autos.repository.AuditoriaTransaccionRepository;

@Service
@RequiredArgsConstructor
public class AuditoriaTransaccionService {

    private final AuditoriaTransaccionRepository auditoriaTransaccionRepository;

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

        auditoriaTransaccionRepository.save(auditoria);
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

        auditoriaTransaccionRepository.save(auditoria);
    }

}
