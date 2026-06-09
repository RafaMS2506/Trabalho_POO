package edu.curso.entity;

public class Livro {
    private int codigo;
    private String titulo;
    private String editora;
    private int ano;
    private String isbn;
    private int quantidade;
    private Autor autor;

    public Livro(String titulo, String editora, int ano, String isbn, int quantidade, Autor autor) {
        this.titulo = titulo;
        this.editora = editora;
        this.ano = ano;
        this.isbn = isbn;
        this.quantidade = quantidade;
        this.autor = autor;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}