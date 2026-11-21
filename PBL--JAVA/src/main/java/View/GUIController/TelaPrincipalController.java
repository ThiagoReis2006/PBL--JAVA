package View.GUIController;

import Model.*;
import app.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

        if (scrollDialogo.getParent() instanceof Pane raiz) {
            raiz.getStyleClass().removeIf(c -> c.startsWith("tema-") || c.startsWith("letra-"));
            raiz.getStyleClass().add("tema-" + config.getTemaVisual());

            String cor = config.getCorLetras();
            if ("Verde".equals(cor) || "Amarelo".equals(cor) || "Azul".equals(cor)) {
                raiz.getStyleClass().add("letra-" + cor.toLowerCase(Locale.ROOT));
            }
        }
        scrollDialogo.getStyleClass().add("caixa-dialogo");
        lblTextoHistoria.getStyleClass().add("label-historia");
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
        atualizarCenario(cena.getId());
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

    // Guarda as imagens ja carregadas para nao ler o arquivo a cada troca de cena.
    private static final Map<String, Image> CACHE_CENARIOS = new HashMap<>();

    private void atualizarCenario(CenasIds id) {
        if (imgCenario == null) return;
        imgCenario.setFitWidth(800);
        imgCenario.setFitHeight(600);
        imgCenario.setPreserveRatio(false);
        imgCenario.setImage(buscarCenario(id));
    }

    private static Image buscarCenario(CenasIds id) {
        String nome = id.name().toLowerCase(Locale.ROOT);
        while (true) {
            for (String ext : new String[]{".png", ".jpg"}) {
                Image img = carregarImagem("/img/cenarios/" + nome + ext);
                if (img != null) return img;
            }
            int corte = nome.lastIndexOf('_');
            if (corte < 0) break;
            nome = nome.substring(0, corte);
        }
        for (String ext : new String[]{".png", ".jpg"}) {
            Image img = carregarImagem("/img/cenarios/padrao" + ext);
            if (img != null) return img;
        }
        return null;
    }

    private static Image carregarImagem(String caminho) {
        if (CACHE_CENARIOS.containsKey(caminho)) return CACHE_CENARIOS.get(caminho);
        URL url = TelaPrincipalController.class.getResource(caminho);
        Image img = (url == null) ? null : new Image(url.toExternalForm(), 800, 600, false, true);
        CACHE_CENARIOS.put(caminho, img);
        return img;
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
        b.getStyleClass().add("botao-escolha");
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

        double largura = 520;
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Status e Inventário");
        alerta.setHeaderText("Ficha da Banda & Mochila de Toin");

        Label lblConteudo = new Label(sb.toString().trim());
        lblConteudo.setWrapText(true);
        lblConteudo.setMinHeight(Region.USE_PREF_SIZE);
        lblConteudo.setPrefHeight(Region.USE_COMPUTED_SIZE);
        lblConteudo.setMaxWidth(largura - 40);

        ScrollPane scroll = new ScrollPane(lblConteudo);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportWidth(largura - 30);
        scroll.setPrefViewportHeight(320);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        alerta.getDialogPane().setContent(scroll);
        alerta.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alerta.getDialogPane().setPrefWidth(largura);
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