package com.ricardoh.practico_desarrollo.domain.Movimiento;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroMovimientoDTO(
        @NotNull Movimiento.TipoMovimiento movimiento,
        @NotNull Integer idProducto,
        @NotNull @Min(1) Integer cantidad
) {
}
