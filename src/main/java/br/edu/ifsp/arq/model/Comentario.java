package br.edu.ifsp.arq.model;

public class Comentario {
	private int id;
	private String texto;
	private Usuario usuario;
	
	
	
	public Comentario(String texto, Usuario usuario) {
		setId(id);
		setTexto(texto);
		setUsuario(usuario);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		id = id++;
	}

	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "Comentario [id=" + id + ", texto=" + texto + ", usuario=" + usuario + "]";
	}
	
	
	
}
