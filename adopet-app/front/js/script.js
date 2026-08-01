// =========================================
// ADOPET - Professional Pet Adoption System
// Main JavaScript with Theme Support
// =========================================

// -----------------------------------------
// API CONFIGURATION
// -----------------------------------------
const API_URL = "http://localhost:8080"
const L = window.L

// -----------------------------------------
// THEME MANAGEMENT
// -----------------------------------------
function initTheme() {
  const savedTheme = localStorage.getItem("theme")
  const prefersDark = window.matchMedia("(prefers-color-scheme: dark)").matches

  if (savedTheme) {
    document.documentElement.setAttribute("data-theme", savedTheme)
  } else if (prefersDark) {
    document.documentElement.setAttribute("data-theme", "dark")
  }
}

function toggleTheme() {
  const currentTheme = document.documentElement.getAttribute("data-theme")
  const newTheme = currentTheme === "dark" ? "light" : "dark"

  document.documentElement.setAttribute("data-theme", newTheme)
  localStorage.setItem("theme", newTheme)
}

// Initialize theme on page load
initTheme()

// -----------------------------------------
// USER MANAGEMENT
// -----------------------------------------
function getUsuarioLogado() {
  const usuario = localStorage.getItem("usuario")

  return usuario ? JSON.parse(usuario) : null
}

function setUsuarioLogado(usuario) {
  localStorage.setItem("usuario", JSON.stringify(usuario))
}

function logout() {
  localStorage.removeItem("usuario")
  window.location.href = "index.html"
}

function protegerPagina() {
  const usuario = getUsuarioLogado()
  if (!usuario) {
    showToast("Acesso restrito", "Faca login para acessar esta pagina.", "error")
    setTimeout(() => (window.location.href = "login.html"), 1500)
    return false
  }
  return true
}

// -----------------------------------------
// TOAST NOTIFICATIONS
// -----------------------------------------
function showToast(title, message, type = "info") {
  const toast = document.getElementById("toast")
  if (!toast) return

  const toastElement = document.createElement("div")
  toastElement.className = `toast-message ${type}`

  let icon = "bi-info-circle"
  if (type === "success") icon = "bi-check-circle"
  if (type === "error") icon = "bi-x-circle"
  if (type === "warning") icon = "bi-exclamation-triangle"

  toastElement.innerHTML = `
    <i class="bi ${icon}"></i>
    <div class="toast-content">
      <div class="toast-title">${title}</div>
      <div class="toast-text">${message}</div>
    </div>
  `
  toast.appendChild(toastElement)

  setTimeout(() => {
    toastElement.style.animation = "toastSlideIn 0.3s ease reverse"
    setTimeout(() => toastElement.remove(), 300)
  }, 4000)
}

// -----------------------------------------
// MOBILE NAVIGATION
// -----------------------------------------
function toggleMobileMenu() {
  const nav = document.getElementById("nav-auth")
  if (nav) {
    nav.classList.toggle("open")
  }
}

// -----------------------------------------
// DYNAMIC NAVIGATION
// -----------------------------------------
function renderNavAuth() {
  const nav = document.getElementById("nav-auth")
  if (!nav) return

  const usuario = getUsuarioLogado()

  if (usuario) {
    let links = `
      <a href="index.html" class="nav-link"><i class="bi bi-grid-1x2"></i> Explorar</a>
      <a href="meus-animais.html" class="nav-link"><i class="bi bi-collection"></i> Meus Animais</a>
      <a href="minhas-solicitacoes.html" class="nav-link"><i class="bi bi-send"></i> Solicitacoes</a>
      <a href="solicitacoes-recebidas.html" class="nav-link"><i class="bi bi-inbox"></i> Recebidas</a>
      <a href="transporte.html" class="nav-link"><i class="bi bi-truck"></i> Transportes</a>
      <span class="nav-divider"></span>
      <a href="perfil.html" class="nav-link"><i class="bi bi-person"></i> Perfil</a>
    `

    if (usuario.papel === "ADMINISTRADOR") {
      links += `<a href="admin.html" class="nav-link"><i class="bi bi-shield-check"></i> Admin</a>`
    }

    links += `<a href="#" onclick="logout(); return false;" class="btn btn-secondary btn-sm"><i class="bi bi-box-arrow-right"></i> Sair</a>`

    nav.innerHTML = links
  } else {
    nav.innerHTML = `
      <a href="index.html" class="nav-link"><i class="bi bi-grid-1x2"></i> Explorar</a>
      <span class="nav-divider"></span>
      <a href="login.html" class="btn btn-primary btn-sm"><i class="bi bi-arrow-right"></i> Entrar</a>
    `
  }
}

// -----------------------------------------
// AUTHENTICATION
// -----------------------------------------
function toggleAuthMode() {
  const loginForm = document.getElementById("login-form")
  const registerForm = document.getElementById("register-form")

  if (loginForm && registerForm) {
    loginForm.classList.toggle("hidden")
    registerForm.classList.toggle("hidden")
  }
}

