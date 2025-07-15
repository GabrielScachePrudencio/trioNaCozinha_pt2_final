package br.edu.ifsp.arq.dao;

import java.util.ArrayList;

public interface GenericDAO<T> {
	boolean add(T obj);
	boolean deletar(int id);
	boolean editar(int id, T obj);
	ArrayList<T> mostrarTodos();
	T buscarPorID(int id);
	ArrayList<T> getDadosArq();
	boolean setDadosArq(ArrayList<T> lista);
	
}
