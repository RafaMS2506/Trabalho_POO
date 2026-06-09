package edu.curso.entity;

import java.time.LocalDate;

public class ItemEmprestimo {
    private Emprestimo emprestimo;
    private Livro livro;
    private LocalDate dataDevolucao;
    private StatusItem status;

    public ItemEmprestimo(Livro livro) {
        this.livro = livro;
        this.status = StatusItem.PENDENTE;
        this.dataDevolucao = null;
    }

    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public StatusItem getStatus() {
        return status;
    }

    public void setStatus(StatusItem status) {
        this.status = status;
    }
}
