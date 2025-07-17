package com.ricardoh.practico_desarrollo.controller;

import com.ricardoh.practico_desarrollo.domain.Producto.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    ProductoService productoService;



    @PostMapping("/registrar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<DatosRespuestaProductoDTO> registrarProducto(@RequestBody @Valid DatosRegistroProductoDTO datos,
                                                                       UriComponentsBuilder uriComponentsBuilder){
        var producto = productoService.registrarProducto(datos);
        var uri = uriComponentsBuilder.path("/productos/{id}").buildAndExpand(producto.getIdProducto()).toUri();
        return ResponseEntity.created(uri).body(new DatosRespuestaProductoDTO(producto));
    }

    @GetMapping("/inventario")
    public ResponseEntity<Page<DatosRespuestaProductoDTO>> verInventario(@PageableDefault(size = 10,sort = {"idProducto"}) Pageable pageable) {
        return ResponseEntity.ok(productoService.listarTodos(pageable).map(DatosRespuestaProductoDTO::new));
    }

    @GetMapping("/activos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ALMACENISTA')")
    public ResponseEntity<Page<DatosRespuestaProductoDTO>> consultarActivos(@PageableDefault(size = 10,sort = {"idProducto"})Pageable pageable) {
        return ResponseEntity.ok(productoService.listarActivos(pageable).map(DatosRespuestaProductoDTO::new));
    }

    @GetMapping("/inactivos")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Page<DatosRespuestaProductoDTO>> consultarInactivos(@PageableDefault(size = 10,sort = {"idProducto"})Pageable pageable) {
        return ResponseEntity.ok(productoService.listarInactivos(pageable).map(DatosRespuestaProductoDTO::new));
    }


    @PostMapping("/agregar-inventario/")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<DatosRespuestaProductoDTO> agregarInventario( @RequestBody @Valid DatosCantidadInventarioDTO datos){
        var producto = productoService.agregarInventario(datos.idProducto(), datos.cantidad());
        return ResponseEntity.ok(new DatosRespuestaProductoDTO(producto));
    }


    @PostMapping("/sacar-inventario/")
    @PreAuthorize("hasRole('ALMACENISTA')")
    public ResponseEntity<DatosRespuestaProductoDTO> sacarInventario(@RequestBody @Valid DatosCantidadInventarioDTO datos){
        var producto = productoService.sacarInventario(datos.idProducto(), datos.cantidad());
        return ResponseEntity.ok(new DatosRespuestaProductoDTO(producto));
    }


    @PatchMapping("/cambiar-estatus")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<DatosRespuestaProductoDTO> cambiarEstatus(@RequestBody DatosCambioEstatusDTO datos){
        var producto = productoService.modificarEstatus(datos);
        return ResponseEntity.ok(new DatosRespuestaProductoDTO(producto));
    }

}
