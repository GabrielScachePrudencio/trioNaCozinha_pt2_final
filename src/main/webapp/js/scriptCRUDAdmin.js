const contextPath = "/TRIO_NACOZINHA";

async function verificarAdmin() {
  try {
    const resp = await fetch(`${contextPath}/UsuarioServletLogar`, { credentials: "include" });
    const usuario = await resp.json();
    if (usuario.tipo !== "ADMINISTRADOR") {
      alert("Acesso restrito ao administrador.");
      window.location.href = `${contextPath}/index.html`;
    }
  } catch (err) {
    alert("Você precisa estar logado como administrador.");
    window.location.href = `${contextPath}/index.html`;
  }
}

async function carregarReceitas() {
  const resp = await fetch(`${contextPath}/ReceitaServletMostrarTodas`);
  const receitas = await resp.json();

  const tabela = document.createElement('table');
  tabela.className = 'table table-bordered table-hover';

  const thead = `
    <thead>
      <tr>
        <th>ID</th>
        <th>Nome</th>
        <th>Autor</th>
        <th>Tempo</th>
        <th>Ações</th>
      </tr>
    </thead>`;
  tabela.innerHTML = thead;

  const tbody = document.createElement('tbody');

  receitas.forEach(rec => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${rec.id}</td>
      <td>${rec.nome}</td>
      <td>${rec.autor}</td>
      <td>${rec.tempoDePreparoMinutos} min</td>
      <td>
        <a href="EditarReceita.html?id=${rec.id}" class="btn btn-warning btn-sm me-2">Editar</a>
        <button class="btn btn-danger btn-sm" onclick="excluirReceita(${rec.id})">Excluir</button>
      </td>
    `;
    tbody.appendChild(tr);
  });

  tabela.appendChild(tbody);
  document.getElementById("tabelaContainer").appendChild(tabela);
}

async function excluirReceita(id) {
  const confirmacao = confirm("Tem certeza que deseja excluir esta receita?");
  if (!confirmacao) return;

  const resp = await fetch(`${contextPath}/ReceitaServletDeletar?id=${id}`, {
    method: 'DELETE',
    credentials: "include"
  });

  if (resp.ok) {
    alert("Receita excluída!");
    location.reload();
  } else {
    alert("Erro ao excluir.");
  }
}

window.onload = async () => {
  await verificarAdmin();
  await carregarReceitas();
};
