package br.edu.ifsp.arq.model;

public enum TipoUsuario {
    NORMAL("Usuário Comum"),
    AVALIADOR("Avaliador de Receitas"),
    ADMINISTRADOR("Administrador do Sistema");

    private final String descricao;

    TipoUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
