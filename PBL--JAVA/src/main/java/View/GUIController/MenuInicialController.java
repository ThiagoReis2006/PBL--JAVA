package View.GUIController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;

public class MenuInicialController {

    @FXML
    private VBox vboxMenu;

    // Botões mapeados do Scene Builder.
    @FXML
    private Button btnNovaPartida;
    @FXML
    private Button btnContinuar;
    @FXML
    private Button btnInstrucoes;
    @FXML
    private Button btnCarregar;
    @FXML
    private Button btnDeletar;
    @FXML
    private Button btnCreditos;
    @FXML
    private Button btnSair;
    @FXML
    private Button btnFinais;
    @FXML
    private Button btnOpcoes;

    // Ações de cada botão.
    @FXML
    public void acaoNovaPartida(ActionEvent event) {
        System.out.println("Iniciando nova partida.");
        app.MainApp.mostrarTelaPrincipal();
    }

    @FXML
    public void acaoContinuar(ActionEvent event) {
        System.out.println("Continuando partida anterior.");
    }

    @FXML
    public void acaoInstrucoes(ActionEvent event) {
        System.out.println("Abrindo instruções.");
    }

    @FXML
    public void acaoCarregar(ActionEvent event) {
        System.out.println("Abrindo gerenciador de saves.");
    }

    @FXML
    public void acaoDeletar(ActionEvent event) {
        System.out.println("Abrindo gerenciador para deletar saves.");
    }

    @FXML
    public void acaoCreditos(ActionEvent event) {
        System.out.println("Mostrando os créditos...");
    }

    @FXML
    public void acaoSair(ActionEvent event) {
        System.out.println("Encerrando o jogo.");
        System.exit(0);
    }

    @FXML
    public void acaoFinais(ActionEvent event) {
        System.out.println("Acessando conteúdo desbloqueável.");
    }

    @FXML
    public void acaoOpcoes(ActionEvent event) {
        System.out.println("Abrindo configurações.");
    }
}