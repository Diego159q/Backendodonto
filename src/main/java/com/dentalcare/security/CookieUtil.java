package com.dentalcare.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class CookieUtil {

    private final String jwtCookieName = "jwt_token";

    @Value("${cookie.secure:false}")
    private boolean isSecure;

    @Value("${cookie.same-site:Lax}")
    private String sameSite;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    public void createCookie(HttpServletResponse response, String token) {
        response.addHeader("Set-Cookie", 
            org.springframework.http.ResponseCookie.from(jwtCookieName, token)
                .httpOnly(true)
                .secure(isSecure)
                .path("/")
                .maxAge(jwtExpiration / 1000)
                .sameSite(sameSite)
                .build().toString()
        );
    }

    public void clearCookie(HttpServletResponse response) {
        response.addHeader("Set-Cookie", 
            org.springframework.http.ResponseCookie.from(jwtCookieName, "")
                .httpOnly(true)
                .secure(isSecure)
                .path("/")
                .maxAge(0)
                .sameSite(sameSite)
                .build().toString()
        );
    }

    public String getTokenFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }
}
