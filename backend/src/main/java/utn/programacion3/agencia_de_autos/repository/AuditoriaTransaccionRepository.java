package utn.programacion3.agencia_de_autos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import utn.programacion3.agencia_de_autos.model.AuditoriaTransaccion;

public interface AuditoriaTransaccionRepository extends
        JpaRepository<AuditoriaTransaccion, Long> ,
        JpaSpecificationExecutor<AuditoriaTransaccion> {
}
