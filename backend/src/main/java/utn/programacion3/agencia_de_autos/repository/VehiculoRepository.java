package utn.programacion3.agencia_de_autos.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;
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

    List<Vehiculo> findByPatente(String patente);

    List<Vehiculo> findByEliminadoFalse();

    Page<Vehiculo> findByEliminadoFalse(Pageable pageable);

    boolean existsByModeloIdAndEliminadoFalse(Long modeloId);

    long countByEstadoAndEliminadoFalse(EstadoVehiculo estado);

    long countByEliminadoTrue();

}
