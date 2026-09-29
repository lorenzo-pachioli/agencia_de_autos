package utn.programacion3.agencia_de_autos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.programacion3.agencia_de_autos.dto.request.MarcaRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.MarcaResponseDTO;
import utn.programacion3.agencia_de_autos.mapper.MarcaMapper;
import utn.programacion3.agencia_de_autos.model.Marca;
import utn.programacion3.agencia_de_autos.repository.MarcaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarcaService {

    private final MarcaRepository marcaRepository;
    private final MarcaMapper marcaMapper;

    // Crear una nueva marca
    public MarcaResponseDTO crearMarca(MarcaRequestDTO request) {

        if (marcaRepository.existsByNombre(request.getNombre())) {
            throw new RuntimeException("La marca ya existe");
        }

        Marca marca = marcaMapper.toEntity(request);
        if(request.getActivo() == null){
            marca.setActivo(true);
        }

        Marca marcaGuardada = marcaRepository.save(marca);

        return marcaMapper.toResponse(marcaGuardada);
    }

    // Obtener todas las marcas
    @Transactional(readOnly = true)
    public List<MarcaResponseDTO> listarMarcas() {

        return marcaRepository.findAll()
                .stream()
                .map(marcaMapper::toResponse)
                .toList();
    }

    // Buscar marca por ID
    @Transactional(readOnly = true)
    public MarcaResponseDTO obtenerMarcaPorId(Long id) {

        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        return marcaMapper.toResponse(marca);
    }

    // Actualizar marca
    public MarcaResponseDTO actualizarMarca(Long id, MarcaRequestDTO request) {

        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        marca.setNombre(request.getNombre());
        if(request.getActivo() != null){
            marca.setActivo(request.getActivo());
        }

        Marca marcaActualizada = marcaRepository.save(marca);

        return marcaMapper.toResponse(marcaActualizada);
    }

    // Eliminar marca
    public void eliminarMarca(Long id) {

        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        marcaRepository.delete(marca);
    }
}