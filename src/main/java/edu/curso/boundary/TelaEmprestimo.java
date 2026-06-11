package edu.curso.boundary;

import edu.curso.controller.EmprestimoController;
import edu.curso.entity.Emprestimo;
import edu.curso.entity.ItemEmprestimo;
import edu.curso.entity.Leitor;
import edu.curso.entity.Livro;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;

public class TelaEmprestimo {

    private final EmprestimoController controller = new EmprestimoController();

    private final ComboBox<Leitor> cbLeitor = new ComboBox<>();
    private final ComboBox<Livro> cbLivro = new ComboBox<>();
    private final DatePicker dpDevolucao = new DatePicker(LocalDate.now().plusDays(30));

    private final ObservableList<ItemEmprestimo> itensAtuais = FXCollections.observableArrayList();
    private final TableView<ItemEmprestimo> tabelaItens = new TableView<>();
    private final TableView<Emprestimo> tabelaEmprestimos = new TableView<>();

    public void mostrar() {
        Stage janela = new Stage();
        janela.setTitle("Registro de Empréstimos");

        cbLeitor.setConverter(new StringConverter<>() {
            @Override public String toString(Leitor l) { return l == null ? "" : l.getNome() + " (" + l.getCpf() + ")"; }
            @Override public Leitor fromString(String s) { return null; }
        });
        cbLivro.setConverter(new StringConverter<>() {
            @Override public String toString(Livro l) { return l == null ? "" : l.getTitulo() + " — qtd: " + l.getQuantidade(); }
            @Override public Livro fromString(String s) { return null; }
        });

        Button btAddLivro = new Button("Adicionar livro");
        btAddLivro.setOnAction(e -> adicionarLivro());

        Button btRegistrar = new Button("Registrar Empréstimo");
        btRegistrar.setOnAction(e -> registrar());

        Button btRemover = new Button("Remover Empréstimo");
        btRemover.setOnAction(e -> remover());

        HBox linhaLeitor = new HBox(10, new Label("Leitor:"), cbLeitor,
                new Label("Devolução:"), dpDevolucao);
        HBox linhaLivro = new HBox(10, new Label("Livro:"), cbLivro, btAddLivro);

        configurarTabelaItens();
        configurarTabelaEmprestimos();

        VBox layout = new VBox(8,
                linhaLeitor,
                linhaLivro,
                new Label("Livros deste empréstimo:"),
                tabelaItens,
                btRegistrar,
                new Separator(),
                new Label("Empréstimos cadastrados:"),
                tabelaEmprestimos,
                btRemover);
        layout.setPadding(new Insets(12));

        janela.setScene(new Scene(layout, 720, 620));
        janela.show();

        carregarCombos();
        atualizarTabelaEmprestimos();
    }

    private void configurarTabelaItens() {
        TableColumn<ItemEmprestimo, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLivro().getTitulo()));
        tabelaItens.getColumns().add(colTitulo);
        tabelaItens.setItems(itensAtuais);
        tabelaItens.setPrefHeight(140);
    }

    private void configurarTabelaEmprestimos() {
        TableColumn<Emprestimo, Integer> colCod = new TableColumn<>("Código");
        colCod.setCellValueFactory(new PropertyValueFactory<>("codigo"));

        TableColumn<Emprestimo, String> colLeitor = new TableColumn<>("Leitor");
        colLeitor.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLeitor().getNome()));

        TableColumn<Emprestimo, LocalDate> colData = new TableColumn<>("Empréstimo");
        colData.setCellValueFactory(new PropertyValueFactory<>("dataEmprestimo"));

        TableColumn<Emprestimo, LocalDate> colDev = new TableColumn<>("Devolução prev.");
        colDev.setCellValueFactory(new PropertyValueFactory<>("dataDevolucaoPrevista"));

        TableColumn<Emprestimo, String> colQtd = new TableColumn<>("Qtd livros");
        colQtd.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getListaLivros().size())));

        TableColumn<Emprestimo, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isStatus() ? "Ativo" : "Encerrado"));

        tabelaEmprestimos.getColumns().addAll(colCod, colLeitor, colData, colDev, colQtd, colStatus);
    }

    private void adicionarLivro() {
        Livro livro = cbLivro.getValue();
        if (livro == null) {
            alerta("Selecione um livro para adicionar.");
            return;
        }
        itensAtuais.add(new ItemEmprestimo(livro));
    }

    private void registrar() {
        Leitor leitor = cbLeitor.getValue();
        if (leitor == null) {
            alerta("Selecione um leitor.");
            return;
        }
        try {
            Emprestimo emp = new Emprestimo(leitor);
            emp.getListaLivros().addAll(itensAtuais);
            if (dpDevolucao.getValue() != null) {
                emp.setDataDevolucaoPrevista(dpDevolucao.getValue());
            }
            controller.cadastrar(emp);

            itensAtuais.clear();
            cbLeitor.setValue(null);
            dpDevolucao.setValue(LocalDate.now().plusDays(30));
            carregarCombos();
            atualizarTabelaEmprestimos();
        } catch (IllegalArgumentException ex) {
            alerta(ex.getMessage());
        }
    }

    private void remover() {
        Emprestimo sel = tabelaEmprestimos.getSelectionModel().getSelectedItem();
        if (sel == null) {
            alerta("Selecione um empréstimo para remover.");
            return;
        }
        controller.apagar(sel);
        atualizarTabelaEmprestimos();
    }

    private void carregarCombos() {
        cbLeitor.setItems(FXCollections.observableArrayList(controller.listarLeitores()));
        cbLivro.setItems(FXCollections.observableArrayList(controller.listarLivros()));
    }

    private void atualizarTabelaEmprestimos() {
        tabelaEmprestimos.setItems(FXCollections.observableArrayList(controller.listar()));
    }

    private void alerta(String mensagem) {
        new Alert(Alert.AlertType.WARNING, mensagem).showAndWait();
    }
}
