package Control;

import Model.*;
import View.CenasView;

// Classe que controla o andamento do jogo, passando de uma cena para a outra
public class CenasController {

    private final RepositorioCenas repositorio; // Onde ficam salvas todas as cenas do jogo
    private final CenasView view;               // Responsável por mostrar os textos na tela
    private final IntegracaoJogo jogo;          // Guarda os dados do jogador (vida, itens, etc.)

    // O jogo sempre começa nesta cena (Bar)
    private CenasIds atual = CenasIds.CAP1_BAR;

    // Prepara o controlador com tudo que ele precisa para funcionar
    public CenasController(RepositorioCenas repositorio, CenasView view, IntegracaoJogo jogo) {
        this.repositorio = repositorio;
        this.view = view;
        this.jogo = jogo;
    }

    // Método principal que faz o jogo rodar
    public void jogar() {

        // O jogo fica repetindo esse bloco de código infinitamente
        while (true) {

            // Pega a cena atual e mostra o texto dela na tela
            Cena cena = repositorio.buscar(atual);
            view.exibirCena(cena);

            // Se for uma cena de final de jogo (ganhou ou perdeu), fecha o programa
            if (cena.eFim()) {
                System.exit(0);
            }

            // Se a cena for só de leitura e não tiver escolhas, pula direto pra próxima
            if (cena.getEscolhas().isEmpty()) {
                atual = cena.destinoPadrao;
                continue; // Volta para o início do "while" para carregar a nova cena
            }

            // Pede para o jogador digitar o número da escolha que ele quer fazer
            int opcao = view.pedirEscolha(cena.getEscolhas().size());

            // Se o jogador digitar 0, ele só quer ver o status (vida/itens). O jogo não avança.
            if (opcao == 0) {
                view.exibirStatus(jogo);
                continue; // Volta para o início do "while" sem mudar de cena
            }

            // Pega a opção exata que o jogador escolheu (diminui 1 porque listas no Java começam no zero)
            Escolha escolhida = cena.getEscolhas().get(opcao - 1);

            // Aplica os efeitos da escolha (ex: perder vida) e diz qual será a próxima cena
            escolhida.aplicarEfeito(jogo);
            atual = escolhida.getDestino(jogo);
        }
    }
}