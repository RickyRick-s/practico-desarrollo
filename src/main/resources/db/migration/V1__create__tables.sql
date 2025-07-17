CREATE TABLE roles(
idRol INT PRIMARY KEY AUTO_INCREMENT,
nombreRol VARCHAR(30) NOT NULL
);

CREATE TABLE usuarios(
idUsuario INT PRIMARY KEY AUTO_INCREMENT,
nombre VARCHAR(100) NOT NULL,
correo VARCHAR(50) NOT NULL UNIQUE,
contrasena VARCHAR(255) NOT NULL,
idRol INT NOT NULL,
estatus INT NOT NULL,
CONSTRAINT fk_usuarios_roles FOREIGN KEY (idRol) REFERENCES roles(idRol)
);

CREATE TABLE productos(
idProducto INT PRIMARY KEY AUTO_INCREMENT,
nombre VARCHAR(100) NOT NULL,
cantidad INT NOT NULL DEFAULT 0,
estatus BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE movimientos(
idMovimiento INT PRIMARY KEY AUTO_INCREMENT,
movimiento ENUM('entrada', 'salida') NOT NULL,
cantidad INT NOT NULL,
fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
idProducto INT NOT NULL,
idUsuario INT NOT NULL,
CONSTRAINT fk_movimientos_productos FOREIGN KEY (idProducto) REFERENCES productos(idProducto),
CONSTRAINT fk_movimientos_usuarios FOREIGN KEY (idUsuario) REFERENCES usuarios(idUsuario)
);