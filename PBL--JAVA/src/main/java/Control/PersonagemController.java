package Control;

import View.CenasView;
import Model.PersonagemPrincipal;

// Classe responsável por controlar a criação do personagem do jogador
public class PersonagemController {

    private PersonagemPrincipal personagem; // O personagem que vai receber os pontos de habilidade
    private CenasView view;                 // Responsável por mostrar as mensagens na tela e ler o que o jogador digita

    // Prepara o controlador recebendo o personagem em branco e a tela que será usada
    public PersonagemController(PersonagemPrincipal personagem, CenasView view) {
        this.personagem = personagem;
        this.view = view;
    }

    // Método que guia o jogador na hora de distribuir os pontos nas habilidades
    public void criarPersonagem () {
        // Variáveis temporárias para guardar os pontos que o jogador digitar
        int carisma;
        int samba;
        int ouvido;
        int soma;

        // Esse bloco "do-while" faz a pergunta repetir até o jogador acertar a regra dos pontos
        do {
            view.exibirCabecalhoEdicao();    // Mostra o título da tela de criação
            view.exibirAvisoRegraPontos();   // Avisa que o total de pontos deve ser exatamente 120

            // Pede para o jogador digitar os pontos de cada habilidade
            carisma = view.pedirAtributo ("Carisma de Botequim");
            samba = view.pedirAtributo ("Samba no Pé");
            ouvido = view.pedirAtributo ("Ouvido Absoluto");

            // Soma todos os pontos distribuídos
            soma = carisma + samba + ouvido;

            // Verifica se a soma deu um valor diferente de 120
            if (soma != 120) {
                view.exibirErroSomaPontos (soma); // Mostra erro dizendo que não deu 120
            }
            // Verifica se o jogador tentou colocar mais de 100 ou menos de 0 (negativo) em alguma habilidade
            else if (carisma > 100 || samba > 100 || ouvido > 100 || carisma < 0 || samba < 0 || ouvido < 0) {
                view.exibirErroLimitePontos ();   // Mostra erro dizendo que o valor é inválido
                soma = 0;                         // Zera a soma para forçar o loop a repetir
            }

        } while (soma != 120); // Se a soma não for 120, volta lá para cima e pede tudo de novo

        // Se chegou até aqui, é porque o jogador distribuiu os 120 pontos corretamente
        // Então, salva esses valores no personagem definitivo do jogo
        personagem.setCarisma (carisma);
        personagem.setSamba (samba);
        personagem.setOuvido (ouvido);

        // Mostra uma mensagem confirmando que o personagem foi criado com sucesso
        view.exibirSucessoCriacao ();
    }
}