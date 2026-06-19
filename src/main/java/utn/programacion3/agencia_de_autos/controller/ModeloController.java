package utn.programacion3.agencia_de_autos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utn.programacion3.agencia_de_autos.dto.request.ModeloRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.ModeloResponseDTO;
import utn.programacion3.agencia_de_autos.service.ModeloService;

import java.util.List;

@RestController
@RequestMapping("/modelos")
@RequiredArgsConstructor
@Tag(name = "Modelos", description = "Operaciones relacionadas con la gestión de modelos")
public class ModeloController {

    private final ModeloService modeloService;

    @Operation(summary = "Crear un modelo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Modelo creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('VENDEDOR','ADMINISTRADOR')")
    public ModeloResponseDTO crearModelo(
            @Valid @RequestBody ModeloRequestDTO request) {

        return modeloService.crearModelo(request);
    }

    @Operation(summary = "Obtener todos los modelos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public List<ModeloResponseDTO> listarModelos() {

        return modeloService.listarModelos();
    }

    @Operation(summary = "Obtener un modelo por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Modelo encontrado"),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado")
    })
    @GetMapping("/{id}")
    public ModeloResponseDTO obtenerModeloPorId(
            @PathVariable Long id) {

        return modeloService.obtenerModeloPorId(id);
    }

    @Operation(summary = "Actualizar un modelo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Modelo actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('VENDEDOR','ADMINISTRADOR')")
    public ModeloResponseDTO actualizarModelo(
            @PathVariable Long id,
            @Valid @RequestBody ModeloRequestDTO request) {

        return modeloService.actualizarModelo(id, request);
    }

    @Operation(summary = "Eliminar un modelo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Modelo eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void eliminarModelo(
            @PathVariable Long id) {

        modeloService.eliminarModelo(id);
    }
}