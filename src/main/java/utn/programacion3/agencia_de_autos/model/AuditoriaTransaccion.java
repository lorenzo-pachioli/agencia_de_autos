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
public class AuditoriaTransaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long transaccion_id;

    @Column(nullable = false)
    private Long vendedor_id;

    @Builder.Default
    private LocalDate created_at = LocalDate.now();

    private BigDecimal precioFinalAnterior;
    private BigDecimal precioFinalNuevo;

    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPagoAnterior;

    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPagoNuevo;

    @Enumerated(EnumType.STRING)
    private EstadoTransaccion estadoAnterior;

    @Enumerated(EnumType.STRING)
    private EstadoTransaccion estadoNuevo;
}
