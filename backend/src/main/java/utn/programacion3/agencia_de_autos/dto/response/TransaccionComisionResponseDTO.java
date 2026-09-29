package utn.programacion3.agencia_de_autos.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class TransaccionComisionResponseDTO {

    private Long id;

    private BigDecimal comision_total;

    private Long vendedor_id;

    private String nombre_completo;

    private String email;
}
