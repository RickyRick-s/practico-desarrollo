// === Token y datos del usuario ===
const token = localStorage.getItem('token');
const nombreUsuarioSpan = document.getElementById('nombreUsuario');
const rolUsuarioSpan = document.getElementById('rolUsuario');

// === Botones principales ===
const logoutBtn = document.getElementById('logoutBtn');
const btnInventario = document.getElementById('btnInventario');
const btnMovimientos = document.getElementById('btnMovimientos');
const btnRegistrarProducto = document.getElementById('btnRegistrarProducto');
const btnRegistrarUsuario = document.getElementById('btnRegistrarUsuario');

// === Secciones ===
const seccionInventario = document.getElementById('seccionInventario');
const seccionMovimientos = document.getElementById('seccionMovimientos');
const seccionRegistroUsuario = document.getElementById('seccionRegistrarUsuario');

// === Tablas y paginación ===
const tablaInventarioBody = document.getElementById('tablaInventarioBody');
const paginacionInventario = document.getElementById('paginacionInventario');
const tablaMovimientosBody = document.getElementById('tablaMovimientosBody');
const paginacionMovimientos = document.getElementById('paginacionMovimientos');

// === Formulario de registro de usuario ===
const formRegistroUsuario = document.getElementById('formRegistroUsuario');
const mensajeRegistro = document.getElementById('mensajeRegistro');
const nombreNuevoInput = document.getElementById('nombreNuevo');
const correoNuevoInput = document.getElementById('correoNuevo');
const contrasenaNuevoInput = document.getElementById('contrasenaNuevo');
const rolNuevoSelect = document.getElementById('rolNuevo');

let paginaInventario = 0;
let paginaMovimientos = 0;
const tamanioPagina = 10;
let tipoMovimientoActual = 'todos';

(async function inicializarDashboard() {
  if (!token) return redirigirAlLogin();

  try {
    const usuario = await obtenerUsuarioActual(token);
    configurarVistaPara(usuario);
  } catch (error) {
    console.error('Error al autenticar usuario:', error);
    redirigirAlLogin();
  }
})();

function redirigirAlLogin() {
  window.location.href = 'index.html';
}

async function obtenerUsuarioActual(token) {
  const res = await fetch('http://localhost:8080/usuarios/me', {
    headers: { 'Authorization': `Bearer ${token}` }
  });

  if (!res.ok) throw new Error('Token inválido o expirado');
  return res.json();
}

function configurarVistaPara(usuario) {
  nombreUsuarioSpan.textContent = usuario.nombre;
  rolUsuarioSpan.textContent = usuario.rol;

  configurarAccesos(usuario.rol);
  mostrarSeccion('inventario');
  mostrarInventario();
}

function configurarAccesos(rol) {
  const esAdmin = rol === 'ADMINISTRADOR';

  btnMovimientos.disabled = !esAdmin;

  if (btnRegistrarProducto) {
    btnRegistrarProducto.style.display = esAdmin ? 'inline-block' : 'none';
    if (esAdmin) {
      btnRegistrarProducto.onclick = async () => {
        const nombre = prompt('Nombre del nuevo producto:');
        if (!nombre?.trim()) return alert('Nombre inválido');

        try {
          await registrarProducto(nombre.trim());
          alert('Producto registrado exitosamente');
          mostrarInventario();
        } catch (error) {
          alert(error.message);
        }
      };
    }
  }
}

async function registrarProducto(nombre) {
  const res = await fetch('http://localhost:8080/productos/registrar', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({ nombre })
  });

  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.mensaje || 'Error al registrar producto');
  }

  return res.json();
}

btnInventario.addEventListener('click', () => {
  mostrarSeccion('inventario');
  mostrarInventario();
});

btnMovimientos.addEventListener('click', () => {
  mostrarSeccion('movimientos');
  mostrarMovimientos();
});

logoutBtn.addEventListener('click', () => {
  localStorage.removeItem('token');
  window.location.href = 'index.html';
});

btnRegistrarUsuario.addEventListener('click', () => {
  const rol = rolUsuarioSpan.textContent;
  if (rol !== 'ADMINISTRADOR') return;
  mostrarSeccion('registro');
});

