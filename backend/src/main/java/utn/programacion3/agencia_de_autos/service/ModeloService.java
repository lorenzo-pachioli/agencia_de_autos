package utn.programacion3.agencia_de_autos.service;

import utn.programacion3.agencia_de_autos.exception.ResourceNotFoundException;
import utn.programacion3.agencia_de_autos.exception.NegocioException;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;
import utn.programacion3.agencia_de_autos.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.programacion3.agencia_de_autos.dto.request.ModeloRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.ModeloResponseDTO;
import utn.programacion3.agencia_de_autos.mapper.ModeloMapper;
import utn.programacion3.agencia_de_autos.model.Marca;
import utn.programacion3.agencia_de_autos.model.Modelo;
import utn.programacion3.agencia_de_autos.repository.MarcaRepository;
import utn.programacion3.agencia_de_autos.repository.ModeloRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModeloService {

    private final ModeloRepository modeloRepository;
    private final MarcaRepository marcaRepository;
    private final ModeloMapper modeloMapper;
    private final VehiculoRepository vehiculoRepository;

    // Crear un nuevo modelo
    public ModeloResponseDTO crearModelo(ModeloRequestDTO request) {

        Marca marca = buscarMarcaVigente(request.getMarcaId());

        Modelo modelo = modeloMapper.toEntity(request);

        modelo.setMarca(marca);
        modelo.setAnio(request.getAnio());
        modelo.setTipoModelo(request.getTipoModelo());
        if (request.getActivo() != null)
        { modelo.setActivo(request.getActivo()); }

        Modelo modeloGuardado = modeloRepository.save(modelo);

        return modeloMapper.toResponse(modeloGuardado);
    }

    // Obtener todos los modelos
    @Transactional(readOnly = true)
    public List<ModeloResponseDTO> listarModelos() {

        return modeloRepository.findByEliminadoFalse()
                .stream()
                .map(modeloMapper::toResponse)
                .toList();
    }

    // Obtener modelo por ID
    @Transactional(readOnly = true)
    public ModeloResponseDTO obtenerModeloPorId(Long id) {

        return modeloMapper.toResponse(buscarVigente(id));
    }

    // Actualizar modelo
    public ModeloResponseDTO actualizarModelo(Long id, ModeloRequestDTO request) {

        Modelo modelo = buscarVigente(id);

        Marca marca = buscarMarcaVigente(request.getMarcaId());

        modelo.setNombre(request.getNombre());
        modelo.setMarca(marca);
        modelo.setAnio(request.getAnio());
        modelo.setTipoModelo(request.getTipoModelo());
        if (request.getActivo() != null) {
            modelo.setActivo(request.getActivo());
        }

        Modelo modeloActualizado = modeloRepository.save(modelo);

        return modeloMapper.toResponse(modeloActualizado);
    }

    // Eliminar modelo (borrado lógico). Los vehículos dados de baja no lo impiden.
    @Transactional
    public void eliminarModelo(Long id) {

        Modelo modelo = buscarVigente(id);

        if (vehiculoRepository.existsByModeloIdAndEstadoNot(id, EstadoVehiculo.BAJA)) {
            throw new NegocioException("No se puede eliminar el modelo porque tiene vehículos sin eliminar.");
        }

        modelo.setEliminado(true);
        modeloRepository.save(modelo);
    }

    /** Un modelo eliminado se trata como inexistente. */
    private Modelo buscarVigente(Long id) {
        return modeloRepository.findById(id)
                .filter(modelo -> !modelo.getEliminado())
                .orElseThrow(() -> new ResourceNotFoundException("Modelo no encontrado"));
    }

    // 400 y no 404: el error tiene que verse en el formulario que eligió la marca
    private Marca buscarMarcaVigente(Long id) {
        return marcaRepository.findById(id)
                .filter(marca -> !marca.getEliminado())
                .orElseThrow(() -> new NegocioException("La marca seleccionada no existe."));
    }

}