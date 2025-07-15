package br.edu.ifsp.arq.controller.controllerReceita;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import br.edu.ifsp.arq.dao.ReceitaDAO;
import br.edu.ifsp.arq.model.Usuario;

@WebServlet("/ReceitaServletDeletar")
public class ReceitaServletDeletar extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReceitaDAO receitaDao;

    public ReceitaServletDeletar() {
        super();
        receitaDao = ReceitaDAO.getInstance_R();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        HttpSession sessao = request.getSession(false); // melhor evitar criar sessão se não existir
        Usuario usuarioLogado = (sessao != null) ? (Usuario) sessao.getAttribute("usuarioLogado") : null;

        if (usuarioLogado != null && idParam != null) {
            try {
                int idReceita = Integer.parseInt(idParam);

                boolean removido = receitaDao.deletar(idReceita); // remover pelo ID

                if (removido) {
                    usuarioLogado.getMinhasReceitas().removeIf(r -> r.getId() == idReceita);
                    sessao.setAttribute("usuarioLogado", usuarioLogado);

                    response.sendRedirect(request.getContextPath() + "/assets/views/usuario/Conta.html");
                } else {
                    request.setAttribute("msgErro", "Receita não encontrada.");
                    request.getRequestDispatcher("assets/views/extras/Erro.html").forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("msgErro", "ID da receita inválido.");
                request.getRequestDispatcher("assets/views/extras/Erro.html").forward(request, response);
            }
        } else {
            request.setAttribute("msgErro", "Você precisa estar logado para deletar uma receita.");
            request.getRequestDispatcher("assets/views/extras/Erro.html").forward(request, response);
        }
    }
}
