/** Declaração do pacote que agrupa as classes do View. */
package View;

import Control.MenuController;

import java.util.Scanner;
public class MenuView {
    public void iniciar(MenuController menuController) {
        Scanner entrada = new Scanner(System.in);
        int opcao = 0;

        do {
            System.out.println("\nO SHOW TEM DE CONTINUAR\n");
            System.out.println("----Menu----");
            System.out.println("1- Nova Partida");
            System.out.println("2- Instruções");
            System.out.println("3- Créditos");
            System.out.println("4- Saída\n");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(entrada.nextLine().trim());
                menuController.processamentoOpcao(opcao);
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida! Por favor, digite um número.");
                opcao = 0;
            }

        } while (opcao != 4);
    }

    public static void exibeCredito(){
        System.out.println("-------Créditos-------\n");
        System.out.println("Felipe Vieira Aquino\n");
        System.out.println("Thiago Marques Reis");
    }
    public static void exibeInstrucoes() {
        String instrucoes = "\nSeja bem-vindo ao jogo O Show Tem de Continuar!\n" +
                "Neste RPG narrativo, cada decisão sua moldará o destino de Toin e a trajetória da sua banda rumo ao grande Festival de Pagode.\n" +
                "O jogo é orientado por quatro pilares centrais: a Harmonia, que representa a saúde e a união do grupo (se ela chegar a zero, é Game Over);\n" +
                "a Essência, que mede o quanto o seu som se mantém fiel às raízes do pagode tradicional ou se rende ao mercado pop comercial;\n" +
                "a Grana, que representa o seu principal recurso, pois ela é necessária para possíveis investimentos durante a história;\n" +
                "e os seus Relacionamentos com os integrantes, que podem liberar caminhos únicos ou provocar deserções se forem negligenciados.\n" +
                "Além disso, as habilidades que você escolheu no início do jogo – como sua lábia, sua presença de palco e sua técnica no cavaquinho –\n" +
                "serão testadas automaticamente durante as apresentações e conflitos da história. Administre bem a sua Grana para não cair nas garras de agiotas e\n" +
                "pense com cuidado antes de cada escolha: a fama e o respeito da comunidade dependem da sua capacidade de equilibrar a razão, o coração e o samba no pé.\n";

        CenasView.imprimirTextoFormatado(instrucoes, 80);
    }
    public static void saida(){
        System.out.println("Jogo encerrado!");
    }

}
