function inicializarLayoutCompletoLogic() {
    
    const speciesList = document.getElementById("species-list");
    const inputBuscarEspecie = document.getElementById("buscar-especie");

    if (inputBuscarEspecie && speciesList) {
        inputBuscarEspecie.addEventListener("input", function () {
            const filtro = this.value.toLowerCase();
            const speciesItems = speciesList.querySelectorAll("li");
            speciesItems.forEach((li) => {
                li.style.display = li.textContent.toLowerCase().includes(filtro) ? "" : "none";
            });
        });

        speciesList.querySelectorAll("li").forEach((li) => {
            li.addEventListener("click", function() {
                const especieNome = li.textContent.trim();
                window.location.href = `/especie?nome=${encodeURIComponent(especieNome)}`;
            });
        });
    }

    const medicamentosList = document.querySelectorAll(".medication-list li");
    const inputBuscarMedicamento = document.getElementById("buscar-medicamento");

    if (inputBuscarMedicamento && medicamentosList.length > 0) {
        inputBuscarMedicamento.addEventListener("input", function () {
            const filtro = this.value.toLowerCase();
            medicamentosList.forEach((li) => {
                li.style.display = li.textContent.toLowerCase().includes(filtro) ? "" : "none";
            });
        });

        medicamentosList.forEach((li) => {
            li.addEventListener("click", function() {
                const selectMedicamentos = document.getElementById("medicamentos-select");
                if (selectMedicamentos) {
                    selectMedicamentos.value = li.textContent.trim();
                    selectMedicamentos.dispatchEvent(new Event('change'));
                }
            });
        });
    }

    window.abrirMinhaConta = async function () {
        try {
            const response = await fetch("/api/minha-conta");
            if (!response.ok) throw new Error("Erro ao obter dados da conta.");
            const dados = await response.json();

            const historicoLocal = JSON.parse(localStorage.getItem("historico-medicamentos")) || [];
            const ultimosCalculos = historicoLocal.map(item => `${item.medicamento} para ${item.especie} (${item.data})`);

            const mensagem = `
👤 Nome: ${dados.nome || 'Não informado'}
📧 Email: ${dados.email || 'Não informado'}

📋 Últimos Cálculos (local):
${ultimosCalculos.length > 0 ? ultimosCalculos.join("\n") : "Nenhum registrado"}
`.trim();

            alert(mensagem);
        } catch (err) {
            alert("Erro ao carregar informações da conta.");
            console.error("Erro ao abrir minha conta:", err);
        }
    };

    if (typeof inicializarCalculadoraCoreLogic === 'function') {
        inicializarCalculadoraCoreLogic();
    } else {
        console.error("inicializarCalculadoraCoreLogic não encontrada. Verifique o carregamento do script componenteCalculo.js.");
    }
}

document.addEventListener("DOMContentLoaded", async () => {
    inicializarLayoutCompletoLogic();

    try {
        const resposta = await fetch("/templates/componenteCalculo.html");
        const html = await resposta.text();
        document.getElementById("componente-calculo-container").innerHTML = html;

        if (typeof inicializarCalculadoraCoreLogic === "function") {
            inicializarCalculadoraCoreLogic();
        } else {
            console.error("Função de lógica da calculadora não encontrada.");
        }
    } catch (e) {
        console.error("Erro ao carregar componente de cálculo:", e);
    }
});
