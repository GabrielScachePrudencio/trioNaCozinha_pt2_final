package br.edu.ifsp.arq.dao;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.Gson;

import br.edu.ifsp.arq.model.Comentario;
import br.edu.ifsp.arq.model.Receita;

public class ReceitaDAO implements GenericDAO<Receita> {
    private static ReceitaDAO instance;
    private static final String CAMINHO_ARQUIVO = "C:\\Users\\Flavio\\Desktop\\Java\\TerceiroSemestre\\JavaWeb\\Tarefas\\Trio_Na_Cozinha_pt2\\Trio_Na_Cozinha_pt2\\src\\main\\java\\br\\edu\\ifsp\\arq\\dados\\jsonReceitas.json";
    private Gson gson;

    private ReceitaDAO() {
        gson = new Gson();
    }

    public static ReceitaDAO getInstance_R() {
        if (instance == null) {
            instance = new ReceitaDAO();
        }
        return instance;
    }

    public boolean add(Receita r) {
        ArrayList<Receita> lista = getDadosArq();

        int maiorId = 0;
        for (Receita re : lista) {
            if (re.getId() > maiorId) {
                maiorId = re.getId();
            }
        }

        Receita novaRec = new Receita(maiorId + 1, r.getNome(), r.getAutor(), r.getTempoDePreparoMinutos(),
                r.getIngredientes(), r.getModoPreparo(), r.getCategorias(), r.getQtddPorcoes(), r.getImg());
        novaRec.setAvaliacoes(r.getAvaliacoes()); // adiciona avaliações se houver
        lista.add(novaRec);

        return setDadosArq(lista);
    }

    public boolean deletar(int id) {
        ArrayList<Receita> receitas = getDadosArq();

        for (int i = 0; i < receitas.size(); i++) {
            if (receitas.get(i).getId() == id) {
                receitas.remove(i);
                setDadosArq(receitas);
                return true;
            }
        }

        return false;
    }

    public boolean editar(int id, Receita obj) {
        ArrayList<Receita> receitas = getDadosArq();

        for (int i = 0; i < receitas.size(); i++) {
            if (receitas.get(i).getId() == id) {
                obj.setId(id);
                receitas.set(i, obj);
                setDadosArq(receitas);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Receita> mostrarTodos() {
        return getDadosArq();
    }

    public Receita buscarPorID(int id) {
        ArrayList<Receita> receitas = getDadosArq();

        for (Receita r : receitas) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    // ✅ Novo método para adicionar ou atualizar avaliação do usuário
    public boolean adicionarAvaliacao(int idReceita, int nota, String nomeUsuario) {
        ArrayList<Receita> receitas = getDadosArq();

        for (Receita r : receitas) {
            if (r.getId() == idReceita) {
                r.adicionarAvaliacao(nomeUsuario, nota); // adiciona ou sobrescreve
                return setDadosArq(receitas); // persiste
            }
        }

        return false; // receita não encontrada
    }

    public boolean addCom(int idReceita, Comentario c) {
        ArrayList<Receita> receitas = getDadosArq();
        
        for (Receita r : receitas) {
            if (r.getId() == idReceita) {
                r.addCom(c);
                return setDadosArq(receitas);
            }
        }
        return setDadosArq(receitas); 

    }
    
    // 🧾 Lê o JSON
    public ArrayList<Receita> getDadosArq() {
        File arq = new File(CAMINHO_ARQUIVO);

        if (!arq.exists()) {
            try {
                File pasta = arq.getParentFile();
                if (!pasta.exists()) {
                    pasta.mkdirs();
                }
                arq.createNewFile();
            } catch (IOException e) {
                System.err.println("Erro ao criar o arquivo de receitas.");
                e.printStackTrace();
                return new ArrayList<>();
            }
            return new ArrayList<>();
        }

        ArrayList<Receita> lista = new ArrayList<>();
        int maiorId = 0;

        try (Scanner sc = new Scanner(new FileInputStream(arq), "UTF-8")) {
            while (sc.hasNextLine()) {
                String linha = sc.nextLine().trim();

                if (!linha.isEmpty()) {
                    try {
                        Receita t = gson.fromJson(linha, Receita.class);

                        if (t != null) {
                            lista.add(t);
                            if (t.getId() > maiorId) {
                                maiorId = t.getId();
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("❌ Erro ao interpretar a linha como Receita:");
                        System.err.println("Linha com erro: " + linha);
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao ler o arquivo de receitas:");
            e.printStackTrace();
        }

        Receita.atualizarProximoId(maiorId);

        return lista;
    }

    // 💾 Salva no JSON
    public boolean setDadosArq(ArrayList<Receita> lista) {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(CAMINHO_ARQUIVO),
                StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(writer);
             PrintWriter pw = new PrintWriter(bw)) {

            for (Receita t : lista) {
                String json = gson.toJson(t);
                pw.println(json);
            }
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
