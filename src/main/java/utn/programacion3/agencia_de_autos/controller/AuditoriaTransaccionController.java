package utn.programacion3.agencia_de_autos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utn.programacion3.agencia_de_autos.dto.request.AuditoriaTransaccionCambiosFilterDTO;
import utn.programacion3.agencia_de_autos.dto.request.AuditoriaTransaccionFilterDTO;
import utn.programacion3.agencia_de_autos.dto.response.AuditoriaTransaccionResponseDTO;
import utn.programacion3.agencia_de_autos.service.AuditoriaTransaccionService;

@RestController
@RequestMapping("/auditoria")
@RequiredArgsConstructor
public class AuditoriaTransaccionController {

    private final AuditoriaTransaccionService auditoriaTransaccionService;

    @Operation(
            summary = "Listar auditorías con filtros dinámicos",
            description = "Devuelve una lista paginada del historial de auditorías de transacciones aplicando filtros opcionales."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de auditorías obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de filtrado o paginación inválidos"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol ADMINISTRADOR")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<Page<AuditoriaTransaccionResponseDTO>> listar(
            @Valid @ModelAttribute AuditoriaTransaccionFilterDTO filtros,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(auditoriaTransaccionService.listar(filtros, pageable));
    }

    @Operation(
            summary = "Consultar auditorías por ID de Transacción",
            description = "Devuelve de forma paginada todo el historial de cambios y auditoría asociado a una transacción específica."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de la transacción obtenido exitosamente"),
            @ApiResponse(responseCode = "400", description = "El ID de transacción proporcionado es inválido"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol ADMINISTRADOR")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/transaccion/{id}")
    public ResponseEntity<Page<AuditoriaTransaccionResponseDTO>> listarTransaccionPorId(
            @Valid @PathVariable Long id,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        AuditoriaTransaccionFilterDTO transaccionId = AuditoriaTransaccionFilterDTO.builder()
                .transaccionId(id)
                .build();

        return ResponseEntity.ok(auditoriaTransaccionService.listar(transaccionId, pageable));
    }

    @Operation(
            summary = "Consultar auditorías por ID de Vendedor",
            description = "Devuelve de forma paginada todas las operaciones auditadas que fueron realizadas por un vendedor específico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial del vendedor obtenido exitosamente"),
            @ApiResponse(responseCode = "400", description = "El ID de vendedor proporcionado es inválido"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol ADMINISTRADOR")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/vendedor/{id}")
    public ResponseEntity<Page<AuditoriaTransaccionResponseDTO>> listarVendedorPorId(
            @Valid @PathVariable Long id,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        AuditoriaTransaccionFilterDTO vendedorId = AuditoriaTransaccionFilterDTO.builder()
                .vendedorId(id)
                .build();

        return ResponseEntity.ok(auditoriaTransaccionService.listar(vendedorId, pageable));
    }

    @Operation(
            summary = "Listar auditorías por cambios de estado",
            description = "Devuelve una lista paginada de auditorías filtrando específicamente aquellas donde se modificó el estado de la transacción."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cambios de estado obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Filtros de estados o paginación inválidos"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol ADMINISTRADOR")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/cambio-estado")
    public ResponseEntity<Page<AuditoriaTransaccionResponseDTO>> listarCambiosEstado(
            @Valid @ModelAttribute AuditoriaTransaccionCambiosFilterDTO filtros,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(auditoriaTransaccionService.listarCambiosEstado(filtros, pageable));
    }

    @Operation(
            summary = "Listar auditorías por cambios de precio",
            description = "Devuelve una lista paginada de auditorías filtrando específicamente aquellas donde hubo variaciones en los montos económicos de la transacción."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cambios de precio obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Filtros de precios o paginación inválidos"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol ADMINISTRADOR")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/cambio-precio")
    public ResponseEntity<Page<AuditoriaTransaccionResponseDTO>> listarCambiosPrecio(
            @Valid @ModelAttribute AuditoriaTransaccionCambiosFilterDTO filtros,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(auditoriaTransaccionService.listarCambiosPrecio(filtros, pageable));
    }
}
