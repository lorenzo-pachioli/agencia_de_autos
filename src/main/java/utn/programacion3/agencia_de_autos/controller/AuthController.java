package utn.programacion3.agencia_de_autos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utn.programacion3.agencia_de_autos.dto.request.LoginRequestDTO;
import utn.programacion3.agencia_de_autos.dto.request.UsuarioRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.UsuarioResponseDTO;
import utn.programacion3.agencia_de_autos.service.UsuarioService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(
        name = "Autenticacion",
        description = "Operaciones relacionadas a la autenticación de usuarios (Registro y Login)"
)
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Valida y registra un nuevo cliente en el sistema. Por defecto se le asigna el rol CLIENTE."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o correo electrónico ya registrado")
    })
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody UsuarioRequestDTO requestDTO) {
        UsuarioResponseDTO response = usuarioService.registrarUsuario(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Iniciar sesión en el sistema",
            description = "Permite el ingreso al sistema mediante email y contraseña. Retorna un token de acceso simulado para el Sprint 1."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa, token generado"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas o usuario inactivo")
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO loginRequest) {
        String email = loginRequest.getEmail();

        // Simulación temporal del Token para la agencia
        Map<String, String> tokenResponse = new HashMap<>();
        tokenResponse.put("token", "jwt-simulado-token-agencia-" + email);

        return ResponseEntity.ok(tokenResponse);
    }
}
