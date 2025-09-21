function inicializarComponenteCalculo() {
    
    //Abas
    const tabs = document.querySelectorAll('.tab-link');
    const panes = document.querySelectorAll('.tab-pane');
    if (tabs.length > 0) {
        tabs.forEach(tab => {
            tab.addEventListener('click', () => {
                const targetPaneId = tab.getAttribute('data-tab');
                tabs.forEach(t => t.classList.remove('active'));
                panes.forEach(p => p.classList.remove('active'));
                tab.classList.add('active');
                document.getElementById(targetPaneId).classList.add('active');
            });
        });
    }
    inicializarCalculadoraDose();
    inicializarCalculadoraEnergia();
    inicializarCalculadoraGestacao();
}

//DOSE DE MEDICAMENTO
function inicializarCalculadoraDose() {
    const selectEspecies = document.getElementById("especies-select");
    const selectMedicamentos = document.getElementById("medicamentos-select");
    const pesoInput = document.getElementById("peso");
    const doseInput = document.getElementById("dose-recomendada");
    const concentracaoInput = document.getElementById("concentracao");
    const resultadoBox = document.querySelector("#dose .resultado-box");
    const resultadoValor = document.getElementById("resultado-valor");
    const btnCalcular = document.querySelector("#dose .calculate-btn");

    if (!selectEspecies) {
        console.warn("Componente da calculadora de dose não encontrado.");
        return;
    }
    
    async function atualizarSelects() {
        try {
            const especiesRes = await fetch(`${API_BASE_URL}/api/especie/todos`);
            if (!especiesRes.ok) throw new Error(`Erro HTTP ao buscar espécies: ${especiesRes.status}`);
            const especies = await especiesRes.json();
            
            selectEspecies.innerHTML = '<option value="">Selecione</option>';
            especies.forEach((e) => {
                const opt = document.createElement("option");
                opt.value = e.nome;
                opt.textContent = e.nome;
                selectEspecies.appendChild(opt);
            });

            const medicamentosRes = await fetch(`${API_BASE_URL}/api/medicamentos/todos`);
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
        const medicamentoNome = selectMedicamentos.value.trim();
        const especieNome = selectEspecies.value.trim();
        resultadoBox.style.display = 'none';

        if (!medicamentoNome || !especieNome) {
            doseInput.value = "";
            concentracaoInput.value = "";
            doseInput.disabled = false;
            concentracaoInput.disabled = false;
            btnCalcular.disabled = false;
            return;
        }

        try {
            const [medicamentoRes, especieRes] = await Promise.all([
                fetch(`${API_BASE_URL}/api/medicamentos?nome=${encodeURIComponent(medicamentoNome)}`),
                fetch(`${API_BASE_URL}/api/especie?nome=${encodeURIComponent(especieNome)}`),
            ]);
            
            if (!medicamentoRes.ok) throw new Error(`Medicamento não encontrado: ${medicamentoRes.status}`);
            if (!especieRes.ok) throw new Error(`Espécie não encontrada: ${especieRes.status}`);
            
            const medicamento = await medicamentoRes.json();
            const especie = await especieRes.json();
            
            const dosagemRes = await fetch(
                `${API_BASE_URL}/api/dosagem?medicamentoId=${medicamento.id}&especieId=${especie.id}`
            );

            if (dosagemRes.status === 404) {
                const mensagem = "Não se aplica a esta espécie";
                doseInput.value = mensagem;
                concentracaoInput.value = mensagem;
                doseInput.disabled = true;
                concentracaoInput.disabled = true;
                btnCalcular.disabled = true;
            } else if (!dosagemRes.ok) {
                throw new Error(`Erro na API de dosagem: ${dosagemRes.status}`);
            } else {
                const dosagem = await dosagemRes.json();
                doseInput.disabled = false;
                concentracaoInput.disabled = false;
                btnCalcular.disabled = false;
                doseInput.value = dosagem.doseRecomendadaMgPorKg.toFixed(2).replace('.', ',');
                concentracaoInput.value = dosagem.concentracaoMgPorMl.toFixed(2).replace('.', ',');
            }
        } catch (err) {
            console.error("Erro ao obter dosagem ou concentração:", err);
            doseInput.value = "";
            concentracaoInput.value = "";
            doseInput.disabled = false;
            concentracaoInput.disabled = false;
            btnCalcular.disabled = false;
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

        const doseRecomendada = parseFloat(doseInput.value.replace(",", "."));
        const concentracaoMedicamento = parseFloat(concentracaoInput.value.replace(",", "."));
        if (isNaN(doseRecomendada) || doseRecomendada < 0) return alert("Dose recomendada inválida. Selecione uma espécie e medicamento válidos.");
        if (isNaN(concentracaoMedicamento) || concentracaoMedicamento <= 0) return alert("Concentração do medicamento inválida. Selecione uma espécie e medicamento válidos.");
        
        try {
            const [medicamentoRes, especieRes] = await Promise.all([
                fetch(`${API_BASE_URL}/api/medicamentos?nome=${encodeURIComponent(medicamentoNome)}`),
                fetch(`${API_BASE_URL}/api/especie?nome=${encodeURIComponent(especieNome)}`),
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
            
            const token = localStorage.getItem('jwtToken'); 
            const headers = { 'Content-Type': 'application/json' };
            if (token) {
                headers['Authorization'] = `Bearer ${token}`;
            }

            const response = await fetch(`${API_BASE_URL}/api/calculo/dose`, {
                method: "POST",
                headers: headers,
                body: JSON.stringify(body),
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Erro no cálculo via API: ${errorText}`);
            }

            const resultado = await response.json();
            resultadoValor.textContent = resultado.dose.toFixed(2).replace('.',',');
            resultadoBox.style.display = "block";
        } catch (err) {
            console.error("Erro ao calcular:", err);
            alert("Erro ao calcular dose. Verifique as informações fornecidas.");
        }
    }

    selectEspecies.addEventListener("change", atualizarDosagem);
    selectMedicamentos.addEventListener("change", atualizarDosagem);
    btnCalcular.addEventListener("click", calcularDose);

    atualizarSelects();
}

//NECESSIDADE ENERGÉTICA
function inicializarCalculadoraEnergia() {
    const pesoEnergiaInput = document.getElementById('peso-energia');
    if (!pesoEnergiaInput) return;

    const resultadoEnergiaSpan = document.getElementById('resultado-energia');
    const btnsAnimalEnergia = document.querySelectorAll('.btn-animal-energia');
    
    const racasContainer = document.getElementById('racas-container-energia');
    const caracteristicasCaoContainer = document.getElementById('caracteristicas-cao-container');
    const caracteristicasGatoContainer = document.getElementById('caracteristicas-gato-container');
    
    const caracteristicaCaoSelect = document.getElementById('caracteristica-cao');
    const caracteristicaGatoSelect = document.getElementById('caracteristica-gato');
    
    let animalEnergia = 'cao'; 
    const fatores = {
        cao: {
            crescimento_cao: 3.0,
            adulto_moderado_cao: 1.8,
            lactacao_cao: 5.6,
            adulto_sedentario_cao: 1.4
        },
        gato: {
            crescimento_gato: 2.5,
            adulto_ativo_gato: 1.4,
            lactacao_gato: 3.0, 
            adulto_sedentario_gato: 1.2
        }
    };

    function atualizarInterface() {
        if (animalEnergia === 'cao') {
            racasContainer.style.display = 'block';
            caracteristicasCaoContainer.style.display = 'block';
            caracteristicasGatoContainer.style.display = 'none';
        } else { 
            racasContainer.style.display = 'none';
            caracteristicasCaoContainer.style.display = 'none';
            caracteristicasGatoContainer.style.display = 'block';
        }
        calcularEnergia();
    }

    function calcularEnergia() {
        const peso = parseFloat(pesoEnergiaInput.value);
        if (isNaN(peso) || peso <= 0) {
            resultadoEnergiaSpan.textContent = "0,00 Kcal/dia";
            return;
        }

        let fator;
        if (animalEnergia === 'cao') {
            const caracteristicaCao = caracteristicaCaoSelect.value;
            fator = fatores.cao[caracteristicaCao];
        } else {
            const caracteristicaGato = caracteristicaGatoSelect.value;
            fator = fatores.gato[caracteristicaGato];
        }
        
        // Fórmula Padrão: RER = 70 * (peso ^ 0.75)
        const rer = 70 * Math.pow(peso, 0.75);
        const der = rer * fator;

        resultadoEnergiaSpan.textContent = `${der.toFixed(2).replace('.', ',')} Kcal/dia`;
    }

    btnsAnimalEnergia.forEach(btn => {
        btn.addEventListener('click', () => {
            btnsAnimalEnergia.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            animalEnergia = btn.getAttribute('data-animal');
            atualizarInterface();
        });
    });

    pesoEnergiaInput.addEventListener('input', calcularEnergia);
    caracteristicaCaoSelect.addEventListener('change', calcularEnergia);
    caracteristicaGatoSelect.addEventListener('change', calcularEnergia);
    atualizarInterface();
}

// GESTAÇÃO
function inicializarCalculadoraGestacao() {
    const inicioGestacaoInput = document.getElementById('inicio-gestacao');
    const resultadoGestacaoDiv = document.getElementById('resultado-gestacao');
    const btnsAnimalGestacao = document.querySelectorAll('.btn-animal-gestacao');
    let animalGestacao = 'cao';

    if (!inicioGestacaoInput) return;

    btnsAnimalGestacao.forEach(btn => {
        btn.addEventListener('click', () => {
            btnsAnimalGestacao.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            animalGestacao = btn.getAttribute('data-animal');
            calcularGestacao();
        });
    });

    inicioGestacaoInput.addEventListener('change', calcularGestacao);

    function calcularGestacao() {
        const dataInicio = inicioGestacaoInput.value;
        if (!dataInicio) return;
        
        const data = new Date(dataInicio + "T00:00:00");
        const periodos = {
            cao: { min: 62, max: 64 },
            gato: { min: 60, max: 65 }
        };
        const gestacao = periodos[animalGestacao];
        const dataMinima = new Date(data.getTime());
        dataMinima.setDate(data.getDate() + gestacao.min);
        const dataMaxima = new Date(data.getTime());
        dataMaxima.setDate(data.getDate() + gestacao.max);
        const options = { year: 'numeric', month: 'short', day: 'numeric' };
        resultadoGestacaoDiv.innerHTML = `Mínima: ${dataMinima.toLocaleDateString('pt-BR', options)} <br> Máxima: ${dataMaxima.toLocaleDateString('pt-BR', options)}`;
    }
}