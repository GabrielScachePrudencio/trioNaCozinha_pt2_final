package br.edu.ifsp.arq.controller.controllerReceita;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.edu.ifsp.arq.dao.*;
import br.edu.ifsp.arq.model.*;

/**
 * Servlet implementation class ComentarReceitaServlet
 */
@WebServlet("/ComentarReceitaServlet")
public class ComentarReceitaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogado") : null;

        if (usuario == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Usuário não logado.");
            return;
        }

        try {
            int idReceita = Integer.parseInt(request.getParameter("idReceita"));
            String texto = request.getParameter("texto");

            if (texto == null || texto.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Comentário vazio.");
                return;
            }

            // Aqui você buscaria a receita no banco
            ReceitaDAO receitaDAO = ReceitaDAO.getInstance_R();
            Receita receita = receitaDAO.buscarPorID(idReceita);

            if (receita == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Receita não encontrada.");
                return;
            }

            // Criar e salvar o comentário
            Comentario comentario = new Comentario(texto, usuario);
            

            receitaDAO.addCom(idReceita, comentario); 

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Comentário adicionado com sucesso!");

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("ID inválido.");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Erro ao adicionar comentário: " + e.getMessage());
        }
    }


}
