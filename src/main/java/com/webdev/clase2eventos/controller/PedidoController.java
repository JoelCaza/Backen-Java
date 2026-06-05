package com.webdev.clase2eventos.controller;


import com.webdev.clase2eventos.model.DetallePedido;
import com.webdev.clase2eventos.model.Pedido;
import com.webdev.clase2eventos.model.Producto;
import com.webdev.clase2eventos.model.Usuario;
import com.webdev.clase2eventos.repository.PedidoRepository;
import com.webdev.clase2eventos.repository.ProductoRepostiroy;
import com.webdev.clase2eventos.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/pedidos")
@CrossOrigin(origins = "http://localhost:5173")

public class PedidoController {
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepostiroy productoRepostiroy;

    public PedidoController(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository, ProductoRepostiroy productoRepostiroy) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepostiroy = productoRepostiroy;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> crearPedido(@RequestBody List<Map<String, Object>> carrito, Principal principal) {
        String email = principal.getName();
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("erro", "Usuario no encotnrado"));
        }
        Usuario usuario = usuarioOpt.get();

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setUsuario(usuario);
        List<DetallePedido> detalles = new ArrayList<>();
        BigDecimal totalCalculado = BigDecimal.ZERO;

        for (Map<String, Object> item : carrito) {
            Long productoId = Long.valueOf(item.get("productoId").toString());
            Integer cantidad = (Integer) item.get("cantidad");
            Optional<Producto> productOpt = productoRepostiroy.findById(productoId);
            if (productOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Producto Id" + productoId + "no existe"));
            }

            Producto producto = productOpt.get();
            if (producto.getStock() < cantidad) {
                return ResponseEntity.badRequest().body(Map.of("Error", "No hay suficiente stock" + producto.getNombre()));
            }

            producto.setStock(producto.getStock() - cantidad);
            productoRepostiroy.save(producto);

            //Crear el detalle
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(nuevoPedido);
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecio());

            BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
            totalCalculado = totalCalculado.add(subtotal);

            detalles.add(detalle);
        }
        //Asignar el toal y los detalles al pedido y guardamos

        nuevoPedido.setTotal(totalCalculado);
        nuevoPedido.setDetalles(detalles);


        Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);
        return ResponseEntity.ok(Map.of(
                "mensaje", "Pedido generado exitosamente",
                "pedidoId", pedidoGuardado.getId(),
                "total", pedidoGuardado.getTotal(),
                "estado", pedidoGuardado.getEstado()
        ));
    }
        @GetMapping("/mis-compras")
    public ResponseEntity<?> verMisCompras(Principal principal){
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email).get();

        List<Pedido> misPedidos = pedidoRepository.findByUsuarioId(usuario.getId());
        return ResponseEntity.ok(misPedidos);
    }

}



