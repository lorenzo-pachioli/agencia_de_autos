package utn.programacion3.agencia_de_autos.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import utn.programacion3.agencia_de_autos.dto.request.TransaccionFilterDTO;
import utn.programacion3.agencia_de_autos.model.Transaccion;

import java.util.ArrayList;
import java.util.List;

public class TransaccionSpecification {

    public static Specification<Transaccion> conFiltros(TransaccionFilterDTO filtros) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtros.getVehiculo_id() != null) {
                predicates.add(cb.equal(root.get("vehiculo").get("id"), filtros.getVehiculo_id()));
            }
            if (filtros.getCliente_id() != null) {
                predicates.add(cb.equal(root.get("cliente").get("id"), filtros.getCliente_id()));
            }
            if (filtros.getVendedor_id() != null) {
                predicates.add(cb.equal(root.get("vendedor").get("id"), filtros.getVendedor_id()));
            }
            if (filtros.getEstadoTransaccion() != null) {
                predicates.add(cb.equal(root.get("estadoTransaccion"), filtros.getEstadoTransaccion()));
            }
            if (filtros.getFechaDesde() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filtros.getFechaDesde().atStartOfDay()));
            }
            if (filtros.getFechaHasta() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filtros.getFechaHasta().atTime(23, 59, 59)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}