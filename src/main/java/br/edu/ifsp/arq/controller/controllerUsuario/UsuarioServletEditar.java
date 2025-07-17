package br.edu.ifsp.arq.controller.controllerUsuario;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.edu.ifsp.arq.dao.UsuarioDAO;
import br.edu.ifsp.arq.model.Usuario;

/**
 * Servlet implementation class UsuarioServletEditar
 */
@WebServlet("/UsuarioServletEditar")
@MultipartConfig
public class UsuarioServletEditar extends HttpServlet {
    private static final long serialVersionUID = 1L;
    UsuarioDAO usuarioDao;

    public UsuarioServletEditar() {
        super();
        usuarioDao = UsuarioDAO.getInstance_U();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sessao = request.getSession(false);
        Usuario usuarioLogado = (sessao != null) ? (Usuario) sessao.getAttribute("usuarioLogado") : null;

        if (usuarioLogado == null) {
            request.setAttribute("msgErro", "Você precisa estar logado para poder editar.");
            request.getRequestDispatcher("assets/views/extras/Erro.html").forward(request, response);
            return;
        }

        String id = request.getParameter("id");
        int id2;
        try {
            id2 = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            request.setAttribute("msgErro", "ID inválido.");
            request.getRequestDispatcher("assets/views/extras/Erro.html").forward(request, response);
            return;
        }

        Usuario u = usuarioDao.buscarPorID(id2);
        if (u == null) {
            request.setAttribute("msgErro", "Usuário não encontrado.");
            request.getRequestDispatcher("assets/views/extras/Erro.html").forward(request, response);
            return;
        }

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='pt-BR'>");
        out.println("<head>");
        out.println("  <meta charset='UTF-8'>");
        out.println("  <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("  <title>Editar Usuário</title>");
        out.println("  <style>");
        out.println("    body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f4f7f8; margin: 0; padding: 0; }");
        out.println("    .container { max-width: 600px; margin: 50px auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 0 15px rgba(0,0,0,0.1); }");
        out.println("    h1 { text-align: center; color: #333; }");
        out.println("    form { display: flex; flex-direction: column; }");
        out.println("    label { margin: 10px 0 5px; color: #555; font-weight: 600; }");
        out.println("    input[type=text], input[type=file] { padding: 10px; border: 1px solid #ccc; border-radius: 4px; font-size: 1rem; }");
        out.println("    input[type=text]:focus { border-color: #007BFF; outline: none; }");
        out.println("    img { margin-top: 15px; border-radius: 8px; max-width: 100%; height: auto; box-shadow: 0 2px 6px rgba(0,0,0,0.15); }");
        out.println("    button { margin-top: 25px; background: #007BFF; color: white; border: none; padding: 12px; font-size: 1.1rem; border-radius: 6px; cursor: pointer; transition: background 0.3s ease; }");
        out.println("    button:hover { background: #0056b3; }");
        out.println("    a { display: block; text-align: center; margin-top: 20px; color: #007BFF; text-decoration: none; font-weight: 600; }");
        out.println("    a:hover { text-decoration: underline; }");
        out.println("  </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("  <div class='container'>");
        out.println("    <h1>Editar Usuário</h1>");
        out.println("    <form action='" + request.getContextPath() + "/UsuarioServletSalvar?id=" + u.getId() + "' method='POST' enctype='multipart/form-data'>");
        out.println("      <input type='hidden' name='id' value='" + u.getId() + "'>");

        out.println("      <label for='nome'>Nome:</label>");
        out.println("      <input type='text' id='nome' name='nome' value='" + u.getNome() + "' required>");
        out.println("      <input type='hidden' name='nomeAntigo' value='" + u.getNome() + "'>");

        out.println("      <label for='senha'>Senha:</label>");
        out.println("      <input type='text' id='senha' name='senha' value='" + u.getSenha() + "' required>");
        
        out.println("      <label for='img'>Imagem:</label>");
        out.println("      <input type='file' id='img' name='img' accept='image/*'>");
        
        out.println("      <img src='" + request.getContextPath() + "/imagens/" + u.getImg() + "' alt='Imagem do usuário'>");

        out.println("      <button type='submit'>Salvar Alterações</button>");
        out.println("    </form>");
        out.println("    <a href='../../../index.html'>Voltar à página principal</a>");
        out.println("  </div>");
        out.println("</body>");
        out.println("</html>");
    }
}
