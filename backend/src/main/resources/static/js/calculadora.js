document.addEventListener("DOMContentLoaded", function () {

  const especies = document.querySelectorAll(".species-buttons button");
  especies.forEach((btn) => {
    btn.addEventListener("click", function () {
      const especieNome = btn.textContent.trim();
      const especieParam = encodeURIComponent(especieNome);
      window.location.href = `/especie?nome=${especieParam}`;
    });
  });
  const selectEspecies = document.getElementById("especies-select");
  const medicamentosList = document.querySelectorAll(".medication-list li");
  const selectMedicamentos = document.getElementById("medicamentos-select");

  especies.forEach((btn) => {
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

  const doseInput = document.getElementById("dose");
  const concentracaoInput = document.getElementById("concentracao");
  const resultadoBox = document.querySelector(".resultado-box");
  const resultadoValor = document.getElementById("resultado-valor");
  const btnCalcular = document.querySelector(".calculate-btn");

  async function atualizarDosagem() {
    const medicamentoNome = selectMedicamentos.value;
    const especieNome = selectEspecies.value;

    if (!medicamentoNome || !especieNome) {
      doseInput.value = "";
      concentracaoInput.value = "";
      return;
    }

    try {
      const medicamentoResponse = await fetch(`/api/medicamentos?nome=${encodeURIComponent(medicamentoNome)}`);
      if (!medicamentoResponse.ok) throw new Error("Medicamento não encontrado");
      const medicamentoData = await medicamentoResponse.json();

      const especieResponse = await fetch(`/api/especie?nome=${encodeURIComponent(especieNome)}`);
      if (!especieResponse.ok) throw new Error("Espécie não encontrada");
      const especieData = await especieResponse.json();

      const dosagemResponse = await fetch(`/api/dosagem?medicamentoId=${medicamentoData.id}&especieId=${especieData.id}`);
      if (!dosagemResponse.ok) throw new Error("Dosagem não encontrada");
      const dosagemData = await dosagemResponse.json();

      doseInput.value = dosagemData.doseRecomendadaMgPorKg || "";
      concentracaoInput.value = dosagemData.concentracaoMgPorMl || "";

    } catch (error) {
      doseInput.value = "";
      concentracaoInput.value = "";
      console.error(error);
    }
  }

  selectEspecies.addEventListener("change", atualizarDosagem);
  selectMedicamentos.addEventListener("change", atualizarDosagem);

  btnCalcular.addEventListener("click", async function () {
    const pesoStr = document.getElementById("peso").value.trim().replace(",", ".");
    const peso = parseFloat(pesoStr);
    const medicamentoNome = selectMedicamentos.value;
    const especieNome = selectEspecies.value;

    if (!peso || peso <= 0) return alert("Informe um peso válido maior que zero.");
    if (!medicamentoNome) return alert("Selecione um medicamento.");
    if (!especieNome) return alert("Selecione uma espécie.");

    try {
      const medicamentoResponse = await fetch(`/api/medicamentos?nome=${encodeURIComponent(medicamentoNome)}`);
      if (!medicamentoResponse.ok) throw new Error("Medicamento não encontrado");
      const medicamentoData = await medicamentoResponse.json();

      const especieResponse = await fetch(`/api/especie?nome=${encodeURIComponent(especieNome)}`);
      if (!especieResponse.ok) throw new Error("Espécie não encontrada");
      const especieData = await especieResponse.json();

      const requestBody = {
        pesoKg: peso,
        medicamentoId: medicamentoData.id,
        especieId: especieData.id
      };

      const response = await fetch("/api/calculo/dose", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(requestBody)
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Erro no cálculo: ${errorText}`);
      }

      const result = await response.json();
      resultadoValor.textContent = result.dose.toFixed(2);
      resultadoBox.style.display = "block";

      const historico = JSON.parse(localStorage.getItem("historico-medicamentos")) || [];
      historico.push({
        especie: especieNome,
        medicamento: medicamentoNome,
        data: new Date().toLocaleString("pt-BR")
      });
      localStorage.setItem("historico-medicamentos", JSON.stringify(historico));

    } catch (err) {
      alert(err.message);
    }
  });

  const inputBuscar = document.getElementById("buscar-medicamento");
  inputBuscar.addEventListener("input", function () {
    const filtro = this.value.toLowerCase();
    medicamentosList.forEach((li) => {
      li.style.display = li.textContent.toLowerCase().includes(filtro) ? "" : "none";
    });
  });
});
