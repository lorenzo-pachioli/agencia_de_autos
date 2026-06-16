package utn.programacion3.agencia_de_autos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import utn.programacion3.agencia_de_autos.dto.request.TransaccionFilterDTO;
import utn.programacion3.agencia_de_autos.dto.request.TransaccionRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.TransaccionBalanceResponseDTO;
import utn.programacion3.agencia_de_autos.dto.response.TransaccionComisionResponseDTO;
import utn.programacion3.agencia_de_autos.dto.response.TransaccionResponseDTO;
import utn.programacion3.agencia_de_autos.model.enums.EstadoTransaccion;
import utn.programacion3.agencia_de_autos.service.TransaccionService;
import utn.programacion3.agencia_de_autos.validation.Groups;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transacciones")
@RequiredArgsConstructor
@Tag(
        name = "Transacciones",
        description = "Endpoints para la creacion y actualizacion de transacciones y calculo de rendimientos"
)
public class TransaccionController {

    private final TransaccionService transaccionService;

    @Operation(
            summary = "Listar transacciones con filtros",
            description = "Devuelve una lista paginada de las transacciones comerciales aplicando filtros acumulativos y opcionales."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de transacciones obtenida con éxito"),
            @ApiResponse(responseCode = "400", description = "Parámetros de filtrado o formato de paginación incorrectos"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol ADMINISTRADOR o VENDEDOR")
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @GetMapping
    public ResponseEntity<Page<TransaccionResponseDTO>> listar(
            @Valid @ModelAttribute TransaccionFilterDTO filtros,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(transaccionService.listarConFiltros(filtros, pageable));
    }

    @Operation(
            summary = "Buscar transacción por ID",
            description = "Recupera la información completa y el estado actual de una transacción específica mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción localizada de forma exitosa"),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna transacción con el ID provisto"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol ADMINISTRADOR o VENDEDOR")
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<TransaccionResponseDTO> transaccionPorid(@PathVariable Long id) {
        return ResponseEntity.ok(transaccionService.buscarPorId(id));
    }

    @Operation(
            summary = "Registrar una nueva transacción",
            description = "Permite iniciar el flujo de una operación comercial en la agencia (por ejemplo, una venta o reserva)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transacción registrada y creada de forma exitosa"),
            @ApiResponse(responseCode = "400", description = "Cuerpo de la petición inválido o violaciones de validación (Grupo Crear)"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol ADMINISTRADOR o VENDEDOR")
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @PostMapping
    public ResponseEntity<TransaccionResponseDTO> crearTransaccion(@Validated(Groups.Crear.class) @RequestBody TransaccionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transaccionService.crear(dto));
    }

    @Operation(
            summary = "Actualizar datos de una transacción",
            description = "Modifica los datos principales de una transacción existente aplicando las validaciones correspondientes al grupo de actualización."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción actualizada de forma exitosa"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada incorrectos o violaciones de validación (Grupo Actualizar)"),
            @ApiResponse(responseCode = "404", description = "La transacción a modificar no existe"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol ADMINISTRADOR o VENDEDOR")
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @PutMapping("/{id}")
    public ResponseEntity<TransaccionResponseDTO> actualizarTransaccion(@PathVariable Long id, @Validated(Groups.Actualizar.class) @RequestBody TransaccionRequestDTO dto) {
        return ResponseEntity.ok(transaccionService.actualizar(id, dto));
    }

    @Operation(
            summary = "Cancelar una transacción",
            description = "Cambia de forma directa el estado operativo de la transacción a CANCELADO."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción cancelada correctamente"),
            @ApiResponse(responseCode = "404", description = "La transacción especificada no fue encontrada"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol ADMINISTRADOR o VENDEDOR")
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<TransaccionResponseDTO> cancelarTransaccionPorid(@PathVariable Long id) {
        return ResponseEntity.ok(transaccionService.cambiarEstado(id, EstadoTransaccion.CANCELADO, null));
    }

    @Operation(
            summary = "Marcar transacción como vendida",
            description = "Finaliza el flujo de la operación comercial estableciendo el estado en VENDIDO y fijando el monto del precio final."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado modificado a VENDIDO exitosamente"),
            @ApiResponse(responseCode = "400", description = "El parámetro del precio final no es válido o está ausente"),
            @ApiResponse(responseCode = "404", description = "La transacción especificada no existe"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol ADMINISTRADOR o VENDEDOR")
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @PatchMapping("/{id}/vender")
    public ResponseEntity<TransaccionResponseDTO> venderTransaccionPorid(
            @PathVariable Long id,
            @RequestParam BigDecimal precioFinal) {
        return ResponseEntity.ok(transaccionService.cambiarEstado(id, EstadoTransaccion.VENDIDO, precioFinal));
    }

    @Operation(
            summary = "Registrar seña de transacción",
            description = "Asigna el estado de SEÑADO a la operación comercial y asienta el monto económico entregado en concepto de seña."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción señada de manera exitosa"),
            @ApiResponse(responseCode = "400", description = "El valor monetario de la seña no es válido"),
            @ApiResponse(responseCode = "404", description = "La transacción especificada no existe"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol ADMINISTRADOR o VENDEDOR")
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @PatchMapping("/{id}/seniar")
    public ResponseEntity<TransaccionResponseDTO> seniarTransaccionPorid(
            @PathVariable Long id,
            @RequestParam BigDecimal precioSenia) {
        return ResponseEntity.ok(transaccionService.cambiarEstado(id, EstadoTransaccion.SENIADO, precioSenia));
    }

    @Operation(
            summary = "Cambiar estado de forma genérica",
            description = "Permite la alteración directa y manual del estado de una transacción mediante parámetros genéricos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cambio de estado realizado correctamente"),
            @ApiResponse(responseCode = "400", description = "El estado enviado no coincide con los valores permitidos del Enum"),
            @ApiResponse(responseCode = "404", description = "La transacción especificada no existe"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol ADMINISTRADOR o VENDEDOR")
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<TransaccionResponseDTO> cambiarEstadoTransaccion(@PathVariable Long id, @Valid @RequestParam EstadoTransaccion estado) {
        return ResponseEntity.ok(transaccionService.cambiarEstado(id, estado, null));
    }
    @Operation(
            summary = "Calcular comisiones de un vendedor",
            description = "Calcula el monto acumulado de comisiones para un vendedor específico dentro de un rango de fechas, basado en sus transacciones vendidas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comisiones calculadas exitosamente"),
            @ApiResponse(responseCode = "400", description = "Formatos de fecha inválidos o ID de vendedor incorrecto"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol ADMINISTRADOR")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/comision-vendedor/{id}")
    public ResponseEntity<TransaccionComisionResponseDTO> comisionPorVendedor(
            @PathVariable Long id,
            @RequestParam LocalDate fechaDesde,
            @RequestParam LocalDate fechaHasta
    ) {
        TransaccionFilterDTO filtros = TransaccionFilterDTO.builder()
                .fechaDesde(fechaDesde)
                .fechaHasta(fechaHasta)
                .vendedor_id(id)
                .estadoTransaccion(EstadoTransaccion.VENDIDO)
                .build();
        return ResponseEntity.ok(transaccionService.comisionPorVendedor(filtros));
    }

    @Operation(
            summary = "Consultar balance y rendimiento financiero",
            description = "Devuelve un balance general de los ingresos de la agencia (totales, señas, ventas) devengados entre dos fechas específicas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance financiero obtenido exitosamente"),
            @ApiResponse(responseCode = "400", description = "Rango de fechas inválido o formato incorrecto"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol ADMINISTRADOR")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/rendimiento")
    public ResponseEntity<TransaccionBalanceResponseDTO> ingresosPorfecha(
            @RequestParam LocalDate fechaDesde,
            @RequestParam LocalDate fechaHasta
    ) {
        return ResponseEntity.ok(transaccionService.obtenerBalance(fechaDesde, fechaHasta));
    }
}