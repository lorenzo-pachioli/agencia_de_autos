package utn.programacion3.agencia_de_autos.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import utn.programacion3.agencia_de_autos.model.Marca;

public interface MarcaRepository extends JpaRepository<Marca, Long> {

    boolean existsByNombre(String nombre);
}
