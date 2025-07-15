package br.edu.ifsp.arq.controller.controllerReceita;

import java.io.IOException;

import com.google.gson.Gson;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.edu.ifsp.arq.dao.ReceitaDAO;
import br.edu.ifsp.arq.model.Receita;


@WebServlet("/ReceitaServletDetalhada")
public class ReceitaServletDetalhada extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ReceitaDAO receitaDao;
	Gson gson = new Gson();
    public ReceitaServletDetalhada() {
        super();
        receitaDao = ReceitaDAO.getInstance_R();
    }



    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String idStr = request.getParameter("id");

        Receita receita = null;

        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                receita = receitaDao.buscarPorID(id);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
                return;
            }
        } else if (nome != null && !nome.trim().isEmpty()) {
            for (Receita r : receitaDao.mostrarTodos()) {
                if (r.getNome().toLowerCase().contains(nome.toLowerCase())) {
                    receita = r;
                    break;
                }
            }
        }

        if (receita == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Receita não encontrada");
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(receita));
    }



	

}
