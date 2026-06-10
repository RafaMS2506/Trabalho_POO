package edu.curso.boundary;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TelaMenuPrincipal extends Application {

    private BorderPane pane = new BorderPane();
    private Pane livroPane = new TelaLivro().render();
    private Pane leitorPane = new TelaLeitor().render();
    private Pane emprestimoPane = new TelaEmprestimo().render();
    private Pane autorPane = new TelaAutor().render();

    public void start(Stage stage) {
        Scene scene = new Scene(pane, 800, 600);
        pane.setCenter(livroPane);

        MenuBar menuBar = new MenuBar();

        Menu mnuArquivo = new Menu("Arquivo");
        Menu mnuCadastro = new Menu("Cadastro");
        Menu mnuAjuda = new Menu("Ajuda");

        MenuItem mnuLivroItem = new MenuItem("Livros");
        MenuItem mnuLeitorItem = new MenuItem("Leitores");
        MenuItem mnuEmprestimoItem = new MenuItem("Emprestimo");
        MenuItem mnuAutorItem = new MenuItem("Autores");

        menuBar.getMenus().addAll( mnuArquivo, mnuCadastro, mnuAjuda);

        mnuCadastro.getItems().addAll( mnuLivroItem, mnuLeitorItem, mnuEmprestimoItem, mnuAutorItem );

        pane.setTop( menuBar );

        mnuLivroItem.setOnAction( e -> pane.setCenter( livroPane ) );
        mnuLeitorItem.setOnAction( e -> pane.setCenter( leitorPane ) );
        mnuEmprestimoItem.setOnAction( e -> pane.setCenter( emprestimoPane ) );
        mnuAutorItem.setOnAction( e -> pane.setCenter( autorPane ) );

        stage.setScene(scene);
        stage.setTitle("Filmoteca");
        stage.show();

    }
}
