package utn.programacion3.agencia_de_autos.service;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.programacion3.agencia_de_autos.dto.request.UsuarioAdminRequestDto;
import utn.programacion3.agencia_de_autos.dto.request.UsuarioRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.UsuarioResponseDTO;
import utn.programacion3.agencia_de_autos.exception.EmailAlreadyExistsException;
import utn.programacion3.agencia_de_autos.exception.InvalidPasswordException;
import utn.programacion3.agencia_de_autos.exception.ResourceNotFoundException;
import utn.programacion3.agencia_de_autos.mapper.UsuarioMapper;
import utn.programacion3.agencia_de_autos.model.Usuario;
import utn.programacion3.agencia_de_autos.model.enums.Rol;
import utn.programacion3.agencia_de_autos.repository.UsuarioRepository;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    // Inyección por constructor (Buenas prácticas de Spring)
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO requestDTO) {

        if (usuarioRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("El email " + requestDTO.getEmail() + " ya se encuentra registrado.");
        }

        Usuario usuario = usuarioMapper.toEntity(requestDTO);
        usuario.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        usuario.setActivo(true);
        usuario.setRolUsuario(Rol.USUARIO);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDTO(usuarioGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el ID: " + id));
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarEntityPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el ID: " + id));
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO requestDTO) {

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el ID: " + id));

        this.esPropioOAdmin(usuarioExistente);

        // Validar si intenta cambiar el email por uno que ya pertenece a OTRO usuario
        usuarioRepository.findByEmail(requestDTO.getEmail())
                .ifPresent(u -> {
                    if (!u.getId().equals(id)) {
                        throw new EmailAlreadyExistsException("El email ya esta en uso por otro usuario.");
                    }
                });

        usuarioExistente.setNombre(requestDTO.getNombre());
        usuarioExistente.setApellido(requestDTO.getApellido());
        usuarioExistente.setEmail(requestDTO.getEmail());
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
            String nuevaPasswordLimpia = requestDTO.getPassword().trim();

            if (usuarioExistente.getPassword().equals(nuevaPasswordLimpia)) {
                throw new InvalidPasswordException("La nueva contraseña no puede ser igual a la contraseña actual.");
            }

            usuarioExistente.setPassword(nuevaPasswordLimpia);
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);
        return usuarioMapper.toResponseDTO(usuarioActualizado);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO darDeBaja(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el ID: " + id));

        usuario.setActivo(false);
        usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO registrarVendedor(UsuarioAdminRequestDto dto) {

        if (usuarioRepository.findByEmail(dto.getEmail().trim()).isPresent()) {
            throw new EmailAlreadyExistsException("El email ya se encuentra registrado en el sistema.");
        }

        Usuario nuevoVendedor = usuarioMapper.toEntity(dto);

        // Encriptar la contraseña por seguridad antes de guardarla
        // nuevoVendedor.setPassword(passwordEncoder.encode(dto.getPassword()));

        nuevoVendedor.setActivo(true);

        Usuario vendedorGuardado = usuarioRepository.save(nuevoVendedor);

        return usuarioMapper.toResponseDTO(vendedorGuardado);
    }

    @Override
    public void esPropioOAdmin(Usuario usuarioExistente){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailLogueado = authentication.getName();

        // Valida Seguridad: No es el propio usuario O el usuario logeado es ADMINISTRADOR
        boolean esElMismoUsuario = usuarioExistente.getEmail().equals(emailLogueado);
        boolean esAdministrador = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"));

        if (!esElMismoUsuario && !esAdministrador) {
            throw new AccessDeniedException("No tienes permisos para modificar este usuario.");
        }
    }

}
