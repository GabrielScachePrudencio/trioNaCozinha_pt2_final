package br.edu.ifsp.arq.controller;

import br.edu.ifsp.arq.dao.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ServletRenovaPrincipal
 */
@WebServlet("/ServletRenovaPrincipal")
public class ServletRenovaPrincipal extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setAttribute("receitas", ReceitaDAO.getInstance_R().mostrarTodos());
        request.setAttribute("usuarios", UsuarioDAO.getInstance_U().mostrarTodos());

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    doGet(request, response);
	}

}
