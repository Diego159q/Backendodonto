package com.dentalcare.service;

import com.dentalcare.dto.request.*;
import com.dentalcare.dto.response.*;

public interface IAuthService {
    LoginResponse login(LoginRequest request);
    MensajeResponse register(RegisterRequest request);
    MensajeResponse cambiarPassword(CambioPasswordRequest request);
    MensajeResponse recuperarPassword(RecuperarPasswordRequest request);
    MensajeResponse restablecerPassword(RestablecerPasswordRequest request);
    LoginResponse obtenerUsuarioActual(String email);
}
