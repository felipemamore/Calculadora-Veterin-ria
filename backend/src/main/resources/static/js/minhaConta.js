if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') {
    API_BASE_URL = 'http://localhost:8081'; 
} else {
    API_BASE_URL = 'https://calculadora-veterinaria-api.fly.dev';
}
document.addEventListener("DOMContentLoaded", async () => {
    const elements = {
        nomeDisplay: document.getElementById("nome-display"),
        emailDisplay: document.getElementById("email-display"),
        cpfDisplay: document.getElementById("cpf-display"),
        rgDisplay: document.getElementById("rg-display"),
        ocupacaoDisplay: document.getElementById("ocupacao-display"),
        crmvDisplay: document.getElementById("crmv-display"),
        cpfInput: document.getElementById("cpf-input"),
        rgInput: document.getElementById("rg-input"),
        ocupacaoInput: document.getElementById("ocupacao-input"),
        crmvInput: document.getElementById("crmv-input"),
        btnEditar: document.getElementById("btn-editar"),
        btnSalvar: document.getElementById("btn-salvar"),
        btnCancelar: document.getElementById("btn-cancelar"),
        btnVoltar: document.getElementById("btn-voltar"),
        profileAvatar: document.getElementById('profile-avatar'),
        btnTrocarAvatar: document.getElementById('btn-trocar-avatar'),
        form: document.getElementById("profile-form")
    };

    const token = localStorage.getItem('jwtToken');
    if (!token) {
        alert("Sessão não encontrada. Por favor, faça o login.");
        window.location.href = "/pagina-login";
        return;
    }

    // Função para alternar entre modo de visualização e edição
    function toggleEditMode(isEditing) {
        // Mostra/esconde os textos de display
        document.querySelectorAll('.info-text').forEach(el => el.classList.toggle('hidden', isEditing));
        // Mostra/esconde os inputs do formulário
        document.querySelectorAll('.info-input').forEach(el => el.classList.toggle('hidden', !isEditing));

        // Mostra/esconde os botões correspondentes
        elements.btnEditar.classList.toggle('hidden', isEditing);
        elements.btnVoltar.classList.toggle('hidden', isEditing);
        elements.btnSalvar.classList.toggle('hidden', !isEditing);
        elements.btnCancelar.classList.toggle('hidden', !isEditing);
        elements.btnTrocarAvatar.classList.toggle('hidden', !isEditing);
    }

     const avatarList = [
        'panda.png', 'macaco.png', 'porco.png', 'coelho.png', 'sapo.png',
        'cachorro.png', 'vaca.png', 'elefante.png', 'raposa.png'
    ];
    const avatarPath = '/images/avatars/'; // Confirme se este é o caminho correto

    let selectedAvatarUrl = '';
    let currentUserData = {};

    // Função para abrir o modal de avatares
    function openAvatarModal() {
        const avatarGrid = document.querySelector("#avatarModal .avatar-grid");
        avatarGrid.innerHTML = ''; // Limpa a grade

        avatarList.forEach(avatarFile => {
            const fullPath = avatarPath + avatarFile;
            const img = document.createElement('img');
            img.src = fullPath;
            img.classList.add('avatar-option');
            if (fullPath === selectedAvatarUrl) {
                img.classList.add('selected');
            }
            img.onclick = () => {
                selectedAvatarUrl = fullPath;
                elements.profileAvatar.src = fullPath;
                bootstrap.Modal.getInstance(document.getElementById('avatarModal')).hide();
            };
            avatarGrid.appendChild(img);
        });

        new bootstrap.Modal(document.getElementById('avatarModal')).show();
    }
    try {
        const response = await fetch(`${API_BASE_URL}/api/conta-do-usuario`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) throw new Error("Falha ao buscar dados da conta.");
        
        currentUserData = await response.json();
        
        // Preenche tanto os displays quanto os inputs
        elements.nomeDisplay.textContent = currentUserData.nomeCompleto || 'Não informado';
        elements.emailDisplay.textContent = currentUserData.email || 'Não informado';

        elements.cpfDisplay.textContent = currentUserData.cpf || 'Não informado';
        elements.cpfInput.value = currentUserData.cpf || '';
        
        elements.rgDisplay.textContent = currentUserData.rg || 'Não informado';
        elements.rgInput.value = currentUserData.rg || '';

        elements.ocupacaoDisplay.textContent = currentUserData.ocupacao || 'Não informado';
        elements.ocupacaoInput.value = currentUserData.ocupacao || '';

        elements.crmvDisplay.textContent = currentUserData.crmv || 'Não informado';
        elements.crmvInput.value = currentUserData.crmv || '';

        selectedAvatarUrl = currentUserData.avatarUrl || avatarPath + 'default.png';
        elements.profileAvatar.src = selectedAvatarUrl;
        
    } catch (error) {
        console.error("Erro ao carregar dados da conta:", error);
    }

    elements.btnEditar.addEventListener("click", () => {
        toggleEditMode(true);
    });

    elements.btnCancelar.addEventListener("click", () => {
        // Restaura os valores originais nos inputs
        elements.cpfInput.value = currentUserData.cpf || '';
        elements.rgInput.value = currentUserData.rg || '';
        elements.ocupacaoInput.value = currentUserData.ocupacao || '';
        elements.crmvInput.value = currentUserData.crmv || '';
        elements.profileAvatar.src = currentUserData.avatarUrl || avatarPath + 'default.png';
        selectedAvatarUrl = currentUserData.avatarUrl || avatarPath + 'default.png';
        toggleEditMode(false);
    });
    elements.btnTrocarAvatar.addEventListener('click', openAvatarModal);
    elements.form.addEventListener("submit", async (event) => {
        event.preventDefault();

        const profileData = {
            cpf: elements.cpfInput.value,
            rg: elements.rgInput.value,
            ocupacao: elements.ocupacaoInput.value,
            crmv: elements.crmvInput.value,
            avatarUrl: selectedAvatarUrl
        };

        try {
            const updateResponse = await fetch(`${API_BASE_URL}/api/conta-do-usuario`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(profileData)
            });

            if (!updateResponse.ok) throw new Error("Falha ao atualizar o perfil.");

            alert("Perfil atualizado com sucesso!");
            
            // Atualiza os dados locais e os displays na tela
            currentUserData = {...currentUserData, ...profileData};
            elements.cpfDisplay.textContent = currentUserData.cpf || 'Não informado';
            elements.rgDisplay.textContent = currentUserData.rg || 'Não informado';
            elements.ocupacaoDisplay.textContent = currentUserData.ocupacao || 'Não informado';
            elements.crmvDisplay.textContent = currentUserData.crmv || 'Não informado';

            // Volta para o modo de visualização
            toggleEditMode(false);

        } catch (error) {
            console.error("Erro ao salvar alterações:", error);
            alert("Não foi possível salvar as alterações. Tente novamente.");
        }
    });
});