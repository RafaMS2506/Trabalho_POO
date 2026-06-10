package edu.curso.DAO;

import edu.curso.entity.Livro;

import java.util.List;

public interface LivroDAO {

    void cadastrar(Livro livro);
    void apagar(Livro livro);
    void atualizar(Livro livro);
    List<Livro> buscarPorTitulo(String titulo);

}
