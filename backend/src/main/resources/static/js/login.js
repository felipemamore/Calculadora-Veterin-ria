let API_BASE_URL;

if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') {
    API_BASE_URL = 'http://localhost:8081'; 
} else {
    API_BASE_URL = 'https://calculadora-veterinaria-api.fly.dev';
}

document.addEventListener('DOMContentLoaded', function() {
    
    const togglePassword = document.querySelector('.toggle-password');
    const passwordInput = document.getElementById('senha');
    
    if (togglePassword && passwordInput) {
        togglePassword.addEventListener('click', function () {
    
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);
            
            const icon = this.querySelector('i');
            icon.classList.toggle('bi-eye-fill');
            icon.classList.toggle('bi-eye-slash-fill');
        });
    }

    const loginForm = document.getElementById("login-form");
    if (loginForm) {
        loginForm.addEventListener("submit", async function (e) {
            if (!loginForm.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
            } else {
                e.preventDefault();
                const email = document.getElementById("email").value;
                const senha = document.getElementById("senha").value;

                try {
                    const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
                        method: "POST",
                        headers: { "Content-Type": "application/json" },
                        body: JSON.stringify({ email, senha })
                    });

                    if (response.ok) {
                        const data = await response.json();
                        localStorage.setItem("jwtToken", data.token); 
                        window.location.href = "/home";
                    } else {
                        alert("Usuário ou senha inválidos.");
                    }
                } catch (error) {
                    alert("Erro ao tentar fazer login. Tente novamente.");
                    console.error(error);
                }
            }
            loginForm.classList.add('was-validated');
        });
    }
});