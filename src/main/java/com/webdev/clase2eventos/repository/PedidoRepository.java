package com.webdev.clase2eventos.repository;

import com.webdev.clase2eventos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido,Long> {
    List<Pedido> findByUsuarioId(Long usuarioId);
}
