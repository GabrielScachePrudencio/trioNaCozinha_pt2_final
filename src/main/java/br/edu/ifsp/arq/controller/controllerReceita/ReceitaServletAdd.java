package br.edu.ifsp.arq.controller.controllerReceita;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson;

import br.edu.ifsp.arq.dao.ReceitaDAO;
import br.edu.ifsp.arq.dao.UsuarioDAO;
import br.edu.ifsp.arq.model.Receita;
import br.edu.ifsp.arq.model.Usuario;

@WebServlet("/ReceitaServletAdd")
@MultipartConfig
public class ReceitaServletAdd extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReceitaDAO receita_dao;
    private UsuarioDAO usuario_dao;

    public ReceitaServletAdd() {
        super();
        receita_dao = ReceitaDAO.getInstance_R();
        usuario_dao = UsuarioDAO.getInstance_U();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession sessao = request.getSession();
        Usuario usuarioLogado = (Usuario) sessao.getAttribute("usuarioLogado");

        // Teste temporário: se não tiver logado, cria um "usuário fake"
        if (usuarioLogado == null) {
            usuarioLogado = new Usuario();
            usuarioLogado.setNome("Convidado");
            usuarioLogado.setSenha("123");
            usuarioLogado.setMinhasReceitas(new ArrayList<>());
        }

        String nome = request.getParameter("nome");
        if (nome == null || nome.trim().isEmpty()) {
            sendErro(response, "Nome da receita é obrigatório.");
            return;
        }

        for (Receita r : receita_dao.mostrarTodos()) {
            if (r.getNome() != null && r.getNome().equalsIgnoreCase(nome)) {
                sendErro(response, "Já existe uma receita com esse nome. Escolha outro nome.");
                return;
            }
        }

        String modoPreparo = request.getParameter("modoPreparo");
        if (modoPreparo == null || modoPreparo.trim().isEmpty()) {
            sendErro(response, "Modo de preparo é obrigatório.");
            return;
        }

        int tempoDePreparoMinutos;
        int qtddPorcoes;

        try {
            tempoDePreparoMinutos = Integer.parseInt(request.getParameter("tempoDePreparoMinutos"));
            qtddPorcoes = Integer.parseInt(request.getParameter("qtddPorcoes"));

            if (tempoDePreparoMinutos <= 0 || tempoDePreparoMinutos > 10000 ||
                qtddPorcoes <= 0 || qtddPorcoes > 10000) {
                throw new NumberFormatException("Valores fora do intervalo permitido.");
            }
        } catch (NumberFormatException e) {
            sendErro(response, "Tempo de preparo ou quantidade de porções inválidos.");
            return;
        }

        ArrayList<String> ingredientes = new ArrayList<>();
        String[] ingredientesFixos = request.getParameterValues("ingredientes");
        String[] ingredientesPersonalizados = request.getParameterValues("ingredientesPersonalizados");

        if (ingredientesFixos != null) ingredientes.addAll(Arrays.asList(ingredientesFixos));
        if (ingredientesPersonalizados != null) {
            for (String ing : ingredientesPersonalizados) {
                if (ing != null && !ing.trim().isEmpty()) {
                    ingredientes.add(ing.trim());
                }
            }
        }

        ArrayList<String> categorias = new ArrayList<>();
        String[] categoriasArray = request.getParameterValues("categorias");
        if (categoriasArray != null) categorias.addAll(Arrays.asList(categoriasArray));

        Part filePart = request.getPart("img");
        String fileName = "";
        if (filePart != null && filePart.getSize() > 0) {
            fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uploadPath = getServletContext().getRealPath("") + File.separator + "imagens";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();
            filePart.write(uploadPath + File.separator + fileName);
        }

        // Criar nova receita
        Receita novaReceita = new Receita(
            0,
            nome,
            usuarioLogado.getNome(),
            tempoDePreparoMinutos,
            ingredientes,
            modoPreparo,
            categorias,
            qtddPorcoes,
            fileName
        );

        receita_dao.add(novaReceita);
        ArrayList<Receita> todas = receita_dao.mostrarTodos();
        Receita receitaComId = todas.get(todas.size() - 1);

        ArrayList<Receita> minhas = usuarioLogado.getMinhasReceitas();
        if (minhas == null) {
            minhas = new ArrayList<>();
        }
        minhas.add(receitaComId);

        usuario_dao.atualizarMinhasReceitas(usuarioLogado, minhas);

        sessao.setAttribute("usuarioLogado", usuarioLogado);

        List<Receita> lista = receita_dao.mostrarTodos();
        String json = new Gson().toJson(lista);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    private void sendErro(HttpServletResponse response, String mensagem) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"erro\": \"" + mensagem + "\"}");
    }
}
