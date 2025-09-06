const API_BASE_URL = "https://calculadora-veterinaria-api.fly.dev";

document.getElementById("login-form").addEventListener("submit", async function (e) {
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
            alert("Login realizado!");
            window.location.href = "/home";
        } else {
            alert("Usuário ou senha inválidos.");
        }
    } catch (error) {
        alert("erro ao tentar fazer login. Tente novamente.");
        console.error(error);
    }
});