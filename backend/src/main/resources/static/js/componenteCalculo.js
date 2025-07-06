function inicializarCalculadoraCoreLogic() {

    const selectEspecies = document.getElementById("especies-select");
    const selectMedicamentos = document.getElementById("medicamentos-select");
    const pesoInput = document.getElementById("peso");
    const doseInput = document.getElementById("dose");
    const concentracaoInput = document.getElementById("concentracao");
    const resultadoBox = document.querySelector(".resultado-box");
    const resultadoValor = document.getElementById("resultado-valor");
    const btnCalcular = document.querySelector(".calculate-btn");

    if (!selectEspecies || !selectMedicamentos || !pesoInput || !doseInput || !concentracaoInput || !resultadoBox || !resultadoValor || !btnCalcular) {
        console.warn("Elementos essenciais do componente de cálculo não encontrados. A inicialização da calculadora central foi interrompida.");
        return;
    }

    async function atualizarSelects() {
        try {
            const especiesRes = await fetch("/api/especie/todos"); 
            if (!especiesRes.ok) throw new Error(`Erro HTTP ao buscar espécies: ${especiesRes.status}`);
            const especies = await especiesRes.json();
            
            selectEspecies.innerHTML = '<option value="">Selecione</option>';
            especies.forEach((e) => {
                const opt = document.createElement("option");
                opt.value = e.nome;
                opt.textContent = e.nome;
                selectEspecies.appendChild(opt);
            });

            const medicamentosRes = await fetch("/api/medicamentos/todos"); 
            if (!medicamentosRes.ok) throw new Error(`Erro HTTP ao buscar medicamentos: ${medicamentosRes.status}`);
            const medicamentos = await medicamentosRes.json();
            
            selectMedicamentos.innerHTML = '<option value="">Selecione</option>';
            medicamentos.forEach((m) => {
                const opt = document.createElement("option");
                opt.value = m.nome;
                opt.textContent = m.nome;
                selectMedicamentos.appendChild(opt);
            });
        } catch (err) {
            console.error("Erro ao carregar listas de espécies ou medicamentos:", err);
            alert("Não foi possível carregar as listas de espécies ou medicamentos. Verifique sua conexão ou se os endpoints da API estão corretos.");
        }
    }

    async function atualizarDosagem() {
        const medicamentoNome = selectMedicamentos.value;
        const especieNome = selectEspecies.value;

        if (!medicamentoNome || !especieNome) {
            doseInput.value = "";
            concentracaoInput.value = "";
            return;
        }

        try {
            const [medicamentoRes, especieRes] = await Promise.all([
                fetch(`/api/medicamentos?nome=${encodeURIComponent(medicamentoNome)}`),
                fetch(`/api/especie?nome=${encodeURIComponent(especieNome)}`),
            ]);

            if (!medicamentoRes.ok) throw new Error(`Medicamento não encontrado ou erro na API: ${medicamentoRes.status}`);
            if (!especieRes.ok) throw new Error(`Espécie não encontrada ou erro na API: ${especieRes.status}`);
            
            const medicamento = await medicamentoRes.json();
            const especie = await especieRes.json();

            const dosagemRes = await fetch(
                `/api/dosagem?medicamentoId=${medicamento.id}&especieId=${especie.id}`
            );
            if (!dosagemRes.ok) {
                if (dosagemRes.status === 404) {
                    console.warn(`Dosagem não encontrada para ${medicamentoNome} em ${especieNome}.`);
                    alert(`Não há dosagem cadastrada para ${medicamentoNome} em ${especieNome}.`);
                } else {
                    throw new Error(`Erro ao buscar dosagem na API: ${dosagemRes.status}`);
                }
            }
            const dosagem = await dosagemRes.json();

            doseInput.value = (dosagem.doseRecomendadaMgPorKg !== undefined) ? dosagem.doseRecomendadaMgPorKg.toFixed(2) : "";
            concentracaoInput.value = (dosagem.concentracaoMgPorMl !== undefined) ? dosagem.concentracaoMgPorMl.toFixed(2) : "";

        } catch (err) {
            console.error("Erro ao obter dosagem ou concentração:", err);
            alert("Não foi possível obter a dosagem. Verifique as seleções ou se as APIs de busca funcionam.");
            doseInput.value = "";
            concentracaoInput.value = "";
        }
    }

    async function calcularDose() {
        const pesoStr = pesoInput.value.trim().replace(",", ".");
        const peso = parseFloat(pesoStr);
        if (isNaN(peso) || peso <= 0) return alert("Informe um peso válido maior que zero.");

        const medicamentoNome = selectMedicamentos.value;
        const especieNome = selectEspecies.value;
        if (!medicamentoNome) return alert("Selecione um medicamento.");
        if (!especieNome) return alert("Selecione uma espécie.");

        const doseRecomendada = parseFloat(doseInput.value);
        const concentracaoMedicamento = parseFloat(concentracaoInput.value);

        if (isNaN(doseRecomendada) || doseRecomendada <= 0) return alert("Dose recomendada inválida. Selecione uma espécie e medicamento válidos.");
        if (isNaN(concentracaoMedicamento) || concentracaoMedicamento <= 0) return alert("Concentração do medicamento inválida. Selecione uma espécie e medicamento válidos.");
        
        try {
            const [medicamentoRes, especieRes] = await Promise.all([
                fetch(`/api/medicamentos?nome=${encodeURIComponent(medicamentoNome)}`),
                fetch(`/api/especie?nome=${encodeURIComponent(especieNome)}`),
            ]);
            if (!medicamentoRes.ok) throw new Error(`Medicamento não encontrado: ${medicamentoRes.status}`);
            if (!especieRes.ok) throw new Error(`Espécie não encontrada: ${especieRes.status}`);
            
            const medicamento = await medicamentoRes.json();
            const especie = await especieRes.json();

            const body = {
                pesoKg: peso,
                medicamentoId: medicamento.id,
                especieId: especie.id,
            };

            const response = await fetch("/api/calculo/dose", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(body),
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Erro no cálculo via API: ${errorText}`);
            }

            const resultado = await response.json();
            resultadoValor.textContent = resultado.dose.toFixed(2);
            resultadoBox.style.display = "block";

            const historico = JSON.parse(localStorage.getItem("historico-medicamentos")) || [];
            historico.push({
                especie: especieNome,
                medicamento: medicamentoNome,
                data: new Date().toLocaleString("pt-BR"),
            });
            if (historico.length > 10) {
                historico.shift();
            }
            localStorage.setItem("historico-medicamentos", JSON.stringify(historico));

        } catch (err) {
            console.error("Erro ao calcular:", err);
            alert("Erro ao calcular dose. Verifique as informações fornecidas.");
        }
    }

    selectEspecies.addEventListener("change", atualizarDosagem);
    selectMedicamentos.addEventListener("change", atualizarDosagem);

    let calculosFeitos = parseInt(localStorage.getItem("calculos-visitante") || "0");

function usuarioNaoLogado() {
    return !localStorage.getItem("jwt"); // ou verifique o método que você usa para login
}

function controleCalculoParaVisitante(event) {
    if (window.location.pathname === "/" || window.location.pathname.endsWith("index.html")) {
        if (usuarioNaoLogado()) {
            if (calculosFeitos >= 3) {
                alert("Você atingiu o limite de 3 cálculos sem estar cadastrado. Crie sua conta gratuita para continuar.");
                window.location.href = "/cadastro";
                return;
            }
            calculosFeitos++;
            localStorage.setItem("calculos-visitante", calculosFeitos);
        }
    }

    calcularDose(); // chama a função original
}

    btnCalcular.addEventListener("click", controleCalculoParaVisitante);


    atualizarSelects(); 
}

window.inicializarCalculadoraCoreLogic = inicializarCalculadoraCoreLogic;