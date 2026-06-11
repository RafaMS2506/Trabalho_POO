package edu.curso.controller;

import edu.curso.DAO.AutorDAO;
import edu.curso.DAO.AutorDAOImplementation;
import edu.curso.DAO.LivroDAO;
import edu.curso.DAO.LivroDAOImplementation;
import edu.curso.entity.Autor;
import edu.curso.entity.Livro;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class LivroController {

    private ObservableList<Livro> lista = FXCollections.observableArrayList();
    private ObservableList<Autor> autores = FXCollections.observableArrayList();
    private ObservableList<String> nomesAutores = FXCollections.observableArrayList();

    private IntegerProperty codigo = new SimpleIntegerProperty(0);
    private StringProperty titulo = new SimpleStringProperty("");
    private StringProperty editora = new SimpleStringProperty("");
    private IntegerProperty ano = new SimpleIntegerProperty(2026);
    private StringProperty isbn = new SimpleStringProperty("");
    private IntegerProperty quantidade = new SimpleIntegerProperty(1);
    private ObjectProperty<Autor> autor = new SimpleObjectProperty<>(null);
    private StringProperty autorSelecionadoNome = new SimpleStringProperty("");

    private LivroDAO dao  = new LivroDAOImplementation();
    private AutorDAO autorDao = new AutorDAOImplementation();

    public void fromEntity(Livro l) {
        if (l != null) {
            codigo.set(l.getCodigo());
            titulo.set(l.getTitulo());
            editora.set(l.getEditora());
            ano.set(l.getAno());
            isbn.set(l.getIsbn());
            quantidade.set(l.getQuantidade());
            autor.set(l.getAutor());

            if (l.getAutor() != null) {
                autorSelecionadoNome.set(l.getAutor().getNome());
            } else {
                autorSelecionadoNome.set("");
            }
        }
    }

    public Livro toEntity() {
        Autor autorReal = buscarAutorPorNomeNaLista(autorSelecionadoNome.get());
        Livro l = new Livro(
                titulo.get(),
                editora.get(),
                ano.get(),
                isbn.get(),
                quantidade.get(),
                autorReal
        );
        l.setCodigo(codigo.get());
        return l;
    }

    private Autor buscarAutorPorNomeNaLista(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return null;
        }
        for (Autor a : autores) {
            if (a.getNome() != null && a.getNome().trim().equalsIgnoreCase(nome.trim())) {
                return a;
            }
        }
        return null;
    }

    public void limparCampos() {
        codigo.set(0);
        titulo.set("");
        editora.set("");
        ano.set(2026);
        isbn.set("");
        quantidade.set(1);
        autor.set(null);
    }

    public boolean salvar() {
        Livro l = toEntity();
        if (!validar(l)) {
            return false;
        }
        System.out.println("Codigo do Livro ==> " + l.getCodigo());
        if(codigo.get() > 0) {
            dao.atualizar(l);
        } else {
            dao.cadastrar(l);
        }
        limparCampos();
        carregar();
        return true;
    }

    private boolean validar(Livro l) {
        String erro = null;
        int anoAtual = java.time.LocalDate.now().getYear();
        if (l.getTitulo() == null || l.getTitulo().isBlank()) {
            erro = "O título não pode ficar vazio.";
        } else if (l.getIsbn() == null || l.getIsbn().isBlank()) {
            erro = "O ISBN não pode ficar vazio.";
        } else if (l.getAno() <= 0 || l.getAno() > anoAtual) {
            erro = "O ano deve ser válido (até " + anoAtual + ").";
        } else if (l.getQuantidade() < 0) {
            erro = "A quantidade não pode ser negativa.";
        } else if (l.getAutor() == null) {
            erro = "Selecione um autor cadastrado para o livro.";
        }
        if (erro != null) {
            new Alert(Alert.AlertType.WARNING, erro).showAndWait();
            return false;
        }
        return true;
    }

    public void carregar() {
        lista.clear();
        lista.addAll(dao.buscarPorTitulo(""));
    }

    public void carregarAutores() {
        autores.clear();
        nomesAutores.clear();
        autores.addAll(autorDao.listarTodos());

        for (Autor a : autores) {
            if (a != null && a.getNome() != null) {
                nomesAutores.add(a.getNome());
            }
        }
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

    public StringProperty tituloProperty() {
        return titulo;
    }

    public ObservableList<Livro> getLista() {
        return lista;
    }

    public IntegerProperty getCodigo() {
        return codigo;
    }

    public ObservableList<Autor> autoresProperty() {
        return autores;
    }

    public StringProperty editoraProperty() {
        return editora;
    }

    public IntegerProperty anoProperty() {
        return ano;
    }

    public StringProperty isbnProperty() {
        return isbn;
    }

    public ObservableList<String> getNomeAutores() {
        return nomesAutores;
    }

    public StringProperty autorSelecionadoNomeProperty() {
        return autorSelecionadoNome;
    }

    public IntegerProperty quantidadeProperty() {
        return quantidade;
    }

    public ObjectProperty<Autor> autorProperty() {
        return autor;
    }
}
