package utn.programacion3.agencia_de_autos.dto.response;

import lombok.*;
import utn.programacion3.agencia_de_autos.model.enums.EstadoTransaccion;
import utn.programacion3.agencia_de_autos.model.enums.MetodoPago;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransaccionResponseDTO {

    private Long id;

    private String observaciones;

    private BigDecimal precio_final;

    private MetodoPago metodoPago;

    private EstadoTransaccion estadoTransaccion;

    private Long vehiculo_id;

    private Long cliente_id;

    private Long vendedor_id;
}
