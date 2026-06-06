package utn.programacion3.agencia_de_autos.service;


import jakarta.validation.Valid;
import utn.programacion3.agencia_de_autos.dto.request.UsuarioAdminRequestDto;
import utn.programacion3.agencia_de_autos.dto.request.UsuarioRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.UsuarioResponseDTO;
import utn.programacion3.agencia_de_autos.model.Usuario;

import java.util.List;

public interface UsuarioService {


    UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO requestDTO);


    List<UsuarioResponseDTO> listarTodos();

    UsuarioResponseDTO buscarPorId(Long id);

    Usuario buscarEntityPorId(Long id);

    UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO requestDTO);

    UsuarioResponseDTO darDeBaja(Long id);

    UsuarioResponseDTO registrarVendedor(UsuarioAdminRequestDto registroDto);
}