function mostrarSeccion(seccion) {
  seccionInventario.style.display = seccion === 'inventario' ? 'block' : 'none';
  seccionMovimientos.style.display = seccion === 'movimientos' ? 'block' : 'none';
  seccionRegistroUsuario.style.display = seccion === 'registro' ? 'block' : 'none';
}

async function mostrarInventario() {
  try {
    const token = localStorage.getItem('token');
    if (!token) throw new Error('Token no encontrado');

    const payload = JSON.parse(atob(token.split('.')[1]));
    const rolUsuario = payload?.rol || '';

    const url = rolUsuario === 'ADMINISTRADOR'
      ? `http://localhost:8080/productos/inventario?page=${paginaInventario}&size=${tamanioPagina}&sort=idProducto`
      : `http://localhost:8080/productos/activos?page=${paginaInventario}&size=${tamanioPagina}&sort=idProducto`;

    const res = await fetch(url, {
      headers: {
        'Authorization': 'Bearer ' + token
      }
    });

    if (!res.ok) throw new Error('Error al obtener inventario');

    const datos = await res.json();
    const productos = datos.content || [];

    tablaInventarioBody.innerHTML = '';

    productos.forEach(p => {
      const fila = document.createElement('tr');
      fila.innerHTML = `
        <td>${p.idProducto}</td>
        <td>${p.nombre}</td>
        <td>${p.cantidad}</td>
        <td>${p.estatus ? 'Activo' : 'Inactivo'}</td>
        <td>
          <button onclick="editarProducto(${p.idProducto}, '${rolUsuario}')">Editar</button>
          ${rolUsuario === 'ADMINISTRADOR' ? `<button onclick="cambiarEstatus(${p.idProducto})">Cambiar estatus</button>` : ''}
        </td>
      `;
      tablaInventarioBody.appendChild(fila);
    });

    actualizarPaginacionInventario(datos.totalPages);
  } catch (error) {
    alert(error.message);
  }
}

async function editarProducto(idProducto, rol) {
  if (rol === 'ADMINISTRADOR') {
    const cantidad = parseInt(prompt('¿Cuántos productos desea agregar?'), 10);
    if (isNaN(cantidad) || cantidad <= 0) return alert('Cantidad inválida');
    await registrarEntrada(idProducto, cantidad);
  } else if (rol === 'ALMACENISTA') {
    const cantidad = parseInt(prompt('¿Cuántos productos desea quitar?'), 10);
    if (isNaN(cantidad) || cantidad <= 0) return alert('Cantidad inválida');
    await registrarSalida(idProducto, cantidad);
  } else {
    alert('No tienes permiso para editar productos.');
  }
}

function actualizarPaginacionInventario(totalPaginas) {
  paginacionInventario.innerHTML = '';

  for (let i = 0; i < totalPaginas; i++) {
    const btn = document.createElement('button');
    btn.textContent = i + 1;
    btn.className = i === paginaInventario ? 'activo' : '';
    btn.addEventListener('click', () => {
      paginaInventario = i;
      mostrarInventario();
    });
    paginacionInventario.appendChild(btn);
  }
}

async function mostrarMovimientos() {
  if (rolUsuarioSpan.textContent === 'ALMACENISTA') return;

  try {
    let url = '';
    if (tipoMovimientoActual === 'todos') {
      url = `http://localhost:8080/movimientos?page=${paginaMovimientos}&size=${tamanioPagina}`;
    } else {
      url = `http://localhost:8080/movimientos/tipo/${tipoMovimientoActual}?page=${paginaMovimientos}&size=${tamanioPagina}`;
    }

    const res = await fetch(url, {
      headers: { 'Authorization': 'Bearer ' + token }
    });

    if (!res.ok) throw new Error('Error al obtener movimientos');

    const datos = await res.json();
    mostrarTablaMovimientos(datos.content);
    actualizarPaginacionMovimientos(datos.totalPages);
  } catch (error) {
    alert(error.message);
  }
}

