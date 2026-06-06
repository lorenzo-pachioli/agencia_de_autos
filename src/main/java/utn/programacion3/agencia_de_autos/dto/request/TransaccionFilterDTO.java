package utn.programacion3.agencia_de_autos.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import utn.programacion3.agencia_de_autos.model.enums.EstadoTransaccion;

import java.time.LocalDate;

@Getter
@Setter
public class TransaccionFilterDTO {

    @Positive(message = "El vehiculo_id debe ser un numero positivo")
    private Long vehiculo_id;

    @Positive(message = "El cliente_id debe ser un numero positivo")
    private Long cliente_id;

    @Positive(message = "El vendedor_id debe ser un numero positivo")
    private Long vendedor_id;

    @PastOrPresent(message = "La fecha desde no puede ser futura")
    private LocalDate fechaDesde;

    @PastOrPresent(message = "La fecha hasta no puede ser futura")
    private LocalDate fechaHasta;

    private EstadoTransaccion estadoTransaccion;
}