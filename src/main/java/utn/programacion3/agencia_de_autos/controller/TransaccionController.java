package utn.programacion3.agencia_de_autos.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import utn.programacion3.agencia_de_autos.dto.request.TransaccionFilterDTO;
import utn.programacion3.agencia_de_autos.dto.request.TransaccionRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.TransaccionResponseDTO;
import utn.programacion3.agencia_de_autos.model.enums.EstadoTransaccion;
import utn.programacion3.agencia_de_autos.service.TransaccionService;
import utn.programacion3.agencia_de_autos.validation.Groups;

import java.util.List;

@RestController
@RequestMapping("/transacciones")
@RequiredArgsConstructor
public class TransaccionController {

    private final TransaccionService transaccionService;

    @GetMapping
    public ResponseEntity<List<TransaccionResponseDTO>> listar(
            @Valid @ModelAttribute TransaccionFilterDTO filtros) {
        return ResponseEntity.ok(transaccionService.listarConFiltros(filtros));
    }

    @PostMapping
    public ResponseEntity<TransaccionResponseDTO> crearTransaccion(@Validated(Groups.Crear.class) @RequestBody TransaccionRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(transaccionService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransaccionResponseDTO> actualizarTransaccion(@PathVariable Long id, @Validated(Groups.Actualizar.class) @RequestBody TransaccionRequestDTO dto){
        return ResponseEntity.ok(transaccionService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<TransaccionResponseDTO> cambiarEstadoTransaccion(@PathVariable Long id, @Valid @RequestParam EstadoTransaccion estado){
        return ResponseEntity.ok(transaccionService.cambiarEstado(id, estado));
    }
}
