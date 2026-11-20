package View.GUIController;

import Model.*;
import app.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class TelaPrincipalController {

    @FXML private ImageView imgCenario;
    @FXML private ImageView imgPersonagem;
    @FXML private ScrollPane scrollDialogo;
    @FXML private Label lblTextoHistoria;
    @FXML private VBox vboxEscolhas;
    @FXML private Button btnMenu;
    @FXML private Button btnSalvar;

    private final RepositorioCenas repositorio = new RepositorioCenas();
    private final GerenciadorSave gerenciador = new GerenciadorSave();
    private CenasIds ultimaCenaExibida;

    @FXML
    public void initialize() {
        // Move o Label para ser o conteúdo direto do ScrollPane, para que o texto longo role
        if (lblTextoHistoria.getParent() instanceof Pane pai) {
            pai.getChildren().remove(lblTextoHistoria);
        }
        lblTextoHistoria.setWrapText(true);
        lblTextoHistoria.setPrefHeight(Region.USE_COMPUTED_SIZE);
        lblTextoHistoria.setPrefWidth(Region.USE_COMPUTED_SIZE);
        lblTextoHistoria.setMaxWidth(Double.MAX_VALUE);
        lblTextoHistoria.setPadding(new Insets(6));
        scrollDialogo.setContent(lblTextoHistoria);
        scrollDialogo.setFitToWidth(true);

        // Evita re-autosave ao carregar um save que já está num ponto de autosave
        ultimaCenaExibida = SessaoJogo.getInstancia().getCenaAtual();
        exibirCenaAtual();
    }

    // ------------------------- Renderização -------------------------

    private void exibirCenaAtual() {
        SessaoJogo sessao = SessaoJogo.getInstancia();
        Cena cena = repositorio.buscar(sessao.getCenaAtual());

        lblTextoHistoria.setText(cena.getTexto());
        scrollDialogo.setVvalue(0);

        // mantém btnMenu e btnSalvar, remove os botões de escolha antigos
        vboxEscolhas.getChildren().removeIf(n -> n != btnMenu && n != btnSalvar);

        verificarAutosave(cena);
        ultimaCenaExibida = cena.getId();

        if (cena.eFim()) {
            registrarFinal(cena);
            adicionarBotao("Voltar ao Menu", () -> MainApp.mostrarMenuInicial());
            return;
        }

        if (cena.getEscolhas().isEmpty()) {
            // cena automática: o jogador lê e clica para seguir
            CenasIds proxima = cena.destinoPadrao;
            adicionarBotao("[Continuar]", () -> {
                sessao.setCenaAtual(proxima);
                exibirCenaAtual();
            });
            return;
        }

        for (Escolha escolha : cena.getEscolhas()) {
            adicionarBotao(escolha.getTexto(), () -> {
                IntegracaoJogo jogo = sessao.getJogoAtual();
                escolha.aplicarEfeito(jogo);
                sessao.setCenaAtual(escolha.getDestino(jogo));
                exibirCenaAtual();
            });
        }
    }

    private void adicionarBotao(String texto, Runnable acao) {
        Button b = new Button(texto);
        b.setWrapText(true);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setOnAction(e -> acao.run());
        vboxEscolhas.getChildren().add(b);
    }

    // ------------------------- Finais e autosave -------------------------

    private void registrarFinal(Cena cena) {
        DadosGlobais globais = gerenciador.carregarDadosGlobais();
        globais.adicionarFinal(cena.getId().name());
        try {
            gerenciador.salvarDadosGlobais(globais);
        } catch (FalhaSalvamentoException e) {
            CarregamentoHelper.alerta(Alert.AlertType.ERROR, "Erro",
                    "Erro ao guardar progresso global: " + e.getMessage());
        }
    }

    private void verificarAutosave(Cena cena) {
        CenasIds id = cena.getId();
        boolean chegouNaPraca = id == CenasIds.CAP5_PRACA && ultimaCenaExibida != CenasIds.CAP5_PRACA;
        boolean chegouNoFinal = (id == CenasIds.CAP10_FINAL_FALSO
                || id == CenasIds.CAP10_FINAL_ESSENCIALISTA
                || id == CenasIds.CAP10_FINAL_HARMONIOSO) && ultimaCenaExibida != id;

        if (chegouNaPraca || chegouNoFinal) {
            salvarJogo(true);
        }
    }

    @FXML
    public void acaoSalvar(ActionEvent event) {
        salvarJogo(false);
    }

    private void salvarJogo(boolean automatico) {
        SessaoJogo sessao = SessaoJogo.getInstancia();
        IntegracaoJogo jogo = sessao.getJogoAtual();

        int slotDestino = sessao.getSlotAtual();

        if (slotDestino == -1) {
            for (int s = 1; s <= 3; s++) {
                if (!gerenciador.slotTemSave(s)) { slotDestino = s; break; }
            }
            if (slotDestino == -1) {
                slotDestino = gerenciador.slotMaisAntigo();
                Alert conf = new Alert(Alert.AlertType.CONFIRMATION,
                        "Todos os slots estão ocupados. Deseja sobrescrever o Slot " + slotDestino + "?",
                        ButtonType.YES, ButtonType.NO);
                conf.setHeaderText(null);
                Optional<ButtonType> r = conf.showAndWait();
                if (r.isEmpty() || r.get() != ButtonType.YES) return;
            }
            sessao.setSlotAtual(slotDestino);
        }

        String resumo = "Harmonia: " + jogo.getBanda().getHarmonia()
                + " | Grana: R$ " + jogo.getBanda().getGrana()
                + " | Essência: " + jogo.getBanda().getEssencia();

        SalvarDados dados = new SalvarDados("Toin", sessao.getCenaAtual().name(),
                resumo, jogo, sessao.getCenaAtual());
        try {
            gerenciador.salvar(dados, slotDestino);
            CarregamentoHelper.alerta(Alert.AlertType.INFORMATION,
                    automatico ? "Salvamento automático" : "Jogo salvo",
                    "Jogo salvo no slot " + slotDestino + "!");
        } catch (FalhaSalvamentoException e) {
            CarregamentoHelper.alerta(Alert.AlertType.ERROR, "Falha ao salvar", e.getMessage());
        }
    }

    // ------------------------- Navegação -------------------------

    @FXML
    public void acaoAbrirMenu(ActionEvent event) {
        MainApp.mostrarMenuInicial();
    }

    public void atualizarTexto(String texto) {
        lblTextoHistoria.setText(texto);
    }
}