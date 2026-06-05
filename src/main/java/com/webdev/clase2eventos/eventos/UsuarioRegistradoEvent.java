package com.webdev.clase2eventos.eventos;

import org.springframework.context.ApplicationEvent;

public class UsuarioRegistradoEvent extends ApplicationEvent {
    private final Long usuarioId;
    private final String nombre;
    private final String email;

    public UsuarioRegistradoEvent(
        Object source,
        Long usuarioId,
        String nombre,
        String email){
        super(source);
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.email = email;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }
}
