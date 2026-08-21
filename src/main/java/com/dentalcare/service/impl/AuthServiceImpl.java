package com.dentalcare.service.impl;

import com.dentalcare.dto.request.*;
import com.dentalcare.dto.response.*;
import com.dentalcare.entity.Rol;
import com.dentalcare.entity.Usuario;
import com.dentalcare.exception.BadRequestException;
import com.dentalcare.exception.DuplicateResourceException;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.repository.RolRepository;
import com.dentalcare.repository.UsuarioRepository;
import com.dentalcare.security.JwtTokenProvider;
import com.dentalcare.service.IAuthService;
import com.dentalcare.util.EmailService;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AuthServiceImpl implements IAuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    public AuthServiceImpl(UsuarioRepository usuarioRepository,
                           RolRepository rolRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider,
                           EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", request.getEmail()));

        String token = jwtTokenProvider.generateToken(usuario.getId(), usuario.getEmail(), usuario.getRol().getNombre());

        usuario.setUltimoAcceso(LocalDateTime.now());
        usuarioRepository.save(usuario);

        return LoginResponse.builder()
                .token(token)
                .tipo("Bearer")
                .id(usuario.getId())
                .nombres(usuario.getNombres() + " " + usuario.getApellidos())
                .email(usuario.getEmail())
                .rol(usuario.getRol() != null ? usuario.getRol().getNombre() : null)
                .expiresIn(jwtExpiration)
                .build();
    }

    @Override
    public MensajeResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Usuario", "email", request.getEmail());
        }

        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Usuario", "username", request.getUsername());
        }

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", request.getRolId()));

        Usuario usuario = new Usuario();
        usuario.setNombres(request.getNombres());
        usuario.setApellidos(request.getApellidos());
        usuario.setEmail(request.getEmail());
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(rol);
        usuario.setActivo(true);
        usuario.setBloqueado(false);

        usuarioRepository.save(usuario);

        return MensajeResponse.builder()
                .mensaje("Usuario registrado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse cambiarPassword(CambioPasswordRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", request.getEmail()));

        if (!passwordEncoder.matches(request.getPasswordActual(), usuario.getPassword())) {
            throw new BadRequestException("La contrase\u00f1a actual es incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(request.getPasswordNueva()));
        usuarioRepository.save(usuario);

        return MensajeResponse.builder()
                .mensaje("Contrase\u00f1a cambiada exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse recuperarPassword(RecuperarPasswordRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", request.getEmail()));

        String resetToken = UUID.randomUUID().toString();
        usuario.setPassword(resetToken);
        usuarioRepository.save(usuario);

        String resetLink = "http://localhost:8080/api/auth/restablecer-password?token=" + resetToken;
        String subject = "Restablecimiento de Contrase\u00f1a - DentalCare";
        String body = String.format(
                "Estimado(a) %s %s,\n\nPara restablecer su contrase\u00f1a, haga clic en el siguiente enlace:\n%s\n\nSi no solicit\u00f3 este cambio, ignore este mensaje.\n\nSaludos,\nDentalCare",
                usuario.getNombres(), usuario.getApellidos(), resetLink
        );
        emailService.sendEmail(usuario.getEmail(), subject, body);

        return MensajeResponse.builder()
                .mensaje("Se ha enviado un enlace de restablecimiento a su correo electr\u00f3nico")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse restablecerPassword(RestablecerPasswordRequest request) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        Usuario usuario = usuarios.stream()
                .filter(u -> u.getPassword().equals(request.getToken()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Token de restablecimiento inv\u00e1lido o expirado"));

        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuarioRepository.save(usuario);

        return MensajeResponse.builder()
                .mensaje("Contrase\u00f1a restablecida exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public LoginResponse obtenerUsuarioActual(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));
                
        return LoginResponse.builder()
                .id(usuario.getId())
                .nombres(usuario.getNombres() + " " + usuario.getApellidos())
                .email(usuario.getEmail())
                .rol(usuario.getRol() != null ? usuario.getRol().getNombre() : null)
                .build();
    }
}
