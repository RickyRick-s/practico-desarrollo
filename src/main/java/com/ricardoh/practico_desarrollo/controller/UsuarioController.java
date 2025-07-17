package com.ricardoh.practico_desarrollo.controller;

import com.ricardoh.practico_desarrollo.domain.Rol.Rol;
import com.ricardoh.practico_desarrollo.domain.Rol.RolRepository;
import com.ricardoh.practico_desarrollo.domain.Usuario.DatosAutenticacionDTO;
import com.ricardoh.practico_desarrollo.domain.Usuario.DatosRegistroUsuarioDTO;
import com.ricardoh.practico_desarrollo.domain.Usuario.Usuario;
import com.ricardoh.practico_desarrollo.domain.Usuario.UsuarioRepository;
import com.ricardoh.practico_desarrollo.infrastructure.security.DatosJWTDTO;
import com.ricardoh.practico_desarrollo.infrastructure.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity iniciarSesion(@RequestBody @Valid DatosAutenticacionDTO datos ){
        var authenticationToken = new UsernamePasswordAuthenticationToken(datos.correo(), datos.contrasena());
        var authenticacion = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generarToken((Usuario) authenticacion.getPrincipal());
        return  ResponseEntity.ok(new DatosJWTDTO(tokenJWT));
    }

    @GetMapping("/me")
    public ResponseEntity<?> obtenerDatosUsuario(@AuthenticationPrincipal Usuario usuario) {
        if (usuario == null) {
            return ResponseEntity.status(401).body("No autenticado");
        }
        var datosUsuario = new Object() {
            public String nombre = usuario.getNombre();
            public String correo = usuario.getCorreo();
            public String rol = usuario.getRol().getNombreRol();
        };

        return ResponseEntity.ok(datosUsuario);
    }


    @Transactional
    @PostMapping("/registro")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<String> registrarUsuario(@RequestBody @Valid DatosRegistroUsuarioDTO datos){
        Rol rol = rolRepository.findById(datos.idRol())
                .orElseThrow(() -> new RuntimeException("Rol no existe"));

        Usuario nuevoUsuario = new Usuario(
                null,
                datos.nombre(),
                datos.correo(),
                passwordEncoder.encode(datos.contrasena()),
                1,
                rol
        );
        usuarioRepository.save(nuevoUsuario);
        return ResponseEntity.ok("Usuario registrado correctamente");
    }
}
