package utn.programacion3.agencia_de_autos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utn.programacion3.agencia_de_autos.dto.request.CambiarEstadoVehiculoDTO;
import utn.programacion3.agencia_de_autos.dto.request.ReporteStockVehiculoDTO;
import utn.programacion3.agencia_de_autos.dto.request.VehiculoFilterDTO;
import utn.programacion3.agencia_de_autos.dto.request.VehiculoRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.ReporteGananciasResponseDTO;
import utn.programacion3.agencia_de_autos.dto.response.VehiculoResponseDTO;
import utn.programacion3.agencia_de_autos.service.VehiculoServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/vehiculos")
@RequiredArgsConstructor
@Tag(name = "Vehículos", description = "Operaciones relacionadas con la gestión de vehículos")
@SecurityRequirements
public class VehiculoController {

    private final VehiculoServiceImpl vehiculoService;

    @Operation(summary = "Crear un vehículo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vehículo creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('VENDEDOR','ADMINISTRADOR')")
    public VehiculoResponseDTO crearVehiculo(
            @Valid @RequestBody VehiculoRequestDTO request) {

        return vehiculoService.crearVehiculo(request);
    }


    @Operation(summary = "Buscar vehículos aplicando filtros")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada correctamente")
    })
    @GetMapping("/busqueda")
    public ResponseEntity<Page<VehiculoResponseDTO>> buscarConFiltros(
            VehiculoFilterDTO filtros,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {

        return ResponseEntity.ok(
                vehiculoService.buscarConFiltros(filtros, pageable)
        );
    }

    @Operation(summary = "Buscar un vehículo por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehículo encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @GetMapping("/{id}")
    public VehiculoResponseDTO buscarPorIdVehiculo(
            @PathVariable Long id) {
        return vehiculoService.obtenerVehiculoPorId(id);
    }

    @Operation(summary = "Actualizar un vehículo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehículo actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('VENDEDOR','ADMINISTRADOR')")
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
    @PreAuthorize("hasAnyRole('VENDEDOR','ADMINISTRADOR')")
    public ResponseEntity<VehiculoResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoVehiculoDTO request) {

        return ResponseEntity.ok(
                vehiculoService.cambiarEstado(id, request)
        );
    }

    @Operation(summary = "Obtener reporte de ganancias de vehículos vendidos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte obtenido correctamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @GetMapping("/reportes/ganancias")
    @PreAuthorize("hasAnyRole('VENDEDOR','ADMINISTRADOR')")
    public ResponseEntity<ReporteGananciasResponseDTO> obtenerReporteGanancias() {

        return ResponseEntity.ok(
                vehiculoService.obtenerReporteGanancias()
        );
    }

    @Operation(summary = "Obtener reporte de stock por estado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte obtenido correctamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @GetMapping("/reportes/stock")
    @PreAuthorize("hasAnyRole('VENDEDOR','ADMINISTRADOR')")
    public ResponseEntity<ReporteStockVehiculoDTO> obtenerReporteStock() {

        return ResponseEntity.ok(
                vehiculoService.obtenerReporteStock()
        );
    }

    @GetMapping("/reportes/ultimos")
    public ResponseEntity<List<VehiculoResponseDTO>> obtenerUltimosVehiculos(
            @RequestParam(defaultValue = "10") Integer cantidad) {

        return ResponseEntity.ok(
                vehiculoService.obtenerUltimosVehiculos(cantidad)
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