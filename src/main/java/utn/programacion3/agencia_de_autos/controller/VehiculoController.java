package utn.programacion3.agencia_de_autos.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.programacion3.agencia_de_autos.dto.request.CambiarEstadoVehiculoDTO;
import utn.programacion3.agencia_de_autos.dto.request.VehiculoRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.VehiculoResponseDTO;
import utn.programacion3.agencia_de_autos.service.VehiculoService;

import java.util.List;

@RestController
@RequestMapping("/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    // Crear vehículo
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehiculoResponseDTO crearVehiculo(
            @Valid @RequestBody VehiculoRequestDTO request) {

        return vehiculoService.crearVehiculo(request);
    }

    // Obtener todos los vehículos
    @GetMapping
    public List<VehiculoResponseDTO> obtenerVehiculos() {

        return vehiculoService.obtenerVehiculos();
    }

    // Obtener vehículo por ID
    @GetMapping("/{id}")
    public VehiculoResponseDTO obtenerVehiculoPorId(
            @PathVariable Long id) {

        return vehiculoService.obtenerVehiculoPorId(id);
    }

    // Obtener vehículo por patente
    @GetMapping("/patente/{patente}")
    public VehiculoResponseDTO obtenerVehiculoPorPatente(
            @PathVariable String patente) {

        return vehiculoService.obtenerVehiculoPorPatente(patente);
    }

    // Actualizar vehículo
    @PutMapping("/{id}")
    public VehiculoResponseDTO actualizarVehiculo(
            @PathVariable Long id,
            @Valid @RequestBody VehiculoRequestDTO request) {

        return vehiculoService.actualizarVehiculo(id, request);
    }

    //Cambiar estado
    @PatchMapping("/{id}/estado")
    public ResponseEntity<VehiculoResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoVehiculoDTO request){

        return ResponseEntity.ok(
                vehiculoService.cambiarEstado(id, request)
        );
    }

    // Eliminar vehículo
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarVehiculo(
            @PathVariable Long id) {

        vehiculoService.eliminarVehiculo(id);
    }
}