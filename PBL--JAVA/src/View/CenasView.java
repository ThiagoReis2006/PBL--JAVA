/** Declaração do pacote que agrupa as classes do View. */
package View;
import Model.Cena;
import Model.Escolha;
import java.util.List;
import java.util.Scanner;


public class CenasView {


    private Scanner entrada = new Scanner(System.in);

    public void exibirCena(Cena cena) {
        System.out.println();
        imprimirTextoFormatado(cena.getTexto(), 80);

        List<Escolha> escolhas = cena.getEscolhas();
        if (!escolhas.isEmpty()) {
            System.out.println("0 - [Abrir Status e Inventário]");
            for (int i = 0; i < escolhas.size(); i++) {
                System.out.println((i + 1) + " - " + escolhas.get(i).getTexto());
            }
        }
    }

    public int pedirEscolha(int max) {
        int opcao = -1;
        while (true) {
            System.out.print("Escolha uma opção: ");
            try {
                opcao = Integer.parseInt(entrada.nextLine().trim());

                if (opcao >= 0 && opcao <= max) {
                    return opcao;
                } else {
                    System.out.println("Opção inválida! Escolha um número entre 0 e " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Por favor, não digite letras, use apenas números.");
            }
        }
    }

    public static void exibirPrologo() {
        String prologo = "A vida em Feira de Santana pesava nos ombros de Toin.\n" +
                "Preso à rotina de um mercado local, ele recebia apenas o básico para\n" +
                "sobreviver. Seu refúgio era o quarto apertado, onde dedilhava o cavaquinho\n" +
                "até os dedos doerem, sonhando com grandes palcos. A responsabilidade das contas o acorrentava, até que o\n" +
                "destino cruzou seu caminho: um cartaz iluminado anunciava o Grande Concurso Musical no Rio de Janeiro - RJ, com um prêmio de R$ 10.000\n" +
                " e a gravação de um álbum. Com os olhos brilhando, ele sussurrou para si mesmo: \"É a minha chance!\". Juntou suas economias suadas,\n" +
                "enfiou algumas roupas numa mala e pegou o primeiro ônibus para a Cidade Maravilhosa. Ao pisar no asfalto quente do\n" +
                "Rio, a ficha caiu: ninguém faz pagode sozinho. Ele precisava de uma banda.\n";

        imprimirTextoFormatado(prologo, 80);
    }

    public void exibirCabecalhoEdicao() {
        System.out.println("Distribua os pontos para os atributos de Toin:\n");
    }

    public int pedirAtributo(String nomeAtributo) {
        int valor = -1;
        while (true) {
            System.out.print(nomeAtributo + ": ");
            try {
                valor = Integer.parseInt(entrada.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite apenas números inteiros.");
            }
        }
    }

    public void exibirAvisoRegraPontos() {
        System.out.println("Aviso - A soma dos 3 atributos deve ser de 120 pontos.");
        System.out.println("Aviso - Nenhum atributo pode ser menor que 0 ou maior que 100.\n");
    }

    public void exibirErroSomaPontos(int soma) {
        System.out.println("\n Aviso - Você distribuiu " + soma + " pontos. O total deve ser 120. Tente novamente!\n");
    }

    public void exibirErroLimitePontos() {
        System.out.println("\n Aviso - Os atributos vão de 0 a 100. Tente novamente!\n");
    }

    public void exibirSucessoCriacao() {
        System.out.println("\n Personagem criado com sucesso! Sua jornada vai começar...\n");
    }

    public static void imprimirTextoFormatado(String texto, int limiteColunas) {
        String[] paragrafos = texto.split("\n");
        for (String paragrafo : paragrafos) {
            String[] palavras = paragrafo.split(" ");
            int comprimentoLinhaAtual = 0;
            for (String palavra : palavras) {
                if (comprimentoLinhaAtual + palavra.length() > limiteColunas) {
                    System.out.println();
                    comprimentoLinhaAtual = 0;
                }
                System.out.print(palavra + " ");
                comprimentoLinhaAtual += palavra.length() + 1;
            }
            System.out.println();
        }
    }

    public void exibirStatus(Model.IntegracaoJogo jogo) {
        System.out.println("\n");
        System.out.println("          STATUS DA BANDA");
        System.out.println("\n");
        System.out.println("Grana: R$ " + jogo.getBanda().getGrana());
        System.out.println("Harmonia (Vida): " + jogo.getBanda().getHarmonia() + "/100");
        System.out.println("Essência: " + jogo.getBanda().getEssencia());
        System.out.println("\n");
        System.out.println("          RELACIONAMENTOS");
        System.out.println("\n");
        System.out.println("Betinho (Amizade): " + jogo.getBetinho().getNivelRelacionamento());
        System.out.println("Jorge (Confiança): " + jogo.getJorge().getNivelRelacionamento());
        System.out.println("Manuela (Afinidade): " + jogo.getManuela().getNivelRelacionamento());
        System.out.println("\n");
        System.out.println("             INVENTÁRIO");
        System.out.println("\n");

        java.util.List<Model.Item> itens = jogo.getInventario().getItens();
        if (itens.isEmpty()) {
            System.out.println("Seu inventário está vazio.");
        } else {
            for (Model.Item item : itens) {
                System.out.println("- " + item.getNome() + ": " + item.getDescricao());
            }
        }
        System.out.println("\n");
    }
}
