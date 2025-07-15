package br.edu.ifsp.arq.controller.controllerReceita;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import br.edu.ifsp.arq.dao.ReceitaDAO;
import br.edu.ifsp.arq.model.Receita;
import br.edu.ifsp.arq.model.Usuario;

@WebServlet("/AvaliarReceitaServlet")
public class AvaliarReceitaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReceitaDAO receitaDAO;

    @Override
    public void init() {
        receitaDAO = ReceitaDAO.getInstance_R();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("idReceita");
        String notaStr = request.getParameter("nota");

        if (idStr == null || idStr.isEmpty() || notaStr == null || notaStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parâmetros inválidos");
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuário não logado");
            return;
        }

        try {
            int idReceita = Integer.parseInt(idStr);
            int nota = Integer.parseInt(notaStr);

            if (nota < 1 || nota > 5) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nota deve ser entre 1 e 5");
                return;
            }

            Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
            String nomeUsuario = usuario.getNome();

            boolean sucesso = receitaDAO.adicionarAvaliacao(idReceita, nota, nomeUsuario);

            if (sucesso) {
                Receita receitaAtualizada = receitaDAO.buscarPorID(idReceita);
                if (receitaAtualizada != null) {
                    ArrayList<Receita> lista = usuario.getMinhasReceitas();
                    for (int i = 0; i < lista.size(); i++) {
                        if (lista.get(i).getId() == idReceita) {
                            lista.set(i, receitaAtualizada);
                            break;
                        }
                    }
                    // MUITO IMPORTANTE: atualiza o usuário na sessão para refletir a mudança
                    session.setAttribute("usuarioLogado", usuario);
                }
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Receita não encontrada");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parâmetros numéricos inválidos");
        }
    }
}
