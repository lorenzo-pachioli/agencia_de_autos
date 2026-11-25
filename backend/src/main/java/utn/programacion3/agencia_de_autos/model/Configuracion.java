package utn.programacion3.agencia_de_autos.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/** Parámetros de la agencia que define el administrador. Es una tabla de una sola fila. */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Configuracion {

    public static final Long ID_UNICO = 1L;

    @Id
    private Long id;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentaje_senia;
}
