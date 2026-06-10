package edu.curso.boundary;

import edu.curso.controller.EmprestimoController;
import edu.curso.entity.Emprestimo;
import edu.curso.entity.ItemEmprestimo;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TelaEmprestimo implements Tela{

    private final EmprestimoController controller = new EmprestimoController();

    private final ComboBox<String> cbLeitor = new ComboBox<>();
    private final ComboBox<String> cbLivro = new ComboBox<>();
    private final DatePicker dataEmprestimo = new DatePicker();
    private final DatePicker dpDevolucao = new DatePicker(LocalDate.now().plusDays(30));

//    private final ObservableList<ItemEmprestimo> itensAtuais = FXCollections.observableArrayList();
    private final TableView<ItemEmprestimo> tabelaItens = new TableView<>();
    private final TableView<Emprestimo> tabelaEmprestimos = new TableView<>();

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Pane render() {

        controller.carregarCombos();
        controller.carregar();

        Bindings.bindBidirectional(cbLeitor.valueProperty(), controller.leitorSelecionadoProperty());
        cbLeitor.setItems(controller.getNomesLeitores());

        Bindings.bindBidirectional(cbLivro.valueProperty(), controller.livroSelecionadoProperty());
        cbLivro.setItems(controller.getTitulosLivros());



        Bindings.bindBidirectional(dataEmprestimo.valueProperty(), controller.dataEmprestimoProperty());
        Bindings.bindBidirectional(dpDevolucao.valueProperty(), controller.dpDevolucaoProperty());

        TableColumn<ItemEmprestimo, String> colItemLivro = new TableColumn<>("Livro");
        colItemLivro.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(itemData.getValue().getLivro().getTitulo())
        );
        colItemLivro.setPrefWidth(250);

        TableColumn<ItemEmprestimo, String> colItemStatus = new TableColumn<>("Status");
        colItemStatus.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(itemData.getValue().getStatus().name())
        );

        TableColumn<ItemEmprestimo, Void> colItemRemover = new TableColumn<>("Ações");
        colItemRemover.setCellFactory(col -> new TableCell<>() {
            private final Button btRemover = new Button("Remover");
            { btRemover.setOnAction(e -> controller.removerLivro(getIndex())); }

            @Override
            protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : btRemover);
            }
        });

        tabelaItens.getColumns().addAll(colItemLivro,colItemStatus,colItemRemover);
        tabelaItens.setItems(controller.listaLivrosProperty());
        tabelaItens.setPrefHeight(150);

        BorderPane panePrincipal = new BorderPane();

        GridPane paneCampos = new GridPane();
        paneCampos.setHgap(8);
        paneCampos.setVgap(6);
        paneCampos.setPadding(new Insets(10));

        paneCampos.add(new Label("Leitor:"), 0, 0);
        paneCampos.add(cbLeitor, 1, 0);

        paneCampos.add(new Label("Livro: "),0, 1);
        paneCampos.add(cbLivro, 1, 1);

        paneCampos.add(new Label("Data empréstimo:"),0, 2);
        paneCampos.add(dataEmprestimo, 1, 2);

        paneCampos.add(new Label("Data prevista devolução:"),0, 3);
        paneCampos.add(dpDevolucao, 1, 3);

        Button btAdicionarLivro = new Button("Adicionar");
        btAdicionarLivro.setOnAction(e -> controller.adicionarLivro());
        paneCampos.add(btAdicionarLivro, 2, 3);

        Button btSalvar = new Button("Salvar");
        Button btLimpar = new Button("Limpar");
        Button btPesquisar = new Button("Pesquisar");

        panePrincipal.setTop(paneCampos);
        panePrincipal.setCenter(tabelaEmprestimos);

        btSalvar.setOnAction((e) -> {
            controller.salvar();
            new Alert(Alert.AlertType.INFORMATION, "Leitor gravado com sucesso!").showAndWait();
        });

        btPesquisar.setOnAction((e) -> {
            controller.pesquisar();
        });

        btLimpar.setOnAction((e) -> controller.limparCampos());

        paneCampos.add(btSalvar, 0, 5);
        paneCampos.add(btLimpar, 1, 5);
        paneCampos.add(btPesquisar, 2, 5);

        TableColumn<Emprestimo, String> colLeitor = new TableColumn<>("Leitor");
        colLeitor.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(itemData.getValue().getLeitor().getNome())
        );
        colLeitor.setPrefWidth(250);

        TableColumn<Emprestimo, String> colDataEmprestimo = new TableColumn<>("Data emprestimo");
        colDataEmprestimo.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(itemData.getValue().getDataEmprestimo().format(dtf))
        );

        TableColumn<Emprestimo, String> colDpDevolucao = new TableColumn<>("Data devolução prevista");
        colDpDevolucao.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(itemData.getValue().getDataDevolucaoPrevista().format(dtf))
        );

        TableColumn<Emprestimo, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(
                c -> new ReadOnlyStringWrapper(c.getValue().isStatus() ? "Ativo" : "Encerrado")
        );

        TableColumn<Emprestimo, Void> colAcoes = new TableColumn<>("Ações");
        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btApagar = new Button("Apagar");
            {
                btApagar.setOnAction(e -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Apagar este empréstimo?",
                            ButtonType.YES, ButtonType.NO);
                    alert.setTitle("Confirma deleção");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.YES) {
                        controller.apagar(getIndex());
                    }
                });
            }
            protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : btApagar);
            }
        });

        tabelaEmprestimos.getSelectionModel().selectedItemProperty()
                .addListener((obs, antigo, novo) -> controller.fromEntity(novo));

        tabelaEmprestimos.getColumns().addAll(colLeitor, colDataEmprestimo, colDpDevolucao, colStatus, colAcoes);
        tabelaEmprestimos.setItems(controller.getLista());

