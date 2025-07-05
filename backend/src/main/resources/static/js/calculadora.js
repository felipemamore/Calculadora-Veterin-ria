document.addEventListener("DOMContentLoaded", function () {
  const especiesButtons = document.querySelectorAll(".species-buttons button");
  especiesButtons.forEach((btn) => {
    btn.addEventListener("click", function () {
      const especieNome = btn.textContent.trim();
      const especieParam = encodeURIComponent(especieNome);
      window.location.href = `/especie?nome=${especieParam}`;
    });
  });

  const selectEspecies = document.getElementById("especies-select");
  const medicamentosList = document.querySelectorAll(".medication-list li");
  const selectMedicamentos = document.getElementById("medicamentos-select");
  const doseInput = document.getElementById("dose");
  const concentracaoInput = document.getElementById("concentracao");
  const resultadoBox = document.querySelector(".resultado-box");
  const resultadoValor = document.getElementById("resultado-valor");
  const btnCalcular = document.querySelector(".calculate-btn");
  const inputBuscar = document.getElementById("buscar-medicamento");

  especiesButtons.forEach((btn) => {
    const nome = btn.textContent.trim();
    const option = document.createElement("option");
    option.value = nome;
    option.textContent = nome;
    selectEspecies.appendChild(option);
  });

  medicamentosList.forEach((li) => {
    const nome = li.textContent.trim();
    const option = document.createElement("option");
    option.value = nome;
    option.textContent = nome;
    selectMedicamentos.appendChild(option);
  });

  async function atualizarDosagem() {
    const medicamentoNome = selectMedicamentos.value;
    const especieNome = selectEspecies.value;

    if (!medicamentoNome || !especieNome) {
      doseInput.value = "";
      concentracaoInput.value = "";
      return;
    }

    try {
      const medicamentoResponse = await fetch(
        `/api/medicamentos?nome=${encodeURIComponent(medicamentoNome)}`
      );
      if (!medicamentoResponse.ok) throw new Error("Medicamento não encontrado");
      const medicamentoData = await medicamentoResponse.json();

      const especieResponse = await fetch(
        `/api/especie?nome=${encodeURIComponent(especieNome)}`
      );
      if (!especieResponse.ok) throw new Error("Espécie não encontrada");
      const especieData = await especieResponse.json();

      const dosagemResponse = await fetch(
        `/api/dosagem?medicamentoId=${medicamentoData.id}&especieId=${especieData.id}`
      );
      if (!dosagemResponse.ok) throw new Error("Dosagem não encontrada");
      const dosagemData = await dosagemResponse.json();

      doseInput.value = dosagemData.doseRecomendadaMgPorKg || "";
      concentracaoInput.value = dosagemData.concentracaoMgPorMl || "";
    } catch (error) {
      doseInput.value = "";
      concentracaoInput.value = "";
      console.error("Erro ao buscar dosagem:", error);
    }
  }

  selectEspecies.addEventListener("change", atualizarDosagem);
  selectMedicamentos.addEventListener("change", atualizarDosagem);

  btnCalcular.addEventListener("click", async function () {
    const pesoStr = document.getElementById("peso").value.trim().replace(",", ".");
    const peso = parseFloat(pesoStr);
    const medicamentoNome = selectMedicamentos.value;
    const especieNome = selectEspecies.value;

    if (!peso || peso <= 0)
      return alert("Informe um peso válido maior que zero.");
    if (!medicamentoNome) return alert("Selecione um medicamento.");
    if (!especieNome) return alert("Selecione uma espécie.");

    try {
      const medicamentoResponse = await fetch(
        `/api/medicamentos?nome=${encodeURIComponent(medicamentoNome)}`
      );
      if (!medicamentoResponse.ok) throw new Error("Medicamento não encontrado");
      const medicamentoData = await medicamentoResponse.json();

      const especieResponse = await fetch(
        `/api/especie?nome=${encodeURIComponent(especieNome)}`
      );
      if (!especieResponse.ok) throw new Error("Espécie não encontrada");
      const especieData = await especieResponse.json();

      const requestBody = {
        pesoKg: peso,
        medicamentoId: medicamentoData.id,
        especieId: especieData.id,
      };

      const response = await fetch("/api/calculo/dose", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(requestBody),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Erro no cálculo: ${errorText}`);
      }

      const result = await response.json();
      resultadoValor.textContent = result.dose.toFixed(2);
      resultadoBox.style.display = "block";

      const historico =
        JSON.parse(localStorage.getItem("historico-medicamentos")) || [];
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
      alert(err.message);
      console.error("Erro no cálculo:", err);
    }
  });

  inputBuscar.addEventListener("input", function () {
    const filtro = this.value.toLowerCase();
    medicamentosList.forEach((li) => {
      li.style.display = li.textContent.toLowerCase().includes(filtro) ? "" : "none";
    });
  });

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
});