package utn.programacion3.agencia_de_autos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Clave secreta hardcodeada temporalmente para pruebas (Debe tener al menos 256 bits)
    private static final String SECRET_KEY = "MiClaveSuperSecretaQueNadiePuedeAdivinarParaLaAgenciaDeAutosUTN2026";

    // Tiempo de validez del token: 24 horas pasadas a milisegundos
    private static final long JWT_EXPIRATION = 86400000;

    // Genera la clave para nuestro token
    private Key getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //Generamos el token con sus datos para que viajen en el token
    public String generarToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();

        // Guardamos el rol del usuario
        String rol = userDetails.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .findFirst()
                .orElse("");

        // Metemos el rol adentro de los claims extras con la clave "role"
        extraClaims.put("role", rol);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    // Extraer el mail del usuario
    public String extraerUsername(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    // Validar si el token es real y pertenece al user
    public boolean esTokenValido(String token, UserDetails userDetails) {
        final String username = extraerUsername(token);
        return (username.equals(userDetails.getUsername())) && !esTokenExpirado(token);
    }

    // Validar si el token expiro
    private boolean esTokenExpirado(String token) {
        return extraerClaim(token, Claims::getExpiration).before(new Date());
    }

    // Método genérico helper para sacar datos de adentro del token
    public <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraerTodosLosClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extraerTodosLosClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
