package com.webdev.clase2eventos.controller;


import com.webdev.clase2eventos.eventos.UsuarioRegistradoEvent;
import com.webdev.clase2eventos.model.Usuario;
import com.webdev.clase2eventos.repository.UsuarioRepository;
import com.webdev.clase2eventos.security.JwtUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.webdev.clase2eventos.repository.UsuarioRepository;

import javax.swing.text.html.Option;

@RestController
@RequestMapping("/api/v1/auth")
//Aqui se encuentra el endpoint para registrarse y logearse
@CrossOrigin(origins = "http://localhost:5173")

public class AuthController {

    private final ApplicationEventPublisher publisher;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(ApplicationEventPublisher pub, UsuarioRepository usuarioRepository, JwtUtil jwtUtil
    , PasswordEncoder passwordEncoder) {
        this.publisher = pub;
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil= jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registro")
    public ResponseEntity<Map<String, Object>> registrar(
            @RequestBody Map<String, String> body) {
        String nombre = body.get("nombre");
        String email = body.get("email");
        String password = body.get("password");
        if (nombre == null || email == null || password == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Nombre,email,contraseña son requeridos"));
        }

        if (usuarioRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El correo ya esta registrado"));
        }
        String passwordHasheada = passwordEncoder.encode(password);
        Usuario nuevo = new Usuario(nombre, email, passwordHasheada);
        usuarioRepository.save(nuevo);

        publisher.publishEvent(
                new UsuarioRegistradoEvent(this, nuevo.getId(), nombre, email)
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("Mensaje", "Registro exitoso", "id", nuevo.getId()));
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        if (email == null || password == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Email y password son requeridos"));
        }
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent() && passwordEncoder.matches(password,usuarioOpt.get().getPassword())){
            Usuario usuario = usuarioOpt.get();

            String token = jwtUtil.generarToken(usuario.getEmail());
            return ResponseEntity.ok(Map.of(
                    "Mensaje","Login Exitoso",
                    "token",token,
                    "id",usuario.getId(),
                    "nombre",usuario.getNombre()
            ));

        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("Error","Credenciales Incorrectas"));
    }
    //

}