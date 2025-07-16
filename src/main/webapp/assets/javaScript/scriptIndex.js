const contextPath = "http://localhost:8080/TRIO_NACOZINHA";

let usuarioLogado = null;



function carregarTodasReceitasDoUsuario() {
  let containerPai = document.getElementById("containerTodasReceitasDoUsuarioLogado");
  if (!containerPai) return;

  containerPai.innerHTML = "";

  if (usuarioLogado) {
    if (usuarioLogado.minhasReceitas.length === 0) {
      let p = document.createElement("p");
      p.innerHTML = "Nenhuma Receita Add";
      containerPai.appendChild(p);
      return;
    }

    usuarioLogado.minhasReceitas.forEach(d => {
      let card = criarCardReceita(d);
      containerPai.appendChild(card);
    });
  }
}

function criarCardReceita(d) {
  let card = document.createElement("div");
  card.classList.add("card", "card-bege");

  card.onclick = () => irParaReceitaMaisDetalhada5(d.id);
  card.style.cursor = "pointer";

  let media = 0;
  if (d.avaliacoes && d.avaliacoes.length > 0) {
    let soma = d.avaliacoes.reduce((acc, a) => acc + a.nota, 0);
    media = Math.round(soma / d.avaliacoes.length);
  }
  
  
  const imgSrc = `http://localhost:8080/TRIO_NACOZINHA/imagens/${d.img || 'sem-imagem.png'}`;
  //novo
  card.innerHTML = `
  <img src="${imgSrc}" class="card-img-top" alt="${d.nome}">
    <div class="card-body">
      <p><strong>ID:</strong> ${d.id}</p>
      <h5 class="card-title">${d.nome}</h5>
     
      <p><strong>Autor:</strong> ${d.autor}</p>
      <p><strong>Tempo:</strong> ${d.tempoDePreparoMinutos} min</p>
      <p><strong>Porções:</strong> ${d.qtddPorcoes || ''}</p>
      <p><strong>Modo de Preparo:</strong> ${d.modoPreparo || ""}</p>
      <p><strong>Categorias:</strong> ${d.categorias ? d.categorias.join(", ") : ""}</p>
      <p><strong>Ingredientes:</strong></p>
      <ul>${d.ingredientes ? d.ingredientes.map(ing => `<li>${ing}</li>`).join("") : ""}</ul>
    </div>
  `;

/*
	//antigo
  card.innerHTML = `
    <img src="assets/imagens/${d.img || 'imagem-default.jpg'}" class="card-img-top" alt="${d.nome}">
    <div class="card-body">
      <p><strong>ID:</strong> ${d.id}</p>
      <h5 class="card-title">${d.nome}</h5>
     
      <p><strong>Autor:</strong> ${d.autor}</p>
      <p><strong>Tempo:</strong> ${d.tempoDePreparoMinutos} min</p>
      <p><strong>Porções:</strong> ${d.qtddPorcoes || ''}</p>
      <p><strong>Modo de Preparo:</strong> ${d.modoPreparo || ""}</p>
      <p><strong>Categorias:</strong> ${d.categorias ? d.categorias.join(", ") : ""}</p>
      <p><strong>Ingredientes:</strong></p>
      <ul>${d.ingredientes ? d.ingredientes.map(ing => `<li>${ing}</li>`).join("") : ""}</ul>
    </div>
  `;

*/
  /*

    */
  return card;
}

async function verificarLogin() {
  try {
    const resp = await fetch(`${contextPath}/UsuarioServletLogar`, {
      credentials: "include"
    });
    if (!resp.ok) throw new Error("Usuário não logado");
    const usuario = await resp.json();
    usuarioLogado = usuario;
  } catch {
    usuarioLogado = null;
  }
}

