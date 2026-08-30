package utn.programacion3.agencia_de_autos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import utn.programacion3.agencia_de_autos.model.Vehiculo;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;

import java.util.Optional;

@Repository

public interface VehiculoRepository extends
        JpaRepository<Vehiculo, Long>,
        JpaSpecificationExecutor<Vehiculo> {

    boolean existsByPatente(String patente);

    Optional<Vehiculo> findByPatente(String patente);

    long countByEstado(EstadoVehiculo estado);

}
