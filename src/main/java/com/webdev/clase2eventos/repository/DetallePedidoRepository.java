package com.webdev.clase2eventos.repository;

import com.webdev.clase2eventos.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
List<DetallePedido> findByPedidoId(Long pedidoId);

}
