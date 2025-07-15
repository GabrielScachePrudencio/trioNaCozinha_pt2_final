package br.edu.ifsp.arq.controller.controllerUsuario;

import java.awt.Window.Type;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import br.edu.ifsp.arq.dao.UsuarioDAO;
import br.edu.ifsp.arq.model.Receita;
import br.edu.ifsp.arq.model.Usuario;

@WebServlet("/UsuarioServletSalvar")
@MultipartConfig
public class UsuarioServletSalvar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       UsuarioDAO usuarioDao;
 
    public UsuarioServletSalvar() {
        super();
        usuarioDao = UsuarioDAO.getInstance_U();
    }

	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	try {
          
        
    	HttpSession sessao = request.getSession();
        Usuario usuarioLogado = (Usuario) sessao.getAttribute("usuarioLogado");
        
        if(usuarioLogado != null) {	
	        String id = request.getParameter("id");
	        int id2 = Integer.parseInt(id);
	        String nome = request.getParameter("nome");
	        String nomeAnt = request.getParameter("nomeAntigo");
	        String senha = request.getParameter("senha");
	        
	        ArrayList<Receita> minhasRece = usuarioDao.buscarPorID(id2).getMinhasReceitas();
	        
	        boolean nomeJaExiste = false;
	        
	        Usuario usuarioSendoEditado = usuarioDao.buscarPorID(id2);

	      
	        
	        for (Usuario u : usuarioDao.mostrarTodos()) {
	        	if (u.getNome().equalsIgnoreCase(nome) && u.getId() != id2) {
	                nomeJaExiste = true;
	                break;
	            }
	        }
	        
	        if (nomeJaExiste) {
	            request.setAttribute("msgErro", "Já existe um usuário com esse nome. Escolha outro nome.");
	            request.getRequestDispatcher("assets/views/extras/Erro.html").forward(request, response);
	            return; 
	        }
	        
	        Part filePart = request.getPart("img");
	        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
	        if (fileName.isEmpty()) {
	            fileName = usuarioDao.buscarPorID(id2).getImg();
	        } else {
	            String uploadPath = getServletContext().getRealPath("") + File.separator + "imagens";
	            File uploadDir = new File(uploadPath);
	            if (!uploadDir.exists()) uploadDir.mkdir();
	            filePart.write(uploadPath + File.separator + fileName);
	        }
	        
	        Usuario u = new Usuario(id2, nome, senha, fileName, null, minhasRece);
	        
	        usuarioDao.editar(id2, u);
	        
	        if(usuarioLogado.getNome().equals(nomeAnt)) {
	        	sessao.setAttribute("usuarioLogado", u);
	        }
	        
	        	
	        
	        request.setAttribute("usuarios", usuarioDao.mostrarTodos());
	        response.sendRedirect(request.getContextPath() + "/index.html");
	        return; // ← IMPORTANTE: interrompe o fluxo aqui
        } 
        request.setAttribute("msgErro", "precisa estar logado para poder salvar");
	    request.getRequestDispatcher("assets/views/extras/Erro.html").forward(request, response);

    	} catch (Exception e) {
            e.printStackTrace(); // Mostra no console do servidor
            request.setAttribute("msgErro", "Erro interno ao salvar o usuário.");
            request.getRequestDispatcher("assets/views/extras/Erro.html").forward(request, response);
        }
    }

}
