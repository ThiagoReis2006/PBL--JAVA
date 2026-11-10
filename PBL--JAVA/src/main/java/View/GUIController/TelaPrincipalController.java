package View.GUIController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;

public class TelaPrincipalController {

    @FXML
    private ImageView imgCenario;

    @FXML
    private ImageView imgPersonagem;

    @FXML
    private ScrollPane scrollDialogo;

    @FXML
    private Label lblTextoHistoria;

    @FXML
    private VBox vboxEscolhas;

    @FXML
    private Button btnMenu;

    @FXML
    public void acaoAbrirMenu(ActionEvent event) {
        System.out.println("Voltando para o menu principal.");
        app.MainApp.mostrarMenuInicial();
    }

    // Método para atualizar o texto da história em tempo de execução.
    public void atualizarTexto(String texto) {
        lblTextoHistoria.setText(texto);
    }
}