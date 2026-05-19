package utn.programacion3.agencia_de_autos.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import utn.programacion3.agencia_de_autos.model.Vehiculo;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
}