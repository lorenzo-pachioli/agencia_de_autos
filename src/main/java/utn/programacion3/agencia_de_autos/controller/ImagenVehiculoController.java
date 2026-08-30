package utn.programacion3.agencia_de_autos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utn.programacion3.agencia_de_autos.dto.request.ImagenVehiculoRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.ImagenVehiculoResponseDTO;
import utn.programacion3.agencia_de_autos.service.ImagenVehiculoService;

import java.util.List;

@RestController
@RequestMapping("/imagenes")
@RequiredArgsConstructor
@Tag(name = "Imágenes de Vehículos", description = "Operaciones relacionadas con las imágenes de los vehículos")
public class ImagenVehiculoController {

    private final ImagenVehiculoService imagenVehiculoService;

    @Operation(summary = "Crear una imagen de vehículo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Imagen creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    public ImagenVehiculoResponseDTO crearImagen(
            @Valid @RequestBody ImagenVehiculoRequestDTO request) {

        return imagenVehiculoService.crearImagen(request);
    }

    @Operation(summary = "Obtener todas las imágenes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    @SecurityRequirements
    public List<ImagenVehiculoResponseDTO> obtenerTodas() {

        return imagenVehiculoService.obtenerTodas();
    }

    @Operation(summary = "Obtener una imagen por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imagen encontrada"),
            @ApiResponse(responseCode = "404", description = "Imagen no encontrada")
    })
    @GetMapping("/{id}")
    @SecurityRequirements
    public ImagenVehiculoResponseDTO obtenerPorId(
            @PathVariable Long id) {

        return imagenVehiculoService.obtenerPorId(id);
    }

    @Operation(summary = "Obtener imágenes por vehículo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imágenes obtenidas correctamente"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @GetMapping("/vehiculo/{vehiculoId}")
    @SecurityRequirements
    public List<ImagenVehiculoResponseDTO> obtenerPorVehiculo(
            @PathVariable Long vehiculoId) {

        return imagenVehiculoService.obtenerPorVehiculo(vehiculoId);
    }

    @Operation(summary = "Actualizar una imagen")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imagen actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Imagen no encontrada")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    public ImagenVehiculoResponseDTO actualizarImagen(
            @PathVariable Long id,
            @Valid @RequestBody ImagenVehiculoRequestDTO request) {

        return imagenVehiculoService.actualizarImagen(id, request);
    }

    @Operation(summary = "Eliminar una imagen")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Imagen eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Imagen no encontrada")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarImagen(
            @PathVariable Long id) {

        imagenVehiculoService.eliminarImagen(id);
    }
}
