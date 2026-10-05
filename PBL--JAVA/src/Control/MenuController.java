package Control;

import Model.GerenciadorSave;
import Model.SalvarDados;
import View.CenasView;
import View.MenuView;

// Classe responsável por controlar as opções do Menu Principal do jogo
public class MenuController {

    // Controladores que o menu precisa para conseguir iniciar o jogo
    private final PersonagemController controller;      // Vai ser usado para criar o personagem
    private final CenasController cenasController;      // Vai ser usado para rodar a história

    // Prepara o controlador do menu recebendo as ferramentas que ele vai usar
    public MenuController(PersonagemController controller, CenasController cenasController){
        this.controller = controller;
        this.cenasController = cenasController;
    }

    // Recebe o número que o jogador escolheu no menu e decide o que fazer
    public void processamentoOpcao(int opcao){

        // Verifica qual foi a opção digitada
        switch(opcao){

            // Opção 1: Iniciar o jogo
            case 1 -> {
                System.out.println("\n");
                CenasView.exibirPrologo();        // Mostra a introdução da história
                controller.criarPersonagem();     // Pede para o jogador criar seu personagem
                cenasController.jogar();          // Começa a passar as cenas do jogo
            }

            // Opção 2: Ver as instruções
            case 2 -> {
                System.out.println("\n");
                MenuView.exibeInstrucoes();       // Mostra a tela que ensina a jogar
            }

            // Opção 3: Carregar jogo
            case 3 -> {
                System.out.println("\nTentando carregar o Slot 1...\n");
                try {
                    GerenciadorSave gerenciador = new GerenciadorSave();
                    SalvarDados dados = gerenciador.carregar(1);

                    System.out.println("Save encontrado! Jogador: " + dados.getNomeJogador() + " | Data: " + dados.getDataHora());

                    cenasController.setJogoCarregado(dados.getEstadoJogo(), dados.getIdCenaAtual());
                    cenasController.jogar();

                } catch (Exception e) {
                    System.out.println("\nNenhum progresso encontrado neste slot.\n");
                }
            }

            // Opção 4: Ver os créditos
            case 4 -> {
                System.out.println("\n");
                MenuView.exibeCredito();          // Mostra quem desenvolveu o jogo
            }

            // Opção 5: Sair do jogo
            case 5 -> {
                System.out.println("\n");
                MenuView.saida();                 // Mostra a mensagem de despedida e fecha o jogo
            }

            // Se o jogador digitar qualquer outro número que não seja de 1 a 4
            default -> {
                System.out.println("\n");
                System.out.println("Opção inválida! Escolha uma opção válida."); // Mostra um aviso
            }
        }
    }
}