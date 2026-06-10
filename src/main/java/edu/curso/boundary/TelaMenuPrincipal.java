package edu.curso.boundary;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TelaMenuPrincipal {

    public void mostrar(Stage palco) {
        palco.setTitle("Biblioteca - Menu Principal");

        Button btAutor = new Button("Autores");
        Button btEmprestimo = new Button("Empréstimos");

        btAutor.setOnAction(e -> new TelaAutor().mostrar());
        btEmprestimo.setOnAction(e -> new TelaEmprestimo().mostrar());

        btAutor.setMaxWidth(Double.MAX_VALUE);
        btEmprestimo.setMaxWidth(Double.MAX_VALUE);

        VBox layout = new VBox(10, new Label("Sistema de Biblioteca"),
                btAutor, btEmprestimo);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        palco.setScene(new Scene(layout, 300, 240));
        palco.show();
    }
}
