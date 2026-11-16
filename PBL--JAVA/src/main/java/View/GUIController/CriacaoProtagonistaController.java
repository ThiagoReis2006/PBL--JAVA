package View.GUIController;

import Model.IntegracaoJogo;
import Model.PersonagemPrincipal;
import Model.SessaoJogo;
import app.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CriacaoProtagonistaController {

    @FXML private Label lblPrologo;
    @FXML private Label lblPontosRestantes;
    @FXML private Spinner<Integer> spnCarisma;
    @FXML private Spinner<Integer> spnSamba;
    @FXML private Spinner<Integer> spnOuvido;
    @FXML private Button btnVoltar;
    @FXML private Button btnIniciar;

    @FXML
    public void initialize() {
        lblPrologo.setText(
                "A vida em Feira de Santana pesava nos ombros de Toin. Preso à rotina de um mercado local, " +
                        "ele recebia apenas o básico para sobreviver. Seu refúgio era o quarto apertado, onde dedilhava o cavaquinho " +
                        "até os dedos doerem, sonhando com grandes palcos. A responsabilidade das contas o acorrentava, até que o " +
                        "destino cruzou seu caminho: um cartaz iluminado anunciava o Grande Concurso Musical no Rio de Janeiro - RJ, " +
                        "com um prêmio de R$ 10.000 e a gravação de um álbum. Com os olhos brilhando, ele sussurrou para si mesmo: " +
                        "\"É a minha chance!\". Juntou suas economias suadas, enfiou algumas roupas numa mala e pegou o primeiro ônibus " +
                        "para a Cidade Maravilhosa. Ao pisar no asfalto quente do Rio, a ficha caiu: ninguém faz pagode sozinho. " +
                        "Ele precisava de uma banda.\n\n" +
                        "Distribua exatamente 120 pontos entre os atributos de Toin (de 0 a 100 cada):"
        );


        spnCarisma.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));
        spnSamba.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));
        spnOuvido.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));

        spnCarisma.valueProperty().addListener((obs, oldVal, newVal) -> atualizarContador());
        spnSamba.valueProperty().addListener((obs, oldVal, newVal) -> atualizarContador());
        spnOuvido.valueProperty().addListener((obs, oldVal, newVal) -> atualizarContador());

        atualizarContador();
    }

    private int lerValorSeguro(Spinner<Integer> spinner) {
        return spinner.getValue() != null ? spinner.getValue() : 0;
    }

    private void atualizarContador() {
        int soma = lerValorSeguro(spnCarisma) + lerValorSeguro(spnSamba) + lerValorSeguro(spnOuvido);
        int restantes = 120 - soma;

        if (restantes == 0) {
            lblPontosRestantes.setText("Pontos distribuídos: 120 / 120 (Pronto para iniciar!)");
        } else if (restantes > 0) {
            lblPontosRestantes.setText("Faltam distribuir: " + restantes + " pontos (Total atual: " + soma + "/120)");
        } else {
            lblPontosRestantes.setText("Excedeu " + Math.abs(restantes) + " pontos! (Total atual: " + soma + "/120)");
        }
    }

    @FXML
    public void acaoIniciarAventura(ActionEvent event) {
        int carisma = lerValorSeguro(spnCarisma);
        int samba = lerValorSeguro(spnSamba);
        int ouvido = lerValorSeguro(spnOuvido);
        int soma = carisma + samba + ouvido;

        if (soma != 120) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Distribuição Inválida");
            alerta.setHeaderText("A soma dos atributos deve ser exatamente 120 pontos.");
            alerta.setContentText("Você distribuiu " + soma + " pontos. Ajuste os valores antes de continuar.");
            alerta.showAndWait();
            return;
        }

        SessaoJogo.getInstancia().iniciarNovaSessao();
        IntegracaoJogo jogo = SessaoJogo.getInstancia().getJogoAtual();
        PersonagemPrincipal toin = jogo.getProtagonista();
        toin.setCarisma(carisma);
        toin.setSamba(samba);
        toin.setOuvido(ouvido);

        MainApp.mostrarTelaPrincipal();
    }

    @FXML
    public void acaoVoltarMenu(ActionEvent event) {
        MainApp.mostrarMenuInicial();
    }
}