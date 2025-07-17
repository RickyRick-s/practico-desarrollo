package com.ricardoh.practico_desarrollo.domain.Producto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Page<Producto> findByEstatus(Boolean estatus, Pageable pageable);
}
