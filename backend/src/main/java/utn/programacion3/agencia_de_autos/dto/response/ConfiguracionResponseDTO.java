package utn.programacion3.agencia_de_autos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ConfiguracionResponseDTO {

    private BigDecimal porcentaje_senia;

    private BigDecimal porcentaje_maximo_reserva;
}
