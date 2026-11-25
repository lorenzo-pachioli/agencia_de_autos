package utn.programacion3.agencia_de_autos.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.programacion3.agencia_de_autos.model.Usuario;
import utn.programacion3.agencia_de_autos.model.enums.Rol;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findFirstByEmailIgnoreCase(String email);

    List<Usuario> findByRolUsuario(Rol rolUsuario);

    /** El texto tiene que llegar en minúsculas. Vacío devuelve todos. */
    @Query("""
            SELECT u FROM Usuario u
            WHERE u.rolUsuario = :rol AND u.activo = true
              AND (LOCATE(:texto, LOWER(u.email)) > 0
                   OR LOCATE(:texto, LOWER(CONCAT(COALESCE(u.nombre, ''), ' ', COALESCE(u.apellido, '')))) > 0)
            ORDER BY u.email
            """)
    List<Usuario> buscarActivosPorRol(@Param("rol") Rol rol, @Param("texto") String texto, Pageable limite);

}
