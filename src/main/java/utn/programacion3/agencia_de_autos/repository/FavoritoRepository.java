package utn.programacion3.agencia_de_autos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.programacion3.agencia_de_autos.model.Favorito;
import utn.programacion3.agencia_de_autos.model.Usuario;
import utn.programacion3.agencia_de_autos.model.Vehiculo;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    // Para saber si ya tiene ese vehiculo en favoritos
    Optional<Favorito> findByUsuarioAndVehiculo(Usuario usuario, Vehiculo vehiculo);

}
