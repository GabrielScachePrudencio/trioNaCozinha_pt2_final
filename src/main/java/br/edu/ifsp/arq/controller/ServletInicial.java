package br.edu.ifsp.arq.controller;

import java.io.IOException;



import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.edu.ifsp.arq.dao.ReceitaDAO;
import br.edu.ifsp.arq.dao.UsuarioDAO;
import br.edu.ifsp.arq.model.*;
import br.edu.ifsp.arq.dao.*;

@WebServlet("/ServletInicial")
@MultipartConfig
public class ServletInicial extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ReceitaDAO receita_dao;
	private UsuarioDAO usuario_dao;

    public ServletInicial() {
        super();
        receita_dao = ReceitaDAO.getInstance_R();
        usuario_dao = UsuarioDAO.getInstance_U();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Pega a sessão, mas não cria uma nova se não existir
        HttpSession sessao = request.getSession(false);

        if (sessao == null || sessao.getAttribute("usuarioLogado") == null) {
            // Usuário não está logado, redireciona para login
            response.sendRedirect(request.getContextPath() + "/index.html");
            return;
        }

        // Usuário logado - busca os dados para a página inicial
        request.setAttribute("receitas", receita_dao.mostrarTodos());
        request.setAttribute("usuarios", usuario_dao.mostrarTodos());

        // Encaminha para a página JSP que irá mostrar os dados (index.jsp)
        getServletContext().getRequestDispatcher("/index.html").forward(request, response);
    }
}

