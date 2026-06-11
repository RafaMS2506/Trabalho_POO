package edu.curso.DAO;

import edu.curso.entity.Autor;
import edu.curso.entity.Livro;

import java.util.List;

public interface LivroDAO {

    void cadastrar(Livro livro);
    void apagar(Livro livro);
    void atualizar(Livro livro);
    List<Livro> buscarPorTitulo(String titulo);
    Autor buscarAutorPorNome(String nome);

}
