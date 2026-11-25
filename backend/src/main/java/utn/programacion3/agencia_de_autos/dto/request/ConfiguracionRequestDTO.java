package utn.programacion3.agencia_de_autos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ConfiguracionRequestDTO {

    @Schema(description = "Porcentaje del precio del vehículo que se cobra como seña", example = "10", minimum = "1", maximum = "50")
    @NotNull(message = "El porcentaje de seña no puede estar vacio")
    @DecimalMin(value = "1", message = "El porcentaje de seña debe ser de al menos 1%")
    @DecimalMax(value = "50", message = "El porcentaje de seña no puede superar el 50%")
    @Digits(integer = 2, fraction = 2, message = "El porcentaje de seña admite hasta 2 decimales")
    private BigDecimal porcentaje_senia;
}
