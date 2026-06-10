package edu.curso.controller;

import edu.curso.DAO.LeitorDAO;
import edu.curso.DAO.LeitorDAOImplementation;
import edu.curso.entity.Leitor;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class LeitorController {

    private ObservableList<Leitor> lista = FXCollections.observableArrayList();

    private IntegerProperty codigo = new SimpleIntegerProperty(0);
    private StringProperty cpf = new SimpleStringProperty("");
    private StringProperty nome = new SimpleStringProperty("");
    private StringProperty telefone = new SimpleStringProperty("");
    private StringProperty email = new SimpleStringProperty("");
    private StringProperty cep = new SimpleStringProperty("");
    private ObjectProperty<LocalDate> dataCadastro = new SimpleObjectProperty<>(LocalDate.now());

    private LeitorDAO dao = new LeitorDAOImplementation();

    public LeitorController() {
        carregar();
    }

    public void fromEntity(Leitor l) {
        if (l != null) {
            codigo.set(l.getCodigo());
            cpf.set(l.getCpf());
            nome.set(l.getNome());
            telefone.set(l.getTelefone());
            email.set(l.getEmail());
            cep.set(l.getCep());
            dataCadastro.set(l.getDataCadastro());
        }
    }

    public Leitor toEntity() {
        Leitor l = new Leitor(
                cpf.get(),
                nome.get(),
                telefone.get(),
                email.get(),
                cep.get(),
                dataCadastro.get()
        );

        l.setCodigo(codigo.get());
        return l;
    }

    public void limparCampos() {
        codigo.set(0);
        cpf.set("");
        nome.set("");
        telefone.set("");
        email.set("");
        cep.set("");
        dataCadastro.set(LocalDate.now());
    }

    public void salvar() {
        Leitor l = toEntity();
        System.out.println("Codigo do Leitor ==> " + l.getCodigo());
        if (codigo.get() > 0) {
            dao.atualizar(l);
        } else {
            dao.cadastrar(l);
        }
        limparCampos();
        carregar();
    }

    public void carregar() {
        lista.clear();
        lista.addAll(dao.pesquisarPorCpf(""));
    }

    public void apagar(int indice) {
        Leitor l = lista.get(indice);
        dao.apagar(l);
        carregar();
    }

    public void pesquisar() {
        lista.clear();
        lista.addAll(dao.pesquisarPorCpf(getCpf()));
    }

    public String getCpf() {
        return cpf.get();
    }

    public StringProperty cpfProperty() {
        return cpf;
    }

    public ObservableList<Leitor> getLista() {
        return lista;
    }

    public IntegerProperty codigoProperty() {
        return codigo;
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public StringProperty telefoneProperty() {
        return telefone;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty cepProperty() {
        return cep;
    }

    public ObjectProperty<LocalDate> dataCadastroProperty() {
        return dataCadastro;
    }
}
