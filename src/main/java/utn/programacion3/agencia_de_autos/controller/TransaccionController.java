package utn.programacion3.agencia_de_autos.controller;

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
public class TransaccionController {

    private final TransaccionService transaccionService;


    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @GetMapping
    public ResponseEntity<Page<TransaccionResponseDTO>> listar(
            @Valid @ModelAttribute TransaccionFilterDTO filtros,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(transaccionService.listarConFiltros(filtros, pageable));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<TransaccionResponseDTO> transaccionPorid(@PathVariable Long id) {
        return ResponseEntity.ok(transaccionService.buscarPorId(id));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @PostMapping
    public ResponseEntity<TransaccionResponseDTO> crearTransaccion(@Validated(Groups.Crear.class) @RequestBody TransaccionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transaccionService.crear(dto));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @PutMapping("/{id}")
    public ResponseEntity<TransaccionResponseDTO> actualizarTransaccion(@PathVariable Long id, @Validated(Groups.Actualizar.class) @RequestBody TransaccionRequestDTO dto) {
        return ResponseEntity.ok(transaccionService.actualizar(id, dto));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<TransaccionResponseDTO> cancelarTransaccionPorid(@PathVariable Long id) {
        return ResponseEntity.ok(transaccionService.cambiarEstado(id, EstadoTransaccion.CANCELADO, null));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @PatchMapping("/{id}/vender")
    public ResponseEntity<TransaccionResponseDTO> venderTransaccionPorid(
            @PathVariable Long id,
            @RequestParam BigDecimal precioFinal) {
        return ResponseEntity.ok(transaccionService.cambiarEstado(id, EstadoTransaccion.VENDIDO, precioFinal));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @PatchMapping("/{id}/seniar")
    public ResponseEntity<TransaccionResponseDTO> seniarTransaccionPorid(
            @PathVariable Long id,
            @RequestParam BigDecimal precioSenia) {
        return ResponseEntity.ok(transaccionService.cambiarEstado(id, EstadoTransaccion.SENIADO, precioSenia));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<TransaccionResponseDTO> cambiarEstadoTransaccion(@PathVariable Long id, @Valid @RequestParam EstadoTransaccion estado) {
        return ResponseEntity.ok(transaccionService.cambiarEstado(id, estado, null));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
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

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/rendimiento")
    public ResponseEntity<TransaccionBalanceResponseDTO> ingresosPorfecha(
            @RequestParam LocalDate fechaDesde,
            @RequestParam LocalDate fechaHasta
    ) {
        return ResponseEntity.ok(transaccionService.obtenerBalance(fechaDesde, fechaHasta));
    }

}
