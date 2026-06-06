package utn.programacion3.agencia_de_autos.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import utn.programacion3.agencia_de_autos.model.ImagenVehiculo;

import java.util.List;

public interface ImagenVehiculoRepository extends JpaRepository<ImagenVehiculo, Long> {
    List<ImagenVehiculo> findByVehiculoId(Long vehiculoId);
}