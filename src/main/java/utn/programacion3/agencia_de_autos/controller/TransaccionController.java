package utn.programacion3.agencia_de_autos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utn.programacion3.agencia_de_autos.dto.request.TransaccionRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.TransaccionResponseDTO;
import utn.programacion3.agencia_de_autos.service.TransaccionService;
import utn.programacion3.agencia_de_autos.validation.Groups;

@RestController
@RequestMapping("/transacciones")
@RequiredArgsConstructor
public class TransaccionController {

    private final TransaccionService transaccionService;

    @PostMapping
    public ResponseEntity<TransaccionResponseDTO> crearTransaccion(@Validated(Groups.Crear.class) @RequestBody TransaccionRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(transaccionService.crear(dto));
    }
}
