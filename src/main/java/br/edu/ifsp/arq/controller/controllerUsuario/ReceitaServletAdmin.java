package br.edu.ifsp.arq.controller.controllerUsuario;

import br.edu.ifsp.arq.dao.ReceitaDAO;
import br.edu.ifsp.arq.model.Receita;
import br.edu.ifsp.arq.model.Usuario;
import br.edu.ifsp.arq.model.TipoUsuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@WebServlet("/admin/receita")
@MultipartConfig
public class ReceitaServletAdmin extends HttpServlet {

    private ReceitaDAO receitaDAO = ReceitaDAO.getInstance_R();

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        if (sessao == null) return false;
        Usuario usuarioLogado = (Usuario) sessao.getAttribute("usuarioLogado");
        return usuarioLogado != null && usuarioLogado.getTipo() == TipoUsuario.ADMINISTRADOR;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado.");
            return;
        }

        String acao = request.getParameter("acao");

        if (acao == null || acao.equals("listar")) {
            List<Receita> lista = receitaDAO.mostrarTodos();
            request.setAttribute("listaReceitas", lista);
            request.getRequestDispatcher("/admin/receitaListar.jsp").forward(request, response);

        } else if (acao.equals("editar")) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                int id = Integer.parseInt(idStr);
                Receita receita = receitaDAO.buscarPorID(id);
                request.setAttribute("receita", receita);
                request.getRequestDispatcher("/admin/receitaEditar.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/receita?acao=listar");
            }

        } else if (acao.equals("excluir")) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                int id = Integer.parseInt(idStr);
                receitaDAO.deletar(id);
            }
            response.sendRedirect(request.getContextPath() + "/admin/receita?acao=listar");

        } else {
            response.sendRedirect(request.getContextPath() + "/admin/receita?acao=listar");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado.");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");
        int id = (idStr != null && !idStr.isEmpty()) ? Integer.parseInt(idStr) : 0;

        String nome = request.getParameter("nome");
        String autor = request.getParameter("autor");
        int tempoDePreparo = Integer.parseInt(request.getParameter("tempoDePreparoMinutos"));
        int qtddPorcoes = Integer.parseInt(request.getParameter("qtddPorcoes"));
        String modoPreparo = request.getParameter("modoPreparo");

        List<String> categoriasTemp = (request.getParameter("categorias") == null) ? 
                List.of() : Arrays.asList(request.getParameter("categorias").split(","));
        ArrayList<String> categorias = new ArrayList<>(categoriasTemp);

        List<String> ingredientesTemp = (request.getParameter("ingredientes") == null) ? 
                List.of() : Arrays.asList(request.getParameter("ingredientes").split(","));
        ArrayList<String> ingredientes = new ArrayList<>(ingredientesTemp);

        String nomeImagem = null;
        Part imgPart = request.getPart("img");
        if (imgPart != null && imgPart.getSize() > 0) {
            String fileName = imgPart.getSubmittedFileName();
            String ext = fileName.substring(fileName.lastIndexOf('.'));
            nomeImagem = "receita_" + System.currentTimeMillis() + ext;
            String caminhoSalvar = request.getServletContext().getRealPath("/imagens/") + nomeImagem;
            imgPart.write(caminhoSalvar);
        }

        Receita receita = new Receita();
        receita.setId(id);
        receita.setNome(nome);
        receita.setAutor(autor);
        receita.setTempoDePreparoMinutos(tempoDePreparo);
        receita.setQtddPorcoes(qtddPorcoes);
        receita.setModoPreparo(modoPreparo);
        receita.setCategorias(categorias);
        receita.setIngredientes(ingredientes);

        if (nomeImagem != null) {
            receita.setImg(nomeImagem);
        } else if (id != 0) {
            Receita antiga = receitaDAO.buscarPorID(id);
            if (antiga != null) receita.setImg(antiga.getImg());
        }

        boolean sucesso;
        if (id == 0) {
            sucesso = receitaDAO.add(receita);
        } else {
            sucesso = receitaDAO.editar(id, receita);
        }

        if (sucesso) {
            response.sendRedirect(request.getContextPath() + "/admin/receita?acao=listar");
        } else {
            response.getWriter().println("Erro ao salvar a receita.");
        }
    }
}
