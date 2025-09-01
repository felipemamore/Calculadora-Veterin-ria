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

// Variável para guardar qual item está atualmente selecionado
let itemSelecionado = null;

if (inputBuscarMedicamento && medicamentosList.length > 0) {
    inputBuscarMedicamento.addEventListener("input", function () {
        const filtro = this.value.toLowerCase();
        medicamentosList.forEach((li) => {
            li.style.display = li.firstChild.textContent.toLowerCase().includes(filtro) ? "" : "none";
        });
    });

    medicamentosList.forEach((li) => {
        // Lógica para SELECIONAR o medicamento (PRIMEIRO CLIQUE)
        li.addEventListener("click", function() {
            const medicamentoNome = li.firstChild.textContent.trim();
            const selectMedicamentos = document.getElementById("medicamentos-select");

            // Remove a seleção do item anterior, se houver
            if (itemSelecionado) {
                itemSelecionado.classList.remove('selecionado');
            }
            
            // Adiciona a classe 'selecionado' ao item clicado
            li.classList.add('selecionado');
            itemSelecionado = li; // Atualiza a referência do item selecionado

            // Preenche o formulário
            if (selectMedicamentos) {
                selectMedicamentos.value = medicamentoNome;
                selectMedicamentos.dispatchEvent(new Event('change'));
            }
        });

        // Lógica para ABRIR A BULA (CLIQUE NO BOTÃO)
        // Procure por este trecho no seu JavaScript que cria a lista
        const linkBula = li.querySelector(".bula-link");
        if (linkBula) {

        // SUBSTITUA o addEventListener existente por este
        linkBula.addEventListener("click", function(event) {
        // Impede que o clique no botão também selecione o item da lista
        event.stopPropagation();
        
        // Pega o nome do medicamento a partir do elemento pai (o <li>)
        const medicamentoNome = li.firstChild.textContent.trim();
        
        // Simplesmente redireciona o navegador para a página estática bula.html
        window.location.href = `/bula.html?medicamento=${encodeURIComponent(medicamentoNome)}`;
    });
}
    });
}

   window.abrirMinhaConta = async function () {
    try {
        const token = localStorage.getItem('jwtToken'); // Verifique se 'jwtToken' é a chave correta.

        // Se não houver token, nem tenta fazer a requisição.
        if (!token) {
            alert("Você não está logado. Por favor, faça o login.");
            window.location.href = '/pagina-login'; // Use o nome correto da sua página de login
            return;
        }

        const headers = {
            'Authorization': `Bearer ${token}` // Formato padrão para JWT
        };

        const response = await fetch("/api/conta-do-usuario", {
            method: 'GET',
            headers: headers
        });

        if (!response.ok) {
            if (response.status === 401 || response.status === 403) {
                alert("Sua sessão expirou. Por favor, faça o login novamente.");
                localStorage.removeItem('jwtToken'); // Limpa o token antigo
                window.location.href = '/pagina-login';
                return;
            }
            throw new Error("Erro ao obter dados da conta.");
        }

        const dados = await response.json();
        sessionStorage.setItem('dadosUsuario', JSON.stringify(dados));
        window.location.href = '/minhaConta';

    } catch (err) {
        alert("Erro ao carregar informações da conta. Tente fazer o login novamente.");
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
function fazerLogout() {
    localStorage.removeItem('jwtToken');

    sessionStorage.removeItem('dadosUsuario');

    alert("Você saiu da sua conta.");

    window.location.href = '/pagina-login';
}
