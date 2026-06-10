package edu.curso.DAO;

import edu.curso.entity.Emprestimo;
import edu.curso.entity.Leitor;
import edu.curso.entity.Livro;

import java.util.List;

public interface EmprestimoDAO {
    void cadastrar(Emprestimo emprestimo);
    void apagar(Emprestimo emprestimo);
    Emprestimo pesquisarPorCodigo(int id);
    List<Emprestimo> listarTodos();
    List<Leitor> listarLeitores();
    List<Livro> listarLivros();
}
