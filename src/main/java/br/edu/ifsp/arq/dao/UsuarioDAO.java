package br.edu.ifsp.arq.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.Gson;

import br.edu.ifsp.arq.model.Receita;
import br.edu.ifsp.arq.model.TipoUsuario;
import br.edu.ifsp.arq.model.Usuario;

public class UsuarioDAO implements GenericDAO<Usuario> {
    private static UsuarioDAO instance;

    private static final String CAMINHO_ARQUIVO = "C:\\Users\\Flavio\\Desktop\\Java\\TerceiroSemestre\\JavaWeb\\Tarefas\\Trio_Na_Cozinha_pt2\\Trio_Na_Cozinha_pt2\\src\\main\\java\\br\\edu\\ifsp\\arq\\dados\\jsonUsuarios.json";

    private Gson gson;

    private UsuarioDAO() {
        gson = new Gson();
    }

    public static UsuarioDAO getInstance_U() {
        if (instance == null) {
            instance = new UsuarioDAO();
        }
        return instance;
    }

    @Override
    public boolean add(Usuario u) {
        ArrayList<Usuario> usuarios = getDadosArq();

        int maiorId = 0;
        for (Usuario us : usuarios) {
            if (us.getId() > maiorId) {
                maiorId = us.getId();
            }
        }

        // ✅ Corrigido: agora usa o enum TipoUsuario diretamente
        Usuario novoUsuario = new Usuario(
            maiorId + 1,
            u.getNome(),
            u.getSenha(),
            u.getImg(),
            u.getTipo(),  // enum em vez de string
            u.getMinhasReceitas()
        );

        usuarios.add(novoUsuario);
        return setDadosArq(usuarios);
    }

    @Override
    public boolean deletar(int id) {
        ArrayList<Usuario> usuarios = getDadosArq();

        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == id) {
                usuarios.remove(i);
                setDadosArq(usuarios);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean editar(int id, Usuario obj) {
        ArrayList<Usuario> usuarios = getDadosArq();

        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == id) {
                obj.setId(id);
                usuarios.set(i, obj);
                setDadosArq(usuarios);
                return true;
            }
        }
        return false;
    }

    public boolean atualizarMinhasReceitas(Usuario u, ArrayList<Receita> lista) {
        ArrayList<Usuario> usuarios = getDadosArq();

        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == u.getId()) {
                usuarios.get(i).setMinhasReceitas(lista);
                return setDadosArq(usuarios);
            }
        }

        return false; 
    }

    public ArrayList<Usuario> mostrarTodos() {
        return getDadosArq();
    }

    public Usuario buscarPorID(int id) {
        ArrayList<Usuario> usuarios = getDadosArq();

        for (Usuario u : usuarios) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    // Leitura dos dados do arquivo JSON
    public ArrayList<Usuario> getDadosArq() {
        File arq = new File(CAMINHO_ARQUIVO);

        if (!arq.exists()) {
            try {
                File pasta = arq.getParentFile();
                if (!pasta.exists()) {
                    pasta.mkdirs();
                }
                arq.createNewFile();
            } catch (IOException e) {
                System.err.println("❌ Erro ao criar o arquivo de usuários.");
                e.printStackTrace();
                return new ArrayList<>();
            }
            return new ArrayList<>();
        }

        ArrayList<Usuario> lista = new ArrayList<>();
        int maiorId = 0;

        try (Scanner sc = new Scanner(new FileInputStream(arq), "UTF-8")) {
            while (sc.hasNextLine()) {
                String linha = sc.nextLine().trim();

                if (!linha.isEmpty()) {
                    try {
                        Usuario t = gson.fromJson(linha, Usuario.class);
                        if (t != null) {
                            lista.add(t);
                            if (t.getId() > maiorId) {
                                maiorId = t.getId();
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("❌ Erro ao interpretar a linha como Usuário:");
                        System.err.println("Linha com erro: " + linha);
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao ler o arquivo de usuários:");
            e.printStackTrace();
        }

        Usuario.atualizarProximoId(maiorId);
        return lista;
    }

    // Escrita dos dados no arquivo JSON
    public boolean setDadosArq(ArrayList<Usuario> lista) {
        try (
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(CAMINHO_ARQUIVO), StandardCharsets.UTF_8);
            BufferedWriter bw = new BufferedWriter(writer);
            PrintWriter pw = new PrintWriter(bw)
        ) {
            for (Usuario t : lista) {
                String json = gson.toJson(t);
                pw.println(json);
            }
            return true;
        } catch (IOException e) {
            System.err.println("❌ Erro ao salvar os dados no arquivo de usuários:");
            e.printStackTrace();
            return false;
        }
    }
}
