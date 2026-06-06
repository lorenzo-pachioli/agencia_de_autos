package utn.programacion3.agencia_de_autos.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.programacion3.agencia_de_autos.dto.request.CambiarEstadoVehiculoDTO;
import utn.programacion3.agencia_de_autos.dto.request.VehiculoRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.VehiculoResponseDTO;
import utn.programacion3.agencia_de_autos.model.enums.TipoCombustible;
import utn.programacion3.agencia_de_autos.service.VehiculoService;

import java.math.BigDecimal;
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

    //Obtener vehiculo por marca
    @GetMapping("/marca/{marcaId}")
    public ResponseEntity<List<VehiculoResponseDTO>>
    obtenerPorMarca(@PathVariable Long marcaId){

        return ResponseEntity.ok(
                vehiculoService.obtenerPorMarca(marcaId)
        );
    }

    //Obtener vehículo por modelo
    @GetMapping("/modelo/{modeloId}")
    public ResponseEntity<List<VehiculoResponseDTO>> buscarPorModelo(
            @PathVariable Long modeloId){

        return ResponseEntity.ok(
                vehiculoService.obtenerPorModelo(modeloId)
        );
    }

    // Obtener vehículo por precio

    @GetMapping("/precio")
    public ResponseEntity<List<VehiculoResponseDTO>>
    buscarPorRangoPrecio(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max){

        return ResponseEntity.ok(
                vehiculoService.obtenerPorRangoPrecio(min, max)
        );
    }

    //Obtener vehículo por combustible
    @GetMapping("/combustible/{combustible}")
    public ResponseEntity<List<VehiculoResponseDTO>>
    buscarPorCombustible(
            @PathVariable TipoCombustible combustible){

        return ResponseEntity.ok(
                vehiculoService.obtenerPorCombustible(combustible)
        );
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

    @GetMapping("/busqueda")
    public ResponseEntity<List<VehiculoResponseDTO>> buscarConFiltros(

            @RequestParam(required = false) Long marcaId,
            @RequestParam(required = false) Long modeloId,
            @RequestParam(required = false) TipoCombustible combustible,
            @RequestParam(required = false) BigDecimal minPrecio,
            @RequestParam(required = false) BigDecimal maxPrecio){

        return ResponseEntity.ok(
                vehiculoService.buscarConFiltros(
                        marcaId,
                        modeloId,
                        combustible,
                        minPrecio,
                        maxPrecio
                )
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