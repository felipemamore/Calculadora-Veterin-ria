document.getElementById('reset-form').addEventListener('submit', function (e) {
  e.preventDefault();

  const email = document.getElementById('email').value.trim();

  if (!email) {
    alert('Por favor, insira um e-mail válido.');
    return;
  }

  fetch('/api/senha/forgot-password', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ email: email })
  })
  .then(async response => {
    const contentType = response.headers.get('content-type');
    const isJson = contentType && contentType.includes('application/json');
    const data = isJson ? await response.json() : null;

    if (!response.ok) {
      const message = data?.message || 'Erro ao solicitar redefinição.';
      throw new Error(message);
    }

    alert('Se o e-mail estiver cadastrado, você receberá um link para redefinir sua senha.');
    document.getElementById('reset-form').reset();
  })
  .catch(error => {
    alert(error.message || 'Erro desconhecido ao solicitar redefinição.');
  });
});
