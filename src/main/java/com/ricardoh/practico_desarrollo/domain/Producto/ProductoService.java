package com.ricardoh.practico_desarrollo.domain.Producto;

import com.ricardoh.practico_desarrollo.domain.Movimiento.DatosRegistroMovimientoDTO;
import com.ricardoh.practico_desarrollo.domain.Movimiento.Movimiento;
import com.ricardoh.practico_desarrollo.domain.Movimiento.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MovimientoService movimientoService;

    @Transactional
    public Producto registrarProducto(DatosRegistroProductoDTO datos){
        Producto producto = new Producto();
        producto.setNombre(datos.nombre());
        producto.setCantidad(0);
        producto.setEstatus(true);
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto agregarInventario(Integer idProducto, Integer cantidad){
        if (cantidad <= 0){
            throw new RuntimeException("Ingrese una cantidad valida");
        }
        var producto = productoRepository.findById(idProducto)
                .orElseThrow(() ->new RuntimeException("Producto no encontrado"));
        producto.setCantidad(producto.getCantidad()+cantidad);

        movimientoService.registrarMovimiento(new DatosRegistroMovimientoDTO(
                Movimiento.TipoMovimiento.entrada,
                idProducto,
                cantidad
        ));

        return producto;
    }

    @Transactional
    public Producto sacarInventario(Integer idProducto, Integer cantidad){
        if (cantidad <= 0){
            throw new RuntimeException("Ingrese una cantidad valida");
        }
        var producto = productoRepository.findById(idProducto)
                .orElseThrow(() ->new RuntimeException("Producto no encontrado"));
        if (producto.getCantidad() - cantidad < 0){
            throw new RuntimeException("No hay inventario suficiente");
        }

        producto.setCantidad(producto.getCantidad() - cantidad);

        movimientoService.registrarMovimiento(new DatosRegistroMovimientoDTO(
                Movimiento.TipoMovimiento.salida,
                idProducto,
                cantidad
        ));

        return producto;
    }

    public Page<Producto> listarTodos(Pageable pageable){
        return productoRepository.findAll(pageable);
    }

    public Page<Producto> listarActivos(Pageable pageable){
        return productoRepository.findByEstatus(true, pageable);
    }
    public Page<Producto> listarInactivos(Pageable pageable){
        return productoRepository.findByEstatus(false, pageable);
    }

    @Transactional
    public Producto modificarEstatus(DatosCambioEstatusDTO datos){
        var producto = productoRepository.findById(datos.idProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setEstatus(!producto.getEstatus());
        return productoRepository.save(producto);
    }

}
