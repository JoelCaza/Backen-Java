package com.webdev.clase2eventos.controller;


import com.webdev.clase2eventos.model.Categoria;
import com.webdev.clase2eventos.model.Producto;
import com.webdev.clase2eventos.repository.CategoriaRepository;
import com.webdev.clase2eventos.repository.ProductoRepostiroy;
import com.webdev.clase2eventos.service.ProductoService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductoController {
    private final ProductoRepostiroy productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoService productoService;

    public ProductoController(ProductoRepostiroy productoRepository, CategoriaRepository categoriaRepository, ProductoService productoService) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoService = productoService;
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

    @GetMapping("/reporte-pdf")
    public ResponseEntity<byte[]> descargarReporte() {
        ByteArrayInputStream bis = productoService.generarReporteInventario();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte-inventario.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }



}
