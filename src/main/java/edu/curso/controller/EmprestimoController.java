package edu.curso.controller;

import edu.curso.DAO.EmprestimoDAO;
import edu.curso.DAO.EmprestimoDAOImplementation;
import edu.curso.entity.Emprestimo;
import edu.curso.entity.ItemEmprestimo;
import edu.curso.entity.Leitor;
import edu.curso.entity.Livro;

import java.util.List;

public class EmprestimoController {

    private final EmprestimoDAO emprestimoDAO = new EmprestimoDAOImplementation();

    public void cadastrar(Emprestimo emprestimo) {
        validar(emprestimo);
        emprestimoDAO.cadastrar(emprestimo);
    }

    public void apagar(Emprestimo emprestimo) {
        emprestimoDAO.apagar(emprestimo);
    }

    public List<Emprestimo> listar() {
        return emprestimoDAO.listarTodos();
    }

    public List<Leitor> listarLeitores() {
        return emprestimoDAO.listarLeitores();
    }

    public List<Livro> listarLivros() {
        return emprestimoDAO.listarLivros();
    }

    private void validar(Emprestimo emprestimo) {
        if (emprestimo.getLeitor() == null || emprestimo.getLeitor().getCodigo() <= 0) {
            throw new IllegalArgumentException("O leitor deve estar cadastrado.");
        }
        if (emprestimo.getListaLivros() == null || emprestimo.getListaLivros().isEmpty()) {
            throw new IllegalArgumentException("Adicione pelo menos um livro ao empréstimo.");
        }
        if (emprestimo.getDataDevolucaoPrevista().isBefore(emprestimo.getDataEmprestimo())) {
            throw new IllegalArgumentException("A data de devolução não pode ser menor que a data de empréstimo.");
        }
        for (ItemEmprestimo item : emprestimo.getListaLivros()) {
            if (item.getLivro().getQuantidade() <= 0) {
                throw new IllegalArgumentException(
                        "O livro \"" + item.getLivro().getTitulo() + "\" está sem exemplares disponíveis.");
            }
        }
    }
}