async function handleLogin(event) {
  event.preventDefault()

  const form = event.target
  const email = form.querySelector('[name="email"]').value
  const senha = form.querySelector('[name="senha"]').value
  const submitBtn = form.querySelector('button[type="submit"]')

  submitBtn.disabled = true
  submitBtn.innerHTML = '<i class="bi bi-arrow-repeat animate-spin"></i> Entrando...'

  try {
    const response = await fetch(`${API_URL}/usuarios/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, senha }),
    })

    if (!response.ok) {
      const error = await response.json()
      throw new Error(error.message || "Credenciais invalidas")
    }

    const usuario = await response.json()
    setUsuarioLogado(usuario)

    showToast("Bem-vindo!", `Ola, ${usuario.nomeCompleto || "Usuario"}!`, "success")
    setTimeout(() => (window.location.href = "index.html"), 1500)
  } catch (error) {
    showToast("Erro no login", error.message || "Verifique suas credenciais.", "error")
    submitBtn.disabled = false
    submitBtn.innerHTML = '<i class="bi bi-arrow-right"></i> Entrar'
  }
}

async function handleRegister(event) {
  event.preventDefault()

  const form = event.target
  const nomeCompleto = form.querySelector('[name="nomeCompleto"]').value
  const email = form.querySelector('[name="email"]').value
  const senha = form.querySelector('[name="senha"]').value
  const submitBtn = form.querySelector('button[type="submit"]')

  submitBtn.disabled = true
  submitBtn.innerHTML = '<i class="bi bi-arrow-repeat animate-spin"></i> Criando conta...'

  try {
    const response = await fetch(`${API_URL}/usuarios/criar`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ nomeCompleto, email, senha }),
    })

    if (!response.ok) {
      const error = await response.json()
      throw new Error(error.message || "Erro ao criar conta")
    }

    const usuario = await response.json()
    setUsuarioLogado(usuario)

    showToast("Conta criada!", "Sua conta foi criada com sucesso.", "success")
    setTimeout(() => (window.location.href = "index.html"), 1500)
  } catch (error) {
    showToast("Erro no cadastro", error.message || "Tente novamente.", "error")
    submitBtn.disabled = false
    submitBtn.innerHTML = '<i class="bi bi-check"></i> Criar conta'
  }
}

// -----------------------------------------
// ANIMAL LISTING (HOME)
// -----------------------------------------
async function carregarAnimais() {
  const container = document.getElementById("lista-animais")
  if (!container) return

  container.innerHTML = Array(6)
    .fill(`
    <div class="card">
      <div class="card-image skeleton" style="height: 200px;"></div>
      <div class="card-body">
        <div class="skeleton" style="height: 24px; width: 60%; margin-bottom: 12px;"></div>
        <div class="skeleton" style="height: 16px; width: 80%;"></div>
      </div>
    </div>
  `)
    .join("")

  try {
    const response = await fetch(`${API_URL}/animais/listar`)
    if (!response.ok) throw new Error()

    const animais = await response.json()

    if (animais.length === 0) {
      container.innerHTML = `
        <div class="empty-state" style="grid-column: 1 / -1;">
          <div class="empty-state-icon"><i class="bi bi-heart"></i></div>
          <h3 class="empty-state-title">Nenhum pet disponivel</h3>
          <p class="empty-state-description">No momento nao ha animais disponiveis para adocao. Volte em breve!</p>
        </div>
      `
      return
    }

    container.innerHTML = animais
      .map((animal) => {
        const imgUrl =
          animal.fotosUrls && animal.fotosUrls.length > 0 ? animal.fotosUrls[0] : "/cute-pet.png"

        const badgeClass = animal.especie?.toLowerCase() === "gato" ? "cat" : "dog"

        return `
        <a href="animal.html?id=${animal.id}" class="card">
          <div class="card-image">
            <img src="${imgUrl}" alt="${animal.nome}" onerror="this.src='/cute-pet.png'">
            <span class="card-badge ${badgeClass}"><i class="bi bi-tag"></i> ${animal.especie || "Pet"}</span>
          </div>
          <div class="card-body">
            <h3 class="card-title">${animal.nome}</h3>
            <div class="card-meta">
              <span class="card-meta-item"><i class="bi bi-bookmark"></i> ${animal.raca || "SRD"}</span>
              <span class="card-meta-item"><i class="bi bi-calendar3"></i> ${animal.idade} ${animal.idade === 1 ? "ano" : "anos"}</span>
            </div>
          </div>
        </a>
      `
      })
      .join("")
  } catch (error) {
    container.innerHTML = `
      <div class="empty-state" style="grid-column: 1 / -1;">
        <div class="empty-state-icon"><i class="bi bi-exclamation-triangle"></i></div>
        <h3 class="empty-state-title">Erro ao carregar</h3>
        <p class="empty-state-description">Nao foi possivel carregar os animais. Tente novamente mais tarde.</p>
        <button class="btn btn-secondary mt-4" onclick="carregarAnimais()"><i class="bi bi-arrow-clockwise"></i> Tentar novamente</button>
      </div>
    `
  }
}

async function carregarMeusAnimais() {
  const container = document.getElementById("lista-meus-animais");
  if (!container) return;

  // 1. Pegar o ID do usuário logado (armazenado no login)
  const usuarioLogado = JSON.parse(localStorage.getItem("usuario"));

  if (!usuarioLogado || !usuarioLogado.id) {
    container.innerHTML = `<p>Você precisa estar logado para ver seus animais.</p>`;
    return;
  }

  // Skeleton Loading
  container.innerHTML = Array(3).fill(`
    <div class="card">
      <div class="card-image skeleton" style="height: 200px;"></div>
      <div class="card-body">
        <div class="skeleton" style="height: 24px; width: 60%; margin-bottom: 12px;"></div>
      </div>
    </div>
  `).join("");

  try {
    // 2. Chamar o novo endpoint que criamos no Java
    const response = await fetch(`${API_URL}/animais/doador/${usuarioLogado.id}`);

    if (!response.ok) throw new Error("Erro ao buscar animais");

    const animais = await response.json();

    if (animais.length === 0) {
      container.innerHTML = `
        <div class="empty-state" style="grid-column: 1 / -1;">
          <div class="empty-state-icon"><i class="bi bi-plus-circle"></i></div>
          <h3 class="empty-state-title">Você ainda não cadastrou nenhum pet</h3>
          <p class="empty-state-description">Comece ajudando um animal a encontrar um lar!</p>
          <a href="cadastrar-animal.html" class="btn btn-primary mt-4">Cadastrar meu primeiro pet</a>
        </div>
      `;
      return;
    }

    // 3. Renderizar os cards
    container.innerHTML = animais.map((animal) => {
      const imgUrl = animal.fotosUrls && animal.fotosUrls.length > 0
        ? animal.fotosUrls[0]
        : "img/default-pet.png";

      return `
        <div class="card">
          <div class="card-image">
            <img src="${imgUrl}" alt="${animal.nome}">
            <span class="card-badge ${animal.status === 'DISPONIVEL' ? 'bg-success' : 'bg-warning'}">
              ${animal.status}
            </span>
          </div>
          <div class="card-body">
            <h3 class="card-title">${animal.nome}</h3>
            <div class="card-meta">
              <span><i class="bi bi-info-circle"></i> ${animal.especie}</span>
            </div>
            <div class="card-actions" style="margin-top: 15px; display: flex; gap: 10px;">
              <a href="editar-animal.html?id=${animal.id}" class="btn btn-sm btn-outline-primary" style="flex: 1; text-align: center;">
                <i class="bi bi-pencil"></i> Editar
              </a>
              <button onclick="deletarAnimal('${animal.id}')" class="btn btn-sm btn-outline-danger" style="flex: 1;">
                <i class="bi bi-trash"></i> Excluir
              </button>
            </div>
          </div>
        </div>
      `;
    }).join("");

  } catch (error) {
    container.innerHTML = `<div class="error">Erro ao carregar seus animais.</div>`;
  }
}

// Função auxiliar para deletar (aproveitando o endpoint DELETE do seu Service)
async function deletarAnimal(id) {
  if (confirm("Tem certeza que deseja remover este anúncio?")) {
    try {
      const response = await fetch(`${API_URL}/animais/${id}`, { method: 'DELETE' });
      if (response.ok) {
        showToast("Animal removido com sucesso!");
        carregarMeusAnimais(); // Recarrega a lista
      }
    } catch (e) {
      showToast("Erro ao deletar.");
    }
  }
}

// -----------------------------------------
// ANIMAL DETAIL PAGE
// -----------------------------------------
async function carregarAnimal() {
  const params = new URLSearchParams(window.location.search)
  const id = params.get("id")

  if (!id) {
    window.location.href = "index.html"
    return
  }

  try {
    const response = await fetch(`${API_URL}/animais/${id}`)
    if (!response.ok) throw new Error()

    const animal = await response.json()

    document.getElementById("animal-nome").textContent = animal.nome
    document.getElementById("animal-descricao").textContent = animal.descricao || "Sem descricao disponivel."

    const imgUrl = animal.fotosUrls && animal.fotosUrls.length > 0 ? animal.fotosUrls[0] : "/cute-pet.png"
    document.getElementById("animal-imagem").src = imgUrl

    const tagsContainer = document.getElementById("animal-tags")
    tagsContainer.innerHTML = `
      <span class="badge badge-default"><i class="bi bi-tag"></i> ${animal.especie || "Pet"}</span>
      <span class="badge badge-default"><i class="bi bi-bookmark"></i> ${animal.raca || "SRD"}</span>
      ${animal.vacinado ? '<span class="badge badge-success"><i class="bi bi-check"></i> Vacinado</span>' : ""}
      ${animal.castrado ? '<span class="badge badge-success"><i class="bi bi-check"></i> Castrado</span>' : ""}
    `

    document.getElementById("animal-idade").textContent = `${animal.idade} ${animal.idade === 1 ? "ano" : "anos"}`
    document.getElementById("animal-especie").textContent = animal.especie || "Nao informado"
    document.getElementById("animal-raca").textContent = animal.raca || "SRD"
    document.getElementById("animal-status").textContent = animal.status || "Disponivel"

    window.animalAtual = animal
  } catch (error) {
    showToast("Erro", "Nao foi possivel carregar os dados do animal.", "error")
  }
}

async function solicitarAdocao() {
  const usuario = getUsuarioLogado()
  if (!usuario) {
    showToast("Login necessario", "Faca login para solicitar adocao.", "warning")
    setTimeout(() => (window.location.href = "login.html"), 1500)
    return
  }

  if (!window.animalAtual) return

  try {
    const response = await fetch(`${API_URL}/adocoes/solicitar`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        animalId: window.animalAtual.id,
        solicitanteId: usuario.id,
      }),
    })

    if (!response.ok) {
      const error = await response.json()
      throw new Error(error.message || "Erro ao solicitar adocao")
    }

    showToast("Solicitacao enviada!", "O doador sera notificado sobre seu interesse.", "success")
  } catch (error) {
    showToast("Erro", error.message || "Nao foi possivel enviar a solicitacao.", "error")
  }
}

// -----------------------------------------
// REGISTER ANIMAL
// -----------------------------------------
async function cadastrarAnimal(event) {
  event.preventDefault();

  const usuario = getUsuarioLogado();
  if (!usuario) return logout();

  const submitBtn = event.target.querySelector('button[type="submit"]');
  const fotoInput = document.getElementById("foto");
  let fotoUrl = fotoInput.value.trim();

  // 1. Tratamento da URL do Google (Limpeza de parâmetros excessivos)
  if (fotoUrl.includes("google.com/imgres")) {
    showToast("Link Inválido", "Por favor, copie o 'Endereço da Imagem' e não o link da busca.", "error");
    return;
  }

  // 2. Validação de tamanho (Evita erro de banco de dados/payload)
  if (fotoUrl.length > 2000) { // Links normais raramente passam de 500-1000
    showToast("URL muito longa", "O link da imagem é muito extenso. Tente outra imagem.", "error");
    return;
  }

  // Feedback visual de carregamento
  submitBtn.disabled = true;
  const originalBtnText = submitBtn.innerHTML;
  submitBtn.innerHTML = '<i class="bi bi-arrow-repeat animate-spin"></i> Cadastrando...';

  const animalData = {
    nome: document.getElementById("nome").value.trim(),
    especie: document.getElementById("especie").value,
    raca: document.getElementById("raca").value.trim(),
    idade: Number.parseInt(document.getElementById("idade").value) || 0,
    descricao: document.getElementById("descricao").value.trim(),
    vacinado: document.getElementById("vacinado").checked,
    castrado: document.getElementById("castrado").checked,
    fotosUrls: fotoUrl ? [fotoUrl] : [],
    doadorId: usuario.id,
  };

  try {
    const response = await fetch(`${API_URL}/animais/criar`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        // Adicione Token de autenticação se sua API exigir:
        // "Authorization": `Bearer ${usuario.token}` 
      },
      body: JSON.stringify(animalData),
    });

    const data = await response.json();

    // Dentro da função carregarAnimal(), após carregar o JSON:
    const usuarioLogado = getUsuarioLogado();
    const acoesContainer = document.getElementById("animal-acoes");

    if (usuarioLogado && animal.doador && usuarioLogado.id === animal.doador.id) {
      // Se for o DONO, mostra botões de gestão
      acoesContainer.innerHTML = `
    <button onclick="abrirModalEdicao('${animal.id}')" class="btn btn-warning">
      <i class="bi bi-pencil"></i> Editar Informações
    </button>
    <button onclick="confirmarExclusao('${animal.id}')" class="btn btn-danger">
      <i class="bi bi-trash"></i> Remover Anúncio
    </button>
  `;
    } else {
      // Se NÃO for o dono, mostra botão de solicitar adoção
      acoesContainer.innerHTML = `
    <button onclick="solicitarAdocao()" class="btn btn-primary btn-lg">
      <i class="bi bi-heart-fill"></i> Quero Adotar
    </button>
  `;
    }

    // Função de Exclusão
    async function confirmarExclusao(id) {
      if (confirm("Tem certeza que deseja remover permanentemente este pet?")) {
        try {
          const res = await fetch(`${API_URL}/animais/${id}`, { method: 'DELETE' });
          if (res.ok) {
            showToast("Removido", "Animal removido com sucesso", "success");
            setTimeout(() => window.location.href = "index.html", 1500);
          }
        } catch (e) {
          showToast("Erro", "Erro ao excluir", "error");
        }
      }
    }

    if (!response.ok) {
      throw new Error(data.message || "Erro ao cadastrar animal");
    }

    showToast("Pet cadastrado!", "Seu pet foi adicionado com sucesso.", "success");

    // Limpar formulário antes de redirecionar (boa prática)
    event.target.reset();

    setTimeout(() => (window.location.href = "index.html"), 2000);

  } catch (error) {
    showToast("Erro", error.message || "Não foi possível cadastrar o animal.", "error");

    // Restaurar botão em caso de erro
    submitBtn.disabled = false;
    submitBtn.innerHTML = originalBtnText;
  }
}

// --- FUNÇÕES DO MODAL ---
function abrirModalCadastro() {
  document.getElementById("form-animal").reset();
  document.getElementById("animal-id-edit").value = "";
  document.getElementById("modal-animal-titulo").innerText = "Cadastrar Novo Pet";

  // Mostra o modal adicionando a classe active
  document.getElementById("modal-animal").classList.add("active");
}

async function abrirModalEdicao(id) {
  try {
    // Exibe um feedback visual ou apenas busca os dados
    const response = await fetch(`${API_URL}/animais/${id}`);
    if (!response.ok) throw new Error("Não foi possível carregar os dados");

    const animal = await response.json();

    // Preenche os campos do modal
    document.getElementById("animal-id-edit").value = animal.id;
    document.getElementById("animal-nome").value = animal.nome;
    document.getElementById("animal-especie").value = animal.especie;
    document.getElementById("animal-idade").value = animal.idade;
    document.getElementById("animal-raca").value = animal.raca || "";
    document.getElementById("animal-foto").value = (animal.fotosUrls && animal.fotosUrls[0]) || "";
    document.getElementById("animal-descricao").value = animal.descricao || "";
    document.getElementById("animal-vacinado").checked = animal.vacinado;
    document.getElementById("animal-castrado").checked = animal.castrado;

    document.getElementById("modal-animal-titulo").innerText = "Editar Pet";

    // Abre o modal
    document.getElementById("modal-animal").classList.add("active");
  } catch (error) {
    console.error(error);
    alert("Erro ao buscar dados do animal.");
  }
}

function fecharModal() {
  document.getElementById("modal-animal").classList.remove("active");
}

// --- RENDERIZAÇÃO DA LISTA ---

async function carregarMeusAnimais() {
  const container = document.getElementById("lista-meus-animais");
  const usuarioLogado = JSON.parse(localStorage.getItem("usuario"));

  if (!usuarioLogado || !container) return;

  try {
    const response = await fetch(`${API_URL}/animais/doador/${usuarioLogado.id}`);
    const animais = await response.json();

    if (animais.length === 0) {
      container.innerHTML = '<div class="empty-state"><i class="bi bi-info-circle"></i><p>Você ainda não cadastrou nenhum pet.</p></div>';
      return;
    }

    container.innerHTML = animais.map(animal => `
            <div class="section-card" style="margin-bottom: 1rem;">
                <div style="display: flex; align-items: center; padding: 1rem; gap: 1rem;">
                    <img src="${animal.fotosUrls?.[0] || 'img/default.png'}" 
                         style="width: 60px; height: 60px; border-radius: 8px; object-fit: cover;">
                    
                    <div style="flex: 1;">
                        <h3 style="font-size: 1rem; margin: 0; color: var(--text-primary);">${animal.nome}</h3>
                        <span class="badge badge-admin">${animal.especie}</span>
                    </div>

                    <div class="actions">
                        <button onclick="abrirModalEdicao('${animal.id}')" class="btn-icon edit" title="Editar">
                            <i class="bi bi-pencil"></i>
                        </button>
                        <button onclick="deletarAnimal('${animal.id}')" class="btn-icon delete" title="Excluir">
                            <i class="bi bi-trash"></i>
                        </button>
                    </div>
                </div>
            </div>
        `).join("");
  } catch (error) {
    console.error("Erro ao listar:", error);
  }
}

// -----------------------------------------
// MY REQUESTS
// -----------------------------------------
async function carregarMinhasSolicitacoes() {
  const container = document.getElementById("lista-solicitacoes")
  if (!container) return

  const usuario = getUsuarioLogado()
  if (!usuario) return

  try {
    const response = await fetch(`${API_URL}/adocoes/solicitante/${usuario.id}`)
    if (!response.ok) throw new Error()

    const solicitacoes = await response.json()

    if (solicitacoes.length === 0) {
      container.innerHTML = `
        <div class="empty-state">
          <div class="empty-state-icon"><i class="bi bi-send"></i></div>
          <h3 class="empty-state-title">Nenhuma solicitacao</h3>
          <p class="empty-state-description">Voce ainda nao fez nenhuma solicitacao de adocao.</p>
          <a href="index.html" class="btn btn-primary mt-4"><i class="bi bi-search"></i> Explorar pets</a>
        </div>
      `
      return
    }

    container.innerHTML = solicitacoes
      .map(
        (s) => `
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">${s.animal?.nome || "Animal"}</div>
            <div class="list-item-meta">
              <span><i class="bi bi-person"></i> Doador: ${s.animal?.doador?.nomeCompleto || "Desconhecido"}</span>
              <span><i class="bi bi-calendar3"></i> ${new Date(s.dataSolicitacao).toLocaleDateString("pt-BR")}</span>
            </div>
          </div>
          <span class="status ${s.status}">${s.status}</span>
        </div>
      `,
      )
      .join("")
  } catch (error) {
    container.innerHTML = `
      <div class="empty-state">
        <div class="empty-state-icon"><i class="bi bi-exclamation-triangle"></i></div>
        <h3 class="empty-state-title">Erro ao carregar</h3>
        <p class="empty-state-description">Nao foi possivel carregar suas solicitacoes.</p>
      </div>
    `
  }
}


// -----------------------------------------
// RECEIVED REQUESTS
// -----------------------------------------
async function carregarSolicitacoesRecebidas() {
  const container = document.getElementById("lista-recebidas")
  if (!container) return

  const usuario = getUsuarioLogado()
  if (!usuario) return

  try {
    const response = await fetch(`${API_URL}/adocoes/doador/${usuario.id}`)
    if (!response.ok) throw new Error()

    const solicitacoes = await response.json()

    if (solicitacoes.length === 0) {
      container.innerHTML = `
        <div class="empty-state">
          <div class="empty-state-icon"><i class="bi bi-inbox"></i></div>
          <h3 class="empty-state-title">Nenhuma solicitacao recebida</h3>
          <p class="empty-state-description">Voce ainda nao recebeu solicitacoes de adocao.</p>
        </div>
      `
      return
    }

    container.innerHTML = solicitacoes
      .map(
        (s) => `
      <div class="list-item">
        <div class="list-item-content">
          <div class="list-item-title">${s.animal?.nome || "Animal"}</div>
          <div class="list-item-meta">
            <span><i class="bi bi-person"></i> ${s.solicitante?.nomeCompleto || "Solicitante"}</span>
            <span><i class="bi bi-calendar3"></i> ${new Date(s.criadoEm).toLocaleDateString("pt-BR")}</span>
          </div>
        </div>
        <div class="list-item-actions">
          <span class="status ${s.status}">${s.status}</span>
          ${s.status === "PENDENTE"
            ? `
            <button class="btn btn-success btn-sm" onclick="responderSolicitacao('${s.id}', 'APROVADA')">
              <i class="bi bi-check"></i> Aprovar
            </button>
            <button class="btn btn-danger btn-sm" onclick="responderSolicitacao('${s.id}', 'REJEITADA')">
              <i class="bi bi-x"></i> Rejeitar
            </button>
          `
            : ""
          }
        </div>
      </div>
    `,
      )
      .join("")
  } catch (error) {
    container.innerHTML = `
      <div class="empty-state">
        <div class="empty-state-icon"><i class="bi bi-exclamation-triangle"></i></div>
        <h3 class="empty-state-title">Erro ao carregar</h3>
        <p class="empty-state-description">Nao foi possivel carregar as solicitacoes.</p>
      </div>
    `
  }
}

async function responderSolicitacao(id, acao) {
  const endpoint = acao.toLowerCase() === 'aprovada' ? 'aprovar' : 'rejeitar';

  try {
    const response = await fetch(`${API_URL}/adocoes/solicitacoes/${id}/${endpoint}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" }
    });

    if (!response.ok) throw new Error();

    showToast("Sucesso", `Solicitação atualizada com sucesso!`, "success");
    carregarSolicitacoesRecebidas(); // Recarrega a lista
  } catch (error) {
    showToast("Erro", "Não foi possível processar a ação.", "error");
  }
}

