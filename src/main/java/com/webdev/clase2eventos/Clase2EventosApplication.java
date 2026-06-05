package com.webdev.clase2eventos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication

@EnableAsync
public class Clase2EventosApplication {

    public static void main(String[] args) {

        SpringApplication.run(Clase2EventosApplication.class, args);
    }

}
