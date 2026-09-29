package utn.programacion3.agencia_de_autos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.programacion3.agencia_de_autos.dto.response.FavoritoResponseDTO;
import utn.programacion3.agencia_de_autos.service.FavoritoService;

import java.util.List;

@RestController
@RequestMapping("/favoritos")
@RequiredArgsConstructor
@Tag(name = "Favoritos", description = "Controlador para la gestión de vehículos favoritos de los usuarios")
@SecurityRequirements
public class FavoritoController {

    private final FavoritoService favoritoService;

    @Operation(
            summary = "Listar los favoritos del usuario autenticado",
            description = "Obtiene la lista completa de vehículos en favoritos transformados a formato FavoritoResponse directamente desde el servicio."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de favoritos recuperada con éxito"),
            @ApiResponse(responseCode = "401", description = "No autorizado. Token JWT faltante o inválido"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping
    public ResponseEntity<List<FavoritoResponseDTO>> listarFavoritos() {
        return ResponseEntity.ok(favoritoService.listarFavoritos());
    }

    @Operation(
            summary = "Agregar un vehículo a favoritos",
            description = "Vincula un vehículo a la lista de favoritos del usuario autenticado. Devuelve el FavoritoResponse con el ID de la relación."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo agregado a favoritos correctamente."),
            @ApiResponse(responseCode = "400", description = "Error de negocio (Vehículo ya es favorito o está VENDIDO)"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Vehículo o Usuario no encontrado")
    })
    @PostMapping("/agregar/{vehiculoId}")
    public ResponseEntity<FavoritoResponseDTO> agregarFavorito(
            @Parameter(description = "ID del vehículo que se desea agregar", required = true, example = "1")
            @PathVariable Long vehiculoId) {
        return ResponseEntity.ok(favoritoService.agregarFavorito(vehiculoId));
    }

    @Operation(
            summary = "Eliminar un vehículo de favoritos",
            description = "Remueve la relación de favoritos entre el usuario logueado y el vehículo especificado. Devuelve el FavoritoResponse del objeto eliminado para confirmación."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo eliminado de favoritos correctamente."),
            @ApiResponse(responseCode = "400", description = "El vehículo no estaba en la lista de favoritos del usuario"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Vehículo o Usuario no encontrado")
    })
    @DeleteMapping("/eliminar/{vehiculoId}")
    public ResponseEntity<FavoritoResponseDTO> eliminarFavorito(
            @Parameter(description = "ID del vehículo que se desea eliminar", required = true, example = "1")
            @PathVariable Long vehiculoId) {
        return ResponseEntity.ok(favoritoService.eliminarFavorito(vehiculoId));
    }
}