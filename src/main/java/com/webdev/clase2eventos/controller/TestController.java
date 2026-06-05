package com.webdev.clase2eventos.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ecommerce")
public class TestController {
    @GetMapping("/perfil")
    public Map<String,String> perfilProtegido(){
        return Map.of("Mensaje","Tienes acceso a esta ruta protegida");
    }
}
