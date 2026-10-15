package View;

import Control.MenuDeletarController;

import java.util.Scanner;

public class MenuDeletarView {

    // Um único Scanner, usado no menu e na confirmação
    private final Scanner entrada = new Scanner(System.in);

    public void iniciarMenuDeletar(MenuDeletarController menuDeletarController) {
        int opcao = 0;

        do {
            System.out.println("Selecione o Slot que voce deseja deletar:");
            System.out.println("1 - Slot 1");
            System.out.println("2 - Slot 2");
            System.out.println("3 - Slot 3");
            System.out.println("4 - Voltar ao menu inicial");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(entrada.nextLine().trim());
                menuDeletarController.processamentoSlot(opcao);
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida! Por favor, digite um número.");
                opcao = 0;
            }

        } while (opcao != 4);
    }

    // Só pergunta e devolve a resposta; quem decide é o Controller
    public boolean confirmarDelecao(int slot) {
        while (true) {
            System.out.println("Tem certeza que deseja deletar o Slot " + slot + "? Essa ação não pode ser desfeita.");
            System.out.println("1 - Sim");
            System.out.println("2 - Não");
            System.out.print("Escolha uma opção: ");

            try {
                int resp = Integer.parseInt(entrada.nextLine().trim());
                if (resp == 1) return true;
                if (resp == 2) return false;
            } catch (NumberFormatException e) {
                // cai na mensagem abaixo
            }
            System.out.println("Opção inválida!\n");
        }
    }
}