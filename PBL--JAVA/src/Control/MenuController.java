package Control;

import Model.*;
import View.CenasView;
import View.MenuView;
import View.MenuSaveCarregarView;
import View.MenuDeletarView;

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

            case 2 -> {
                try {
                    GerenciadorSave gerenciador = new GerenciadorSave();
                    int slot = gerenciador.slotMaisRecente();

                    if (slot == -1) {
                        System.out.println("\nNenhum progresso encontrado.\n");
                    } else {
                        SalvarDados dados = gerenciador.carregar(slot);

                        System.out.println("Save encontrado no slot " + slot + "! Jogador: "
                                + dados.getNomeJogador() + " | Data: " + dados.getDataHora());

                        cenasController.setJogoCarregado(dados.getEstadoJogo(), dados.getIdCenaAtual(), slot);
                        cenasController.jogar();
                    }

                } catch (Exception e) {
                    System.out.println("\nFalha ao carregar o save: " + e.getMessage() + "\n");
                }
            }

            // Opção 3: Ver as instruções
            case 3 -> {
                System.out.println("\n");
                MenuView.exibeInstrucoes();       // Mostra a tela que ensina a jogar
            }

            // Opção 4: Carregar jogo
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
                CenasController cenasController = new CenasController(repositorio, c1, jogo);

                //cria objeto menu salvar controller
                MenuSaveCarregarController menuSaveCarregarController = new MenuSaveCarregarController(cenasController);

                //cria objeto menu salvar view
                MenuSaveCarregarView menuSaveCarregarView = new MenuSaveCarregarView();
                menuSaveCarregarView.iniciarMenuSaveCarregar(menuSaveCarregarController);

            }

            case 5 -> {
                System.out.println("\n");
                MenuDeletarView menuDeletarView = new MenuDeletarView();
                MenuDeletarController menuDeletarController =
                        new MenuDeletarController(new GerenciadorSave(), menuDeletarView);
                menuDeletarView.iniciarMenuDeletar(menuDeletarController);
            }

            // Opção 6: Ver os créditos
            case 6 -> {
                System.out.println("\n");
                MenuView.exibeCredito();          // Mostra quem desenvolveu o jogo
            }

            // Opção 7: Sair do jogo
            case 7 -> {
                System.out.println("\n");
                MenuView.saida();                 // Mostra a mensagem de despedida e fecha o jogo
            }

            // Opção 8: Conteúdo Desbloqueável (Epílogo Secreto)
            case 8 -> {
                GerenciadorSave ger = new GerenciadorSave();
                DadosGlobais globais = ger.carregarDadosGlobais();

                if (globais.isCenaSecretaDesbloqueada()) {
                    System.out.println("\n--- EPÍLOGO SECRETO: 5 ANOS DEPOIS ---\n");
                    String epilogo = "O tempo passou, mas algumas coisas nunca mudam. Independentemente de terem assinado com a " +
                            "gravadora gigante ou ficado no circuito independente, hoje é o aniversário de fundação da banda.\n" +
                            "O bar da Dona Gertrudes está de portas fechadas para o público geral, reservado apenas para vocês. " +
                            "Léo tenta (sem sucesso) ensinar passos de dança para Jorge, enquanto Betinho e Manuela conferem os lucros " +
                            "da última turnê. Toin senta num canto, afina o mesmo cavaquinho que trouxe de Feira de Santana, e sorri. " +
                            "O show continua, e a família está completa.\n";
                    CenasView.imprimirTextoFormatado(epilogo, 80);

                    System.out.println("\nObrigado por jogar 'O Show Tem de Continuar' e descobrir todos os finais!\n");
                } else {
                    java.util.List<String> finais = globais.getFinaisAlcancados();

                    System.out.println("\n[!] Conteúdo bloqueado.");
                    System.out.println("Você precisa alcançar 3 finais diferentes para liberar esta cena.");
                    System.out.println("Finais descobertos até agora (" + finais.size() + "/3):");

                    if (finais.isEmpty()) {
                        System.out.println("- Nenhum final alcançado ainda.");
                    } else {
                        for (String f : finais) {
                            System.out.println("- " + f);
                        }
                    }
                    System.out.println();
                }
            }

            // Opção 9: Configurações Persistentes
            case 9 -> {
                try {
                    GerenciadorSave ger = new GerenciadorSave();
                    DadosGlobais globais = ger.carregarDadosGlobais();
                    Configuracoes config = globais.getConfiguracoes();

                    java.util.Scanner sc = new java.util.Scanner(System.in);
                    int respOpcoes = 0;

                    do {
                        System.out.println("\n--- OPÇÕES ---");
                        System.out.println("1 - Ocultar UHD (Interface): " + (config.isOcultarUHD() ? "LIGADO" : "DESLIGADO"));

                        // Formatação do nome do tema visual escolhido
                        String nomeTema = "NEUTRO";
                        if (config.getTemaVisual() == 2) nomeTema = "ESCURO";
                        else if (config.getTemaVisual() == 3) nomeTema = "CLARO";

                        System.out.println("2 - Tema Visual: " + nomeTema);
                        System.out.println("3 - Cor das Letras: " + config.getCorLetras());
                        System.out.println("4 - Voltar ao Menu Principal");
                        System.out.print("Escolha o que alterar: ");

                        try {
                            respOpcoes = Integer.parseInt(sc.nextLine().trim());

                            // Lógica para alternar as opções
                            if (respOpcoes == 1) {
                                config.setOcultarUHD(!config.isOcultarUHD());
                            } else if (respOpcoes == 2) {
                                // Roda entre Neutro(1), Escuro(2) e Claro(3)
                                int novoTema = config.getTemaVisual() + 1;
                                if (novoTema > 3) novoTema = 1;
                                config.setTemaVisual(novoTema);
                            } else if (respOpcoes == 3) {
                                // Roda entre as cores
                                switch (config.getCorLetras()) {
                                    case "Branco" -> config.setCorLetras("Verde");
                                    case "Verde" -> config.setCorLetras("Amarelo");
                                    case "Amarelo" -> config.setCorLetras("Azul");
                                    default -> config.setCorLetras("Branco");
                                }
                            }

                            // Grava no ficheiro de imediato
                            if (respOpcoes >= 1 && respOpcoes <= 3) {
                                ger.salvarDadosGlobais(globais);
                                System.out.println("\n[!] Configuração guardada com sucesso!");
                            }

                        } catch (NumberFormatException e) {
                            System.out.println("\nOpção inválida! Digite um número.");
                            respOpcoes = 0;
                        }

                    } while (respOpcoes != 4);

                } catch (Exception e) {
                    System.out.println("\nErro ao aceder às configurações.");
                }
            }

            // Se o jogador digitar qualquer outro número que não seja de 1 a 4
            default -> {
                System.out.println("\n");
                System.out.println("Opção inválida! Escolha uma opção válida."); // Mostra um aviso
            }
        }
    }
}