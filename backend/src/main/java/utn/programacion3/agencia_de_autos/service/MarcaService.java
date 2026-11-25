package utn.programacion3.agencia_de_autos.service;

import utn.programacion3.agencia_de_autos.exception.ResourceNotFoundException;
import utn.programacion3.agencia_de_autos.repository.ModeloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.programacion3.agencia_de_autos.dto.request.MarcaRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.MarcaResponseDTO;
import utn.programacion3.agencia_de_autos.exception.NegocioException;
import utn.programacion3.agencia_de_autos.mapper.MarcaMapper;
import utn.programacion3.agencia_de_autos.model.Marca;
import utn.programacion3.agencia_de_autos.repository.MarcaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarcaService {

    private final MarcaRepository marcaRepository;
    private final MarcaMapper marcaMapper;
    private final ModeloRepository modeloRepository;

    // Crear una nueva marca
    @Transactional
    public MarcaResponseDTO crearMarca(MarcaRequestDTO request) {

        Marca existente = marcaRepository.findByNombre(request.getNombre()).orElse(null);
        if (existente != null && !existente.getEliminado()) {
            throw new NegocioException("La marca ya existe");
        }

        // Si el nombre es de una marca eliminada, se recupera esa en lugar de crear otra
        Marca marca = existente != null ? existente : marcaMapper.toEntity(request);
        marca.setEliminado(false);
        marca.setActivo(request.getActivo() == null || request.getActivo());

        Marca marcaGuardada = marcaRepository.save(marca);

        return marcaMapper.toResponse(marcaGuardada);
    }

    // Obtener todas las marcas
    @Transactional(readOnly = true)
    public List<MarcaResponseDTO> listarMarcas() {

        return marcaRepository.findByEliminadoFalse()
                .stream()
                .map(marcaMapper::toResponse)
                .toList();
    }

    // Buscar marca por ID
    @Transactional(readOnly = true)
    public MarcaResponseDTO obtenerMarcaPorId(Long id) {

        return marcaMapper.toResponse(buscarVigente(id));
    }

    // Actualizar marca
    @Transactional
    public MarcaResponseDTO actualizarMarca(Long id, MarcaRequestDTO request) {

        Marca marca = buscarVigente(id);

        marcaRepository.findByNombre(request.getNombre())
                .filter(otra -> !otra.getId().equals(id))
                .ifPresent(otra -> {
                    throw new NegocioException(otra.getEliminado()
                            ? "Ese nombre es de una marca eliminada. Creala de nuevo para recuperarla."
                            : "La marca ya existe");
                });

        marca.setNombre(request.getNombre());
        if(request.getActivo() != null){
            marca.setActivo(request.getActivo());
        }

        Marca marcaActualizada = marcaRepository.save(marca);

        return marcaMapper.toResponse(marcaActualizada);
    }

    // Eliminar marca (borrado lógico)
    @Transactional
    public void eliminarMarca(Long id) {

        Marca marca = buscarVigente(id);

        if (modeloRepository.existsByMarcaIdAndEliminadoFalse(id)) {
            throw new NegocioException("No se puede eliminar la marca porque tiene modelos. Eliminalos primero.");
        }

        marca.setEliminado(true);
        marcaRepository.save(marca);
    }

    /** Una marca eliminada se trata como inexistente. */
    private Marca buscarVigente(Long id) {
        return marcaRepository.findById(id)
                .filter(marca -> !marca.getEliminado())
                .orElseThrow(() -> new ResourceNotFoundException("Marca no encontrada"));
    }

}