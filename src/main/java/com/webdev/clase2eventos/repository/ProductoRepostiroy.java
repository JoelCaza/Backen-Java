package com.webdev.clase2eventos.repository;

import com.webdev.clase2eventos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepostiroy extends JpaRepository<Producto,Long> {
    List<Producto> findByCategoriaId(Long categoriaId);
}
