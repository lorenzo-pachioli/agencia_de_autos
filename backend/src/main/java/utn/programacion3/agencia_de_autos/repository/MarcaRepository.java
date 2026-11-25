package utn.programacion3.agencia_de_autos.repository;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.programacion3.agencia_de_autos.model.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {

    Optional<Marca> findByNombre(String nombre);

    List<Marca> findByEliminadoFalse();
}
