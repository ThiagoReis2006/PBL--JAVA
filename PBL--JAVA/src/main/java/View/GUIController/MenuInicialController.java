package View.GUIController;

import Model.Configuracoes;
import Model.DadosGlobais;
import Model.FalhaSalvamentoException;
import Model.GerenciadorSave;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class MenuInicialController {

    @FXML private VBox vboxMenu;

    @FXML private Button btnNovaPartida;
    @FXML private Button btnContinuar;
    @FXML private Button btnInstrucoes;
    @FXML private Button btnCarregar;
    @FXML private Button btnDeletar;
    @FXML private Button btnCreditos;
    @FXML private Button btnSair;
    @FXML private Button btnFinais;
    @FXML private Button btnOpcoes;

    @FXML
    public void initialize() {
        atualizarTextoBotaoFinais();
    }

    private void atualizarTextoBotaoFinais() {
        if (btnFinais != null) {
            DadosGlobais globais = new GerenciadorSave().carregarDadosGlobais();
            if (globais.isCenaSecretaDesbloqueada()) {
                btnFinais.setText("Epílogo Secreto (DESBLOQUEADO!)");
            } else {
                btnFinais.setText("Finais (" + globais.getFinaisAlcancados().size() + "/3)");
            }
        }
    }

    @FXML
    public void acaoNovaPartida(ActionEvent event) {
        System.out.println("Iniciando nova partida.");
        app.MainApp.mostrarCriacaoProtagonista();
    }

    @FXML
    public void acaoContinuar(ActionEvent event) {
        int slot = new GerenciadorSave().slotMaisRecente();
        if (slot == -1) {
            CarregamentoHelper.alerta(Alert.AlertType.INFORMATION,
                    "Continuar Partida", "Nenhum progresso encontrado.");
            return;
        }
        CarregamentoHelper.carregarSlotEIniciar(slot);
    }

    @FXML
    public void acaoInstrucoes(ActionEvent event) {
        String instrucoes = "Seja bem-vindo ao jogo O Show Tem de Continuar!\n\n" +
                "Neste RPG narrativo, cada decisão sua moldará o destino de Toin e a trajetória da sua banda rumo ao grande Festival de Pagode.\n\n" +
                "O jogo é orientado por quatro pilares centrais:\n\n" +
                "• Harmonia: representa a saúde e a união do grupo (se chegar a zero, é Game Over);\n" +
                "• Essência: mede o quanto o seu som se mantém fiel às raízes do pagode tradicional ou se rende ao mercado pop comercial;\n" +
                "• Grana: representa o seu principal recurso, necessária para investimentos durante a história;\n" +
                "• Relacionamentos: vínculos com os integrantes, que liberam caminhos únicos ou provocam deserções.\n\n" +
                "Além disso, as habilidades que você escolheu no início do jogo – como sua lábia, sua presença de palco e sua técnica no cavaquinho – " +
                "serão testadas automaticamente durante as apresentações e conflitos da história. Administre bem a sua Grana para não cair nas garras de agiotas e " +
                "pense com cuidado antes de cada escolha: a fama e o respeito da comunidade dependem da sua capacidade de equilibrar a razão, o coração e o samba no pé.";

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Instruções");
        alerta.setHeaderText("Como Jogar - O Show Tem de Continuar");
        alerta.setContentText(instrucoes);
        alerta.getDialogPane().setPrefWidth(520);
        alerta.showAndWait();
    }

    @FXML
    public void acaoCarregar(ActionEvent event) {
        app.MainApp.mostrarCarregarJogo();
    }

    @FXML
    public void acaoDeletar(ActionEvent event) {
        System.out.println("Abrindo gerenciador para deletar saves.");
    }

    @FXML
    public void acaoCreditos(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Créditos");
        alerta.setHeaderText("O Show Tem de Continuar");
        alerta.setContentText("Desenvolvido por:\n\n• Felipe Vieira Aquino\n• Thiago Marques Reis\n");
        alerta.showAndWait();
    }

    @FXML
    public void acaoSair(ActionEvent event) {
        System.out.println("Encerrando o jogo.");
        System.exit(0);
    }

    @FXML
    public void acaoFinais(ActionEvent event) {
        GerenciadorSave ger = new GerenciadorSave();
        DadosGlobais globais = ger.carregarDadosGlobais();

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Finais e Conteúdo Extra");

        if (globais.isCenaSecretaDesbloqueada()) {
            alerta.setHeaderText("EPÍLOGO SECRETO: 5 ANOS DEPOIS");
            alerta.setContentText(
                    "O tempo passou, mas algumas coisas nunca mudam. Independentemente de terem assinado com a " +
                            "gravadora gigante ou ficado no circuito independente, hoje é o aniversário de fundação da banda.\n\n" +
                            "O bar da Dona Gertrudes está de portas fechadas para o público geral, reservado apenas para vocês. " +
                            "Léo tenta (sem sucesso) ensinar passos de dança para Jorge, enquanto Betinho e Manuela conferem os lucros " +
                            "da última turnê. Toin senta num canto, afina o mesmo cavaquinho que trouxe de Feira de Santana, e sorri. " +
                            "O show continua, e a família está completa.\n\n" +
                            "Obrigado por jogar 'O Show Tem de Continuar' e descobrir todos os finais!"
            );
        } else {
            List<String> finais = globais.getFinaisAlcancados();
            StringBuilder sb = new StringBuilder();
            sb.append("Você precisa alcançar os 3 finais principais para liberar o Epílogo Secreto.\n\n");
            sb.append("Finais descobertos até agora (").append(finais.size()).append("/3):\n");

            if (finais.isEmpty()) {
                sb.append("• Nenhum final alcançado ainda.");
            } else {
                for (String f : finais) {
                    sb.append("• ").append(f).append("\n");
                }
            }

            alerta.setHeaderText("Conteúdo Bloqueado");
            alerta.setContentText(sb.toString());
        }

        alerta.getDialogPane().setPrefWidth(520);
        alerta.showAndWait();
    }

    @FXML
    public void acaoOpcoes(ActionEvent event) {
        GerenciadorSave ger = new GerenciadorSave();
        DadosGlobais globais = ger.carregarDadosGlobais();
        Configuracoes config = globais.getConfiguracoes();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Configurações");
        dialog.setHeaderText("Preferências do Jogo (Salvas Automaticamente)");

        CheckBox chkOcultarHud = new CheckBox("Ocultar HUD (Botões extras durante a partida)");
        chkOcultarHud.setSelected(config.isOcultarUHD());

        ComboBox<String> cbTema = new ComboBox<>();
        cbTema.getItems().addAll("1 - Neutro", "2 - Escuro", "3 - Claro");
        cbTema.getSelectionModel().select(Math.max(0, Math.min(2, config.getTemaVisual() - 1)));

        ComboBox<String> cbCor = new ComboBox<>();
        cbCor.getItems().addAll("Branco", "Verde", "Amarelo", "Azul");
        cbCor.setValue(config.getCorLetras());

        VBox conteudo = new VBox(12,
                chkOcultarHud,
                new Label("Tema Visual:"), cbTema,
                new Label("Cor das Letras:"), cbCor
        );
        conteudo.setPadding(new Insets(15));

        dialog.getDialogPane().setContent(conteudo);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                config.setOcultarUHD(chkOcultarHud.isSelected());
                config.setTemaVisual(cbTema.getSelectionModel().getSelectedIndex() + 1);
                config.setCorLetras(cbCor.getValue());

                try {
                    ger.salvarDadosGlobais(globais);
                    Alert ok = new Alert(Alert.AlertType.INFORMATION, "Configurações salvas com sucesso!");
                    ok.setHeaderText(null);
                    ok.showAndWait();
                } catch (FalhaSalvamentoException e) {
                    Alert erro = new Alert(Alert.AlertType.ERROR, "Erro ao salvar configurações: " + e.getMessage());
                    erro.showAndWait();
                }
            }
        });
    }
}