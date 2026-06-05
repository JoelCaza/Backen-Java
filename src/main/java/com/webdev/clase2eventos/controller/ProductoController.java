package com.webdev.clase2eventos.controller;


import com.webdev.clase2eventos.model.Categoria;
import com.webdev.clase2eventos.model.Producto;
import com.webdev.clase2eventos.repository.CategoriaRepository;
import com.webdev.clase2eventos.repository.ProductoRepostiroy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductoController {
    private final ProductoRepostiroy productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoController(ProductoRepostiroy productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }
    @GetMapping
    public List<Producto> listar(){
        return productoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id){
        Optional<Producto> producto = productoRepository.findById(id);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public  ResponseEntity<?> crear(@RequestBody Producto producto){
        if (producto.getCategoria() == null || producto.getCategoria().getId()== null){
            return ResponseEntity.badRequest().body("La categoria es obligatoria");
        }
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(producto.getCategoria().getId());
        if (categoriaOpt.isEmpty()){
            return ResponseEntity.badRequest().body("La categoria no existe");
        }
        producto.setCategoria(categoriaOpt.get());
        return ResponseEntity.ok(productoRepository.save(producto));
    }



}
