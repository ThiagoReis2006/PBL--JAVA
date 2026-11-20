package View.GUIController;

import Model.DadosInvalidosException;
import Model.FalhaCarregamentoException;
import Model.GerenciadorSave;
import Model.SalvarDados;
import Model.SessaoJogo;
import app.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.util.Optional;

public class DeletarSavesController {

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
        atualizarTodosSlots();
    }

    private void atualizarTodosSlots() {
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
            btn.setDisable(false);
        }
    }

    private void confirmarEDeletar(int slot) {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Deseja realmente apagar o save do Slot " + slot + "?");
        confirmacao.setContentText("Esta ação não pode ser desfeita.");

        Optional<ButtonType> resposta = confirmacao.showAndWait();
        if (resposta.isPresent() && resposta.get() == ButtonType.OK) {
            boolean apagou = gerenciador.deletar(slot);
            if (apagou) {
                if (SessaoJogo.getInstancia().getSlotAtual() == slot) {
                    SessaoJogo.getInstancia().setSlotAtual(-1);
                }
                CarregamentoHelper.alerta(Alert.AlertType.INFORMATION,
                        "Save Deletado", "O Slot " + slot + " foi apagado com sucesso!");
                atualizarTodosSlots();
            } else {
                CarregamentoHelper.alerta(Alert.AlertType.ERROR,
                        "Erro", "Não foi possível apagar o Slot " + slot + ".");
            }
        }
    }

    @FXML public void acaoDeletarSlot1(ActionEvent e) { confirmarEDeletar(1); }
    @FXML public void acaoDeletarSlot2(ActionEvent e) { confirmarEDeletar(2); }
    @FXML public void acaoDeletarSlot3(ActionEvent e) { confirmarEDeletar(3); }

    @FXML
    public void acaoVoltarMenu(ActionEvent event) {
        MainApp.mostrarMenuInicial();
    }
}