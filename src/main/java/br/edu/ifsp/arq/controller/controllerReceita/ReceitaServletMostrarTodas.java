package br.edu.ifsp.arq.controller.controllerReceita;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import br.edu.ifsp.arq.dao.ReceitaDAO;
import br.edu.ifsp.arq.model.Receita;

@WebServlet("/ReceitaServletMostrarTodas")
public class ReceitaServletMostrarTodas extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ReceitaDAO dao;
	
    public ReceitaServletMostrarTodas() {
        super();
        dao = ReceitaDAO.getInstance_R();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
		List<Receita> lista = dao.mostrarTodos();
		String json = new Gson().toJson(lista);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);		
	}

	

}
