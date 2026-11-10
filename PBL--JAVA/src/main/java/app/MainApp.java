package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("O Show Tem de Continuar");

        // Abre o menu inicial.
        mostrarMenuInicial();

        primaryStage.show();
    }

    // Método para carregar a tela do menu.
    public static void mostrarMenuInicial() {
        try {
            Parent root = FXMLLoader.load(MainApp.class.getResource("/MenuInicial.fxml"));
            primaryStage.setScene(new Scene(root, 800, 600));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para carregar a tela principal (cena default).
    public static void mostrarTelaPrincipal() {
        try {
            Parent root = FXMLLoader.load(MainApp.class.getResource("/TelaPrincipal.fxml"));
            primaryStage.setScene(new Scene(root, 800, 600));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}