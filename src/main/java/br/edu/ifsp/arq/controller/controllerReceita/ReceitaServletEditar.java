package br.edu.ifsp.arq.controller.controllerReceita;

import java.io.IOException;


import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import br.edu.ifsp.arq.dao.ReceitaDAO;
import br.edu.ifsp.arq.model.Receita;
import br.edu.ifsp.arq.model.Usuario; 

import java.util.ArrayList;




@WebServlet("/ReceitaServletEditar")
@MultipartConfig
public class ReceitaServletEditar extends HttpServlet {
	private static final long serialVersionUID = 1L;

    ReceitaDAO receitaDao;   	
    
    public ReceitaServletEditar() {
        super();
        receitaDao = ReceitaDAO.getInstance_R();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        int id2 = Integer.parseInt(id);
        HttpSession sessao = request.getSession(false);
        Usuario usuarioLogado = (Usuario) sessao.getAttribute("usuarioLogado");
        System.out.print("Editar Receita: " + id2);
        Receita receita = receitaDao.buscarPorID(id2);

        if (usuarioLogado != null) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html lang='pt-br'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
            out.println("<title>Editar Receita</title>");
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container my-5'>");
            out.println("<h1 class='mb-4'>Editar Receita</h1>");
            out.println("<form action='ReceitaServletSalvar' method='POST' enctype='multipart/form-data'>");

            // Campo oculto com ID
            out.println("<input type='hidden' name='id' value='" + receita.getId() + "'>");

            // Nome da Receita
            out.println("<div class='mb-3'>");
            out.println("<label for='nome' class='form-label'>Nome da Receita:</label>");
            out.println("<input type='text' class='form-control' id='nome' name='nome' value='" + receita.getNome() + "' required>");
            out.println("</div>");

            // Autor (só leitura)
            out.println("<div class='mb-3'>");
            out.println("<label class='form-label'>Autor:</label>");
            out.println("<input type='text' class='form-control' name='autor' value='" + receita.getAutor() + "' readonly>");
            out.println("</div>");

            // Modo de preparo
            out.println("<div class='mb-3'>");
            out.println("<label for='modoPreparo' class='form-label'>Modo de Preparo:</label>");
            out.println("<textarea class='form-control' id='modoPreparo' name='modoPreparo' rows='5' required>");
            out.println(receita.getModoPreparo());
            out.println("</textarea>");
            out.println("</div>");

            // Tempo de preparo (minutos)
            out.println("<div class='mb-3'>");
            out.println("<label for='tempoDePreparoMinutos' class='form-label'>Tempo de Preparo (em minutos):</label>");
            out.println("<input type='number' class='form-control' id='tempoDePreparoMinutos' name='tempoDePreparoMinutos' value='" + receita.getTempoDePreparoMinutos() + "' min='1' required>");
            out.println("</div>");

            // Quantidade de porções
            out.println("<div class='mb-3'>");
            out.println("<label for='qtddPorcoes' class='form-label'>Quantidade de Porções:</label>");
            out.println("<input type='number' class='form-control' id='qtddPorcoes' name='qtddPorcoes' value='" + receita.getQtddPorcoes() + "' min='1' required>");
            out.println("</div>");

            // Ingredientes (checkboxes em grid responsivo)
            out.println("<fieldset class='mb-3'>");
            out.println("<legend>Ingredientes:</legend>");
            out.println("<div class='row'>");
            ArrayList<String> ingredientes = receita.getIngredientes();
            String[] todosIngredientes = {"Açúcar", "Farinha", "Leite", "Ovo", "Chocolate", "Fermento", "Essencia de baunilha", "Agua", "Guarana", "Coco", "avelã", "manteiga", "3 ovos"};
            for (String ing : todosIngredientes) {
                boolean checked = ingredientes.contains(ing);
                String safeId = ing.replaceAll("\\s+", "").replaceAll("[^a-zA-Z0-9]", "");
                out.println("<div class='col-6 col-md-4'>");
                out.println("<div class='form-check'>");
                out.println("<input class='form-check-input' type='checkbox' id='ingrediente_" + safeId + "' name='ingredientes' value='" + ing + "'" + (checked ? " checked" : "") + ">");
                out.println("<label class='form-check-label' for='ingrediente_" + safeId + "'>" + ing + "</label>");
                out.println("</div>");
                out.println("</div>");
            }
            out.println("</div>");
            out.println("</fieldset>");

            // Categorias (checkboxes inline)
            out.println("<fieldset class='mb-3'>");
            out.println("<legend>Categorias:</legend>");
            ArrayList<String> categorias = receita.getCategorias();
            String[] todasCategorias = {"Bolos", "Biscoitos", "Naturais", "Doce"};
            for (String cat : todasCategorias) {
                boolean checked = categorias.contains(cat);
                String safeId = cat.replaceAll("\\s+", "").replaceAll("[^a-zA-Z0-9]", "");
                out.println("<div class='form-check form-check-inline'>");
                out.println("<input class='form-check-input' type='checkbox' id='categoria_" + safeId + "' name='categorias' value='" + cat + "'" + (checked ? " checked" : "") + ">");
                out.println("<label class='form-check-label' for='categoria_" + safeId + "'>" + cat + "</label>");
                out.println("</div>");
            }
            out.println("</fieldset>");

            // Imagem (upload + preview)
            out.println("<div class='mb-3'>");
            out.println("<label for='img' class='form-label'>Imagem:</label>");
            out.println("<input class='form-control' type='file' name='img' id='img' accept='image/*'>");
            out.println("</div>");

            out.println("<div class='mb-3'>");
            out.println("<label class='form-label'>Imagem Atual:</label><br>");
            out.println("<img src='" + request.getContextPath() + "/imagens/" + receita.getImg() + "' alt='Imagem da Receita' class='img-thumbnail' style='max-width: 200px;'>");
            out.println("</div>");

            // Botões
            out.println("<button type='submit' class='btn btn-primary me-2'>Salvar Alterações</button>");
            out.println("<a href='" + request.getContextPath() + "/assets/views/usuario/Conta.html'>Voltar conta</a>");

            out.println("</form>");
            out.println("</div>");

            // Bootstrap JS (opcional)
            out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>");
            out.println("</body>");
            out.println("</html>");

        } else {
            request.setAttribute("msgErro", "precisa estar logado para poder editar");
            request.getRequestDispatcher("assets/views/extras/Erro.html").forward(request, response);
        }
    }
}

