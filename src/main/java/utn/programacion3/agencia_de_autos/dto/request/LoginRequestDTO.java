package utn.programacion3.agencia_de_autos.dto.request;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String email;
    private String password;
}
