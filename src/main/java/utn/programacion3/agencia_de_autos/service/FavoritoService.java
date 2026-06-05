package utn.programacion3.agencia_de_autos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.programacion3.agencia_de_autos.dto.response.FavoritoResponseDTO;
import utn.programacion3.agencia_de_autos.model.*;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;
import utn.programacion3.agencia_de_autos.repository.*;
import utn.programacion3.agencia_de_autos.exception.*;
import utn.programacion3.agencia_de_autos.mapper.FavoritoMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoritoService {

    private final UsuarioRepository usuarioRepository;
    private final VehiculoRepository vehiculoRepository;
    private final FavoritoRepository favoritoRepository;
    private final FavoritoMapper favoritoMapper;

    @Transactional
    public FavoritoResponseDTO agregarFavorito(Long vehiculoId) {

        String emailLogueado = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(emailLogueado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado"));

        if (vehiculo.getEstadoVehiculo() == EstadoVehiculo.VENDIDO) {
            throw new NegocioException("No se puede agregar a favoritos un vehículo que ya fue VENDIDO.");
        }

        Optional<Favorito> favoritoExistente = favoritoRepository.findByUsuarioAndVehiculo(usuario, vehiculo);
        if (favoritoExistente.isPresent()) {
            throw new NegocioException("El vehículo ya se encuentra en tu lista de favoritos.");
        }

        Favorito nuevoFavorito = new Favorito();
        nuevoFavorito.setUsuario(usuario);
        nuevoFavorito.setVehiculo(vehiculo);

        Favorito favoritoGuardado = favoritoRepository.save(nuevoFavorito);
        usuario.getFavoritos().add(favoritoGuardado);

        return favoritoMapper.toResponse(favoritoGuardado);
    }

    @Transactional
    public FavoritoResponseDTO eliminarFavorito(Long vehiculoId) {

        String emailLogueado = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(emailLogueado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado"));

        Optional<Favorito> favoritoOpcional = favoritoRepository.findByUsuarioAndVehiculo(usuario, vehiculo);
        if (favoritoOpcional.isEmpty()) {
            throw new NegocioException("No se puede eliminar un vehículo que no tenés en tus favoritos.");
        }

        Favorito favoritoExistente = favoritoOpcional.get();
        favoritoRepository.delete(favoritoExistente);
        usuario.getFavoritos().remove(favoritoExistente);

        return favoritoMapper.toResponse(favoritoExistente);
    }

    @Transactional(readOnly = true)
    public List<FavoritoResponseDTO> listarFavoritos() {
        String emailLogueado = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(emailLogueado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return usuario.getFavoritos().stream()
                .map(favoritoMapper::toResponse)
                .toList();
    }
}