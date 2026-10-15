package Control;

import Model.*;
import View.MenuDeletarView;

public class MenuDeletarController {

    private final GerenciadorSave gerenciador;
    private final MenuDeletarView view;

    public MenuDeletarController(GerenciadorSave gerenciador, MenuDeletarView view) {
        this.gerenciador = gerenciador;
        this.view = view;
    }

    public void processamentoSlot(int opcao) {
        switch (opcao) {
            case 1, 2, 3 -> deletarSlot(opcao);

            case 4 -> {
                // só volta: o loop da View termina sozinho
            }

            default -> System.out.println("\nOpção inválida! Escolha uma opção válida.\n");
        }
    }

    private void deletarSlot(int slot) {
        if (!gerenciador.slotTemSave(slot)) {
            System.out.println("\nO Slot " + slot + " está vazio, não há nada para deletar.\n");
            return;
        }

        // Mostra as informações padrão do save antes de confirmar
        try {
            SalvarDados dados = gerenciador.carregar(slot);
            System.out.println("\nSave encontrado no Slot " + slot + "!"
                    + " Jogador: " + dados.getNomeJogador()
                    + " | Data: " + dados.getDataHora() + "\n");
        } catch (Exception e) {
            System.out.println("\nNão foi possível ler as informações do Slot " + slot + ".\n");
        }

        // Só deleta se o jogador confirmar
        if (view.confirmarDelecao(slot)) {
            if (gerenciador.deletar(slot)) {
                System.out.println("\nSlot " + slot + " deletado com sucesso!\n");
            } else {
                System.out.println("\nFalha ao deletar o Slot " + slot + ".\n");
            }
        } else {
            System.out.println("\nExclusão cancelada.\n");
        }
    }
}