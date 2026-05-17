package utn.programacion3.agencia_de_autos.service;


import utn.programacion3.agencia_de_autos.dto.request.UsuarioRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {


    UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO requestDTO);


    List<UsuarioResponseDTO> listarTodos();

    UsuarioResponseDTO buscarPorId(Long id);

    UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO requestDTO);

    UsuarioResponseDTO darDeBaja(Long id);
}
