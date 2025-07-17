CREATE SCHEMA conocimientos_sql;

/*
1.1) Describe el funcionamiento general de la sentencia JOIN 
La sentencia JOIN se emplea con el fin de combinar dos o más tablas en relación con sus 
claves primarias y foráneas u otra condición específica con el fin de  contrastar o complementar información, de esta manera 
se obtiene una combinación de información de tablas diferentes en una única consulta, 
en términos generales combina columnas de diferentes tablas y devuelve filas para analizar.

1.2) ¿Cuáles son los tipos de JOIN y cuál es el funcionamiento de los mismos?
1.- INNER JOIN: Devuelve las filas con coincidencias en ambas tablas.
2.- RIGHT JOIN: Devuelve todas las filas de la tabla de la derecha y únicamente las que coincidan en la izquierda, si un registro
no tiene coincidencias en la izquierda se muestran valores null.
3.- LEFT JOIN: Devuelve todas las filas de la tabla de la  izquierda y únicamente las que coincidan en la derecha, si un registro 
no tiene coincidencias en la derecha se muestran valores null.
4.- FULL JOIN: Devuelve todas las filas de las tablas, las que no tengan coincidencias se muestran con null
5.- CROSS JOIN: Devuelve todas las combinaciones posibles de filas

1.3) ¿Cuál es el funcionamiento general de los TRIGGER y que propósito tienen?
Los TRIGGER son un conjunto de instrucciones que se ejecutan automáticamente cuando ocurre un evento específico(INSERT, UPDATE, DELETE)
en un momento específico (BEFORE, AFTER) 
en la base de datos, se utilizan para  automatizar tareas o controlar las modificaciones no deseadas, su implementación
mejora la consistencia y ayuda a estandarizar las operaciones para conservar la integridad de los datos.

1.4)¿Qué es y para que sirve un STORED PROCEDURE?
un STORED PROCEDURE es un conjunto de instrucciones almacenadas en la base de datos que se ejecutan mediante llamadas directas por comandos o desde
aplicaciones, sirven para  automatizar tareas repetitivas, encapsular lógica compleja y simplificar su uso, optimizar 
rendimiento y seguridad en la integridad de los datos.

*/

CREATE TABLE productos(
idProducto INT PRIMARY KEY,
nombre VARCHAR(40) NOT NULL,
precio DECIMAL(16,2) NOT NULL
);

CREATE TABLE ventas(
idVENTA INT PRIMARY KEY,
idProducto INT,
cantidad INT NOT NULL,
CONSTRAINT fk_ventas_productos
FOREIGN KEY (idProducto)
REFERENCES productos(idProducto)
);

INSERT INTO productos(idProducto, nombre, precio)
VALUES
(1, "LAPTOP", 3000.00),
(2, "PC", 4000.00),
(3, "MOUSE", 100.00),
(4, "TECLADO", 150.00),
(5, "MONITOR", 2000.00),
(6, "MICROFONO", 350.00),
(7, "AUDIFONOS", 450.00);

INSERT INTO ventas(idventa, idProducto, cantidad)
VALUES
(1,5,8),
(2,1,15),
(3,6,13),
(4,6,4),
(5,2,3),
(6,5,1),
(7,4,5),
(8,2,5),
(9,6,2),
(10,1,8);


-- Todos los productos que tienen una venta.
SELECT p.idProducto, p.nombre, p.precio
FROM productos p
JOIN ventas v ON p.idProducto = v.idProducto;

-- Traer todos los productos que tengan ventas y la cantidad total de productos vendidos.
SELECT p.idProducto, p.nombre, SUM(v.cantidad) AS total_cantidad_ventas
FROM productos p
JOIN ventas v ON p.idProducto = v.idProducto
GROUP BY p.idProducto, p.nombre; 

-- Traer todos los productos (tengan ventas o no) y la suma total vendida por producto.
SELECT p.idProducto, p.nombre, IFNULL(SUM(v.cantidad * p.precio), 0) AS total_ingresos_ventas
FROM productos p
LEFT JOIN ventas v ON p.idProducto = v.idProducto
GROUP BY p.idProducto, p.nombre;






