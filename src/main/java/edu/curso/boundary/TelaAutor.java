package edu.curso.boundary;

import edu.curso.controller.AutorController;
import edu.curso.entity.Autor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class TelaAutor {

    private final AutorController controller = new AutorController();
    private final TableView<Autor> tabela = new TableView<>();

    private final TextField campoNome = new TextField();
    private final TextField campoNacionalidade = new TextField();
    private final DatePicker campoDataNascimento = new DatePicker();
    private final TextField campoEmail = new TextField();
    private final TextField campoTelefone = new TextField();

    private int codigoSelecionado = 0; // 0 = novo cadastro

    public void mostrar() {
        Stage janela = new Stage();
        janela.setTitle("Cadastro de Autores");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.setPadding(new Insets(10));
        form.add(new Label("Nome:"), 0, 0);          form.add(campoNome, 1, 0);
        form.add(new Label("Nacionalidade:"), 0, 1); form.add(campoNacionalidade, 1, 1);
        form.add(new Label("Nascimento:"), 0, 2);    form.add(campoDataNascimento, 1, 2);
        form.add(new Label("E-mail:"), 0, 3);        form.add(campoEmail, 1, 3);
        form.add(new Label("Telefone:"), 0, 4);      form.add(campoTelefone, 1, 4);

        Button btSalvar = new Button("Salvar");
        Button btRemover = new Button("Remover");
        Button btLimpar = new Button("Limpar");
        HBox botoes = new HBox(10, btSalvar, btRemover, btLimpar);
        botoes.setPadding(new Insets(0, 10, 10, 10));

        configurarColunas();

        btSalvar.setOnAction(e -> salvar());
        btRemover.setOnAction(e -> remover());
        btLimpar.setOnAction(e -> limparFormulario());

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) preencherFormulario(novo);
        });

        VBox layout = new VBox(10, form, botoes, tabela);
        janela.setScene(new Scene(layout, 640, 500));
        janela.show();

        atualizarTabela();
    }

    private void configurarColunas() {
        TableColumn<Autor, Integer> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));

        TableColumn<Autor, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Autor, String> colNac = new TableColumn<>("Nacionalidade");
        colNac.setCellValueFactory(new PropertyValueFactory<>("nacionalidade"));

        TableColumn<Autor, LocalDate> colNasc = new TableColumn<>("Nascimento");
        colNasc.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));

        TableColumn<Autor, String> colEmail = new TableColumn<>("E-mail");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Autor, String> colTel = new TableColumn<>("Telefone");
        colTel.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        tabela.getColumns().addAll(colCodigo, colNome, colNac, colNasc, colEmail, colTel);
    }

    private void atualizarTabela() {
        ObservableList<Autor> dados = FXCollections.observableArrayList(controller.listar());
        tabela.setItems(dados);
    }

    private void salvar() {
        try {
            Autor autor = new Autor(
                    campoNome.getText(),
                    campoNacionalidade.getText(),
                    campoDataNascimento.getValue(),
                    campoEmail.getText(),
                    campoTelefone.getText());

            if (codigoSelecionado == 0) {
                controller.cadastrar(autor);
            } else {
                controller.atualizar(codigoSelecionado, autor);
            }
            limparFormulario();
            atualizarTabela();
        } catch (IllegalArgumentException ex) {
            alerta(ex.getMessage());
        }
    }

    private void remover() {
        Autor selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            alerta("Selecione um autor na tabela para remover.");
            return;
        }
        controller.apagar(selecionado);
        limparFormulario();
        atualizarTabela();
    }

    private void preencherFormulario(Autor autor) {
        codigoSelecionado = autor.getCodigo();
        campoNome.setText(autor.getNome());
        campoNacionalidade.setText(autor.getNacionalidade());
        campoDataNascimento.setValue(autor.getDataNascimento());
        campoEmail.setText(autor.getEmail());
        campoTelefone.setText(autor.getTelefone());
    }

    private void limparFormulario() {
        codigoSelecionado = 0;
        campoNome.clear();
        campoNacionalidade.clear();
        campoDataNascimento.setValue(null);
        campoEmail.clear();
        campoTelefone.clear();
        tabela.getSelectionModel().clearSelection();
    }

    private void alerta(String mensagem) {
        new Alert(Alert.AlertType.WARNING, mensagem).showAndWait();
    }
}
