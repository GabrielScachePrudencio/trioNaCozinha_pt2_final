package br.edu.ifsp.arq.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Optional;

public class Receita implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String autor;
    private int tempoDePreparoMinutos;
    private ArrayList<String> ingredientes;
    private String modoPreparo;
    private ArrayList<String> categorias;
    private int qtddPorcoes;
    private String img;
    private static int proximo_id = 0;

    private ArrayList<Avaliacao> avaliacoes = new ArrayList<>();
    private ArrayList<Comentario> comentarios = new ArrayList<>(); // <- aqui

    
    public Receita() {
        this.id = ++proximo_id;
    }

    public Receita(int id, String nome, String autor, int tempoDePreparoMinutos,
                   ArrayList<String> ingredientes, String modoPreparo,
                   ArrayList<String> categorias, int qtddPorcoes, String img) {
        this.id = id;
        this.nome = nome;
        this.autor = autor;
        this.tempoDePreparoMinutos = tempoDePreparoMinutos;
        this.ingredientes = ingredientes;
        this.modoPreparo = modoPreparo;
        this.categorias = categorias;
        this.qtddPorcoes = qtddPorcoes;
        this.img = img;
        this.avaliacoes = new ArrayList<>();
        this.comentarios = new ArrayList<>();
        		
    }

    public Receita(String nome, String autor, int tempoDePreparoMinutos,
                   ArrayList<String> ingredientes, String modoPreparo,
                   ArrayList<String> categorias, int qtddPorcoes, String img) {
        this();
        this.nome = nome;
        this.autor = autor;
        this.tempoDePreparoMinutos = tempoDePreparoMinutos;
        this.ingredientes = ingredientes;
        this.modoPreparo = modoPreparo;
        this.categorias = categorias;
        this.qtddPorcoes = qtddPorcoes;
        this.img = img;
    }

    // Getters e setters padrão

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static void atualizarProximoId(int id) {
        if (id > proximo_id) {
            proximo_id = id;
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getTempoDePreparoMinutos() {
        return tempoDePreparoMinutos;
    }

    public void setTempoDePreparoMinutos(int tempoDePreparoMinutos) {
        this.tempoDePreparoMinutos = tempoDePreparoMinutos;
    }

    public ArrayList<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(ArrayList<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getModoPreparo() {
        return modoPreparo;
    }

    public void setModoPreparo(String modoPreparo) {
        this.modoPreparo = modoPreparo;
    }

    public ArrayList<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(ArrayList<String> categorias) {
        this.categorias = categorias;
    }

    public int getQtddPorcoes() {
        return qtddPorcoes;
    }

    public void setQtddPorcoes(int qtddPorcoes) {
        this.qtddPorcoes = qtddPorcoes;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    // Avaliações - lista de Avaliacao

    public ArrayList<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(ArrayList<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    
    public void adicionarAvaliacao(String nomeUsuario, int nota) {
        if (nomeUsuario == null || nomeUsuario.trim().isEmpty()) return;
        if (nota < 1 || nota > 5) return;

        Optional<Avaliacao> existente = avaliacoes.stream()
            .filter(a -> a.getUsuario().equalsIgnoreCase(nomeUsuario))
            .findFirst();

        if (existente.isPresent()) {
            existente.get().setNota(nota);
        } else {
            avaliacoes.add(new Avaliacao(nomeUsuario, nota));
        }
    }

    
    public double getMediaAvaliacoes() {
        if (avaliacoes == null || avaliacoes.isEmpty()) return 0.0;
        return avaliacoes.stream()
                .mapToInt(Avaliacao::getNota)
                .average()
                .orElse(0.0);
    }
    
    public void addCom(Comentario c) {
    	comentarios.add(c);
    }
    
    public ArrayList<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(ArrayList<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

	@Override
    public String toString() {
        return "Receita {" +
                "\n  ID: " + id +
                "\n  Nome: " + nome +
                "\n  Autor: " + autor +
                "\n  Tempo de Preparo: " + tempoDePreparoMinutos + " minutos" +
                "\n  Ingredientes: " + ingredientes +
                "\n  Modo de Preparo: " + modoPreparo +
                "\n  Categorias: " + categorias +
                "\n  Quantidade de Porções: " + qtddPorcoes +
                "\n  Média das Avaliações: " + getMediaAvaliacoes() +
                "\n  Avaliações: " + avaliacoes + "\ncomentarios" + comentarios +
                "\n}";
    }
}