// -----------------------------------------
// PROFILE
// -----------------------------------------
async function carregarPerfil() {
  const usuario = getUsuarioLogado()
  if (!usuario) return

  try {
    const response = await fetch(`${API_URL}/usuarios/${usuario.id}`)
    if (!response.ok) throw new Error()

    const dados = await response.json()

    document.getElementById("nomeCompleto").value = dados.nomeCompleto || ""
    document.getElementById("email").value = dados.email || ""
    document.getElementById("telefone").value = dados.telefone || ""

    if (dados.endereco) {
      document.getElementById("rua").value = dados.endereco.rua || ""
      document.getElementById("numero").value = dados.endereco.numero || ""
      document.getElementById("bairro").value = dados.endereco.bairro || ""
      document.getElementById("cidade").value = dados.endereco.cidade || ""
      document.getElementById("estado").value = dados.endereco.estado || ""
      document.getElementById("cep").value = dados.endereco.cep || ""
    }
  } catch (error) {
    showToast("Erro", "Nao foi possivel carregar o perfil.", "error")
  }
}

async function salvarPerfil(event) {
  event.preventDefault()

  const usuario = getUsuarioLogado()
  if (!usuario) return logout()

  const submitBtn = event.target.querySelector('button[type="submit"]')
  submitBtn.disabled = true
  submitBtn.innerHTML = '<i class="bi bi-arrow-repeat animate-spin"></i> Salvando...'

  const dadosAtualizados = {
    nomeCompleto: document.getElementById("nomeCompleto").value,
    email: document.getElementById("email").value,
    telefone: document.getElementById("telefone").value,
    endereco: {
      rua: document.getElementById("rua").value,
      numero: document.getElementById("numero").value,
      bairro: document.getElementById("bairro").value,
      cidade: document.getElementById("cidade").value,
      estado: document.getElementById("estado").value,
      cep: document.getElementById("cep").value,
    },
  }

  try {
    const response = await fetch(`${API_URL}/usuarios/${usuario.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dadosAtualizados),
    })

    if (!response.ok) throw new Error()

    const usuarioAtualizado = await response.json()
    setUsuarioLogado(usuarioAtualizado)

    showToast("Perfil atualizado!", "Suas informacoes foram salvas.", "success")
  } catch (error) {
    showToast("Erro", "Nao foi possivel salvar o perfil.", "error")
  } finally {
    submitBtn.disabled = false
    submitBtn.innerHTML = '<i class="bi bi-check"></i> Salvar alteracoes'
  }
}

// -----------------------------------------
// DENUNCIA
// -----------------------------------------

async function enviarDenuncia(event) {
  event.preventDefault();

  const usuarioLogado = getUsuarioLogado();

  if (!usuarioLogado) {
    showToast("Acesso negado", "Você precisa estar logado para enviar uma denúncia.", "error");
    return;
  }

  const submitBtn = event.target.querySelector('button[type="submit"]');
  submitBtn.disabled = true;
  submitBtn.innerHTML = '<i class="bi bi-arrow-repeat animate-spin"></i> Enviando...';

  const pet = window.animalAtual;

  const denuncia = {
    usuarioId: usuarioLogado.id,

    categoria: document.getElementById("tipo").value,
    descricao: document.getElementById("descricao").value,

    animalId: pet ? pet.id : null,
    usuarioDenunciadoId: pet ? (pet.doadorId || (pet.doador ? pet.doador.id : null)) : null
  };

  try {
    const response = await fetch(`${API_URL}/denuncias/criar`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(denuncia),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || "Erro ao processar denúncia");
    }

    showToast("Denúncia enviada!", "Sua denúncia foi registrada com sucesso.", "success");
    event.target.reset();
  } catch (error) {
    showToast("Erro", error.message || "Não foi possível enviar a denúncia.", "error");
  } finally {
    submitBtn.disabled = false;
    submitBtn.innerHTML = '<i class="bi bi-send"></i> Enviar denúncia';
  }
}

// -----------------------------------------
// TRANSPORT
// -----------------------------------------

let transportes = [];
let filtroAtual = 'todos';

// Inicializacao
document.addEventListener('DOMContentLoaded', () => {
  carregarTransportes();
});

// Carregar transportes do usuario
async function carregarTransportes() {
  try {
    const token = localStorage.getItem('token');
    const response = await fetch(`${API_URL}/transportes/usuario`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });

    if (response.ok) {
      transportes = await response.json();
    } else {
      // Dados de exemplo para demonstracao
      transportes = [
        {
          id: '1',
          criadoEm: '2024-01-15T10:30:00Z',
          adocao: {
            id: 'a1',
            animal: {
              nome: 'Thor',
              especie: 'CACHORRO',
              raca: 'Golden Retriever',
              foto: 'https://images.unsplash.com/photo-1552053831-71594a27632d?w=400'
            }
          },
          rastreamentos: [
            { id: 'r1', status: 'PENDENTE', mensagem: 'Transporte solicitado', criadoEm: '2024-01-15T10:30:00Z' },
            { id: 'r2', status: 'A_CAMINHO', mensagem: 'Pet a caminho do seu endereco', criadoEm: '2024-01-15T14:00:00Z' }
          ]
        },
        {
          id: '2',
          criadoEm: '2024-01-10T09:00:00Z',
          adocao: {
            id: 'a2',
            animal: {
              nome: 'Luna',
              especie: 'GATO',
              raca: 'Siames',
              foto: 'https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=400'
            }
          },
          rastreamentos: [
            { id: 'r3', status: 'PENDENTE', mensagem: 'Transporte solicitado', criadoEm: '2024-01-10T09:00:00Z' },
            { id: 'r4', status: 'A_CAMINHO', mensagem: 'Pet a caminho', criadoEm: '2024-01-10T12:00:00Z' },
            { id: 'r5', status: 'ENTREGUE', mensagem: 'Pet entregue com sucesso!', criadoEm: '2024-01-10T15:30:00Z' }
          ]
        },
        {
          id: '3',
          criadoEm: '2024-01-16T08:00:00Z',
          adocao: {
            id: 'a3',
            animal: {
              nome: 'Max',
              especie: 'CACHORRO',
              raca: 'Labrador',
              foto: 'https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=400'
            }
          },
          rastreamentos: [
            { id: 'r6', status: 'PENDENTE', mensagem: 'Aguardando confirmacao do transporte', criadoEm: '2024-01-16T08:00:00Z' }
          ]
        }
      ];
    }
    atualizarEstatisticas();
    renderizarTransportes();
  } catch (error) {
    console.error('Erro ao carregar transportes:', error);
    //mostrarToast('Erro ao carregar transportes', 'error');
  }
}

// Atualizar estatisticas
function atualizarEstatisticas() {
  const pendentes = transportes.filter(t => getStatusAtual(t) === 'PENDENTE').length;
  const aCaminho = transportes.filter(t => getStatusAtual(t) === 'A_CAMINHO').length;
  const entregues = transportes.filter(t => getStatusAtual(t) === 'ENTREGUE').length;

  document.getElementById('stat-total').textContent = transportes.length;
  document.getElementById('stat-pending').textContent = pendentes;
  document.getElementById('stat-transit').textContent = aCaminho;
  document.getElementById('stat-delivered').textContent = entregues;
}

// Obter status atual do transporte
function getStatusAtual(transporte) {
  if (!transporte.rastreamentos || transporte.rastreamentos.length === 0) {
    return 'PENDENTE';
  }
  const ultimoRastreamento = transporte.rastreamentos[transporte.rastreamentos.length - 1];
  return ultimoRastreamento.status;
}

// Filtrar transportes
function filtrarTransportes(filtro) {
  filtroAtual = filtro;

  document.querySelectorAll('.tab-btn').forEach(btn => {
    btn.classList.toggle('active', btn.dataset.filter === filtro);
  });

  renderizarTransportes();
}

// Renderizar transportes
function renderizarTransportes() {
  const tbody = document.getElementById('table-body');

  let transportesFiltrados = transportes;
  if (filtroAtual !== 'todos') {
    transportesFiltrados = transportes.filter(t => getStatusAtual(t) === filtroAtual);
  }

  if (transportesFiltrados.length === 0) {
    tbody.innerHTML = `
        <tr>
          <td colspan="5">
            <div class="empty-state">
              <div class="empty-state-icon">
                <i class="bi bi-truck"></i>
              </div>
              <h3 class="empty-state-title">Nenhum transporte encontrado</h3>
              <p class="empty-state-desc">
                ${filtroAtual === 'todos'
        ? 'Voce ainda nao tem transportes. Adote um pet para comecar!'
        : 'Nenhum transporte com este status.'}
              </p>
              ${filtroAtual === 'todos' ? `
                <a href="animais.html" class="btn btn-primary">
                  <i class="bi bi-heart"></i> Ver Animais
                </a>
              ` : ''}
            </div>
          </td>
        </tr>
      `;
    return;
  }

  tbody.innerHTML = transportesFiltrados.map(transporte => {
    const status = getStatusAtual(transporte);
    const animal = transporte.adocao?.animal || {};

    const step1Completed = true;
    const step2Active = status === 'A_CAMINHO';
    const step2Completed = status === 'ENTREGUE';
    const step3Completed = status === 'ENTREGUE';

    return `
        <tr>
          <td>
            <div class="animal-cell-transport">
              <img src="${animal.foto || 'https://via.placeholder.com/44'}" alt="${animal.nome}" class="animal-img-transport">
              <div class="animal-info-transport">
                <span class="animal-name-transport">${animal.nome || 'Animal'}</span>
                <span class="animal-meta-transport">${animal.especie || 'N/A'} - ${animal.raca || 'N/A'}</span>
              </div>
            </div>
          </td>
          <td>${formatarData(transporte.criadoEm)}</td>
          <td class="progress-cell">
            <div class="progress-mini">
              <div class="progress-dot ${step1Completed ? 'completed' : ''}">
                <i class="bi bi-check"></i>
              </div>
              <div class="progress-line ${step2Active || step2Completed ? 'completed' : ''}"></div>
              <div class="progress-dot ${step2Active ? 'active' : ''} ${step2Completed ? 'completed' : ''}">
                <i class="bi bi-${step2Completed ? 'check' : 'truck'}"></i>
              </div>
              <div class="progress-line ${step3Completed ? 'completed' : ''}"></div>
              <div class="progress-dot ${step3Completed ? 'completed' : ''}">
                <i class="bi bi-${step3Completed ? 'check' : 'house'}"></i>
              </div>
            </div>
          </td>
          <td>
            <span class="badge badge-${status === 'PENDENTE' ? 'pending' : status === 'A_CAMINHO' ? 'transit' : 'delivered'}">
              ${formatarStatus(status)}
            </span>
          </td>
          <td>
            <div class="actions">
              <button onclick="abrirRastreamento('${transporte.id}')" class="btn btn-icon view" title="Ver rastreamento">
                <i class="bi bi-geo-alt"></i>
              </button>
              ${status !== 'ENTREGUE' ? `
                <button onclick="entrarContato('${transporte.id}')" class="btn btn-icon" style="background: var(--success-bg); color: var(--success);" title="Contato">
                  <i class="bi bi-chat-dots"></i>
                </button>
              ` : ''}
            </div>
          </td>
        </tr>
      `;
  }).join('');
}

// Abrir modal de rastreamento
function abrirRastreamento(id) {
  const transporte = transportes.find(t => t.id === id);
  if (!transporte) return;

  const container = document.getElementById('timeline-container');
  const rastreamentos = transporte.rastreamentos || [];

  if (rastreamentos.length === 0) {
    container.innerHTML = `
        <div class="empty-state" style="padding: 2rem;">
          <i class="bi bi-clock-history" style="font-size: 2rem; color: var(--gray-400);"></i>
          <p style="margin-top: 1rem;">Nenhuma atualizacao disponivel</p>
        </div>
      `;
  } else {
    container.innerHTML = rastreamentos.map(r => `
        <div class="timeline-item">
          <div class="timeline-icon ${r.status === 'PENDENTE' ? 'pending' : r.status === 'A_CAMINHO' ? 'transit' : 'delivered'}">
            <i class="bi bi-${r.status === 'PENDENTE' ? 'clock' : r.status === 'A_CAMINHO' ? 'truck' : 'check-lg'}"></i>
          </div>
          <div class="timeline-content">
            <div class="timeline-title">${formatarStatus(r.status)}</div>
            <div class="timeline-desc">${r.mensagem || 'Sem descricao'}</div>
            <div class="timeline-time">${formatarDataHora(r.criadoEm)}</div>
          </div>
        </div>
      `).join('');
  }

  document.getElementById('modal-rastreamento').classList.add('active');
}

// Fechar modal de rastreamento
function fecharModalRastreamento(event) {
  if (event.target.classList.contains('modal-overlay')) {
    document.getElementById('modal-rastreamento').classList.remove('active');
  }
}

// Fechar modal
function fecharModal(id) {
  document.getElementById(id).classList.remove('active');
}

// Entrar em contato
function entrarContato(id) {
  mostrarToast('Abrindo chat de suporte...', 'success');
}

// Formatar status
function formatarStatus(status) {
  const statusMap = {
    'PENDENTE': 'Pendente',
    'A_CAMINHO': 'A Caminho',
    'ENTREGUE': 'Entregue'
  };
  return statusMap[status] || status;
}

// Formatar data
function formatarData(dataStr) {
  if (!dataStr) return 'N/A';
  const data = new Date(dataStr);
  return data.toLocaleDateString('pt-BR', {
    day: '2-digit',
    month: 'short',
    year: 'numeric'
  });
}

// Formatar data e hora
function formatarDataHora(dataStr) {
  if (!dataStr) return 'N/A';
  const data = new Date(dataStr);
  return data.toLocaleDateString('pt-BR', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
}

// Toast notification
function mostrarToast(mensagem, tipo = '') {
  const toast = document.getElementById('toast');
  toast.textContent = mensagem;
  toast.className = 'toast show ' + tipo;
  setTimeout(() => {
    toast.classList.remove('show');
  }, 3000);
}

// -----------------------------------------
// ADMIN
// -----------------------------------------
let tabAtual = 'usuarios'
let dadosCache = {}

// Carregar estatisticas dos cards
async function carregarDadosAdmin() {
  try {
    const [animaisRes, usuariosRes, solicitacoesRes, denunciasRes] = await Promise.all([
      fetch(`${API_URL}/animais/listar`),
      fetch(`${API_URL}/usuarios/listar`),
      fetch(`${API_URL}/adocoes/listar`),
      fetch(`${API_URL}/denuncias/listar`),
    ])

    const animais = animaisRes.ok ? await animaisRes.json() : []
    const usuarios = usuariosRes.ok ? await usuariosRes.json() : []
    const solicitacoes = solicitacoesRes.ok ? await solicitacoesRes.json() : []
    const denuncias = denunciasRes.ok ? await denunciasRes.json() : []

    // Atualizar cards de estatísticas
    const totalAnimaisEl = document.getElementById("total-animais")
    const totalUsuariosEl = document.getElementById("total-usuarios")
    const totalSolicitacoesEl = document.getElementById("total-solicitacoes")
    const totalDenunciasEl = document.getElementById("total-denuncias")
    const totalTransportesEl = document.getElementById("total-transportes")

    if (totalAnimaisEl) totalAnimaisEl.textContent = animais.length
    if (totalUsuariosEl) totalUsuariosEl.textContent = usuarios.length
    if (totalSolicitacoesEl) totalSolicitacoesEl.textContent = solicitacoes.length
    if (totalDenunciasEl) totalDenunciasEl.textContent = denuncias.length

    // Contar transportes (a partir das adocoes que tem transporte)
    if (totalTransportesEl) {
      const transportesCount = solicitacoes.filter(s => s.transporte).length
      totalTransportesEl.textContent = transportesCount
    }

    // Cache dos dados
    dadosCache.usuarios = usuarios
    dadosCache.animais = animais
    dadosCache.adocoes = solicitacoes
    dadosCache.denuncias = denuncias
  } catch (error) {
    showToast("Erro ao carregar dados", "Falha ao carregar estatísticas", "error")
  }
}

// Mudar tab
function mudarTab(tab) {
  tabAtual = tab

  document.querySelectorAll(".tab-btn").forEach(b => b.classList.remove("active"))
  document.querySelector(`[data-tab="${tab}"]`)?.classList.add("active")

  const tituloSecao = document.getElementById("titulo-secao")
  const btnAdicionar = document.getElementById("btn-adicionar")
  const btnTexto = document.getElementById("btn-adicionar-texto")

  const config = {
    usuarios: { class: "stat-icon", icon: "bi-people", titulo: "Gerenciar Usuarios", btnTexto: "Novo Usuario", btnVisible: true },
    animais: { class: "stat-icon", icon: "bi-heart", titulo: "Gerenciar Animais", btnTexto: "Novo Animal", btnVisible: true },
    adocoes: { class: "stat-icon", icon: "bi-send", titulo: "Gerenciar Adocoes", btnTexto: "", btnVisible: false },
    transportes: { class: "stat-icon", icon: "bi-truck", titulo: "Gerenciar Transportes", btnTexto: "", btnVisible: false },
    denuncias: { class: "stat-icon", icon: "bi-flag", titulo: "Gerenciar Denuncias", btnTexto: "", btnVisible: false },
  }

  if (tituloSecao) tituloSecao.innerHTML = `<i class="bi ${config[tab].icon}  ${config[tab].class}"></i> ${config[tab].titulo}`
  if (btnTexto) btnTexto.textContent = config[tab].btnTexto
  if (btnAdicionar) btnAdicionar.style.display = config[tab].btnVisible ? "flex" : "none"

  carregarDadosTabela()
}

// Carregar dados da tabela
async function carregarDadosTabela() {
  try {
    const urls = {
      usuarios: `${API_URL}/usuarios/listar`,
      animais: `${API_URL}/animais/listar`,
      adocoes: `${API_URL}/adocoes/listar`,
      transportes: `${API_URL}/transportes/listar`,
      denuncias: `${API_URL}/denuncias/listar`,
    }

    const res = await fetch(urls[tabAtual])
    if (!res.ok) throw new Error()

    let dados = await res.json()
    dadosCache[tabAtual] = dados

    const head = document.getElementById("table-head")
    const body = document.getElementById("table-body")

    if (tabAtual === "usuarios") {
      head.innerHTML = `<th>Nome</th><th>Email</th><th>Papel</th><th>Acoes</th>`
      body.innerHTML = dados
        .map(
          (u) => `
        <tr>
          <td class="name">${u.nomeCompleto}</td>
          <td>${u.email}</td>
          <td><span class="badge ${u.papel === "ADMINISTRADOR" ? "badge-admin" : "badge-user"}">${u.papel}</span></td>
          <td>
            <div class="actions">
              <button onclick="editarUsuario('${u.id}')" class="btn-icon edit" title="Editar"><i class="bi bi-pencil"></i></button>
              <button onclick="deletarItem('usuarios', '${u.id}')" class="btn-icon delete" title="Excluir"><i class="bi bi-trash"></i></button>
            </div>
          </td>
        </tr>
      `,
        )
        .join("")
    } else if (tabAtual === "animais") {
      head.innerHTML = `<th>Nome</th><th>Especie</th><th>Raca</th><th>Idade</th><th>Doador</th><th>Acoes</th>`
      body.innerHTML = dados
        .map(
          (a) => `
        <tr>
          <td class="name">${a.nome}</td>
          <td>${a.especie || "-"}</td>
          <td>${a.raca || "SRD"}</td>
          <td>${a.idade} ${a.idade === 1 ? "ano" : "anos"}</td>
          <td>${a.doador?.nomeCompleto || "-"}</td>
          <td>
            <div class="actions">
              <button onclick="editarAnimal('${a.id}')" class="btn-icon edit" title="Editar"><i class="bi bi-pencil"></i></button>
              <button onclick="deletarItem('animais', '${a.id}')" class="btn-icon delete" title="Excluir"><i class="bi bi-trash"></i></button>
            </div>
          </td>
        </tr>
      `,
        )
        .join("")
    } else if (tabAtual === "adocoes") {
      head.innerHTML = `<th>Animal</th><th>Solicitante</th><th>Data</th><th>Status</th><th>Acoes</th>`
      body.innerHTML = dados
        .map(
          (s) => `
        <tr>
          <td class="name">${s.animal?.nome || "-"}</td>
          <td>${s.solicitante?.nomeCompleto || "-"}</td>
          <td>${s.dataSolicitacao ? new Date(s.dataSolicitacao).toLocaleDateString("pt-BR") : "-"}</td>
          <td><span class="badge badge-pending">${s.status || "PENDENTE"}</span></td>
          <td>
            <div class="actions">
              <button onclick="solicitarTransporte('${s.id}')" class="btn-icon view" title="Solicitar Transporte"><i class="bi bi-truck"></i></button>
              <button onclick="deletarItem('adocoes', '${s.id}')" class="btn-icon delete" title="Excluir"><i class="bi bi-trash"></i></button>
            </div>
          </td>
        </tr>
      `,
        )
        .join("")
    } else if (tabAtual === "transportes") {
      const adocoesComTransporte = dados.filter(s => s.transporte)
      dadosCache.transportes = adocoesComTransporte

      head.innerHTML = `<th>Animal</th><th>Adotante</th><th>Status</th><th>Criado em</th><th>Acoes</th>`

      if (adocoesComTransporte.length === 0) {
        body.innerHTML = `<tr><td colspan="5" class="empty-state"><i class="bi bi-truck"></i><p>Nenhum transporte encontrado</p></td></tr>`
      } else {
        body.innerHTML = adocoesComTransporte
          .map(s => {
            const ultimoStatus = s.transporte?.rastreamentos?.[0]?.status || "PENDENTE"
            const statusClass =
              ultimoStatus === "ENTREGUE"
                ? "badge-delivered"
                : ultimoStatus === "A_CAMINHO"
                  ? "badge-transport"
                  : "badge-pending"
            const statusText = ultimoStatus.replace("_", " ")
            return `
              <tr>
                <td class="name">${s.animal?.nome || "-"}</td>
                <td>${s.solicitante?.nomeCompleto || "-"}</td>
                <td><span class="badge ${statusClass}">${statusText}</span></td>
                <td>${s.transporte?.criadoEm ? new Date(s.transporte.criadoEm).toLocaleDateString("pt-BR") : "-"}</td>
                <td>
                  <div class="actions">
                    <button onclick="verRastreamento('${s.transporte?.id}')" class="btn-icon view" title="Ver Rastreamento"><i class="bi bi-geo-alt"></i></button>
                  </div>
                </td>
              </tr>
            `
          })
          .join("")
      }
    } else if (tabAtual === "denuncias") {
      head.innerHTML = `<th>Descricao</th><th>Status</th><th>Acao Admin</th><th>Acoes</th>`
      body.innerHTML = dados
        .map(
          (d) => `
        <tr>
          <td>${d.descricao || "-"}</td>
          <td><span class="badge ${d.status === "RESOLVIDA" ? "badge-resolved" : "badge-pending"}">${d.status || "PENDENTE"}</span></td>
          <td>
            <select onchange="aplicarAcaoDenuncia('${d.id}', '${d.usuarioAlvoId}', this.value)" class="form-select" style="max-width: 160px; font-size: 0.8125rem;">
              <option value="">Selecione...</option>
              <option value="ADVERTENCIA">Advertencia</option>
              <option value="SUSPENSAO">Suspensao</option>
              <option value="BLOQUEIO">Bloqueio</option>
              <option value="REMOCAO_CONTEUDO">Remover Conteudo</option>
            </select>
          </td>
          <td>
            <div class="actions">
              <button onclick="deletarItem('denuncias', '${d.id}')" class="btn-icon delete" title="Excluir"><i class="bi bi-trash"></i></button>
            </div>
          </td>
        </tr>
      `,
        )
        .join("")
    }

    if (dados.length === 0 && tabAtual !== "transportes") {
      body.innerHTML = `<tr><td colspan="6" class="empty-state"><i class="bi bi-inbox"></i><p>Nenhum registro encontrado</p></td></tr>`
    }
  } catch (error) {
    showToast("Erro ao carregar dados da tabela", "Falha na operação", "error")
  }
}

// Abrir modal para adicionar
function abrirModalAdicionar() {
  if (tabAtual === "usuarios") {
    const modalTitulo = document.getElementById("modal-usuario-titulo")
    const form = document.getElementById("form-usuario")
    const usuarioId = document.getElementById("usuario-id")
    if (modalTitulo) modalTitulo.textContent = "Novo Usuario"
    if (form) form.reset()
    if (usuarioId) usuarioId.value = ""
    abrirModal("modal-usuario")
  } else if (tabAtual === "animais") {
    const modalTitulo = document.getElementById("modal-animal-titulo")
    const form = document.getElementById("form-animal")
    const animalId = document.getElementById("animal-id")
    if (modalTitulo) modalTitulo.textContent = "Novo Animal"
    if (form) form.reset()
    if (animalId) animalId.value = ""
    abrirModal("modal-animal")
  }
}

// Editar usuario
function editarUsuario(id) {
  const usuario = dadosCache.usuarios?.find(u => u.id === id)
  if (!usuario) return

  const modalTitulo = document.getElementById("modal-usuario-titulo")
  const usuarioId = document.getElementById("usuario-id")
  const usuarioNome = document.getElementById("usuario-nome")
  const usuarioEmail = document.getElementById("usuario-email")
  const usuarioSenha = document.getElementById("usuario-senha")
  const usuarioPapel = document.getElementById("usuario-papel")

  if (modalTitulo) modalTitulo.textContent = "Editar Usuario"
  if (usuarioId) usuarioId.value = usuario.id
  if (usuarioNome) usuarioNome.value = usuario.nomeCompleto
  if (usuarioEmail) usuarioEmail.value = usuario.email
  if (usuarioSenha) usuarioSenha.value = ""
  if (usuarioPapel) usuarioPapel.value = usuario.papel

  abrirModal("modal-usuario")
}

// Editar animal
function editarAnimal(id) {
  const animal = dadosCache.animais?.find(a => a.id === id)
  if (!animal) return

  const modalTitulo = document.getElementById("modal-animal-titulo")
  const animalId = document.getElementById("animal-id")
  const animalNome = document.getElementById("animal-nome")
  const animalEspecie = document.getElementById("animal-especie")
  const animalRaca = document.getElementById("animal-raca")
  const animalIdade = document.getElementById("animal-idade")
  const animalFoto = document.getElementById("animal-foto")
  const animalDescricao = document.getElementById("animal-descricao")
  const animalVacinado = document.getElementById("animal-vacinado")
  const animalCastrado = document.getElementById("animal-castrado")

  if (modalTitulo) modalTitulo.textContent = "Editar Animal"
  if (animalId) animalId.value = animal.id
  if (animalNome) animalNome.value = animal.nome
  if (animalEspecie) animalEspecie.value = animal.especie || ""
  if (animalRaca) animalRaca.value = animal.raca || ""
  if (animalIdade) animalIdade.value = animal.idade || 1
  if (animalFoto) animalFoto.value = animal.fotoUrl || ""
  if (animalDescricao) animalDescricao.value = animal.descricao || ""
  if (animalVacinado) animalVacinado.checked = animal.vacinado || false
  if (animalCastrado) animalCastrado.checked = animal.castrado || false

  abrirModal("modal-animal")
}

// Salvar usuario
async function salvarUsuario(event) {
  event.preventDefault()

  const id = document.getElementById("usuario-id")?.value
  const dados = {
    nomeCompleto: document.getElementById("usuario-nome")?.value,
    email: document.getElementById("usuario-email")?.value,
    papel: document.getElementById("usuario-papel")?.value,
  }

  const senha = document.getElementById("usuario-senha")?.value
  if (senha) dados.senha = senha

  try {
    const url = id ? `${API_URL}/usuarios/atualizar/${id}` : `${API_URL}/usuarios/cadastrar`
    const method = id ? "PUT" : "POST"

    const res = await fetch(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dados),
    })

    if (res.ok) {
      showToast(id ? "Usuario atualizado!" : "Usuario criado!", "", "success")
      fecharModal("modal-usuario")
      carregarDadosTabela()
      carregarDadosAdmin()
    } else {
      throw new Error()
    }
  } catch (error) {
    showToast("Erro ao salvar usuario", "Falha na operação", "error")
  }
}

// Salvar animal
async function salvarAnimal(event) {
  event.preventDefault()

  const usuario = getUsuarioLogado()
  if (!usuario) return logout()

  const id = document.getElementById("animal-id")?.value
    || document.getElementById("animal-id-edit")?.value
  const fotoUrl = document.getElementById("animal-foto")?.value?.trim()
  const dados = {
    nome: document.getElementById("animal-nome")?.value,
    especie: document.getElementById("animal-especie")?.value,
    raca: document.getElementById("animal-raca")?.value,
    idade: parseInt(document.getElementById("animal-idade")?.value),
    fotosUrls: fotoUrl ? [fotoUrl] : [],
    descricao: document.getElementById("animal-descricao")?.value,
    vacinado: document.getElementById("animal-vacinado")?.checked,
    castrado: document.getElementById("animal-castrado")?.checked,
    doadorId: usuario.id,
  }

  try {
    const url = id ? `${API_URL}/animais/${id}` : `${API_URL}/animais/criar`
    const method = id ? "PUT" : "POST"

    const res = await fetch(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dados),
    })

    if (res.ok) {
      showToast(id ? "Animal atualizado!" : "Animal cadastrado!", "", "success")
      fecharModal("modal-animal")
      carregarDadosTabela()
      carregarDadosAdmin()
    } else {
      throw new Error()
    }
  } catch (error) {
    showToast("Erro ao salvar animal", "Falha na operação", "error")
  }
}

// Solicitar transporte
async function solicitarTransporte(adocaoId) {
  if (!confirm("Deseja solicitar o transporte para esta adocao?")) return

  try {
    const res = await fetch(`${API_URL}/transportes/adocao/${adocaoId}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
    })

    if (res.ok) {
      showToast("Transporte solicitado!", "", "success")
      carregarDadosTabela()
      carregarDadosAdmin()
    } else {
      const error = await res.json()
      showToast(error.message || "Erro ao solicitar transporte", "Falha na operação", "error")
    }
  } catch (error) {
    showToast("Erro ao solicitar transporte", "Falha na operação", "error")
  }
}