//        Callback<TableColumn<Emprestimo, Void>, TableCell<Emprestimo, Void>>
//                callback = new Callback<>() {
//            public TableCell<Emprestimo, Void> call(TableColumn<Emprestimo, Void> column) {
//                return new TableCell<Emprestimo, Void>() {
//                    Button btApagar = new Button("Apagar");
//                    {
//                        btApagar.setOnAction(e -> {
//                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
//                                    "Apagar este Emprestimo?", ButtonType.YES, ButtonType.NO);
//                            alert.setTitle("Confirma Deleção");
//
//                            Optional<ButtonType> result = alert.showAndWait();
//
//                            if (result.isPresent() && result.get() == ButtonType.YES) {
//                                controllerEmp.apagar(getIndex());
//                            }
//                        });
//                    }
//                    @Override
//                    public void updateItem(Void parm, boolean empty) {
//                        if (!empty) {
//                            setGraphic(btApagar);
//                        } else {
//                            setGraphic(null);
//                        }
//                    }
//                };
//            }
//
//        };
//
//        colAcoes.setCellFactory(callback);

        paneCampos.add(new Label("Livros no empréstimo:"), 0, 4);
        paneCampos.add(tabelaItens,                        1, 4, 2, 1);

        return panePrincipal;
    }
//    public void mostrar() {
//        Stage janela = new Stage();
//        janela.setTitle("Registro de Empréstimos");
//
//        cbLeitor.setConverter(new StringConverter<>() {
//            @Override public String toString(Leitor l) { return l == null ? "" : l.getNome() + " (" + l.getCpf() + ")"; }
//            @Override public Leitor fromString(String s) { return null; }
//        });
//        cbLivro.setConverter(new StringConverter<>() {
//            @Override public String toString(Livro l) { return l == null ? "" : l.getTitulo() + " — qtd: " + l.getQuantidade(); }
//            @Override public Livro fromString(String s) { return null; }
//        });
//
//        Button btAddLivro = new Button("Adicionar livro");
//        btAddLivro.setOnAction(e -> adicionarLivro());
//
//        Button btRegistrar = new Button("Registrar Empréstimo");
//        btRegistrar.setOnAction(e -> registrar());
//
//        Button btRemover = new Button("Remover Empréstimo");
//        btRemover.setOnAction(e -> remover());
//
//        HBox linhaLeitor = new HBox(10, new Label("Leitor:"), cbLeitor,
//                new Label("Devolução:"), dpDevolucao);
//        HBox linhaLivro = new HBox(10, new Label("Livro:"), cbLivro, btAddLivro);
//
//        configurarTabelaItens();
//        configurarTabelaEmprestimos();
//
//        VBox layout = new VBox(8,
//                linhaLeitor,
//                linhaLivro,
//                new Label("Livros deste empréstimo:"),
//                tabelaItens,
//                btRegistrar,
//                new Separator(),
//                new Label("Empréstimos cadastrados:"),
//                tabelaEmprestimos,
//                btRemover);
//        layout.setPadding(new Insets(12));
//
//        janela.setScene(new Scene(layout, 720, 620));
//        janela.show();
//
//        carregarCombos();
//        atualizarTabelaEmprestimos();
//    }
//
//    private void configurarTabelaItens() {
//        TableColumn<ItemEmprestimo, String> colTitulo = new TableColumn<>("Título");
//        colTitulo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLivro().getTitulo()));
//        tabelaItens.getColumns().add(colTitulo);
//        tabelaItens.setItems(itensAtuais);
//        tabelaItens.setPrefHeight(140);
//    }
//
//    private void configurarTabelaEmprestimos() {
//        TableColumn<Emprestimo, Integer> colCod = new TableColumn<>("Código");
//        colCod.setCellValueFactory(new PropertyValueFactory<>("codigo"));
//
//        TableColumn<Emprestimo, String> colLeitor = new TableColumn<>("Leitor");
//        colLeitor.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLeitor().getNome()));
//
//        TableColumn<Emprestimo, LocalDate> colData = new TableColumn<>("Empréstimo");
//        colData.setCellValueFactory(new PropertyValueFactory<>("dataEmprestimo"));
//
//        TableColumn<Emprestimo, LocalDate> colDev = new TableColumn<>("Devolução prev.");
//        colDev.setCellValueFactory(new PropertyValueFactory<>("dataDevolucaoPrevista"));
//
//        TableColumn<Emprestimo, String> colQtd = new TableColumn<>("Qtd livros");
//        colQtd.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getListaLivros().size())));
//
//        TableColumn<Emprestimo, String> colStatus = new TableColumn<>("Status");
//        colStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isStatus() ? "Ativo" : "Encerrado"));
//
//        tabelaEmprestimos.getColumns().addAll(colCod, colLeitor, colData, colDev, colQtd, colStatus);
//    }
//
//    private void adicionarLivro() {
//        Livro livro = cbLivro.getValue();
//        if (livro == null) {
//            alerta("Selecione um livro para adicionar.");
//            return;
//        }
//        itensAtuais.add(new ItemEmprestimo(livro));
//    }
//
//    private void registrar() {
//        Leitor leitor = cbLeitor.getValue();
//        if (leitor == null) {
//            alerta("Selecione um leitor.");
//            return;
//        }
//        try {
//            Emprestimo emp = new Emprestimo(leitor);
//            emp.getListaLivros().addAll(itensAtuais);
//            if (dpDevolucao.getValue() != null) {
//                emp.setDataDevolucaoPrevista(dpDevolucao.getValue());
//            }
//            controller.cadastrar(emp);
//
//            itensAtuais.clear();
//            cbLeitor.setValue(null);
//            dpDevolucao.setValue(LocalDate.now().plusDays(30));
//            carregarCombos();
//            atualizarTabelaEmprestimos();
//        } catch (IllegalArgumentException ex) {
//            alerta(ex.getMessage());
//        }
//    }
//
//    private void remover() {
//        Emprestimo sel = tabelaEmprestimos.getSelectionModel().getSelectedItem();
//        if (sel == null) {
//            alerta("Selecione um empréstimo para remover.");
//            return;
//        }
//        controller.apagar(sel);
//        atualizarTabelaEmprestimos();
//    }
//
//    private void carregarCombos() {
//        cbLeitor.setItems(FXCollections.observableArrayList(controller.listarLeitores()));
//        cbLivro.setItems(FXCollections.observableArrayList(controller.listarLivros()));
//    }
//
//    private void atualizarTabelaEmprestimos() {
//        tabelaEmprestimos.setItems(FXCollections.observableArrayList(controller.listar()));
//    }
//
//    private void alerta(String mensagem) {
//        new Alert(Alert.AlertType.WARNING, mensagem).showAndWait();
//    }
}