function mostrarTablaMovimientos(movimientos) {
  tablaMovimientosBody.innerHTML = '';
  movimientos.forEach(m => {
    const fila = document.createElement('tr');
    fila.innerHTML = `
      <td>${m.idMovimiento}</td>
      <td>${m.producto.nombre || m.producto}</td>
      <td>${m.movimiento}</td>
      <td>${m.cantidad}</td>
      <td>${new Date(m.fecha).toLocaleString()}</td>
      <td>${m.usuario.nombre || m.usuario}</td>
    `;
    tablaMovimientosBody.appendChild(fila);
  });
}

function filtrarMovimientos(tipo) {
  paginaMovimientos = 0;
  tipoMovimientoActual = tipo;
  mostrarMovimientos();
}

function actualizarPaginacionMovimientos(totalPaginas) {
  paginacionMovimientos.innerHTML = '';

  for (let i = 0; i < totalPaginas; i++) {
    const btn = document.createElement('button');
    btn.textContent = i + 1;
    btn.className = i === paginaMovimientos ? 'activo' : '';
    btn.addEventListener('click', () => {
      paginaMovimientos = i;
      mostrarMovimientos();
    });
    paginacionMovimientos.appendChild(btn);
  }
}

async function registrarEntrada(idProducto, cantidad) {
  try {
    const res = await fetch('http://localhost:8080/productos/agregar-inventario/', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ idProducto, cantidad })
    });

    if (!res.ok) {
      const err = await res.json().catch(() => ({}));
      throw new Error(err.mensaje || 'Error al registrar entrada');
    }

    alert('Entrada registrada exitosamente');
    mostrarInventario();
    mostrarMovimientos();
  } catch (error) {
    alert('Error: ' + error.message);
  }
}

async function registrarSalida(idProducto, cantidad) {
  try {
    const res = await fetch('http://localhost:8080/productos/sacar-inventario/', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ idProducto, cantidad })
    });

    if (!res.ok) {
      const err = await res.json().catch(() => ({}));
      throw new Error(err.mensaje || 'Error al registrar salida');
    }

    alert('Salida registrada exitosamente');
    mostrarInventario();
    mostrarMovimientos();
  } catch (error) {
    alert('Error: ' + error.message);
  }
}

async function cambiarEstatus(idProducto) {
  try {
    const res = await fetch('http://localhost:8080/productos/cambiar-estatus', {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ idProducto, nuevoEstatus: null })
    });

    if (!res.ok) {
      const err = await res.json().catch(() => ({}));
      throw new Error(err.mensaje || 'Error al cambiar estatus');
    }

    alert('Estatus cambiado correctamente');
    mostrarInventario();
  } catch (error) {
    alert('Error: ' + error.message);
  }
}

formRegistroUsuario.addEventListener('submit', async (e) => {
  e.preventDefault();

  const nombre = nombreNuevoInput.value.trim();
  const correo = correoNuevoInput.value.trim();
  const contrasena = contrasenaNuevoInput.value.trim();
  const rol = rolNuevoSelect.value;

  if (!nombre || !correo || !contrasena || !rol) {
    mensajeRegistro.textContent = 'Todos los campos son obligatorios';
    mensajeRegistro.style.color = 'red';
    return;
  }

  try {
    const token = localStorage.getItem('token');

    const res = await fetch('http://localhost:8080/usuarios/registro', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token
      },
      body: JSON.stringify({
        nombre,
        correo,
        contrasena,
        idRol: rol === 'ADMINISTRADOR' ? 1 : 2
      })
    });

    if (!res.ok) {
      const err = await res.json().catch(() => ({}));
      throw new Error(err.mensaje || 'Error al registrar usuario');
    }

    mensajeRegistro.textContent = 'Usuario registrado exitosamente';
    mensajeRegistro.style.color = 'green';
    formRegistroUsuario.reset();

    setTimeout(() => {
      mostrarSeccion('inventario');
      mostrarInventario();
    }, 1500);
  } catch (error) {
    mensajeRegistro.textContent = error.message;
    mensajeRegistro.style.color = 'red';
  }
});
