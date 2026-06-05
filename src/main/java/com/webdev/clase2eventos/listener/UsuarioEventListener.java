package com.webdev.clase2eventos.listener;


import com.webdev.clase2eventos.eventos.UsuarioRegistradoEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UsuarioEventListener {

    @Async
    @EventListener
    public void enviarEmail(UsuarioRegistradoEvent evento){
        System.out.println("Email Hilo"+
                Thread.currentThread().getName());
        System.out.println("Email Envia a:"+evento.getEmail());
        try{
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Email Enviado a:"+evento.getEmail());
    }

    @EventListener
    public void crearPerfil(UsuarioRegistradoEvent evento){
        System.out.println("Perfil Creado para Id:"+evento.getUsuarioId());

    }
}
