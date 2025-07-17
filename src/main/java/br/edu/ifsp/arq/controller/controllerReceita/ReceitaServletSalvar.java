package br.edu.ifsp.arq.controller.controllerReceita;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.google.gson.Gson;

import br.edu.ifsp.arq.dao.ReceitaDAO;
import br.edu.ifsp.arq.dao.UsuarioDAO;
import br.edu.ifsp.arq.model.Receita;
import br.edu.ifsp.arq.model.Usuario;



@WebServlet("/ReceitaServletSalvar")
@MultipartConfig
public class ReceitaServletSalvar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ReceitaDAO receitaDao;
    private UsuarioDAO usuarioDao;


    public ReceitaServletSalvar() {
        super();
        receitaDao = ReceitaDAO.getInstance_R();
        usuarioDao = UsuarioDAO.getInstance_U(); 

    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sessao = request.getSession();
		Usuario usuarioLogado = (Usuario) sessao.getAttribute("usuarioLogado");

		if (usuarioLogado == null) {
			sendErro(response, "tem que estar logado para add");
            return;
		}

		int id2 = Integer.parseInt(request.getParameter("id"));
		String nome = request.getParameter("nome");
		String autor = request.getParameter("autor");
		String modoPreparo = request.getParameter("modoPreparo");
		int tempoDePreparoMinutos = Integer.parseInt(request.getParameter("tempoDePreparoMinutos"));
		int qtddPorcoes = Integer.parseInt(request.getParameter("qtddPorcoes"));

		// Ingredientes
		ArrayList<String> ingredientes = new ArrayList<>();
		String[] ingredientesArray = request.getParameterValues("ingredientes");
		if (ingredientesArray != null) {
			for (String i : ingredientesArray) ingredientes.add(i);
		}

		// Categorias
		ArrayList<String> categorias = new ArrayList<>();
		String[] categoriasArray = request.getParameterValues("categorias");
		if (categoriasArray != null) {
			for (String c : categoriasArray) categorias.add(c);
		}

		// Imagem
		Part filePart = request.getPart("img");
		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
		if (fileName.isEmpty()) {
			fileName = receitaDao.buscarPorID(id2).getImg(); // Mantém imagem anterior
		} else {
			String uploadPath = getServletContext().getRealPath("") + File.separator + "imagens";
			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) uploadDir.mkdir();
			filePart.write(uploadPath + File.separator + fileName);
		}

		// Verifica se nome já existe
		boolean nomeJaExiste = receitaDao.mostrarTodos().stream()
			.anyMatch(r -> r.getNome().equalsIgnoreCase(nome) && r.getId() != id2);

		if (nomeJaExiste) {
			sendErro(response, "Já existe uma receita com esse nome. Escolha outro nome.");
            return;
		}

		// Cria receita com o ID correto
		Receita r = new Receita(id2, nome, autor, tempoDePreparoMinutos, ingredientes, modoPreparo, categorias, qtddPorcoes, fileName);
		receitaDao.editar(id2, r);

		// Atualiza lista do usuário logado
		

        Usuario dono = usuarioDao.buscarDonoDaReceita(id2);
        
        if (dono != null) {
            // Remove receita antiga da lista
            dono.getMinhasReceitas().removeIf(receita -> receita.getId() == id2);

            // Adiciona a nova versão da receita
            dono.getMinhasReceitas().add(r);

            // Atualiza o usuário no DAO
            usuarioDao.atualizarMinhasReceitas(dono, dono.getMinhasReceitas());

            
        }
		for (int i = 0; i < usuarioLogado.getMinhasReceitas().size(); i++) {
			if (usuarioLogado.getMinhasReceitas().get(i).getId() == id2) {
				usuarioLogado.getMinhasReceitas().set(i, r);
				break;
			}
		}

		sessao.setAttribute("usuarioLogado", usuarioLogado);
		request.setAttribute("receitas", receitaDao.mostrarTodos());
		
        response.sendRedirect(request.getContextPath() + "/index.html");

        
	}
	
	private void sendErro(HttpServletResponse response, String mensagem) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"erro\": \"" + mensagem + "\"}");
	}
}
