package com.ricardoh.practico_desarrollo.domain.Producto;

public record DatosRespuestaProductoDTO(
        Integer idProducto,
        String nombre,
        Integer cantidad,
        Boolean estatus
) {
    public DatosRespuestaProductoDTO(Producto producto){
        this(producto.getIdProducto(), producto.getNombre(), producto.getCantidad(), producto.getEstatus());
    }
}
