let API_BASE_URL;

if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') {
    API_BASE_URL = 'http://localhost:8081'; 
} else {
    API_BASE_URL = 'https://calculadora-veterinaria-api.fly.dev';
}

window.onload = function () {
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get("token");

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

    if (!token) {
        alert("Token inválido ou ausente.");
        document.getElementById("new-password-form").style.display = "none";
        return;
    }

    document.getElementById("new-password-form").addEventListener("submit", async function (e) {
        e.preventDefault();

        const novaSenha = document.getElementById("senha").value;
        const confirmarSenha = document.getElementById("confirmarSenha").value;

        if (novaSenha !== confirmarSenha) {
            alert("As senhas não coincidem.");
            return;
        }

        try {
            const response = await fetch(`${API_BASE_URL}/api/senha/reset-password?token=${encodeURIComponent(token)}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ novaSenha: novaSenha })
            });

            const resultado = await response.text();

            if (response.ok) {
                alert("Senha redefinida com sucesso!");
                window.location.href = "/pagina-login";
            } else {
                alert("Erro: " + resultado);
            }
        } catch (error) {
            alert("Erro ao redefinir a senha. Tente novamente mais tarde.");
        }
    });
};