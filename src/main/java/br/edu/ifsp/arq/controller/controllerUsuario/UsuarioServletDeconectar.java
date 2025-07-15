package br.edu.ifsp.arq.controller.controllerUsuario;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;


@WebServlet("/UsuarioServletDeconectar")
public class UsuarioServletDeconectar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sessao = request.getSession(false);
		if (sessao != null) {
			sessao.setAttribute("usuarioLogado", null);
			sessao.invalidate(); 
		}
		
		// Redireciona para a página inicial
		response.sendRedirect(request.getContextPath() + "/index.html");
	}	

	
}
