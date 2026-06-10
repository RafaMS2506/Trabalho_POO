package edu.curso.controller;

import edu.curso.DAO.LivroDAO;
import edu.curso.DAO.LivroDAOImplementation;
import edu.curso.entity.Autor;
import edu.curso.entity.Livro;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LivroController {

    private ObservableList<Livro> lista = FXCollections.observableArrayList();

    private IntegerProperty codigo = new SimpleIntegerProperty(0);
    private StringProperty titulo = new SimpleStringProperty("");
    private StringProperty editora = new SimpleStringProperty("");
    private IntegerProperty ano = new SimpleIntegerProperty(0);
    private StringProperty isbn = new SimpleStringProperty("");
    private IntegerProperty quantidade = new SimpleIntegerProperty(0);
    private ObjectProperty<Autor> autor = new SimpleObjectProperty<>(null);

    private LivroDAO dao  = new LivroDAOImplementation();

    public void fromEntity(Livro l) {
        if (l != null) {
            codigo.set(l.getCodigo());
            titulo.set(l.getTitulo());
            editora.set(l.getEditora());
            ano.set(l.getAno());
            isbn.set(l.getIsbn());
            quantidade.set(l.getQuantidade());
            autor.set(l.getAutor());
        }
    }

    public Livro toEntity() {
        Autor autorAtual = autor.get();

        Livro l = new Livro(
                titulo.get(),
                editora.get(),
                ano.get(),
                isbn.get(),
                quantidade.get(),
                autorAtual
        );
        l.setCodigo(codigo.get());
        return l;
    }

    public void limparCampos() {
        codigo.set(0);
        titulo.set("");
        editora.set("");
        ano.set(0);
        isbn.set("");
        quantidade.set(0);
        autor.set(null);
    }

    public void salvar() {
        Livro l = toEntity();
        System.out.println("Codigo do Livro ==> " + l.getCodigo());
        if(codigo.get() > 0) {
            dao.atualizar(l);
        } else {
            dao.cadastrar(l);
        }
        limparCampos();
        carregar();
    }

    public void carregar() {
        lista.clear();
        lista.addAll(dao.buscarPorTitulo(""));
    }

    public void apagar(int indice) {
        Livro l = lista.get(indice);
        dao.apagar(l);
        carregar();
    }

    public void pesquisar() {
        lista.clear();
        lista.addAll(dao.buscarPorTitulo(getTitulo()));
    }

    public String getTitulo() {
        return titulo.get();
    }

    public ObservableList<Livro> getLista() {
        return lista;
    }

    public IntegerProperty getCodigo() {
        return codigo;
    }

    public StringProperty getEditora() {
        return editora;
    }

    public IntegerProperty getAno() {
        return ano;
    }

    public StringProperty getIsbn() {
        return isbn;
    }

    public IntegerProperty getQuantidade() {
        return quantidade;
    }

    public ObjectProperty<Autor> getAutor() {
        return autor;
    }
}
