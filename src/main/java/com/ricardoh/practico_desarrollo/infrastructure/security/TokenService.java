package com.ricardoh.practico_desarrollo.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ricardoh.practico_desarrollo.domain.Usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String Secret;

    public String generarToken(Usuario usuario) {
        var algoritmo = Algorithm.HMAC256(Secret);
        try {
            return JWT.create()
                    .withIssuer("inventario.rich")
                    .withSubject(usuario.getCorreo())
                    .withClaim("rol", usuario.getRol().getNombreRol())
                    .withExpiresAt(fechaExpiracion())
                    .sign(algoritmo);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error al generar token" + e.getMessage());
        }
    }
    private Instant fechaExpiracion(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-6"));
    }

    public String getSubject(String tokenJWT) {
        try{
        var algoritmo = Algorithm.HMAC256(Secret);
        return JWT.require(algoritmo)
                .withIssuer("inventario.rich")
                .build()
                .verify(tokenJWT)
                .getSubject();

        } catch (JWTVerificationException e){
            throw new RuntimeException("Token invalido o expirado");
        }
    }
}


