package Control;

import Model.*;
import View.CenasView;
import View.MenuView;

public class MenuSaveCarregarController {
    private final CenasController cenasController;

    public MenuSaveCarregarController(CenasController cenasController){
        this.cenasController = cenasController;
    }

    public void processamentoSlot(int opcao){
        switch (opcao){

            case 1, 2, 3 -> {
                try {
                    GerenciadorSave gerenciador = new GerenciadorSave();
                    // O método carregar agora usa a variável 'opcao' (1, 2 ou 3)
                    SalvarDados dados = gerenciador.carregar(opcao);

                    System.out.println("\nSave encontrado! Jogador: " + dados.getNomeJogador() + " | Data: " + dados.getDataHora());

                    cenasController.setJogoCarregado(dados.getEstadoJogo(), dados.getIdCenaAtual(), opcao);
                    cenasController.jogar();

                } catch (FalhaCarregamentoException e) {
                    // Captura arquivo ausente ou falha de leitura
                    System.out.println("\n[Aviso] " + e.getMessage() + "\n");
                } catch (DadosInvalidosException e) {
                    // Captura JSON quebrado ou dados faltando
                    System.out.println("\n[Erro Crítico] " + e.getMessage() + "\n");
                }
            }

            case 4 -> {
                // 1. Prepara o estado inicial do jogo
                // Cria a central que guarda todas as informações da partida (personagem, banda, etc.)
                IntegracaoJogo jogo = new IntegracaoJogo();

                // Logo no começo, coloca dois itens na mochila (inventário) do personagem
                jogo.getInventario().adicionarItem(new Item("Cavaquinho de Feira", "Relembra a motivação e o peso das economias gastas na passagem de ônibus."));
                jogo.getInventario().adicionarItem(new Item("Panfleto do Concurso", "Lembrete do objetivo principal: prêmio de R$ 10.000 e as regras da gravadora."));

                // 2. Prepara a tela e o criador de personagem
                CenasView c1 = new CenasView(); // Cria a tela onde os textos da história vão aparecer
                // Cria o controlador que vai guiar o jogador na hora de distribuir os atributos
                PersonagemController personagemController = new PersonagemController(jogo.getProtagonista(), c1);

                // 3. Prepara o "motor" da história
                RepositorioCenas repositorio = new RepositorioCenas(); // Cria o "livro" que contém todas as cenas
                // Cria o controlador que vai virar as páginas da história e aplicar as escolhas
                CenasController cenasControllerNovo = new CenasController(repositorio, c1, jogo);

                // 4. Prepara o menu do jogo
                // Cria o controlador do menu, entregando para ele o controle do personagem e das cenas
                // (Isso permite que o menu consiga iniciar a história quando o jogador apertar '1')
                MenuController menuController = new MenuController(personagemController, cenasControllerNovo);

                // 5. Dá a partida
                MenuView menuView = new MenuView(); // Cria a tela visual do menu
                menuView.iniciar(menuController);   // Exibe o menu no console e fica esperando a escolha do jogador
            }

            default -> {
                System.out.println("\n");
                System.out.println("Opção inválida! Escolha uma opção válida."); // Mostra um aviso
            }
        }
    }
}