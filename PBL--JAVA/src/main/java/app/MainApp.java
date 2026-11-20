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

    private static void trocarTela(String caminhoFxml) {
        try {
            Parent root = FXMLLoader.load(MainApp.class.getResource(caminhoFxml));
            primaryStage.setScene(new Scene(root, 800, 600));
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao carregar FXML: " + caminhoFxml, e);
        }
    }

    public static void mostrarMenuInicial() {
        trocarTela("/MenuInicial.fxml");
    }

    public static void mostrarTelaPrincipal() {
        trocarTela("/TelaPrincipal.fxml");
    }

    public static void mostrarCriacaoProtagonista() {
        trocarTela("/CriacaoProtagonista.fxml");
    }

    public static void mostrarCarregarJogo() {
        trocarTela("/CarregarJogo.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}