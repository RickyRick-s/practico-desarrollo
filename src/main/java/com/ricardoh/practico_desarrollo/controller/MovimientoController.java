package com.ricardoh.practico_desarrollo.controller;

import com.ricardoh.practico_desarrollo.domain.Movimiento.Movimiento;
import com.ricardoh.practico_desarrollo.domain.Movimiento.MovimientoDTO;
import com.ricardoh.practico_desarrollo.domain.Movimiento.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Page<MovimientoDTO>> listarTodos( @PageableDefault(size = 10, sort = "fecha")Pageable pageable){
        Page<MovimientoDTO> movimientos = movimientoService.listarTodos(pageable);
        return ResponseEntity.ok(movimientos);
        }

    @GetMapping("/tipo/{tipo}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Page<MovimientoDTO>> listarPorTipo(
            @PathVariable Movimiento.TipoMovimiento tipo,
            @PageableDefault(size = 10, sort = "fecha") Pageable pageable) {
        Page<MovimientoDTO> movimientos = movimientoService.listarPorTipo(tipo, pageable);
        return ResponseEntity.ok(movimientos);
        }
    }





