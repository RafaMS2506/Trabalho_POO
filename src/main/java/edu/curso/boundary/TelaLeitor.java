package edu.curso.boundary;

import edu.curso.controller.LeitorController;
import edu.curso.entity.Leitor;
import edu.curso.entity.Livro;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

public class TelaLeitor implements Tela {

    private LeitorController controller = new LeitorController();

    private final TextField txtCpf = new TextField();
    private final TextField txtNome = new TextField();
    private final TextField txtTelefone = new TextField();
    private final TextField txtEmail = new TextField();
    private final TextField txtCep = new TextField();
    private final DatePicker dtaCadastro = new DatePicker();

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final TableView<Leitor> tabela = new TableView<>();

    @Override
    public Pane render() {

        BorderPane panePrincipal = new BorderPane();

        GridPane paneCampos = new GridPane();
        paneCampos.add(new Label("Cpf:"), 0, 0);
        paneCampos.add(txtCpf, 1, 0);

        paneCampos.add(new Label("Nome:"),0, 1);
        paneCampos.add(txtNome, 1, 1);

        paneCampos.add(new Label("Telefone:"),0, 2);
        paneCampos.add(txtTelefone, 1, 2);

        paneCampos.add(new Label("Email:"),0, 3);
        paneCampos.add(txtEmail, 1, 3);

        paneCampos.add(new Label("Cep:"),0, 4);
        paneCampos.add(txtCep, 1, 4);

        paneCampos.add(new Label("Data cadastro:"),0, 5);
        paneCampos.add(dtaCadastro, 1, 5);

        Button btSalvar = new Button("Salvar");
        Button btLimpar = new Button("Limpar");
        Button btPesquisar = new Button("Pesquisar");

        panePrincipal.setTop(paneCampos);
        panePrincipal.setCenter(tabela);

        btSalvar.setOnAction((e) -> {
            controller.salvar();
            new Alert(Alert.AlertType.INFORMATION, "Leitor gravado com sucesso!");
        });

        btPesquisar.setOnAction((e) -> {
            controller.pesquisar();
        });

        btLimpar.setOnAction((e) -> controller.limparCampos());

        paneCampos.add(btSalvar, 0, 6);
        paneCampos.add(btLimpar, 1, 6);
        paneCampos.add(btPesquisar, 2, 6);

        Bindings.bindBidirectional(txtCpf.textProperty(), controller.cpfProperty());
        Bindings.bindBidirectional(txtNome.textProperty(), controller.nomeProperty());
        Bindings.bindBidirectional(txtTelefone.textProperty(), controller.telefoneProperty());
        Bindings.bindBidirectional(txtEmail.textProperty(), controller.emailProperty());
        Bindings.bindBidirectional(txtCep.textProperty(), controller.cepProperty());
        Bindings.bindBidirectional(dtaCadastro.valueProperty(), controller.dataCadastroProperty());

        TableColumn<Leitor, String> colCpf = new TableColumn<>("Cpf");
        colCpf.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(itemData.getValue().getCpf())
        );

        TableColumn<Leitor, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(itemData.getValue().getNome())
        );

        TableColumn<Leitor, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(itemData.getValue().getTelefone())
        );

        TableColumn<Leitor, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(itemData.getValue().getEmail())
        );

        TableColumn<Leitor, String> colCep = new TableColumn<>("CEP");
        colCep.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(itemData.getValue().getCep())
        );

        TableColumn<Leitor, String> colDataCadastro = new TableColumn<>("Data cadastro");
        colDataCadastro.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(itemData.getValue().getDataCadastro().format(dtf))
        );

        TableColumn<Leitor, Void> colAcoes = new TableColumn<>("Ações");

        tabela.getSelectionModel().selectedItemProperty().addListener(
                (obj, antigo, novo) -> controller.fromEntity(novo)
        );

        tabela.getColumns().add(colCpf);
        tabela.getColumns().add(colNome);
        tabela.getColumns().add(colTelefone);
        tabela.getColumns().add(colEmail);
        tabela.getColumns().add(colCep);
        tabela.getColumns().add(colDataCadastro);
        tabela.getColumns().add(colAcoes);

        tabela.setItems(controller.getLista());

        Callback<TableColumn<Leitor, Void>, TableCell<Leitor, Void>>
                callback = new Callback<>() {
            public TableCell<Leitor, Void> call(TableColumn<Leitor, Void> column) {
                return new TableCell<Leitor, Void>() {
                    Button btApagar = new Button("Apagar");
                    {
                        btApagar.setOnAction(e -> {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                                    "Apagar este Leitor?", ButtonType.YES, ButtonType.NO);
                            alert.setTitle("Confirma Deleção");

                            Optional<ButtonType> result = alert.showAndWait();

                            if (result.isPresent() && result.get() == ButtonType.YES) {
                                controller.apagar(getIndex());
                            }


                        });
                    }
                    @Override
                    public void updateItem(Void parm, boolean empty) {
                        if (!empty) {
                            setGraphic(btApagar);
                        } else {
                            setGraphic(null);
                        }
                    }
                };

            }

        };

        colAcoes.setCellFactory(callback);

        return panePrincipal;
    }
}
