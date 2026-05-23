package utn.programacion3.agencia_de_autos.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.programacion3.agencia_de_autos.dto.request.ImagenVehiculoRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.ImagenVehiculoResponseDTO;
import utn.programacion3.agencia_de_autos.service.ImagenVehiculoService;

import java.util.List;

@RestController
@RequestMapping("/imagenes")
@RequiredArgsConstructor
public class ImagenVehiculoController {

    private final ImagenVehiculoService imagenVehiculoService;

    // Crear imagen
    @PostMapping
    public ResponseEntity<ImagenVehiculoResponseDTO> crearImagen(
            @Valid @RequestBody ImagenVehiculoRequestDTO request){

        return ResponseEntity.ok(imagenVehiculoService.crearImagen(request));
    }

    // Obtener todas las imagenes
    @GetMapping
    public ResponseEntity<List<ImagenVehiculoResponseDTO>> obtenerTodas(){

        return ResponseEntity.ok(imagenVehiculoService.obtenerTodas());
    }

    // Obtener imagen por ID
    @GetMapping("/{id}")
    public ResponseEntity<ImagenVehiculoResponseDTO> obtenerPorId(
            @PathVariable Long id){

        return ResponseEntity.ok(imagenVehiculoService.obtenerPorId(id));
    }

    // Obtener imagenes por vehiculo
    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<ImagenVehiculoResponseDTO>> obtenerPorVehiculo(
            @PathVariable Long vehiculoId){

        return ResponseEntity.ok(imagenVehiculoService.obtenerPorVehiculo(vehiculoId));
    }

    // Eliminar imagen
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarImagen(
            @PathVariable Long id){

        imagenVehiculoService.eliminarImagen(id);

        return ResponseEntity.noContent().build();
    }
}
