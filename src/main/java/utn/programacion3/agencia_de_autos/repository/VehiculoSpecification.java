package utn.programacion3.agencia_de_autos.repository;

import org.springframework.data.jpa.domain.Specification;
import utn.programacion3.agencia_de_autos.dto.request.VehiculoFilterDTO;
import utn.programacion3.agencia_de_autos.model.Vehiculo;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class VehiculoSpecification {

    public static Specification<Vehiculo> conFiltros(VehiculoFilterDTO filtros) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filtros.getMarcaId() != null) {
                predicates.add(
                        cb.equal(
                                root.get("modelo")
                                        .get("marca")
                                        .get("id"),
                                filtros.getMarcaId()
                        )
                );
            }

            if (filtros.getModeloId() != null) {
                predicates.add(
                        cb.equal(
                                root.get("modelo").get("id"),
                                filtros.getModeloId()
                        )
                );
            }

            if (filtros.getCombustible() != null) {
                predicates.add(
                        cb.equal(
                                root.get("tipoCombustible"),
                                filtros.getCombustible()
                        )
                );
            }

            if (filtros.getEstado() != null) {
                predicates.add(
                        cb.equal(
                                root.get("estado"),
                                filtros.getEstado()
                        )
                );
            }

            if (filtros.getMinPrecio() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("precio"),
                                filtros.getMinPrecio()
                        )
                );
            }

            if (filtros.getMaxPrecio() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("precio"),
                                filtros.getMaxPrecio()
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

