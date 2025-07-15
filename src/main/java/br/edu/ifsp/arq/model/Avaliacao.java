package br.edu.ifsp.arq.model;

import java.io.Serializable;

public class Avaliacao implements Serializable {
    private String usuario;
    private int nota;

    public Avaliacao() {}

    public Avaliacao(String usuario, int nota) {
        this.usuario = usuario;
        this.nota = nota;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    @Override
    public String toString() {
        return "{usuario='" + usuario + "', nota=" + nota + "}";
    }
}
