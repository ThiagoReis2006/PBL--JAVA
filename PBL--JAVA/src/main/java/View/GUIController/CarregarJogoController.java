package View.GUIController;

import Model.DadosInvalidosException;
import Model.FalhaCarregamentoException;
import Model.GerenciadorSave;
import Model.SalvarDados;
import app.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CarregarJogoController {

    @FXML private Label lblSlot1;
    @FXML private Label lblSlot2;
    @FXML private Label lblSlot3;
    @FXML private Button btnSlot1;
    @FXML private Button btnSlot2;
    @FXML private Button btnSlot3;
    @FXML private Button btnVoltar;

    private final GerenciadorSave gerenciador = new GerenciadorSave();

    @FXML
    public void initialize() {
        atualizarSlot(1, lblSlot1, btnSlot1);
        atualizarSlot(2, lblSlot2, btnSlot2);
        atualizarSlot(3, lblSlot3, btnSlot3);
    }

    private void atualizarSlot(int slot, Label lbl, Button btn) {
        if (!gerenciador.slotTemSave(slot)) {
            lbl.setText("Slot " + slot + " — vazio");
            btn.setDisable(true);
            return;
        }
        try {
            SalvarDados d = gerenciador.carregar(slot);
            lbl.setText("Slot " + slot + " — " + d.getNomeJogador()
                    + "\nCapítulo: " + d.getNomeCapitulo()
                    + "\n" + d.getResumoProgressao()
                    + "\nData: " + d.getDataHora());
            btn.setDisable(false);
        } catch (FalhaCarregamentoException | DadosInvalidosException e) {
            lbl.setText("Slot " + slot + " — save ilegível ou corrompido");
            btn.setDisable(true);
        }
    }

    @FXML public void acaoCarregarSlot1(ActionEvent e) { CarregamentoHelper.carregarSlotEIniciar(1); }
    @FXML public void acaoCarregarSlot2(ActionEvent e) { CarregamentoHelper.carregarSlotEIniciar(2); }
    @FXML public void acaoCarregarSlot3(ActionEvent e) { CarregamentoHelper.carregarSlotEIniciar(3); }

    @FXML
    public void acaoVoltarMenu(ActionEvent event) {
        MainApp.mostrarMenuInicial();
    }
}