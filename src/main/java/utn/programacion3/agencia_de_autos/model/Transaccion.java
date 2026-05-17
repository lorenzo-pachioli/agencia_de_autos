package utn.programacion3.agencia_de_autos.model;

import jakarta.persistence.*;
import lombok.*;
import utn.programacion3.agencia_de_autos.model.enums.EstadoTransaccion;
import utn.programacion3.agencia_de_autos.model.enums.MetodoPago;
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
    private BigDecimal comision_calculada;

    @Column(nullable = false)
    private BigDecimal precio_final;

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
}
