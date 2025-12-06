let API_BASE_URL;

if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') {
    API_BASE_URL = 'http://localhost:8081'; 
} else {
    API_BASE_URL = 'https://calculadora-veterinaria-api.fly.dev';
}

function validarSenhaForte(senha) {
    const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
    return regex.test(senha);
}

document.addEventListener("DOMContentLoaded", function() {
    const togglePasswordIcons = document.querySelectorAll(".toggle-password");

    togglePasswordIcons.forEach(icon => {
        icon.addEventListener("click", function () {

            const passwordField = this.previousElementSibling;

            if (passwordField.type === "password") {
                passwordField.type = "text";
              
                this.classList.remove("fa-eye");
                this.classList.add("fa-eye-slash");
            } else {
                passwordField.type = "password";
                
                this.classList.remove("fa-eye-slash");
                this.classList.add("fa-eye");
            }
        });
    });

    const form = document.querySelector(".cadastro-form");
    if (form) {
        form.addEventListener("submit", async function (e) {
            e.preventDefault();

            const nomeCompleto = document.getElementById("nomeCompleto").value;
            const email = document.getElementById("email").value;
            const senha = document.getElementById("senha").value;
            const confirmarSenha = document.getElementById("confirmarSenha").value;

            if (senha !== confirmarSenha) {
                alert("As senhas não correspondem. Por favor, tente novamente.");
                return;
            }
            if (!validarSenhaForte(senha)) {
                alert("A senha é muito fraca!\n\nEla deve conter:\n- Pelo menos 8 caracteres\n- Letra maiúscula e minúscula\n- Um número\n- Um caractere especial (ex: @, #, $)");
                return;
            }

            const payload = { nomeCompleto, email, senha };

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
    }
});