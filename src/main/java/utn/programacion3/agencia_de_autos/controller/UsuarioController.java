package utn.programacion3.agencia_de_autos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.programacion3.agencia_de_autos.dto.request.UsuarioRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.UsuarioResponseDTO;
import utn.programacion3.agencia_de_autos.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(
        name = "Usuarios",
        description = "Endpoints para la gestión, actualización y baja de usuarios del sistema"
)
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Constructor explícito para la inyección de dependencias
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ==========================================
    // ENDPOINTS DE GESTIÓN / ADMINISTRACIÓN
    // ==========================================

    @Operation(
            summary = "Listar todos los usuarios",
            description = "Retorna una lista completa de todos los usuarios registrados en el sistema (Clientes, Vendedores y Administradores)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @Operation(
            summary = "Actualizar datos de un usuario",
            description = "Permite modificar el nombre, apellido, email o contraseña de un usuario existente mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o email ya en uso por otro usuario"),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún usuario con el ID provisto")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO requestDTO) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, requestDTO));
    }

    @Operation(
            summary = "Dar de baja lógica a un usuario",
            description = "Cambia de forma parcial el estado del usuario a inactivo (activo = false) sin eliminarlo físicamente de la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado del usuario modificado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontro ningún usuario con el ID provisto")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> cambiarEstado(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.darDeBaja(id));
    }
}