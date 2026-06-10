package utn.programacion3.agencia_de_autos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.programacion3.agencia_de_autos.dto.request.CambiarEstadoVehiculoDTO;
import utn.programacion3.agencia_de_autos.dto.request.VehiculoFilterDTO;
import utn.programacion3.agencia_de_autos.dto.request.VehiculoRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.VehiculoResponseDTO;
import utn.programacion3.agencia_de_autos.service.VehiculoServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/vehiculos")
@RequiredArgsConstructor
@Tag(name = "Vehículos", description = "Operaciones relacionadas con la gestión de vehículos")
public class VehiculoController {

    private final VehiculoServiceImpl vehiculoService;

    @Operation(summary = "Crear un vehículo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vehículo creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehiculoResponseDTO crearVehiculo(
            @Valid @RequestBody VehiculoRequestDTO request) {

        return vehiculoService.crearVehiculo(request);
    }

    @Operation(summary = "Obtener todos los vehículos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public List<VehiculoResponseDTO> obtenerVehiculos() {

        return vehiculoService.obtenerVehiculos();
    }

    @Operation(summary = "Buscar vehículos aplicando filtros")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada correctamente")
    })
    @GetMapping("/busqueda")
    public ResponseEntity<List<VehiculoResponseDTO>> buscarConFiltros(
            VehiculoFilterDTO filtros) {

        return ResponseEntity.ok(
                vehiculoService.buscarConFiltros(filtros)
        );
    }

    @Operation(summary = "Actualizar un vehículo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehículo actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @PutMapping("/{id}")
    public VehiculoResponseDTO actualizarVehiculo(
            @PathVariable Long id,
            @Valid @RequestBody VehiculoRequestDTO request) {

        return vehiculoService.actualizarVehiculo(id, request);
    }

    @Operation(summary = "Cambiar el estado de un vehículo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<VehiculoResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoVehiculoDTO request) {

        return ResponseEntity.ok(
                vehiculoService.cambiarEstado(id, request)
        );
    }

    @Operation(summary = "Eliminar un vehículo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vehículo eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarVehiculo(
            @PathVariable Long id) {

        vehiculoService.eliminarVehiculo(id);
    }
}