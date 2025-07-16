package br.edu.ifsp.arq.controller.controllerUsuario;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import br.edu.ifsp.arq.dao.UsuarioDAO;
import br.edu.ifsp.arq.model.Receita;
import br.edu.ifsp.arq.model.Usuario;

/**
 * Servlet implementation class todosUsuarios
 */
@WebServlet("/todosUsuarios")
public class todosUsuarios extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private UsuarioDAO dao;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public todosUsuarios() {
        super();
        dao = UsuarioDAO.getInstance_U();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<Usuario> lista = dao.mostrarTodos();
		String json = new Gson().toJson(lista);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
