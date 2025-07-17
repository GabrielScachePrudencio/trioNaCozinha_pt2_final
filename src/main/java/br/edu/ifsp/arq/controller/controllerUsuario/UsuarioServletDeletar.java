package br.edu.ifsp.arq.controller.controllerUsuario;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.edu.ifsp.arq.dao.ReceitaDAO;
import br.edu.ifsp.arq.dao.UsuarioDAO;
import br.edu.ifsp.arq.model.*;

@WebServlet("/UsuarioServletDeletar")
public class UsuarioServletDeletar extends HttpServlet {
    private static final long serialVersionUID = 1L;
    UsuarioDAO usuarioDao;
    ReceitaDAO receitaDAO;

    public UsuarioServletDeletar() {
        super();
        usuarioDao = UsuarioDAO.getInstance_U();
        receitaDAO = ReceitaDAO.getInstance_R();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ID recebido: " + request.getParameter("id"));
        HttpSession sessao = request.getSession(false);

        if (sessao == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"erro\":\"Sessão inexistente\"}");
            return;
        }

        Usuario usuarioLogado = (Usuario) sessao.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"erro\":\"Usuário não logado\"}");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Parâmetro 'id' é obrigatório");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Parâmetro 'id' inválido");
            return;
        }

        Usuario u = usuarioDao.buscarPorID(id);

        if (u == null) {
            System.out.println("Usuário não encontrado.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Usuário não encontrado.");
            return;
        }

        // Deleta usuário
        usuarioDao.deletar(id);

        // Filtra receitas para remover as que são do autor deletado
        ArrayList<Receita> todasReceitas = receitaDAO.mostrarTodos();
        ArrayList<Receita> receitasFiltradas = new ArrayList<>();

        for (Receita r : todasReceitas) {
            if (!r.getAutor().equals(u.getNome())) {
                receitasFiltradas.add(r);
            }
        }

        receitaDAO.setDadosArq(receitasFiltradas);

        // Se quiser redirecionar:
        // response.sendRedirect(request.getContextPath() + "/index.html");
        // return;

        // Se estiver usando AJAX e quiser retornar status OK:
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Usuário e receitas deletados com sucesso.");
    }
}
