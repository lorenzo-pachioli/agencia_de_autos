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
import utn.programacion3.agencia_de_autos.dto.request.MarcaRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.MarcaResponseDTO;
import utn.programacion3.agencia_de_autos.service.MarcaService;

import java.util.List;

@RestController
@RequestMapping("/marcas")
@RequiredArgsConstructor
@Tag(name = "Marcas", description = "Operaciones relacionadas con la gestión de marcas")
public class MarcaController {

    private final MarcaService marcaService;

    @Operation(summary = "Crear una marca")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Marca creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('VENDEDOR','ADMINISTRADOR')")
    public MarcaResponseDTO crearMarca(
            @Valid @RequestBody MarcaRequestDTO request) {

        return marcaService.crearMarca(request);
    }

    @Operation(summary = "Obtener todas las marcas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public List<MarcaResponseDTO> listarMarcas() {

        return marcaService.listarMarcas();
    }

    @Operation(summary = "Obtener una marca por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Marca encontrada"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    })
    @GetMapping("/{id}")
    public MarcaResponseDTO obtenerMarcaPorId(
            @PathVariable Long id) {

        return marcaService.obtenerMarcaPorId(id);
    }

    @Operation(summary = "Actualizar una marca")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Marca actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('VENDEDOR','ADMINISTRADOR')")
    public MarcaResponseDTO actualizarMarca(
            @PathVariable Long id,
            @Valid @RequestBody MarcaRequestDTO request) {

        return marcaService.actualizarMarca(id, request);
    }

    @Operation(summary = "Eliminar una marca")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Marca eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void eliminarMarca(
            @PathVariable Long id) {

        marcaService.eliminarMarca(id);
    }
}