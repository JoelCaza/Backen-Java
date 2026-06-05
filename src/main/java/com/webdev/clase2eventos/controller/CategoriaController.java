package com.webdev.clase2eventos.controller;


import com.webdev.clase2eventos.model.Categoria;
import com.webdev.clase2eventos.model.Producto;
import com.webdev.clase2eventos.repository.CategoriaRepository;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/categorias")
@CrossOrigin(origins = "http://localhost:5173")

public class CategoriaController {
    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository){
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public List<Categoria> listar(){
        return categoriaRepository.findAll();
    }

    @PostMapping
    public Categoria crear(@RequestBody Categoria categoria){
        return categoriaRepository.save(categoria);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Long id){
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return categoria.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Long id,@RequestBody Categoria detalles){
        return categoriaRepository.findById(id).map(cat ->{
            cat.setNombre(detalles.getNombre());
            cat.setActiva(detalles.isActiva());
            cat.setDescripcion(detalles.getDescripcion());
            return ResponseEntity.ok(categoriaRepository.save(cat));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        if(categoriaRepository.existsById(id)){
            categoriaRepository.deleteById(id);
            return ResponseEntity.ok().build();

        }
        return ResponseEntity.notFound().build();
    }



}
