
document.getElementById('login-form').addEventListener('submit', async (e) => {
  e.preventDefault();

  const correo = document.getElementById('correo').value;
  const contrasena = document.getElementById('contrasena').value;
  const errorMsg = document.getElementById('error-msg');

  try {
    const res = await fetch('http://localhost:8080/usuarios/login', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({ correo, contrasena })
    });

    if (!res.ok) throw new Error('Credenciales inválidas');

    const data = await res.json();
    console.log("Token recibido en login:", data.tokenJWT);

    if (!data.tokenJWT) throw new Error('No se recibió token en la respuesta');

    localStorage.setItem('token', data.tokenJWT);
    window.location.href = 'dashboard.html';

  } catch (error) {
    console.error("Error en login:", error);
    errorMsg.textContent = error.message;
  }
});
