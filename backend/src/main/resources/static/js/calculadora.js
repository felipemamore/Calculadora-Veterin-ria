document.addEventListener("DOMContentLoaded", () => {
    inicializarLayoutCompletoLogic();
    carregarComponenteCalculo();
});

async function carregarComponenteCalculo() {
    const container = document.getElementById("componente-calculo-container");
    if (!container) return;

    try {
        const response = await fetch("/templates/componenteCalculo.html");
        if (!response.ok) throw new Error("Falha ao buscar o HTML do componente.");
        
        container.innerHTML = await response.text();
        if (typeof inicializarComponenteCalculo === "function") {
            inicializarComponenteCalculo();
        } else {
            console.error("Função inicializarComponenteCalculo não encontrada! Verifique se componenteCalculo.js foi carregado.");
        }
    } catch (e) {
        console.error("Erro ao carregar componente de cálculo:", e);
    }
}

function inicializarLayoutCompletoLogic() {
    
    //LISTA DE ESPÉCIES (Sidebar Esquerda) ---
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

    //LISTA DE MEDICAMENTOS (Sidebar Direita) -
    const medicamentosList = document.querySelectorAll(".medication-list li");
    const inputBuscarMedicamento = document.getElementById("buscar-medicamento");
    let itemSelecionado = null;

    if (inputBuscarMedicamento && medicamentosList.length > 0) {
        inputBuscarMedicamento.addEventListener("input", function () {
            const filtro = this.value.toLowerCase();
            medicamentosList.forEach((li) => {
                li.style.display = li.firstChild.textContent.toLowerCase().includes(filtro) ? "" : "none";
            });
        });

        medicamentosList.forEach((li) => {
            //SELECIONAR o medicamento
            li.addEventListener("click", function() {
                const medicamentoNome = li.firstChild.textContent.trim();
                const selectMedicamentos = document.getElementById("medicamentos-select");

                if (itemSelecionado) {
                    itemSelecionado.classList.remove('selecionado');
                }
                
                li.classList.add('selecionado');
                itemSelecionado = li; 

                if (selectMedicamentos) {
                    selectMedicamentos.value = medicamentoNome;
                    selectMedicamentos.dispatchEvent(new Event('change'));
                }
            });

            // ABRIR A BULA 
            const linkBula = li.querySelector(".bula-link");
            if (linkBula) {
                linkBula.addEventListener("click", function(event) {
                    event.stopPropagation();
                    const medicamentoNome = li.firstChild.textContent.trim();
                    window.location.href = `/bula.html?medicamento=${encodeURIComponent(medicamentoNome)}`;
                });
            }
        });
    }
}

async function abrirMinhaConta() {
    try {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            alert("Você não está logado. Por favor, faça o login.");
            window.location.href = '/pagina-login';
            return;
        }

        const headers = { 'Authorization': `Bearer ${token}` };
        const response = await fetch(`${API_BASE_URL}/api/conta-do-usuario`, {
            method: 'GET',
            headers: headers
        });

        if (!response.ok) {
            if (response.status === 401 || response.status === 403) {
                alert("Sua sessão expirou. Por favor, faça o login novamente.");
                localStorage.removeItem('jwtToken');
                window.location.href = '/pagina-login';
            } else {
                throw new Error("Erro ao obter dados da conta.");
            }
            return;
        }

        const dados = await response.json();
        sessionStorage.setItem('dadosUsuario', JSON.stringify(dados));
        window.location.href = '/minhaConta';
    } catch (err) {
        alert("Erro ao carregar informações da conta.");
        console.error("Erro ao abrir minha conta:", err);
    }
}

function fazerLogout() {
    localStorage.removeItem('jwtToken');
    sessionStorage.removeItem('dadosUsuario');
    alert("Você saiu da sua conta.");
    window.location.href = '/pagina-login';
}