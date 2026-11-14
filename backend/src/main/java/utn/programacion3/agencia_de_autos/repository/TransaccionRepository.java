package utn.programacion3.agencia_de_autos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utn.programacion3.agencia_de_autos.model.BalanceProyeccion;
import utn.programacion3.agencia_de_autos.model.Transaccion;
import utn.programacion3.agencia_de_autos.model.enums.EstadoTransaccion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransaccionRepository extends
        JpaRepository<Transaccion,Long>,
        JpaSpecificationExecutor<Transaccion> {

    boolean existsByVehiculoIdAndClienteEmailAndEstadoTransaccionNot(
            Long vehiculoId,
            String clienteEmail,
            EstadoTransaccion estadoTransaccion
    );

    boolean existsByClienteEmailAndEstadoTransaccion(String clienteEmail, EstadoTransaccion estadoTransaccion);

    List<Transaccion> findByVehiculoIdAndEstadoTransaccionAndIdNot(
            Long vehiculoId,
            EstadoTransaccion estadoTransaccion,
            Long id
    );

    @Query("SELECT " +
            "SUM(t.precio_final) as precioFinalTotal, " +
            "SUM(t.comision_calculada) as comisionesTotal, " +
            "SUM(t.vehiculo.precioAdquisicion) as costosVehiculos " +
            "FROM Transaccion t " +
            "WHERE t.created_at BETWEEN :desde AND :hasta " +
            "AND t.estadoTransaccion = utn.programacion3.agencia_de_autos.model.enums.EstadoTransaccion.VENDIDO")
    Optional<BalanceProyeccion> calcularBalanceEntreFechas(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta
    );
}
