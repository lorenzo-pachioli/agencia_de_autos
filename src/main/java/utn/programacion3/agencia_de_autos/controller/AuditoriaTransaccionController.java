package utn.programacion3.agencia_de_autos.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.programacion3.agencia_de_autos.dto.request.AuditoriaTransaccionFilterDTO;
import utn.programacion3.agencia_de_autos.dto.response.AuditoriaTransaccionResponseDTO;
import utn.programacion3.agencia_de_autos.service.AuditoriaTransaccionService;


@RestController
@RequestMapping("/auditoria")
@RequiredArgsConstructor
public class AuditoriaTransaccionController {

    private final AuditoriaTransaccionService auditoriaTransaccionService;

    @GetMapping
    public ResponseEntity<Page<AuditoriaTransaccionResponseDTO>> listar(
            @Valid @ModelAttribute AuditoriaTransaccionFilterDTO filtros) {
        return ResponseEntity.ok(auditoriaTransaccionService.listar(filtros));
    }

    @GetMapping("/transaccion/{id}")
    public ResponseEntity<Page<AuditoriaTransaccionResponseDTO>> listarTransaccionPorId(
            @Valid @PathVariable Long id) {

        AuditoriaTransaccionFilterDTO transaccionId = AuditoriaTransaccionFilterDTO.builder()
                        .transaccionId(id)
                        .build();

        return ResponseEntity.ok(auditoriaTransaccionService.listar(transaccionId));
    }

    @GetMapping("/vendedor/{id}")
    public ResponseEntity<Page<AuditoriaTransaccionResponseDTO>> listarVendedorPorId(
            @Valid @PathVariable Long id) {

        AuditoriaTransaccionFilterDTO vendedorId = AuditoriaTransaccionFilterDTO.builder()
                .vendedorId(id)
                .build();

        return ResponseEntity.ok(auditoriaTransaccionService.listar(vendedorId));
    }
}
