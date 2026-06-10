package edu.curso.controller;

import edu.curso.DAO.AutorDAO;
import edu.curso.DAO.AutorDAOImplementation;
import edu.curso.entity.Autor;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class AutorController {

    ObservableList<Autor> lista = FXCollections.observableArrayList();

    private IntegerProperty codigo = new SimpleIntegerProperty(0);
    private StringProperty nome = new SimpleStringProperty("");
    private StringProperty nacionalidade = new SimpleStringProperty("");
    private ObjectProperty<LocalDate> dataNascimento = new SimpleObjectProperty<>(LocalDate.now());
    private StringProperty email = new SimpleStringProperty("");
    private StringProperty telefone = new SimpleStringProperty("");

    private final AutorDAO dao = new AutorDAOImplementation();

    public void fromEntity(Autor autor) {
        if (autor != null) {
            codigo.set(autor.getCodigo());
            nome.set(autor.getNome());
            nacionalidade.set(autor.getNacionalidade());
            dataNascimento.set(autor.getDataNascimento());
            email.set(autor.getEmail());
            telefone.set(autor.getTelefone());
        }
    }
    public Autor toEntity() {
        Autor autor = new Autor(
                nome.get(),
                nacionalidade.get(),
                dataNascimento.get(),
                email.get(),
                telefone.get()
        );


        autor.setCodigo(codigo.get());
        return autor;
    }

    public void limparCampos() {
        codigo.set(0);
        nome.set("");
        nacionalidade.set("");
        dataNascimento.set(LocalDate.now());
        email.set("");
        telefone.set("");
    }

    public void salvar() {
        Autor autor = toEntity();
        System.out.println("Codigo do Autor ==> " + autor.getCodigo());
        if (codigo.get() > 0) {
            dao.atualizar(autor);
        } else {
            dao.cadastrar(autor);
        }
        limparCampos();
        carregar();
    }

    public void carregar() {
        lista.clear();
        lista.addAll(dao.pesquisarPorNome(""));
    }

    public void apagar(int indice) {
        Autor autor = lista.get(indice);
        dao.apagar(autor);
        carregar();
    }

    public void pesquisar() {
        lista.clear();
        lista.addAll(dao.pesquisarPorNome(getNome()));
    }

//    public void cadastrar(Autor autor) {
//        validar(autor);
//        autorDAO.cadastrar(autor);
//    }
//
//    public void atualizar(int id, Autor autor) {
//        validar(autor);
//        autorDAO.atualizar(id, autor);
//    }
//
//    public void apagar(Autor autor) {
//        autorDAO.apagar(autor);
//    }
//
//    public List<Autor> listar() {
//        return autorDAO.listarTodos();
//    }
//
//    private void validar(Autor autor) {
//        if (autor.getNome() == null || autor.getNome().isBlank()) {
//            throw new IllegalArgumentException("O nome do autor não pode ficar vazio.");
//        }
//        if (autor.getTelefone() == null || autor.getTelefone().isBlank()) {
//            throw new IllegalArgumentException("O telefone não pode ficar vazio.");
//        }
//        if (autor.getEmail() == null || !autor.getEmail().contains("@") || !autor.getEmail().contains(".")) {
//            throw new IllegalArgumentException("O e-mail deve ter um formato válido.");
//        }
//        if (autor.getDataNascimento() == null) {
//            throw new IllegalArgumentException("A data de nascimento deve ser válida.");
//        }
//    }


    public IntegerProperty getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome.get();
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public StringProperty nacionalidadeProperty() {
        return nacionalidade;
    }

    public ObservableList<Autor> getLista() {
        return lista;
    }

    public ObjectProperty<LocalDate> dataNascProperty() {
        return dataNascimento;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty telefoneProperty() {
        return telefone;
    }
}
