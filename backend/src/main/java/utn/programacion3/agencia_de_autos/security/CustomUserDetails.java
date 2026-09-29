package utn.programacion3.agencia_de_autos.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import utn.programacion3.agencia_de_autos.model.Usuario;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    // Traduce tu ENUM Rol al idioma del framework
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Formato usado por Spring:  "ROLE_VENDEDOR", "ROLE_ADMIN"
        String rolConPrefijo = "ROLE_" + usuario.getRolUsuario().name();
        return List.of(new SimpleGrantedAuthority(rolConPrefijo));
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail(); // Tu identificador único es el email
    }

    // Estados de la cuenta (Todos en true para no bloquear los accesos por ahora)
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return usuario.isActivo(); }
}