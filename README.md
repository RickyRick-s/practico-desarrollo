# Sistema de Inventario - Spring Boot + JavaScript

Este proyecto es un sistema de control de inventario con autenticación basada en JWT, control de accesos por roles (`ADMINISTRADOR` y `ALMACENISTA`), historial de movimientos, y una interfaz web simple y funcional desarrollada con JavaScript, HTML y CSS.

---

## 🛠️ Tecnologías Utilizadas

- **IDE utilizado:** IntelliJ IDEA 2024.1.1  
- **Lenguaje de programación:** Java 17  
- **Framework:** Spring Boot 3.x  
- **Gestor de dependencias:** Maven  
- **DBMS utilizado:** MySQL 8.0  
- **Control de versiones de BD:** Flyway  
- **Frontend:** HTML + CSS + JavaScript
- **Autenticación:** JSON Web Token (JWT)  
- **Librerías externas:**
  - `auth0/java-jwt` para generación y validación de tokens  
  - `Lombok` para reducir código repetitivo  
  - `Spring Security`, `Spring Web`, `Spring Data JPA`

---

## 🚀 Cómo correr el proyecto

### Requisitos previos

- Tener instalado:
  - Java 17
  - MySQL 8.0 o superior
  - Maven
  - IDE como IntelliJ IDEA, Eclipse o VSCode
  - Un navegador web moderno (Chrome, Firefox, Edge...)

---

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/sistema-inventario.git
cd sistema-inventario
```

---

### 2. Crear la base de datos

En tu gestor MySQL, crea la base de datos vacía:

```sql
CREATE DATABASE inventario;
```

> ⚠️ **No necesitas crear tablas ni insertar datos manualmente.** Flyway lo hará automáticamente al ejecutar el proyecto.

---

### 3. Configurar propiedades

Edita el archivo `src/main/resources/application.properties` con los datos de conexión de tu base:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventario
spring.datasource.username=root
spring.datasource.password=tu_contraseña
```

---

### 4. Ejecutar la aplicación

Desde tu IDE:

- Ejecuta la clase `PracticoDesarrolloApplication.java`
- El servidor se iniciará en:  
  `http://localhost:8080`

---

### 5. Acceder al frontend

- Abre el proyecto frontend en un servidor local (se recomienda usar Live Server de VSCode).
- Asegúrate de que el frontend se sirva en `http://localhost:5500` para evitar problemas de CORS y que la comunicación con el backend funcione correctamente.
- Abre en tu navegador `http://localhost:5500/index.html`.
- Desde ahí, ingresa con tus credenciales para acceder al sistema.


Desde ahí podrás:

- Iniciar sesión
- Ver y editar inventario
- Registrar usuarios (solo ADMINISTRADOR)
- Registrar entradas/salidas
- Consultar historial de movimientos

---

## 👤 Usuarios precargados (Flyway)

Flyway crea automáticamente los siguientes usuarios y roles al iniciar el proyecto:

| Rol           | Correo                      | Contraseña |
|---------------|-----------------------------|------------|
| ADMINISTRADOR | `admin@outlook.com`         | `123`      |
| ALMACENISTA   | `almacenista@outlook.com`   | `123`      |

Puedes usarlos para probar las funcionalidades desde el frontend.

---

## 📂 Estructura del proyecto

```
├── src
│   ├── main
│   │   ├── java/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   └── domain/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/ (archivos Flyway)
│
├── frontend/
│   ├── dashboard.html
│   └── scripts/
│       └── dashboard.js
```

---

## 📌 Notas adicionales

- El backend está protegido con JWT y control de roles vía Spring Security.
- Las operaciones de entrada y salida generan movimientos registrados automáticamente.
- La aplicación soporta paginación tanto en el módulo de inventario como en el de movimientos.
- El ADMINISTRADOR puede ver todos los productos (activos e inactivos).
- El ALMACENISTA solo puede ver los productos activos.

---

## 🧑‍💻 Autor

Desarrollado por Ricardo Henaine

