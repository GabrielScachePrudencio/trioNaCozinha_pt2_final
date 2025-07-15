package br.edu.ifsp.arq.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {
    private int id;
    private String nome;
    private String senha;
    private String img;

    // Alterado de String para TipoUsuario
    private TipoUsuario tipo;

    private ArrayList<Receita> minhasReceitas;
    private static int proximo_id = 0;

    private static final long serialVersionUID = 1L;

    public Usuario() {
        this.id = ++proximo_id;
    }

    public Usuario(int id, String nome, String senha, String img, TipoUsuario tipo, ArrayList<Receita> minhasReceitas) {
        setId(id);
        setNome(nome);
        setSenha(senha);
        setTipo(tipo);
        setImg(img);
        setMinhasReceitas(minhasReceitas);
    }

    public Usuario(String nome, String senha, String img, TipoUsuario tipo, ArrayList<Receita> minhasReceitas) {
        this();
        setNome(nome);
        setSenha(senha);
        setTipo(tipo);
        setImg(img);
        setMinhasReceitas(minhasReceitas);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public ArrayList<Receita> getMinhasReceitas() {
        return minhasReceitas;
    }

    public void setMinhasReceitas(ArrayList<Receita> minhasReceitas) {
        this.minhasReceitas = minhasReceitas;
    }

    // 🔄 Getter e Setter do enum TipoUsuario
    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public static void atualizarProximoId(int id) {
        if (id > proximo_id) {
            proximo_id = id;
        }
    }

    @Override
    public String toString() {
        return "Usuário {" +
               "\n  ID: " + id +
               "\n  Nome: " + nome +
               "\n  Senha: " + senha +
               "\n  Tipo: " + tipo +
               "\n}";
    }
}
