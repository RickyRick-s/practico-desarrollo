package com.ricardoh.practico_desarrollo.domain.Movimiento;

import java.time.LocalDateTime;

public record MovimientoDTO(
        Integer idMovimiento,
        Movimiento.TipoMovimiento movimiento,
        Integer cantidad,
        LocalDateTime fecha,
        String producto,
        String usuario

) {
    public MovimientoDTO(Movimiento movimiento){
        this(
                movimiento.getIdMovimiento(),
                movimiento.getMovimiento(),
                movimiento.getCantidad(),
                movimiento.getFecha(),
                movimiento.getProducto().getNombre(),
                movimiento.getUsuario().getNombre()
        );
    }
}
