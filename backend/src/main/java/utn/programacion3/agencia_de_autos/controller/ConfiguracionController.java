package utn.programacion3.agencia_de_autos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utn.programacion3.agencia_de_autos.dto.request.ConfiguracionRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.ConfiguracionResponseDTO;
import utn.programacion3.agencia_de_autos.service.ConfiguracionService;

@RestController
@RequestMapping("/configuracion")
@RequiredArgsConstructor
@Tag(name = "Configuración", description = "Parámetros comerciales de la agencia")
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    @Operation(summary = "Consultar la configuración comercial",
            description = "Devuelve el porcentaje de seña vigente y el porcentaje máximo que puede tener una reserva.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuración obtenida con éxito"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol ADMINISTRADOR o VENDEDOR")
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @GetMapping
    public ResponseEntity<ConfiguracionResponseDTO> obtener() {
        return ResponseEntity.ok(configuracionService.obtener());
    }

    @Operation(summary = "Actualizar el porcentaje de seña",
            description = "Se aplica a las señas que se registren desde ese momento. Las ya cargadas no cambian.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Porcentaje actualizado con éxito"),
            @ApiResponse(responseCode = "400", description = "El porcentaje está fuera del rango permitido"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol ADMINISTRADOR")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping
    public ResponseEntity<ConfiguracionResponseDTO> actualizar(@Valid @RequestBody ConfiguracionRequestDTO dto) {
        return ResponseEntity.ok(configuracionService.actualizar(dto));
    }
}
