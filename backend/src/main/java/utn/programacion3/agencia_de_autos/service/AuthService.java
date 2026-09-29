package utn.programacion3.agencia_de_autos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import utn.programacion3.agencia_de_autos.dto.request.LoginRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.LoginResponseDTO;
import utn.programacion3.agencia_de_autos.security.JwtService;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public LoginResponseDTO iniciarSesion(LoginRequestDTO request) {

        // Spring se encarga de verificar email y contraseña encriptada de forma automática
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        //Si la línea de arriba no falla (si las credenciales son válidas), buscamos al usuario completo
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        //Le pasamos el usuario a nuestro JwtService para que fabrique el token con su rol
        String jwtToken = jwtService.generarToken(userDetails);

        //Por ultimo devolvemos el DTO con el token adentro listo para el cliente
        return LoginResponseDTO.builder()
                .token(jwtToken)
                .build();
    }
}