// Ver rastreamento
async function verRastreamento(transporteId) {
  if (!transporteId) return

  try {
    const res = await fetch(`${API_URL}/transportes/${transporteId}`)
    if (!res.ok) throw new Error()

    const transporte = await res.json()

    const transporteIdDisplay = document.getElementById("transporte-id-display")
    const transporteAdocaoDisplay = document.getElementById("transporte-adocao-display")
    const transporteCriadoDisplay = document.getElementById("transporte-criado-display")
    const timeline = document.getElementById("transporte-timeline")

    if (transporteIdDisplay) transporteIdDisplay.textContent = transporte.id
    if (transporteAdocaoDisplay) transporteAdocaoDisplay.textContent = transporte.adocao?.id || "-"
    if (transporteCriadoDisplay)
      transporteCriadoDisplay.textContent = transporte.criadoEm
        ? new Date(transporte.criadoEm).toLocaleString("pt-BR")
        : "-"

    if (!transporte.rastreamentos || transporte.rastreamentos.length === 0) {
      if (timeline)
        timeline.innerHTML = '<p style="color: var(--gray-500); font-size: 0.875rem;">Nenhum rastreamento disponivel</p>'
    } else {
      if (timeline)
        timeline.innerHTML = transporte.rastreamentos
          .map(r => {
            const iconClass =
              r.status === "ENTREGUE" ? "delivered" : r.status === "A_CAMINHO" ? "transit" : "pending"
            const icon =
              r.status === "ENTREGUE"
                ? "bi-check-circle-fill"
                : r.status === "A_CAMINHO"
                  ? "bi-truck"
                  : "bi-clock-fill"
            return `
            <div class="timeline-item">
              <div class="timeline-icon ${iconClass}"><i class="bi ${icon}"></i></div>
              <div class="timeline-content">
                <div class="timeline-title">${r.status.replace("_", " ")}</div>
                <div class="timeline-desc">${r.mensagem}</div>
                <div class="timeline-time">${r.criadoEm ? new Date(r.criadoEm).toLocaleString("pt-BR") : "-"}</div>
              </div>
            </div>
          `
          })
          .join("")
    }

    abrirModal("modal-transporte")
  } catch (error) {
    showToast("Erro ao carregar rastreamento", "Falha na operação", "error")
  }
}

