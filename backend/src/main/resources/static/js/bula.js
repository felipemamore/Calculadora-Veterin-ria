const API_BASE_URL = "https://calculadora-veterinaria-api.fly.dev";

document.addEventListener("DOMContentLoaded", async function() {
    const nomeMedicamentoEl = document.getElementById("bula-nome");
    const conteudoBulaEl = document.getElementById("bula-content");
    const pageTitle = document.querySelector("title");

    const params = new URLSearchParams(window.location.search);
    const medicamentoNome = params.get("medicamento");

    if (!medicamentoNome) {
        nomeMedicamentoEl.textContent = "Medicamento não especificado";
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/api/bula?nome=${encodeURIComponent(medicamentoNome)}`);
        
        if (!response.ok) {
            throw new Error(`Bula não encontrada para ${medicamentoNome}. Status: ${response.status}`);
        }

        const bula = await response.json();

        pageTitle.textContent = bula.nome;
        nomeMedicamentoEl.textContent = bula.nome;

        let htmlContent = '';

        if (bula.indicacoes) htmlContent += `<div class="bula-section"><h2>Indicações</h2><p>${bula.indicacoes}</p></div>`;
        if (bula.contraIndicacoes) htmlContent += `<div class="bula-section"><h2>Contraindicações / Precauções</h2><p>${bula.contraIndicacoes}</p></div>`;
        if (bula.efeitosAdversos) htmlContent += `<div class="bula-section"><h2>Efeitos Adversos</h2><p>${bula.efeitosAdversos}</p></div>`;
        if (bula.reproducao) htmlContent += `<div class="bula-section"><h2>Reprodução, Gestação e Lactação</h2><p>${bula.reproducao}</p></div>`;
        if (bula.superdosagem) htmlContent += `<div class="bula-section"><h2>Superdosagem</h2><p>${bula.superdosagem}</p></div>`;
        if (bula.farmacodinamica) htmlContent += `<div class="bula-section"><h2>Farmacodinâmica</h2><p>${bula.farmacodinamica}</p></div>`;
        if (bula.farmacocinetica) htmlContent += `<div class="bula-section"><h2>Farmacocinética</h2><p>${bula.farmacocinetica}</p></div>`;
        if (bula.monitoramento) htmlContent += `<div class="bula-section"><h2>Monitoramento</h2><p>${bula.monitoramento}</p></div>`;

        conteudoBulaEl.innerHTML = htmlContent;

    } catch (error) {
        console.error(error);
        nomeMedicamentoEl.textContent = "Erro ao Carregar Bula";
        conteudoBulaEl.innerHTML = `<p class="text-danger">Não foi possível encontrar as informações para o medicamento solicitado. Por favor, tente novamente.</p>`;
    }
});