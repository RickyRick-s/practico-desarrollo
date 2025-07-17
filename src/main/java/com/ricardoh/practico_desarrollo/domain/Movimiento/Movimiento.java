package com.ricardoh.practico_desarrollo.domain.Movimiento;

import com.ricardoh.practico_desarrollo.domain.Producto.Producto;
import com.ricardoh.practico_desarrollo.domain.Usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idMovimiento")
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMovimiento;
    @Enumerated(EnumType.STRING)
    private TipoMovimiento movimiento;
    private Integer cantidad;
    private LocalDateTime fecha;
    @ManyToOne
    @JoinColumn(name = "idProducto", nullable = false)
    private Producto producto;
    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @PrePersist
    protected void autoFecha(){
        this.fecha = LocalDateTime.now();
    }

    public enum TipoMovimiento{
        entrada,
        salida
    }
}
