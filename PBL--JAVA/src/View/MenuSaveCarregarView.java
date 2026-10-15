package View;

import Control.MenuSaveCarregarController;
import Control.CenasController;

import java.util.Scanner;

public class MenuSaveCarregarView {
    public void iniciarMenuSaveCarregar(MenuSaveCarregarController menuSaveCarregarController) {
        Scanner entrada = new Scanner(System.in);
        int opcao = 0;

        do {
            System.out.println("Selecione o Slot que voce deseja carregar:");
            System.out.println("1 - Slot 1");
            System.out.println("2 - Slot 2");
            System.out.println("3 - Slot 3");
            System.out.println("4 - Voltar ao menu inicial");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(entrada.nextLine().trim());
                menuSaveCarregarController.processamentoSlot(opcao);
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida! Por favor, digite um número.");
                opcao = 0;
            }

        } while (opcao != 4);
    }
}



