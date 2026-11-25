package utn.programacion3.agencia_de_autos.model;

import jakarta.persistence.*;
import lombok.*;
import utn.programacion3.agencia_de_autos.model.enums.EstadoTransaccion;
import utn.programacion3.agencia_de_autos.model.enums.MetodoPago;
import utn.programacion3.agencia_de_autos.service.TransaccionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private LocalDateTime fecha_visita;

    private BigDecimal precio_final;

    /** Lo que el cliente ya pagó: la reserva, la seña o el total si está vendida. */
    private BigDecimal monto_entregado;

    @Builder.Default
    private BigDecimal comision_calculada = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EstadoTransaccion estadoTransaccion = EstadoTransaccion.RESERVA;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Usuario vendedor;

    /** Foto de los datos actuales, para comparar en la auditoría antes de modificarlos. */
    public Transaccion copia() {
        return Transaccion.builder()
                .id(id)
                .estadoTransaccion(estadoTransaccion)
                .precio_final(precio_final)
                .metodoPago(metodoPago)
                .vendedor(vendedor)
                .cliente(cliente)
                .vehiculo(vehiculo)
                .build();
    }

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
