package utn.programacion3.agencia_de_autos.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utn.programacion3.agencia_de_autos.model.Vehiculo;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;
import utn.programacion3.agencia_de_autos.model.enums.TipoCombustible;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
@Repository

public interface VehiculoRepository extends
        JpaRepository<Vehiculo, Long>,
        JpaSpecificationExecutor<Vehiculo> {

    boolean existsByPatente(String patente);

    Optional<Vehiculo> findByPatente(String patente);

    long countByEstado(EstadoVehiculo estado);

}
