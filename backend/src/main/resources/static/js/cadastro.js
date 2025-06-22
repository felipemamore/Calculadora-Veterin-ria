document.querySelector(".cadastro-form").addEventListener("submit", async function (e) {
    e.preventDefault();

    const nomeCompleto = document.getElementById("nomeCompleto").value;
    const rg = document.getElementById("rg").value;
    const cpf = document.getElementById("cpf").value;
    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;

    const payload = {
      nomeCompleto: nomeCompleto,
      rg: rg,
      cpf: cpf,
      email: email,
      senha: senha
    };

    try {
      const response = await fetch("/api/users", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      if (response.ok) {
        alert("Cadastro realizado com sucesso!");
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