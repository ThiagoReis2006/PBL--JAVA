package app;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("O Show Tem de Continuar");

        // Todo Alert/Dialog abre numa janela propria: aplica o tema nela tambem.
        Window.getWindows().addListener((ListChangeListener<Window>) mudanca -> {
            while (mudanca.next()) {
                if (!mudanca.wasAdded()) continue;
                for (Window janela : mudanca.getAddedSubList()) {
                    Scene cena = janela.getScene();
                    if (cena != null && cena.getRoot() instanceof DialogPane) {
                        aplicarTema(cena);
                    }
                }
            }
        });

        // Abre o menu inicial.
        mostrarMenuInicial();

        primaryStage.show();
    }

    private static void trocarTela(String caminhoFxml) {
        try {
            Parent root = FXMLLoader.load(MainApp.class.getResource(caminhoFxml));
            Scene cena = new Scene(root, 800, 600);
            aplicarTema(cena);
            primaryStage.setScene(cena);
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao carregar FXML: " + caminhoFxml, e);
        }
    }

    private static void aplicarTema(Scene cena) {
        String css = MainApp.class.getResource("/css/tema.css").toExternalForm();
        if (!cena.getStylesheets().contains(css)) {
            cena.getStylesheets().add(css);
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

    public static void mostrarDeletarSaves() {
        trocarTela("/DeletarSaves.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}