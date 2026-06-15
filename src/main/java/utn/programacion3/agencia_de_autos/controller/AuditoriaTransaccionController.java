package utn.programacion3.agencia_de_autos.controller;

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

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/auditoria")
@RequiredArgsConstructor
public class AuditoriaTransaccionController {

    private final AuditoriaTransaccionService auditoriaTransaccionService;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<Page<AuditoriaTransaccionResponseDTO>> listar(
            @Valid @ModelAttribute AuditoriaTransaccionFilterDTO filtros,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(auditoriaTransaccionService.listar(filtros, pageable));
    }

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

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/cambio-estado")
    public ResponseEntity<Page<AuditoriaTransaccionResponseDTO>> listarCambiosEstado(
            @Valid @ModelAttribute AuditoriaTransaccionCambiosFilterDTO filtros,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {

        return ResponseEntity.ok(auditoriaTransaccionService.listarCambiosEstado(filtros, pageable));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/cambio-precio")
    public ResponseEntity<Page<AuditoriaTransaccionResponseDTO>> listarCambiosPrecio(
            @Valid @ModelAttribute AuditoriaTransaccionCambiosFilterDTO filtros,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {

        return ResponseEntity.ok(auditoriaTransaccionService.listarCambiosPrecio(filtros, pageable));
    }
}
