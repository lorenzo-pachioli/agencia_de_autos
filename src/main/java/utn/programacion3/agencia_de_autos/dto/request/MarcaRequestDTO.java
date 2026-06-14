package utn.programacion3.agencia_de_autos.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarcaRequestDTO {

    @NotBlank(message = "El nombre de la marca es obligatorio")
    private String nombre;

    private Boolean activo;
}
