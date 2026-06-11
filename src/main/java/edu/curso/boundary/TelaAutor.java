package edu.curso.boundary;

import edu.curso.controller.AutorController;
import edu.curso.entity.Autor;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TelaAutor implements Tela {

    private final AutorController controller = new AutorController();
    private final TableView<Autor> tabela = new TableView<>();

    private final TextField txtNome = new TextField();
    private final TextField txtNacionalidade = new TextField();
    private final DatePicker dtaNascimento = new DatePicker();
    private final TextField txtEmail = new TextField();
    private final TextField txtTelefone = new TextField();

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Pane render() {

        BorderPane panePrincipal = new BorderPane();

        GridPane paneCampos = new GridPane();
        paneCampos.add(new Label("Nome:"), 0, 0);
        paneCampos.add(txtNome, 1, 0);

        paneCampos.add(new Label("Nacionalidade:"), 0, 1);
        paneCampos.add(txtNacionalidade, 1, 1);

        paneCampos.add(new Label("Data Nascimento:"), 0, 2);
        paneCampos.add(dtaNascimento, 1, 2);

        paneCampos.add(new Label("Email:"), 0, 3);
        paneCampos.add(txtEmail, 1, 3);

        paneCampos.add(new Label("Telefone:"), 0, 4);
        paneCampos.add(txtTelefone, 1, 4);

        Button btSalvar = new Button("Salvar");
        Button btLimpar = new Button("Limpar");
        Button btPesquisar = new Button("Pesquisar");

        panePrincipal.setTop(paneCampos);
        panePrincipal.setCenter(tabela);

        btSalvar.setOnAction((e) -> {
            if (controller.salvar()) {
                new Alert(Alert.AlertType.INFORMATION, "Autor gravado com sucesso!").show();
            }
        });

        btPesquisar.setOnAction((e) -> {
            controller.pesquisar();
        });

        btLimpar.setOnAction((e) -> controller.limparCampos());

        paneCampos.add(btSalvar, 0, 5);
        paneCampos.add(btLimpar, 1, 5);
        paneCampos.add(btPesquisar, 2, 5);

        Bindings.bindBidirectional(txtNome.textProperty(), controller.nomeProperty());
        Bindings.bindBidirectional(txtNacionalidade.textProperty(), controller.nacionalidadeProperty());
        Bindings.bindBidirectional(dtaNascimento.valueProperty(), controller.dataNascProperty());
        Bindings.bindBidirectional(txtEmail.textProperty(), controller.emailProperty());
        Bindings.bindBidirectional(txtTelefone.textProperty(), controller.telefoneProperty());
        TableColumn<Autor, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(itemData.getValue().getNome())
        );

        TableColumn<Autor, String> colNacionalidade = new TableColumn<>("Nacionalidade");
        colNacionalidade.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(itemData.getValue().getNacionalidade())
        );

        TableColumn<Autor, String> colDataNasc = new TableColumn<>("Data Nascimento");
        colDataNasc.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(
                        itemData.getValue().getDataNascimento() != null
                                ? itemData.getValue().getDataNascimento().format(dtf)
                                : ""
                )
        );

        TableColumn<Autor, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(itemData.getValue().getEmail())
        );

        TableColumn<Autor, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(itemData.getValue().getTelefone())
        );

        TableColumn<Autor, Void> colAcoes = new TableColumn<>("Ações");

        tabela.getSelectionModel().selectedItemProperty().addListener(
                (obj, antigo, novo) -> controller.fromEntity(novo)
        );

        tabela.getColumns().add(colNome);
        tabela.getColumns().add(colNacionalidade);
        tabela.getColumns().add(colDataNasc);
        tabela.getColumns().add(colEmail);
        tabela.getColumns().add(colTelefone);
        tabela.getColumns().add(colAcoes);

        tabela.setItems(controller.getLista());
        controller.carregar();

        Callback<TableColumn<Autor, Void>, TableCell<Autor, Void>>
                callback = new Callback<>() {
            public TableCell<Autor, Void> call(TableColumn<Autor, Void> column) {
                return new TableCell<Autor, Void>() {
                    Button btApagar = new Button("Apagar");

                    {
                        btApagar.setOnAction(e -> {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                                    "Apagar este Autor?", ButtonType.YES, ButtonType.NO);
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
