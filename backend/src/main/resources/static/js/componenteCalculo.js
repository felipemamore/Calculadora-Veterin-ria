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
    inicializarCalculadoraSilvestres();
}

//DOSE DE MEDICAMENTO
function inicializarCalculadoraDose() {
    const selectEspecies = document.getElementById("especies-select");
    const selectMedicamentos = document.getElementById("medicamentos-select");
    const selectApresentacao = document.getElementById("apresentacao-select");
    const pesoInput = document.getElementById("peso");
    const doseInput = document.getElementById("dose-recomendada");
    const concentracaoInput = document.getElementById("concentracao");
    const resultadoBox = document.querySelector("#dose .resultado-box");
    const resultadoValor = document.getElementById("resultado-valor");
    const btnCalcular = document.querySelector("#dose .calculate-btn");
    
    if (!selectEspecies) return;

    function tratarNaoSeAplica() {
        const mensagem = "Não se aplica a esta espécie";
        selectApresentacao.innerHTML = `<option value="">${mensagem}</option>`;
        doseInput.value = mensagem;
        concentracaoInput.value = mensagem;
        doseInput.disabled = true;
        concentracaoInput.disabled = true;
        btnCalcular.disabled = true;
    }

    async function carregarListasIniciais() {
        try {
            const [especiesRes, medicamentosRes] = await Promise.all([
                fetch(`${API_BASE_URL}/api/especie/todos`),
                fetch(`${API_BASE_URL}/api/medicamentos/todos`)
            ]);
            if (!especiesRes.ok || !medicamentosRes.ok) throw new Error("Falha ao carregar listas iniciais.");

            const especies = await especiesRes.json();
            selectEspecies.innerHTML = '<option value="">Selecione</option>';
            especies.forEach(e => {
                const opt = document.createElement("option");
                opt.value = e.id;
                opt.textContent = e.nome;
                selectEspecies.appendChild(opt);
            });

            const medicamentos = await medicamentosRes.json();
            selectMedicamentos.innerHTML = '<option value="">Selecione</option>';
            medicamentos.forEach(m => {
                const opt = document.createElement("option");
                opt.value = m.id;
                opt.textContent = m.nome;
                selectMedicamentos.appendChild(opt);
            });
        } catch (err) {
            console.error("Erro ao carregar listas:", err);
            alert("Não foi possível carregar as listas de espécies ou medicamentos.");
        }
    }

    async function buscarApresentacoes() {
        const especieId = selectEspecies.value;
        const medicamentoId = selectMedicamentos.value;

        selectApresentacao.innerHTML = '<option value="">Selecione</option>';
        selectApresentacao.disabled = true;
        doseInput.value = "";
        concentracaoInput.value = "";
        doseInput.disabled = false;
        concentracaoInput.disabled = false;
        btnCalcular.disabled = false;
        resultadoBox.style.display = 'none';

        if (!especieId || !medicamentoId) return;

        try {
            const res = await fetch(`${API_BASE_URL}/api/dosagem/apresentacoes?medicamentoId=${medicamentoId}&especieId=${especieId}`);
            if (!res.ok) throw new Error("Nenhuma apresentação encontrada.");

            const apresentacoes = await res.json();
            if (apresentacoes.length > 0) {
                apresentacoes.forEach(ap => {
                    const opt = document.createElement("option");
                    opt.value = ap;
                    opt.textContent = ap;
                    selectApresentacao.appendChild(opt);
                });
                selectApresentacao.disabled = false;
            } else {
                tratarNaoSeAplica();
            }
        } catch (err) {
            console.error(err);
            tratarNaoSeAplica();
        }
    }

    async function buscarDosagemFinal() {
        const especieId = selectEspecies.value;
        const medicamentoId = selectMedicamentos.value;
        const apresentacao = selectApresentacao.value;

        if (!especieId || !medicamentoId || !apresentacao) return;

        try {
            const res = await fetch(`${API_BASE_URL}/api/dosagem?medicamentoId=${medicamentoId}&especieId=${especieId}&apresentacao=${encodeURIComponent(apresentacao)}`);
            if (!res.ok) throw new Error("Dosagem específica não encontrada.");

            const dosagem = await res.json();
            doseInput.value = dosagem.doseRecomendadaMgPorKg.toFixed(2).replace('.', ',');
            concentracaoInput.value = dosagem.concentracaoMgPorMl.toFixed(2).replace('.', ',');
        } catch (err) {
            console.error(err);
            doseInput.value = "Erro";
            concentracaoInput.value = "Erro";
        }
    }

    async function calcularDose() {
        const pesoStr = pesoInput.value.trim().replace(",", ".");
        const peso = parseFloat(pesoStr);
        if (isNaN(peso) || peso <= 0) return alert("Informe um peso válido maior que zero.");

        const medicamentoId = selectMedicamentos.value;
        const especieId = selectEspecies.value;
        if (!medicamentoId) return alert("Selecione um medicamento.");
        if (!especieId) return alert("Selecione uma espécie.");

        const doseRecomendada = parseFloat(doseInput.value.replace(",", "."));
        const concentracaoMedicamento = parseFloat(concentracaoInput.value.replace(",", "."));
        if (isNaN(doseRecomendada) || isNaN(concentracaoMedicamento) || concentracaoMedicamento <= 0) {
            return alert("Dados de dosagem inválidos. Verifique as seleções.");
        }
        
        try {
            const body = {
                pesoKg: peso,
                medicamentoId: medicamentoId,
                especieId: especieId,    
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

    let calculosFeitos = parseInt(localStorage.getItem("calculos-visitante") || "0");

    // Função que verifica se o usuário está logado
    function usuarioNaoLogado() {
        return !localStorage.getItem("jwtToken");
    }

    // Função que controla o clique no botão "Calcular"
    function controleCalculoParaVisitante(event) {
        event.preventDefault(); 

        if (usuarioNaoLogado()) {
            
            if (calculosFeitos >= 5) { 
                alert("Você atingiu o limite de 5 cálculos como visitante. Por favor, faça login ou cadastre-se para continuar.");
                window.location.href = "/pagina-login"; 
                return; 
            }
            
            calculosFeitos++;
            localStorage.setItem("calculos-visitante", calculosFeitos.toString());
        }

        calcularDose();
    }



    selectEspecies.addEventListener("change", buscarApresentacoes);
    selectMedicamentos.addEventListener("change", buscarApresentacoes);
    selectApresentacao.addEventListener("change", buscarDosagemFinal);
    btnCalcular.addEventListener("click", controleCalculoParaVisitante);

    carregarListasIniciais();
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

// CALCULADORA DE SILVESTRES (ALOMETRIA)
function inicializarCalculadoraSilvestres() {
    const selectGrupo = document.getElementById("silvestres-grupo");
    const selectMedicamento = document.getElementById("silvestres-medicamento");
    const inputPeso = document.getElementById("silvestres-peso");
    const btnCalcular = document.getElementById("btn-calcular-silvestre");
    const resultadoBox = document.getElementById("resultado-box-silvestre");
    const resultadoValor = document.getElementById("resultado-valor-silvestre");
    const infoDetalhe = document.getElementById("info-detalhe-silvestre");

    if (!selectGrupo) return;

    // 1. Carregar Grupos ao iniciar (Aves, Répteis, etc.)
    async function carregarGrupos() {
        try {
            const response = await fetch(`${API_BASE_URL}/api/silvestres/grupos`);
            if (!response.ok) throw new Error("Erro ao carregar grupos");
            
            const grupos = await response.json();
            selectGrupo.innerHTML = '<option value="">Selecione</option>';
            grupos.forEach(grupo => {
                const opt = document.createElement("option");
                opt.value = grupo;
                opt.textContent = grupo;
                selectGrupo.appendChild(opt);
            });
        } catch (error) {
            console.error(error);
            selectGrupo.innerHTML = '<option value="">Erro ao carregar</option>';
        }
    }

    // 2. Carregar Medicamentos quando o Grupo muda
    async function carregarMedicamentos() {
        const grupo = selectGrupo.value;
        
        // Resetar campos
        selectMedicamento.innerHTML = '<option value="">Selecione</option>';
        selectMedicamento.disabled = true;
        resultadoBox.style.display = 'none';

        if (!grupo) return;

        try {
            const response = await fetch(`${API_BASE_URL}/api/silvestres/medicamentos?grupo=${encodeURIComponent(grupo)}`);
            if (!response.ok) throw new Error("Erro ao carregar medicamentos");

            const medicamentos = await response.json(); // Espera lista de objetos DosagemSilvestre
            
            if (medicamentos.length > 0) {
                medicamentos.forEach(med => {
                    const opt = document.createElement("option");
                    opt.value = med.medicamento; // Usamos o nome como ID para o cálculo
                    opt.textContent = med.medicamento;
                    selectMedicamento.appendChild(opt);
                });
                selectMedicamento.disabled = false;
            } else {
                selectMedicamento.innerHTML = '<option value="">Nenhum medicamento encontrado</option>';
            }
        } catch (error) {
            console.error(error);
        }
    }

    // 3. Realizar o Cálculo
    async function calcular() {
        const grupo = selectGrupo.value;
        const medicamento = selectMedicamento.value;
        const peso = parseFloat(inputPeso.value);

        if (!grupo || !medicamento) {
            alert("Selecione o grupo e o medicamento.");
            return;
        }
        if (isNaN(peso) || peso <= 0) {
            alert("Informe um peso válido em gramas.");
            return;
        }

        // Prepara o payload para o backend
        const payload = {
            grupo: grupo,
            medicamento: medicamento,
            peso: peso // Backend espera peso em gramas conforme combinamos
        };

        // Pega o token para autorização
        const token = localStorage.getItem('jwtToken');
        const headers = { 'Content-Type': 'application/json' };
        if (token) headers['Authorization'] = `Bearer ${token}`;

        try {
            const response = await fetch(`${API_BASE_URL}/api/silvestres/calcular`, {
                method: 'POST',
                headers: headers,
                body: JSON.stringify(payload)
            });

            if (!response.ok) throw new Error("Erro ao calcular");

            const resultado = await response.json();
            
            // Exibe o resultado
            resultadoValor.textContent = resultado.doseTotalMg.toFixed(2).replace('.', ',');
            
            // Mostra detalhes extras se houver (ex: Ação: Antibiótico)
            if (resultado.acao) {
                infoDetalhe.textContent = `Ação: ${resultado.acao}`;
            } else {
                infoDetalhe.textContent = "";
            }
            
            resultadoBox.style.display = 'block';

        } catch (error) {
            console.error(error);
            alert("Erro ao realizar o cálculo. Tente novamente.");
        }
    }

    // Event Listeners
    selectGrupo.addEventListener("change", carregarMedicamentos);
    btnCalcular.addEventListener("click", calcular);

    // Inicializa
    carregarGrupos();
}