package utn.programacion3.agencia_de_autos.repository;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.CriteriaBuilder;
import java.util.Locale;
import org.springframework.data.jpa.domain.Specification;
import utn.programacion3.agencia_de_autos.dto.request.VehiculoFilterDTO;
import utn.programacion3.agencia_de_autos.model.Vehiculo;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;

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

            // Los dados de baja no aparecen en ninguna búsqueda
            predicates.add(cb.notEqual(root.get("estado"), EstadoVehiculo.BAJA));

            if (!filtros.isIncluirVendidos()) {
                predicates.add(
                        cb.notEqual(
                                root.get("estado"),
                                EstadoVehiculo.VENDIDO
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
                                root.get("precioVenta"),
                                filtros.getMinPrecio()
                        )
                );
            }

            if (filtros.getMaxPrecio() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("precioVenta"),
                                filtros.getMaxPrecio()
                        )
                );
            }

            if (filtros.getTexto() != null && !filtros.getTexto().isBlank()) {
                // Cada palabra tiene que aparecer en la marca, el modelo o la patente
                for (String palabra : filtros.getTexto().trim().toLowerCase(Locale.ROOT).split("\\s+")) {
                    predicates.add(cb.or(
                            contiene(cb, root.get("patente"), palabra),
                            contiene(cb, root.get("modelo").get("nombre"), palabra),
                            contiene(cb, root.get("modelo").get("marca").get("nombre"), palabra)
                    ));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate contiene(CriteriaBuilder cb, Expression<String> campo, String texto) {
        return cb.greaterThan(cb.locate(cb.lower(campo), texto), 0);
    }
}

