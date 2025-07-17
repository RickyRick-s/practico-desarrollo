package com.ricardoh.practico_desarrollo.infrastructure.security;

import com.ricardoh.practico_desarrollo.domain.Usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.antlr.v4.runtime.Token;
import org.aspectj.weaver.patterns.ITokenSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            var tokenJWT = recuperarToken(request);
            System.out.println("Token recibido: " + tokenJWT);
            if (tokenJWT != null) {
                var subject = tokenService.getSubject(tokenJWT);
                System.out.println("Subject obtenido: " + subject);
                var usuario = usuarioRepository.findByCorreo(subject);
                System.out.println("Usuario encontrado: " + (usuario != null ? usuario.getUsername() : "null"));
                if (usuario != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (RuntimeException e) {
            SecurityContextHolder.clearContext();
            System.out.println("Error en filtro seguridad: " + e.getMessage());
        }
        filterChain.doFilter(request, response);
    }


    private String recuperarToken(HttpServletRequest request) {
        var authentincationHeader = request.getHeader("Authorization");
        if (authentincationHeader != null){
            return authentincationHeader.replace("Bearer ", "");
        }
        return null;
    }
}
