package View.GUIController;

import Model.DadosInvalidosException;
import Model.FalhaCarregamentoException;
import Model.GerenciadorSave;
import Model.SalvarDados;
import Model.SessaoJogo;
import app.MainApp;
import javafx.scene.control.Alert;

public final class CarregamentoHelper {

    private CarregamentoHelper() {}

    /** Carrega o slot na SessaoJogo e abre a tela principal. Retorna true se deu certo. */
    public static boolean carregarSlotEIniciar(int slot) {
        GerenciadorSave ger = new GerenciadorSave();
        try {
            SalvarDados dados = ger.carregar(slot);

            if (dados.getEstadoJogo() == null || dados.getIdCenaAtual() == null) {
                throw new DadosInvalidosException(
                        "O save do Slot " + slot + " está incompleto (estado ou cena ausente).");
            }

            SessaoJogo.getInstancia().carregarSessao(dados, slot);
            MainApp.mostrarTelaPrincipal();
            return true;

        } catch (FalhaCarregamentoException e) {
            alerta(Alert.AlertType.WARNING, "Falha ao carregar", e.getMessage());
        } catch (DadosInvalidosException e) {
            alerta(Alert.AlertType.ERROR, "Save corrompido", e.getMessage());
        }
        return false;
    }

    public static void alerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(mensagem);
        a.showAndWait();
    }
}