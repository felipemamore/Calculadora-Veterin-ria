const API_BASE_URL = "https://calculadora-veterinaria-api.fly.dev";

document.addEventListener("DOMContentLoaded", async () => {

  const urlParams = new URLSearchParams(window.location.search);
  const especieNome = urlParams.get("nome");

  if (!especieNome) return;

  try {
    document.getElementById("titulo-especie").textContent = especieNome;

    const resumos = {
      Canina: "Espécie canina inclui cães de diferentes raças e portes.",
      Felina: "Espécie felina refere-se aos gatos, animais domésticos ágeis e limpos.",
      Equina: "Espécie equina compreende cavalos, usados historicamente para transporte e esporte.",
      Bovino: "Espécie bovina inclui animais de produção como bois e vacas.",
      Mamíferos: "Mamíferos incluem diversas espécies com glândulas mamárias.",
      Aves: "Aves são animais vertebrados com penas e bico.",
      Répteis: "Répteis como lagartos e cobras são animais de sangue frio.",
      Roedores: "Roedores como ratos e hamsters possuem dentes incisivos desenvolvidos."
    };
    document.getElementById("resumo-especie").textContent = resumos[especieNome] || "Resumo não disponível.";

    const imagens = {
      Canina: "images/canina.jpg",
      Felina: "images/felina.jpg",
      Equina: "images/equina.jpg",
      Bovino: "images/bovina.jpg",
      Mamíferos: "images/mamifero.png",
      Aves: "images/aves.jpg",
      Répteis: "images/repteis.jpg",
      Roedores: "images/roedores.jpg"
    };
    document.getElementById("imagem-especie").src = imagens[especieNome] || "images/default.jpg";

    const toxicasResponse = await fetch(`${API_BASE_URL}/api/toxicas?especie=${encodeURIComponent(especieNome)}`);
    const toxicasLista = document.getElementById("toxicas-lista");
    toxicasLista.innerHTML = "";

    if (toxicasResponse.ok) {
      const toxicas = await toxicasResponse.json();
      if (toxicas.length === 0) {
        toxicasLista.innerHTML = "<li>Nenhuma informação disponível.</li>";
      } else {
        toxicas.forEach(nomeMedicamento => {
          const li = document.createElement("li");
          li.textContent = nomeMedicamento;
          toxicasLista.appendChild(li);
        });
      }
    } else {
      toxicasLista.innerHTML = "<li>Erro ao carregar dados de medicamentos tóxicos.</li>";
    }

    async function carregarHistoricoFiltrado(especieNome) {
    const historicoLista = document.getElementById("historico-lista");
    historicoLista.innerHTML = "<li>Carregando histórico...</li>"; // Mensagem de feedback

    try {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            historicoLista.innerHTML = "<li>Faça o login para ver seu histórico.</li>";
            return;
        }

        const headers = { 'Authorization': `Bearer ${token}` };

        const response = await fetch(`${API_BASE_URL}/api/calculo/historico`, { headers });

        if (!response.ok) {
            throw new Error('Falha ao buscar histórico do usuário.');
        }

        const historico = await response.json();

        historicoLista.innerHTML = "";
        const historicoFiltrado = historico.filter(item => item.especie === especieNome);

        if (historicoFiltrado.length === 0) {
            historicoLista.innerHTML = "<li>Nenhum histórico encontrado para esta espécie.</li>";
        } else {
            historicoFiltrado.forEach(item => {
                const li = document.createElement("li");
                const dataFormatada = new Date(item.data).toLocaleString('pt-BR', {
                        day: '2-digit', month: '2-digit', year: 'numeric',
                        hour: '2-digit', minute: '2-digit'
            });
                li.textContent = `${item.nome} - ${dataFormatada}`;
                    historicoLista.appendChild(li);
          });
        }

    } catch (error) {
        console.error("Erro ao carregar histórico:", error);
        historicoLista.innerHTML = "<li>Erro ao carregar histórico.</li>";
    }
}
    carregarHistoricoFiltrado(especieNome);

  } catch (err) {
    console.error("Erro ao carregar dados da espécie:", err);
  }
});
