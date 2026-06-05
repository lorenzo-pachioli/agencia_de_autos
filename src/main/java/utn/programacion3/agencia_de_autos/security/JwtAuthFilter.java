package utn.programacion3.agencia_de_autos.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    // La interfaz de Spring para buscar usuarios en la BD
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // EXTRAER LA CABECERA "Authorization"
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Si no viene la cabecera o no empieza con "Bearer ", dejamos pasar la petición al siguiente filtro
        // (Por ejemplo, si van a /login o /registrarse, no van a traer token y está bien)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // EXTRAER EL TOKEN DE LA CABECERA (Cortamos los primeros 7 caracteres: "Bearer ")
        jwt = authHeader.substring(7);

        // Usamos nuestro JwtService para sacar el email de adentro del token
        userEmail = jwtService.extraerUsername(jwt);

        //SI EL EMAIL ES VÁLIDO Y EL USUARIO NO ESTÁ AUTENTICADO TODAVÍA EN EL CONTEXTO DE SPRING
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Buscamos el usuario en la base de datos (con sus roles/authorities)
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            //SI EL TOKEN ES VÁLIDO EN TODAS SUS REGLAS
            if (jwtService.esTokenValido(jwt, userDetails)) {

                // Creamos el objeto de autenticación que Spring Security entiende
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities() //Acá van los roles mapeados de la base de datos
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Seteamos al usuario en el contexto global de Spring Security.
                // A partir de esta línea, para Spring el usuario está LOGUEADO.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Le pasamos la pelota al siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }
}