// Deletar item
async function deletarItem(tipo, id) {
  if (!confirm("Tem certeza que deseja excluir este registro?")) return

  try {
    const res = await fetch(`${API_URL}/${tipo}/deletar/${id}`, { method: "DELETE" })

    if (res.ok) {
      showToast("Registro excluido!", "", "success")
      carregarDadosTabela()
      carregarDadosAdmin()
    } else {
      throw new Error()
    }
  } catch (error) {
    showToast("Erro ao excluir registro", "Falha na operação", "error")
  }
}

// Aplicar acao em denuncia
async function aplicarAcaoDenuncia(denunciaId, usuarioId, acao) {
  if (!acao) return

  try {
    const res = await fetch(`${API_URL}/denuncias/aplicar-acao`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ denunciaId, usuarioId, acao }),
    })

    if (res.ok) {
      showToast("Acao aplicada!", "", "success")
      carregarDadosTabela()
    } else {
      throw new Error()
    }
  } catch (error) {
    showToast("Erro ao aplicar acao", "Falha na operação", "error")
  }
}

// Controle de modais
function abrirModal(id) {
  const modal = document.getElementById(id)
  if (modal) {
    modal.classList.add("active")
    document.body.style.overflow = "hidden"
  }
}

function fecharModal(id) {
  const modal = document.getElementById(id)
  if (modal) {
    modal.classList.remove("active")
    document.body.style.overflow = ""
  }
}

// Fechar modal ao clicar fora
document.querySelectorAll(".modal-overlay").forEach(overlay => {
  overlay.addEventListener("click", e => {
    if (e.target === overlay) {
      overlay.classList.remove("active")
      document.body.style.overflow = ""
    }
  })
})

// -----------------------------------------
// PAGE INITIALIZATION
// -----------------------------------------
document.addEventListener("DOMContentLoaded", () => {
  renderNavAuth()

  const currentPage = window.location.pathname.split("/").pop() || "index.html"

  switch (currentPage) {
    case "index.html":
    case "":
      carregarAnimais()
      break
    case "animal.html":
      carregarAnimal()
      break
    case "minhas-solicitacoes.html":
      if (protegerPagina()) carregarMinhasSolicitacoes()
      break
    case "solicitacoes-recebidas.html":
      if (protegerPagina()) carregarSolicitacoesRecebidas()
      break
    case "perfil.html":
      if (protegerPagina()) carregarPerfil()
      break
    case "transporte.html":
      carregarTransporte()
      break
    case "transporte copy.html":
      carregarTransportes()
      break
    case "admin.html":
      carregarDadosAdmin()
      break
    case "cadastro-animal.html":
    case "agendar-transporte.html":
      protegerPagina()
      break
  }
})
