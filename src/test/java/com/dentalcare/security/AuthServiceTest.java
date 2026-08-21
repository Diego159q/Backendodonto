package com.dentalcare.security;

import com.dentalcare.dto.request.LoginRequest;
import com.dentalcare.dto.response.LoginResponse;
import com.dentalcare.entity.Rol;
import com.dentalcare.entity.Usuario;
import com.dentalcare.repository.UsuarioRepository;
import com.dentalcare.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequest loginRequest;
    private Usuario usuario;
    private Rol rol;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@dentalcare.com");
        loginRequest.setPassword("admin123");

        rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ADMINISTRADOR");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombres("Admin");
        usuario.setApellidos("Sistema");
        usuario.setEmail("admin@dentalcare.com");
        usuario.setPassword("encodedPassword");
        usuario.setRol(rol);
        usuario.setActivo(true);
        usuario.setBloqueado(false);
    }

    @Test
    void testLogin_Success() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(usuarioRepository.findByEmail("admin@dentalcare.com")).thenReturn(Optional.of(usuario));
        when(jwtTokenProvider.generateToken(anyLong(), anyString(), anyString())).thenReturn("test-jwt-token");

        LoginResponse result = authService.login(loginRequest);

        assertNotNull(result);
        assertEquals("test-jwt-token", result.getToken());
        assertEquals("admin@dentalcare.com", result.getEmail());
        assertEquals("ADMINISTRADOR", result.getRol());
    }

    @Test
    void testLogin_UserNotFound() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(usuarioRepository.findByEmail("admin@dentalcare.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }
}
