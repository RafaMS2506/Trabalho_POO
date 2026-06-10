package edu.curso.boundary;

import edu.curso.controller.LivroController;
import edu.curso.entity.Livro;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.util.Optional;

public class TelaLivro implements Tela{

    private LivroController controller = new LivroController();
    private final TableView<Livro> tabela = new TableView<>();

    private final TextField txtTitulo = new TextField();
    private final TextField txtEditora = new TextField();
    private final TextField txtAno = new TextField();
    private final TextField txtIsbn = new TextField();
    private final TextField txtQuantidade = new TextField();
    private final ComboBox<String> cbAutor = new ComboBox<>();

    @Override
    public Pane render() {

        BorderPane panePrincipal = new BorderPane();

        GridPane paneCampos = new GridPane();
        paneCampos.add(new Label("Titulo:"), 0, 0);
        paneCampos.add(txtTitulo, 1, 0);

        paneCampos.add(new Label("Editora:"),0, 1);
        paneCampos.add(txtEditora, 1, 1);

        paneCampos.add(new Label("Ano Publicação:"),0, 2);
        paneCampos.add(txtAno, 1, 2);

        paneCampos.add(new Label("ISBN:"),0, 3);
        paneCampos.add(txtIsbn, 1, 3);

        paneCampos.add(new Label("Quantidade:"),0, 4);
        paneCampos.add(txtQuantidade, 1, 4);

        paneCampos.add(new Label("Autor:"),0, 5);
        paneCampos.add(cbAutor, 1, 5);

        Button btSalvar = new Button("Salvar");
        Button btLimpar = new Button("Limpar");
        Button btPesquisar = new Button("Pesquisar");

        panePrincipal.setTop(paneCampos);
        panePrincipal.setCenter(tabela);

        btSalvar.setOnAction((e) -> {
            controller.salvar();
            new Alert(Alert.AlertType.INFORMATION, "Livro gravado com sucesso!").show();
        });

        btPesquisar.setOnAction((e) -> {
            controller.pesquisar();
        });

        btLimpar.setOnAction((e) -> controller.limparCampos());

        paneCampos.add(btSalvar, 0, 6);
        paneCampos.add(btLimpar, 1, 6);
        paneCampos.add(btPesquisar, 2, 6);

        Bindings.bindBidirectional(txtTitulo.textProperty(), controller.tituloProperty());
        Bindings.bindBidirectional(txtEditora.textProperty(), controller.editoraProperty());
        Bindings.bindBidirectional(txtAno.textProperty(), controller.anoProperty(), new javafx.util.converter.NumberStringConverter());
        Bindings.bindBidirectional(txtIsbn.textProperty(), controller.isbnProperty());
        Bindings.bindBidirectional(txtQuantidade.textProperty(), controller.quantidadeProperty(), new javafx.util.converter.NumberStringConverter());

        Bindings.bindBidirectional(cbAutor.valueProperty(), controller.autorSelecionadoNomeProperty());
        controller.carregarAutores();
        cbAutor.setItems(controller.getNomeAutores());

        TableColumn<Livro, String> colTitulo = new TableColumn<>("Titulo");
        colTitulo.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(itemData.getValue().getTitulo())
        );

        TableColumn<Livro, String> colEditora = new TableColumn<>("Editora");
        colEditora.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(itemData.getValue().getEditora())
        );

        TableColumn<Livro, Integer> colAno = new TableColumn<>("Ano");
        colAno.setCellValueFactory(itemData -> {
            if (itemData.getValue() != null) {
                return new javafx.beans.property.SimpleObjectProperty<>(itemData.getValue().getAno());
            }
            return new javafx.beans.property.SimpleObjectProperty<>(0);
        });

        TableColumn<Livro, String> colIsbn = new TableColumn<>("Isbn");
        colIsbn.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(itemData.getValue().getIsbn())
        );

        TableColumn<Livro, String> colQuantidade = new TableColumn<>("Quantidade");
        colQuantidade.setCellValueFactory(
                itemData -> new javafx.beans.property.ReadOnlyStringWrapper(String.valueOf(itemData.getValue().getQuantidade()))
        );

        TableColumn<Livro, String> colAutor = new TableColumn<>("Autor");
        colAutor.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(itemData.getValue().getAutor().getNome())
        );

        TableColumn<Livro, Void> colAcoes = new TableColumn<>("Ações");

        tabela.getSelectionModel().selectedItemProperty().addListener(
                (obj, antigo, novo) -> controller.fromEntity(novo)
        );

        tabela.getColumns().add(colTitulo);
        tabela.getColumns().add(colEditora);
        tabela.getColumns().add(colAno);
        tabela.getColumns().add(colIsbn);
        tabela.getColumns().add(colQuantidade);
        tabela.getColumns().add(colAutor);
        tabela.getColumns().add(colAcoes);

        tabela.setItems(controller.getLista());

        Callback<TableColumn<Livro, Void>, TableCell<Livro, Void>>
            callback = new Callback<>() {
                public TableCell<Livro, Void> call(TableColumn<Livro, Void> column) {
                    return new TableCell<Livro, Void>() {
                        Button btApagar = new Button("Apagar");
                        {
                            btApagar.setOnAction(e -> {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                                        "Apagar este Livro?", ButtonType.YES, ButtonType.NO);
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
