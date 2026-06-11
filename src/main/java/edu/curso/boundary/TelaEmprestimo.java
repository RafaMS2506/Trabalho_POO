package edu.curso.boundary;

import edu.curso.controller.EmprestimoController;
import edu.curso.entity.Emprestimo;
import edu.curso.entity.ItemEmprestimo;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TelaEmprestimo implements Tela{

    private final EmprestimoController controller = new EmprestimoController();

    private final ComboBox<String> cbLeitor = new ComboBox<>();
    private final ComboBox<String> cbLivro = new ComboBox<>();
    private final DatePicker dataEmprestimo = new DatePicker();
    private final DatePicker dpDevolucao = new DatePicker(LocalDate.now().plusDays(30));
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

        // Cada combo recarrega APENAS a sua própria lista ao ser aberto, preservando
        // a seleção. Antes ambos chamavam carregarCombos(), o que reconstruía também a
        // lista do outro combo e zerava a seleção do leitor ao abrir o de livro.
        cbLeitor.setOnShowing(ev -> controller.recarregarLeitores());
        cbLivro.setOnShowing(ev -> controller.recarregarLivros());



        Bindings.bindBidirectional(dataEmprestimo.valueProperty(), controller.dataEmprestimoProperty());
        Bindings.bindBidirectional(dpDevolucao.valueProperty(), controller.dpDevolucaoProperty());

        TableColumn<ItemEmprestimo, String> colItemLivro = new TableColumn<>("Livro");
        colItemLivro.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(
                        itemData.getValue().getLivro() != null
                                ? itemData.getValue().getLivro().getTitulo()
                                : ""
                )
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
            // Se um empréstimo está selecionado (modo edição), confirma antes de
            // sobrescrevê-lo. Evita perder um empréstimo achando que está criando outro.
            if (controller.isEdicao()) {
                Alert conf = new Alert(Alert.AlertType.CONFIRMATION,
                        "Você está editando um empréstimo já existente. Salvar irá ATUALIZÁ-LO.\n" +
                        "Para registrar um NOVO empréstimo, clique em \"Limpar\" antes.\n\nDeseja atualizar?",
                        ButtonType.YES, ButtonType.NO);
                conf.setTitle("Confirmar atualização");
                Optional<ButtonType> r = conf.showAndWait();
                if (r.isEmpty() || r.get() != ButtonType.YES) {
                    return;
                }
            }
            if (controller.salvar()) {
                new Alert(Alert.AlertType.INFORMATION, "Empréstimo gravado com sucesso!").showAndWait();
                tabelaEmprestimos.getSelectionModel().clearSelection();
            }
        });

        // "Limpar" reinicia o formulário em modo de inserção (codigo = 0), limpa os combos
        // e tira a seleção da tabela, para o próximo Salvar ser um INSERT e não um UPDATE.
        btLimpar.setOnAction((e) -> {
            controller.limparCampos();
            tabelaEmprestimos.getSelectionModel().clearSelection();
        });

        btPesquisar.setOnAction((e) -> {
            controller.pesquisar();
        });

        paneCampos.add(btSalvar, 0, 5);
        paneCampos.add(btLimpar, 1, 5);
        paneCampos.add(btPesquisar, 2, 5);

        TableColumn<Emprestimo, String> colLeitor = new TableColumn<>("Leitor");
        colLeitor.setCellValueFactory(
                itemData -> new ReadOnlyStringWrapper(
                        itemData.getValue().getLeitor() != null
                                ? itemData.getValue().getLeitor().getNome()
                                : ""
                )
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

        paneCampos.add(new Label("Livros no empréstimo:"), 0, 4);
        paneCampos.add(tabelaItens,                        1, 4, 2, 1);

        return panePrincipal;
    }
}
