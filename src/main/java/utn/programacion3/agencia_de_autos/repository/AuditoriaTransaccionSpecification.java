package utn.programacion3.agencia_de_autos.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import utn.programacion3.agencia_de_autos.dto.request.AuditoriaTransaccionFilterDTO;
import utn.programacion3.agencia_de_autos.model.AuditoriaTransaccion;
import utn.programacion3.agencia_de_autos.model.enums.EstadoTransaccion;
import utn.programacion3.agencia_de_autos.model.enums.MetodoPago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuditoriaTransaccionSpecification {

    public static Specification<AuditoriaTransaccion> conFiltros(AuditoriaTransaccionFilterDTO filtros) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filtros.getTransaccionId() != null) {
                predicates.add(cb.equal(root.get("transaccion_id"), filtros.getTransaccionId()));
            }
            if (filtros.getVendedorId() != null) {
                predicates.add(cb.equal(root.get("vendedor_id"), filtros.getVendedorId()));
            }
            if (filtros.getFechaDesde() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("created_at"), filtros.getFechaDesde()));
            }
            if (filtros.getFechaHasta() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("created_at"), filtros.getFechaHasta()));
            }
            if (filtros.getPrecioFinalAnterior() != null) {
                predicates.add(cb.equal(root.get("precioFinalAnterior"), filtros.getPrecioFinalAnterior()));
            }
            if (filtros.getPrecioFinalNuevo() != null) {
                predicates.add(cb.equal(root.get("precioFinalNuevo"), filtros.getPrecioFinalNuevo()));
            }
            if (filtros.getMetodoPagoAnterior() != null) {
                predicates.add(cb.equal(root.get("metodoPagoAnterior"), filtros.getMetodoPagoAnterior()));
            }
            if (filtros.getMetodoPagoNuevo() != null) {
                predicates.add(cb.equal(root.get("metodoPagoNuevo"), filtros.getMetodoPagoNuevo()));
            }
            if (filtros.getEstadoAnterior() != null) {
                predicates.add(cb.equal(root.get("estadoAnterior"), filtros.getEstadoAnterior()));
            }
            if (filtros.getEstadoNuevo() != null) {
                predicates.add(cb.equal(root.get("estadoNuevo"), filtros.getEstadoNuevo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
