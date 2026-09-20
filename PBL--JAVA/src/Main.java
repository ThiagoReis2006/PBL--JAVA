import Control.CenasController;
import Control.MenuController;
import Control.PersonagemController;
import Model.IntegracaoJogo;
import Model.RepositorioCenas;
import View.CenasView;
import View.MenuView;
import Model.Item;

// Classe principal do programa.
public class Main{

    // Método "main": é a primeira coisa que o Java executa ao rodar o programa.
    public static void main(String[] args){

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

        // 4. Prepara o menu do jogo
        // Cria o controlador do menu, entregando para ele o controle do personagem e das cenas
        // (Isso permite que o menu consiga iniciar a história quando o jogador apertar '1')
        MenuController menuController = new MenuController(personagemController, cenasController);

        // 5. Dá a partida
        MenuView menuView = new MenuView(); // Cria a tela visual do menu
        menuView.iniciar(menuController);   // Exibe o menu no console e fica esperando a escolha do jogador

    }
}
