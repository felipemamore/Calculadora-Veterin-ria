let API_BASE_URL;

if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') {
    API_BASE_URL = 'http://localhost:8081'; 
} else {
    API_BASE_URL = 'https://calculadora-veterinaria-api.fly.dev';
}

document.addEventListener("DOMContentLoaded", function() {
    
    const form = document.querySelector(".cadastro-form");
    if (form) {
        form.addEventListener("submit", async function (e) {
            e.preventDefault();

            const nomeCompleto = document.getElementById("nomeCompleto").value;
            const email = document.getElementById("email").value;
            const senha = document.getElementById("senha").value;

            const payload = {
              nomeCompleto: nomeCompleto,
              email: email,
              senha: senha
            };

            try {
              const response = await fetch(`${API_BASE_URL}/api/users`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
              });

              if (response.ok) {
                alert("Cadastro realizado com sucesso! Faça o login para continuar.");
                window.location.href = "/pagina-login";
              } else {
                const error = await response.text();
                alert("Erro ao cadastrar: " + error);
              }
            } catch (error) {
              console.error("Erro inesperado:", error);
              alert("Erro inesperado ao tentar se cadastrar.");
            }
        });
    } else {
        console.error("Elemento .cadastro-form não foi encontrado na página.");
    }
});