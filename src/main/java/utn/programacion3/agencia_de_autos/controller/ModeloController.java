package utn.programacion3.agencia_de_autos.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import utn.programacion3.agencia_de_autos.dto.request.ModeloRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.ModeloResponseDTO;
import utn.programacion3.agencia_de_autos.service.ModeloService;

import java.util.List;

@RestController
@RequestMapping("/modelos")
@RequiredArgsConstructor
public class ModeloController {

    private final ModeloService modeloService;

    // Crear un nuevo modelo
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ModeloResponseDTO crearModelo(@Valid @RequestBody ModeloRequestDTO request) {

        return modeloService.crearModelo(request);
    }

    // Obtener todos los modelos
    @GetMapping
    public List<ModeloResponseDTO> listarModelos() {

        return modeloService.listarModelos();
    }

    // Obtener modelo por ID
    @GetMapping("/{id}")
    public ModeloResponseDTO obtenerModeloPorId(@PathVariable Long id) {

        return modeloService.obtenerModeloPorId(id);
    }

    // Actualizar modelo
    @PutMapping("/{id}")
    public ModeloResponseDTO actualizarModelo(
            @PathVariable Long id,
            @Valid @RequestBody ModeloRequestDTO request) {

        return modeloService.actualizarModelo(id, request);
    }

    // Eliminar modelo
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarModelo(@PathVariable Long id) {

        modeloService.eliminarModelo(id);
    }
}