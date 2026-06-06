package utn.programacion3.agencia_de_autos.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import utn.programacion3.agencia_de_autos.model.Vehiculo;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;
import utn.programacion3.agencia_de_autos.model.enums.TipoCombustible;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    boolean existsByPatente(String patente);

    Optional<Vehiculo> findByPatente(String patente);

    @Query("""
       SELECT v
       FROM Vehiculo v
       WHERE v.modelo.marca.id = :marcaId
       """)
    List<Vehiculo> buscarPorMarca(Long marcaId);

    @Query("""
       SELECT v
       FROM Vehiculo v
       WHERE v.modelo.id = :modeloId
       """)
    List<Vehiculo> buscarPorModelo(Long modeloId);

    @Query("""
       SELECT v
       FROM Vehiculo v
       WHERE v.precio BETWEEN :precioMin AND :precioMax
       """)
    List<Vehiculo> buscarPorRangoPrecio(
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax
    );

    @Query("""
       SELECT v
       FROM Vehiculo v
       WHERE v.tipoCombustible = :combustible
       """)
    List<Vehiculo> buscarPorCombustible(
            @Param("combustible") TipoCombustible combustible
    );

    @Query("""
       SELECT v
       FROM Vehiculo v
       WHERE v.estado = :estado
       """)
    List<Vehiculo> buscarPorEstado(
            @Param("estado") EstadoVehiculo estado
    );

    @Query("""
    SELECT v
    FROM Vehiculo v
    WHERE (:marcaId IS NULL OR v.modelo.marca.id = :marcaId)
    AND (:modeloId IS NULL OR v.modelo.id = :modeloId)
    AND (:combustible IS NULL OR v.tipoCombustible = :combustible)
    AND (:minPrecio IS NULL OR v.precio >= :minPrecio)
    AND (:maxPrecio IS NULL OR v.precio <= :maxPrecio)
""")
    List<Vehiculo> buscarConFiltros(
            @Param("marcaId") Long marcaId,
            @Param("modeloId") Long modeloId,
            @Param("combustible") TipoCombustible combustible,
            @Param("minPrecio") BigDecimal minPrecio,
            @Param("maxPrecio") BigDecimal maxPrecio
    );
}
