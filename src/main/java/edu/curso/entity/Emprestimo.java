package edu.curso.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Emprestimo {
    private int codigo;
    private Leitor leitor;
    private List<ItemEmprestimo> listaLivros;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private boolean status;

    public Emprestimo(Leitor leitor) {
        this.leitor = leitor;
        this.listaLivros = new ArrayList<>();
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucaoPrevista = this.dataEmprestimo.plusDays(30);
        this.status = true;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Leitor getLeitor() {
        return leitor;
    }

    public void setLeitor(Leitor leitor) {
        this.leitor = leitor;
    }

    public List<ItemEmprestimo> getListaLivros() {
        return listaLivros;
    }

    public void setListaLivros(List<ItemEmprestimo> listaLivros) {
        this.listaLivros = listaLivros;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
