package View.GUIController;

import Model.*;
import app.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class TelaPrincipalController {

    @FXML private ImageView imgCenario;
    @FXML private ImageView imgPersonagem;
    @FXML private ScrollPane scrollDialogo;
    @FXML private Label lblTextoHistoria;
    @FXML private VBox vboxEscolhas;
    @FXML private Button btnMenu;
    @FXML private Button btnSalvar;
    @FXML private Button btnInventario;
    @FXML private ProgressBar barProgresso;
    @FXML private Label lblCapitulo;

    private final RepositorioCenas repositorio = new RepositorioCenas();
    private final GerenciadorSave gerenciador = new GerenciadorSave();
    private CenasIds ultimaCenaExibida;

    @FXML
    public void initialize() {
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

        ultimaCenaExibida = SessaoJogo.getInstancia().getCenaAtual();

        aplicarConfiguracoes();
        configurarAtalhosTeclado();
        exibirCenaAtual();
    }


    private void aplicarConfiguracoes() {
        Configuracoes config = gerenciador.carregarDadosGlobais().getConfiguracoes();

        boolean mostrarHud = !config.isOcultarUHD();
        alterarVisibilidadeBotao(btnMenu, mostrarHud);
        alterarVisibilidadeBotao(btnSalvar, mostrarHud);
        alterarVisibilidadeBotao(btnInventario, mostrarHud);
        if (barProgresso != null) {
            barProgresso.setVisible(mostrarHud);
            barProgresso.setManaged(mostrarHud);
        }
        if (lblCapitulo != null) {
            lblCapitulo.setVisible(mostrarHud);
            lblCapitulo.setManaged(mostrarHud);
        }

        String corFundoTela;
        String corFundoCaixa;
        switch (config.getTemaVisual()) {
            case 2 -> {
                corFundoTela = "#121212";
                corFundoCaixa = "#1e1e1e";
            }
            case 3 -> {
                corFundoTela = "#f1f2f6";
                corFundoCaixa = "#ffffff";
            }
            default -> {
                corFundoTela = "#2f3542";
                corFundoCaixa = "#3d4452";
            }
        }

        String corTexto = switch (config.getCorLetras()) {
            case "Verde" -> "#2ed573";
            case "Amarelo" -> "#eccc68";
            case "Azul" -> "#70a1ff";
            default -> (config.getTemaVisual() == 3) ? "#2f3542" : "#ffffff";
        };

        if (scrollDialogo.getParent() instanceof Pane raiz) {
            raiz.setStyle("-fx-background-color: " + corFundoTela + ";");
        }
        scrollDialogo.setStyle("-fx-background: " + corFundoCaixa + "; -fx-background-color: " + corFundoCaixa + ";");
        lblTextoHistoria.setStyle("-fx-text-fill: " + corTexto + ";");
        if (lblCapitulo != null) {
            lblCapitulo.setStyle("-fx-text-fill: " + corTexto + ";");
        }
    }

    private void alterarVisibilidadeBotao(Button botao, boolean visivel) {
        if (botao != null) {
            botao.setVisible(visivel);
            botao.setManaged(visivel);
        }
    }

    private void configurarAtalhosTeclado() {
        scrollDialogo.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    if (event.getCode() == KeyCode.ESCAPE) {
                        MainApp.mostrarMenuInicial();
                        event.consume();
                    } else if (event.isControlDown() && event.getCode() == KeyCode.S) {
                        salvarJogo(false);
                        event.consume();
                    } else if (event.getCode() == KeyCode.I) {
                        mostrarInventario();
                        event.consume();
                    }
                });
            }
        });
    }


    private void limparBotoesEscolha() {
        vboxEscolhas.getChildren().removeIf(n -> n != btnMenu && n != btnSalvar && n != btnInventario);
    }

    private void exibirCenaAtual() {
        SessaoJogo sessao = SessaoJogo.getInstancia();
        Cena cena = repositorio.buscar(sessao.getCenaAtual());

        lblTextoHistoria.setText(cena.getTexto());
        atualizarProgresso(cena.getId());
        scrollDialogo.setVvalue(0);
        limparBotoesEscolha();

        verificarAutosave(cena);
        ultimaCenaExibida = cena.getId();

        if (cena.eFim()) {
            registrarFinal(cena);
            adicionarBotao("Voltar ao Menu", () -> MainApp.mostrarMenuInicial());
            return;
        }

        if (cena.getEscolhas().isEmpty()) {
            CenasIds proxima = cena.destinoPadrao;
            adicionarBotao("[Continuar]", () -> {
                sessao.setCenaAtual(proxima);
                exibirCenaAtual();
            });
            return;
        }

        for (Escolha escolha : cena.getEscolhas()) {
            adicionarBotao(escolha.getTexto(), () -> processarEscolha(escolha));
        }
    }

    private void atualizarProgresso(CenasIds id) {
        if (barProgresso == null || lblCapitulo == null) return;

        int capitulo = 1;
        String nome = id.name();
        if (nome.startsWith("CAP")) {
            int fimNumero = nome.indexOf('_');
            if (fimNumero > 3) {
                try {
                    capitulo = Integer.parseInt(nome.substring(3, fimNumero));
                } catch (NumberFormatException ignored) {
                }
            }
        }

        double progresso = Math.min(1.0, Math.max(0.0, capitulo / 10.0));
        barProgresso.setProgress(progresso);
        lblCapitulo.setText("Capítulo " + capitulo + "/10");
    }

    private void processarEscolha(Escolha escolha) {
        SessaoJogo sessao = SessaoJogo.getInstancia();
        IntegracaoJogo jogo = sessao.getJogoAtual();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream saidaOriginal = System.out;
        try (PrintStream captura = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setOut(captura);
            escolha.aplicarEfeito(jogo);
        } finally {
            System.setOut(saidaOriginal);
        }

        CenasIds proximaCena = escolha.getDestino(jogo);
        sessao.setCenaAtual(proximaCena);

        String falaReacao = buffer.toString(StandardCharsets.UTF_8).trim();
        if (!falaReacao.isEmpty()) {
            lblTextoHistoria.setText(falaReacao);
            scrollDialogo.setVvalue(0);
            limparBotoesEscolha();
            adicionarBotao("[Continuar]", this::exibirCenaAtual);
        } else {
            exibirCenaAtual();
        }
    }

    private void adicionarBotao(String texto, Runnable acao) {
        Button b = new Button(texto);
        b.setWrapText(true);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setOnAction(e -> acao.run());
        vboxEscolhas.getChildren().add(b);
    }


    @FXML
    public void acaoAbrirInventario(ActionEvent event) {
        mostrarInventario();
    }

    private void mostrarInventario() {
        IntegracaoJogo jogo = SessaoJogo.getInstancia().getJogoAtual();
        PersonagemPrincipal toin = jogo.getProtagonista();
        List<Item> itens = jogo.getInventario().getItens();

        StringBuilder sb = new StringBuilder();

        sb.append("=== PILARES DA BANDA ===\n");
        sb.append("• Harmonia: ").append(jogo.getBanda().getHarmonia()).append("\n");
        sb.append("• Essência: ").append(jogo.getBanda().getEssencia()).append("\n");
        sb.append("• Grana: R$ ").append(jogo.getBanda().getGrana()).append("\n\n");

        sb.append("=== ATRIBUTOS DE TOIN ===\n");
        sb.append("• Carisma de Botequim: ").append(toin.getCarisma()).append("\n");
        sb.append("• Samba no Pé: ").append(toin.getSamba()).append("\n");
        sb.append("• Ouvido Absoluto: ").append(toin.getOuvido()).append("\n\n");

        sb.append("=== RELACIONAMENTOS ===\n");
        sb.append("• Betinho: ").append(jogo.getBetinho().getNivelRelacionamento()).append("\n");
        sb.append("• Jorge: ").append(jogo.getJorge().getNivelRelacionamento()).append("\n");
        sb.append("• Manuela: ").append(jogo.getManuela().getNivelRelacionamento()).append("\n\n");

        sb.append("=== MOCHILA (").append(itens.size()).append(itens.size() == 1 ? " item" : " itens").append(") ===\n");
        if (itens.isEmpty()) {
            sb.append("• Nenhum item carregado no momento.");
        } else {
            for (Item item : itens) {
                sb.append("• ").append(item.getNome()).append(": ")
                        .append(item.getDescricao()).append("\n");
            }
        }

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Status e Inventário");
        alerta.setHeaderText("Ficha da Banda & Mochila de Toin");
        alerta.setContentText(sb.toString().trim());
        alerta.getDialogPane().setPrefWidth(500);
        alerta.showAndWait();
    }


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


    @FXML
    public void acaoAbrirMenu(ActionEvent event) {
        MainApp.mostrarMenuInicial();
    }

    public void atualizarTexto(String texto) {
        lblTextoHistoria.setText(texto);
    }
}