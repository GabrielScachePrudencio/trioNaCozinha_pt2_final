package br.edu.ifsp.arq.controller.controllerUsuario;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import br.edu.ifsp.arq.dao.UsuarioDAO;
import br.edu.ifsp.arq.model.Usuario;

@WebServlet("/UsuarioServletLogar")
public class UsuarioServletLogar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UsuarioDAO usuario_dao;

	public UsuarioServletLogar() {
		super();
		usuario_dao = UsuarioDAO.getInstance_U();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		StringBuilder jsonBuffer = new StringBuilder();
		try (BufferedReader reader = request.getReader()) {
			String linha;
			while ((linha = reader.readLine()) != null) {
				jsonBuffer.append(linha);
			}
		}

		Gson gson = new Gson();
		Usuario inputUsuario;

		try {

			inputUsuario = gson.fromJson(jsonBuffer.toString(), Usuario.class);
		} catch (JsonSyntaxException e) {
			sendErro(response, "JSON malformado");
			return;
		}

		String nome = inputUsuario.getNome();
		String senha = inputUsuario.getSenha();




		if (nome != null && senha != null) {
			for (Usuario u : usuario_dao.mostrarTodos()) {
				if (u.getNome().equals(nome) && u.getSenha().equals(senha)) {

					HttpSession sessao = request.getSession();
					sessao.setAttribute("usuarioLogado", u);

					String jsonResposta = gson.toJson(u);
					response.getWriter().write(jsonResposta);
					return;
				}
			}
		}


		sendErro(response, "Usuário ou senha inválidos");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession sessao = request.getSession(false);
	    if (sessao != null) {
	        Usuario usuarioLogado = (Usuario) sessao.getAttribute("usuarioLogado");
	        if (usuarioLogado != null) {
	            response.setContentType("application/json");
	            response.getWriter().write(new Gson().toJson(usuarioLogado));
	            return;
	        }
	    }
	    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    response.setContentType("application/json");
	    response.getWriter().write("{\"erro\":\"Usuário não logado\"}");
	}

	
	private void sendErro(HttpServletResponse response, String mensagem) throws IOException {
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.setContentType("application/json");
		response.getWriter().write("{\"erro\": \"" + mensagem + "\"}");
	}
}
