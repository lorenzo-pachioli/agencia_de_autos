package utn.programacion3.agencia_de_autos.model;

import jakarta.persistence.*;
import lombok.*;
import utn.programacion3.agencia_de_autos.model.enums.EstadoTransaccion;
import utn.programacion3.agencia_de_autos.model.enums.MetodoPago;
import utn.programacion3.agencia_de_autos.service.TransaccionService;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String observaciones;

    @Builder.Default
    private LocalDate created_at = LocalDate.now();

    private LocalDate updated_at;

    @Column(nullable = false)
    private BigDecimal precio_final;

    @Builder.Default
    private BigDecimal comision_calculada = BigDecimal.ZERO;

    @Enumerated
    @Column(nullable = false)
    private MetodoPago metodoPago;

    @Enumerated
    @Builder.Default
    private EstadoTransaccion estadoTransaccion = EstadoTransaccion.RESERVA;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Usuario cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vendedor_id")
    private Usuario vendedor;

    @PrePersist
    public void onCreate(){
        if (this.observaciones == null){
            this.observaciones = "Sin observaciones";
        }
        if(this.estadoTransaccion == EstadoTransaccion.VENDIDO){
            BigDecimal comision_vendedores = new BigDecimal("0.05");
            this.comision_calculada = TransaccionService.calcularComision(this.precio_final);
        }
        if (this.estadoTransaccion == null){
            this.estadoTransaccion = EstadoTransaccion.RESERVA;
        }
    }

    @PreUpdate      // Asigna la fecha del momento en que se realiza un save en una entidad que ya tiene id (en el POST por ej)
    public void onUpdate() {
        this.updated_at = LocalDate.now();
    }
}