function carregarMenu() {
  const nav = document.createElement('nav');
  nav.className = 'navbar';

  const brandLink = document.createElement('a');
  brandLink.className = 'navbar-brand';
  brandLink.href = `${contextPath}/index.html`;

  const logoImg = document.createElement('img');
  logoImg.src = 'assets/imagens/logo/logo_pageindex.png';
  logoImg.alt = 'Trio Na Cozinha';
  logoImg.height = 40;
  brandLink.appendChild(logoImg);
  nav.appendChild(brandLink);

  const containerDiv = document.createElement('div');
  containerDiv.className = 'd-flex align-items-center gap-3';

  const sobreNosLink = document.createElement('a');
  sobreNosLink.href = 'assets/views/extras/SobreNos.html';
  sobreNosLink.className = 'btn btn-outline-bege';
  sobreNosLink.textContent = 'Sobre nós';
  containerDiv.appendChild(sobreNosLink);

  const form = document.createElement('form');
  form.className = 'd-flex';
  form.setAttribute('role', 'search');
  form.method = 'POST';

  const inputBusca = document.createElement('input');
  inputBusca.className = 'form-control me-2';
  inputBusca.type = 'search';
  inputBusca.name = 'busca';
  inputBusca.placeholder = 'Pesquisar';
  form.appendChild(inputBusca);

  const btnBusca = document.createElement('button');
  btnBusca.className = 'btn btn-bege';
  btnBusca.type = 'submit';
  btnBusca.textContent = 'Pesquisar';
  form.appendChild(btnBusca);

  // Painel Admin ao lado do campo de busca
  if (usuarioLogado?.tipo === "ADMINISTRADOR") {
    const adminLink = document.createElement("a");
    adminLink.href = 'assets/views/receita/ListarReceitas.html';
    adminLink.className = 'btn btn-warning ms-2';
    adminLink.textContent = 'Painel Admin';
    form.appendChild(adminLink);
  }

  form.onsubmit = (event) => {
    event.preventDefault();
    const nomeBuscado = inputBusca.value.trim();
    if (nomeBuscado) {
      window.location.href = `assets/views/receita/ReceitaEspecifica.html?nome=${encodeURIComponent(nomeBuscado)}`;
    }
  };

  containerDiv.appendChild(form);

  if (usuarioLogado) {
    if (usuarioLogado.tipo === "NORMAL") {
      const addReceitaLink = document.createElement('a');
      addReceitaLink.href = 'assets/views/receita/AddReceita.html';
      addReceitaLink.className = 'btn btn-bege';
      addReceitaLink.textContent = 'Adicionar Receita';
      containerDiv.appendChild(addReceitaLink);
    }

    const contaLink = document.createElement('a');
    contaLink.href = 'assets/views/usuario/Conta.html';
    contaLink.className = 'btn btn-outline-bege d-flex align-items-center';
    contaLink.style.gap = '10px';

    const imgPerfil = document.createElement('img');
    imgPerfil.src = `${contextPath}/imagens/${usuarioLogado.img}`;
    imgPerfil.alt = 'Foto do Usuário';
    imgPerfil.className = 'imgPerfil';
    imgPerfil.style.width = '32px';
    imgPerfil.style.height = '32px';
    imgPerfil.style.borderRadius = '50%';

    const nomeSpan = document.createElement('span');
    nomeSpan.className = 'text-bege';
    nomeSpan.textContent = usuarioLogado.nome || 'Usuário';

    contaLink.appendChild(imgPerfil);
    contaLink.appendChild(nomeSpan);
    containerDiv.appendChild(contaLink);
  } else {
    const cadastrarLink = document.createElement('a');
    cadastrarLink.href = 'assets/views/usuario/AddUsuario.html';
    cadastrarLink.className = 'btn btn-outline-bege';
    cadastrarLink.textContent = 'Cadastrar-se';
    containerDiv.appendChild(cadastrarLink);

    const logarLink = document.createElement('a');
    logarLink.href = 'assets/views/usuario/LogarUsuario.html';
    logarLink.className = 'btn btn-outline-bege';
    logarLink.textContent = 'Logar';
    containerDiv.appendChild(logarLink);
  }

  nav.appendChild(containerDiv);

  const menuAntigo = document.querySelector('nav.navbar');
  if (menuAntigo) {
    menuAntigo.replaceWith(nav);
  } else {
    const menuContainer = document.querySelector('#menu') || document.body;
    menuContainer.prepend(nav);
  }
}

async function carregarTodas() {
  try {
    const response = await fetch(`${contextPath}/ReceitaServletMostrarTodas`);
    const data = await response.json();

    let containerPai = document.querySelector("#containerTodasReceitas");
    if (!containerPai) return;

    containerPai.innerHTML = "";

    for (let d of data) {
      try {
        let card = criarCardReceita(d);
        containerPai.appendChild(card);
      } catch (error) {
        console.error("Erro ao criar card da receita", d, error);
      }
    }
  } catch (err) {
    console.error("Erro ao carregar receitas:", err);
  }
}

function irParaReceitaMaisDetalhada5(id) {
  window.location.href = `${contextPath}/assets/views/receita/ReceitaEspecifica.html?id=${id}`;
}

async function inicializar() {
  await verificarLogin();

  // ✅ Protege a página AddReceita.html
  if (window.location.pathname.includes("AddReceita.html")) {
    if (!usuarioLogado || usuarioLogado.tipo !== "NORMAL") {
      alert("Somente usuários do tipo NORMAL podem adicionar receitas.");
      window.location.href = `${contextPath}/index.html`;
      return;
    }
  }

  carregarMenu();
  if (usuarioLogado) carregarTodasReceitasDoUsuario();
  carregarTodas();
}

inicializar();
