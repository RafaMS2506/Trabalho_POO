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
        Button btnLivro = new Button("Livros");
        Button btnLeitor = new Button("Leitores");
        Button btnEmprestimo = new Button("Emprestimos");
        Button btnAutor = new Button("Autores");

        ToolBar toolBar = new ToolBar(
                btnLivro,
                btnLeitor,
                btnEmprestimo,
                btnAutor
        );

        pane.setTop(toolBar);

        btnLivro.setOnAction(e -> pane.setCenter(livroPane));
        btnLeitor.setOnAction(e -> pane.setCenter(leitorPane));
        btnEmprestimo.setOnAction(e -> pane.setCenter(emprestimoPane));
        btnAutor.setOnAction(e -> pane.setCenter(autorPane));

        stage.setScene(scene);
        stage.setTitle("Biblioteca");
        stage.show();
    }
}
