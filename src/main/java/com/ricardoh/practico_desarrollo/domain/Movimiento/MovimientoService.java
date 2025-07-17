package com.ricardoh.practico_desarrollo.domain.Movimiento;


import com.ricardoh.practico_desarrollo.domain.Producto.ProductoRepository;
import com.ricardoh.practico_desarrollo.domain.Usuario.Usuario;
import com.ricardoh.practico_desarrollo.domain.Usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public void registrarMovimiento(DatosRegistroMovimientoDTO datos){
        var producto = productoRepository.findById(datos.idProducto())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        var usuario = usuarioRepository.findByCorreo(email);
        if (usuario == null) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }
        Movimiento movimiento = new Movimiento();
        movimiento.setMovimiento(datos.movimiento());
        movimiento.setCantidad(datos.cantidad());
        movimiento.setProducto(producto);
        movimiento.setUsuario((Usuario) usuario);

        movimientoRepository.save(movimiento);
    }
    public Page<MovimientoDTO> listarTodos(Pageable pageable){
        return movimientoRepository.findAll(pageable).map(MovimientoDTO::new);
    }
    public Page<MovimientoDTO>listarPorTipo(Movimiento.TipoMovimiento tipo, Pageable pageable){
        return movimientoRepository.findByMovimiento(tipo, pageable).map(MovimientoDTO::new);
    }
}
