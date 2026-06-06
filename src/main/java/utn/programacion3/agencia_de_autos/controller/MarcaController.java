package utn.programacion3.agencia_de_autos.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import utn.programacion3.agencia_de_autos.dto.request.MarcaRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.MarcaResponseDTO;
import utn.programacion3.agencia_de_autos.service.MarcaService;

import java.util.List;

@RestController
@RequestMapping("/marcas")
@RequiredArgsConstructor
public class MarcaController {

    private final MarcaService marcaService;

    // Crear una nueva marca
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MarcaResponseDTO crearMarca(@Valid @RequestBody MarcaRequestDTO request) {

        return marcaService.crearMarca(request);
    }

    // Obtener todas las marcas
    @GetMapping
    public List<MarcaResponseDTO> listarMarcas() {

        return marcaService.listarMarcas();
    }

    // Obtener marca por ID
    @GetMapping("/{id}")
    public MarcaResponseDTO obtenerMarcaPorId(@PathVariable Long id) {

        return marcaService.obtenerMarcaPorId(id);
    }

    // Actualizar una marca
    @PutMapping("/{id}")
    public MarcaResponseDTO actualizarMarca(
            @PathVariable Long id,
            @Valid @RequestBody MarcaRequestDTO request) {

        return marcaService.actualizarMarca(id, request);
    }

    // Eliminar una marca
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarMarca(@PathVariable Long id) {

        marcaService.eliminarMarca(id);
    }
}