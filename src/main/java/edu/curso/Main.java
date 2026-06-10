package edu.curso;

import edu.curso.boundary.TelaMenuPrincipal;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage palco) {
        new TelaMenuPrincipal().mostrar(palco);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
