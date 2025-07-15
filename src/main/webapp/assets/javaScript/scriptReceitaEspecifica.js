const contextPath = "http://localhost:8080/TRIO_NACOZINHA";

/*
async function verificarLogin() {
  try {
	const resp = await fetch(`${contextPath}/UsuarioServletLogar`, {
	  credentials: "include"
	});
    if (!resp.ok) throw new Error("Usuário não logado");
    const usuario = await resp.json();
    console.log("Usuário logado:", usuario.nome);
    usuarioLogado = usuario;
	console.log("dados do usuarioLogado: ",usuarioLogado);
  } catch (err) {
    console.warn("Usuário não autenticado!");
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
  logoImg.src = '../../imagens/logo/logo_pageindex.png';
  logoImg.alt = 'Trio Na Cozinha';
  	
  logoImg.height = 40;
  brandLink.appendChild(logoImg);
  nav.appendChild(brandLink);

  const containerDiv = document.createElement('div');
  containerDiv.className = 'd-flex align-items-center gap-3';

  const sobreNosLink = document.createElement('a');
  sobreNosLink.href = '../../views/extras/SobreNos.html';
  sobreNosLink.className = 'btn btn-outline-bege';
  sobreNosLink.textContent = 'Sobre nós';
  containerDiv.appendChild(sobreNosLink);

  const form = document.createElement('form');
  form.className = 'd-flex';
  form.setAttribute('role', 'search');

  form.method = 'GET';

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

  form.onsubmit = (event) => {
    event.preventDefault();
    const nomeBuscado = inputBusca.value.trim();
    if (nomeBuscado) {
		window.location = `ReceitaEspecifica.html?nome=${encodeURIComponent(nomeBuscado)}`;
    }
  };

  containerDiv.appendChild(form);

  if (usuarioLogado) {
    const addReceitaLink = document.createElement('a');
    addReceitaLink.href = '../../views/receita/AddReceita.html';
    addReceitaLink.className = 'btn btn-bege';
    addReceitaLink.textContent = 'Adicionar Receita';
    containerDiv.appendChild(addReceitaLink);

    const contaLink = document.createElement('a');
    contaLink.href = '../../views/usuario/Conta.html';
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
    cadastrarLink.href = '../../views/usuario/AddUsuario.html';
    cadastrarLink.className = 'btn btn-outline-bege';
    cadastrarLink.textContent = 'Cadastrar-se';
    containerDiv.appendChild(cadastrarLink);

    const logarLink = document.createElement('a');
    logarLink.href = '../../views/usuario/LogarUsuario.html';
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


function getIdFromUrl() {
  const params = new URLSearchParams(window.location.search);
  return params.get('id');
}

function getNomeFromUrl() {
  const params = new URLSearchParams(window.location.search);
  return params.get('nome');
}


async function carregarReceita() {
  const id = getIdFromUrl();
  const nome = getNomeFromUrl();

  try {
    let url = '';

    if (id) {
      url = `${contextPath}/ReceitaServletDetalhada?id=${id}`;
    } else if (nome) {
      url = `${contextPath}/ReceitaServletDetalhada?nome=${encodeURIComponent(nome)}`;
    } else {
      alert('ID ou nome da receita não informado na URL');
      return;
    }

    const response = await fetch(url);
    if (!response.ok) throw new Error('Erro na requisição');

    const receita = await response.json();

    document.getElementById('nomeReceita').textContent = receita.nome;
    document.getElementById('autorReceita').textContent = receita.autor;

    const categoriasDiv = document.getElementById('categoriasReceita');
    categoriasDiv.innerHTML = '';
    receita.categorias.forEach(cat => {
      const span = document.createElement('span');
      span.textContent = cat;
      span.classList.add('border', 'px-2', 'py-1', 'small', 'rounded', 'categoriaIndividual', 'hover-effect', 'me-2');
      categoriasDiv.appendChild(span);
    });

    const img = document.getElementById('imgReceita');
    img.src = `/TRIO_NACOZINHA/assets/imagens/${receita.img}`;
    img.alt = `Imagem da receita ${receita.nome}`;

    document.getElementById('tempoReceita').textContent = receita.tempoDePreparoMinutos;
    document.getElementById('porcoesReceita').textContent = receita.qtddPorcoes;

    const ingredientesUL = document.getElementById('ingredientesReceita');
    ingredientesUL.innerHTML = '';
    receita.ingredientes.forEach((ing, i) => {
      const li = document.createElement('li');
      li.classList.add('form-check', 'd-flex', 'align-items-center', 'ms-3', 'mb-1');

      const checkbox = document.createElement('input');
      checkbox.type = 'checkbox';
      checkbox.classList.add('form-check-input', 'me-2');
      checkbox.id = `ingrediente${i}`;

      const label = document.createElement('label');
      label.classList.add('form-check-label', 'w-100', 'py-1', 'px-2', 'rounded');
      label.htmlFor = `ingrediente${i}`;
      label.textContent = ing;

      li.appendChild(checkbox);
      li.appendChild(label);
      ingredientesUL.appendChild(li);
    });

    document.getElementById('modoPreparoReceita').textContent = receita.modoPreparo;

  } catch (error) {
    alert('Erro ao carregar receita devido ao nome ou id: ' + error.message);
  }
}

async function inicializar() {
  await verificarLogin();
  carregarMenu();
	carregarReceita();
}

// Chamar inicialização
inicializar();


*/
