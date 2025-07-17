package com.ricardoh.practico_desarrollo.domain.Producto;

import jakarta.validation.constraints.NotBlank;

public record DatosRegistroProductoDTO(@NotBlank  String nombre) {
}
