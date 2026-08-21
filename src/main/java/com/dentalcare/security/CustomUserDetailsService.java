package com.dentalcare.security;

import com.dentalcare.entity.Usuario;
import com.dentalcare.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmailWithRol(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con email: " + email));

        Collection<GrantedAuthority> authorities = usuario.getRol() != null
                ? List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre()))
                : List.of();

        return new UserPrincipal(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getActivo() != null ? usuario.getActivo() : true,
                authorities
        );
    }

    public static class UserPrincipal implements UserDetails {
        private final Long userId;
        private final String email;
        private final String password;
        private final boolean enabled;
        private final Collection<? extends GrantedAuthority> authorities;

        public UserPrincipal(Long userId, String email, String password,
                             boolean enabled,
                             Collection<? extends GrantedAuthority> authorities) {
            this.userId = userId;
            this.email = email;
            this.password = password;
            this.enabled = enabled;
            this.authorities = authorities;
        }

        public Long getUserId() {
            return userId;
        }

        @Override
        public String getUsername() {
            return email;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }
    }
}