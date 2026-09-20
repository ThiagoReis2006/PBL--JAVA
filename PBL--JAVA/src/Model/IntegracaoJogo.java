package Model;

// Classe que reúne e guarda todas as informações principais da partida.
// É através dela que o jogo sabe quem é o jogador, os itens que ele tem e quem são os amigos.
public class IntegracaoJogo {

    // Cria e guarda o personagem principal (que o jogador controla)
    private PersonagemPrincipal protagonista = new PersonagemPrincipal();

    // Cria e guarda os dados da banda da história
    private Banda banda = new Banda();

    // Cria e guarda a mochila (inventário) onde ficam os itens ganhos no jogo
    private Inventario inventario = new Inventario();

    // Cria os outros personagens da história e os atributos ligados a eles
    private PersonagemSecundario jorge = new PersonagemSecundario("Jorge", "Confiança");
    private PersonagemSecundario manuela = new PersonagemSecundario("Manuela", "Afinidade");
    private PersonagemSecundario betinho = new PersonagemSecundario("Betinho", "Amizade");

    // Daqui para baixo, temos os métodos que "entregam" esses dados para outras partes do jogo.
    // Se uma cena precisa tirar pontos da Manuela, ela pede a Manuela por aqui.

    // Devolve os dados do protagonista
    public PersonagemPrincipal getProtagonista () {
        return protagonista;
    }

    // Devolve os dados da banda
    public Banda getBanda () {
        return banda;
    }

    // Devolve os itens que o jogador tem
    public Inventario getInventario () {
        return inventario;
    }

    // Devolve os dados do personagem Jorge
    public PersonagemSecundario getJorge () {
        return jorge;
    }

    // Devolve os dados da personagem Manuela
    public PersonagemSecundario getManuela () {
        return manuela;
    }

    // Devolve os dados do personagem Betinho
    public PersonagemSecundario getBetinho () {
        return betinho;
    }
}