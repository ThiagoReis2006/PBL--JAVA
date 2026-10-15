package Control;

import Model.*;
import View.CenasView;

public class CenasController {

    private final RepositorioCenas repositorio;
    private final CenasView view;
    private final IntegracaoJogo jogo;
    private CenasIds ultimaCenaExibida = null;

    private CenasIds atual = CenasIds.CAP1_BAR;

    public CenasController(RepositorioCenas repositorio, CenasView view, IntegracaoJogo jogo) {
        this.repositorio = repositorio;
        this.view = view;
        this.jogo = jogo;
    }

    public void setJogoCarregado(IntegracaoJogo jogoSalvo, CenasIds cenaSalva) {
        this.atual = cenaSalva;
        this.ultimaCenaExibida = cenaSalva;
    }

    private void salvarJogo() {
        GerenciadorSave gerenciador = new GerenciadorSave();

        try {
            int slotDestino = -1;

            // procura o primeiro slot vazio
            for (int slot = 1; slot <= 3; slot++) {
                if (!gerenciador.slotTemSave(slot)) {
                    slotDestino = slot;
                    break;
                }
            }

            // todos cheios: sugere o mais antigo e pede confirmação
            if (slotDestino == -1) {
                slotDestino = gerenciador.slotMaisAntigo();
                if (!view.confirmarSobrescrita(slotDestino)) {
                    System.out.println("\nSave cancelado.\n");
                    return;
                }
            }

            SalvarDados dadosDoSave = new SalvarDados(
                    "Toin",
                    atual.name(),
                    "Progresso salvo.",
                    this.jogo,
                    this.atual
            );
            gerenciador.salvar(dadosDoSave, slotDestino);
            System.out.println("\nJogo salvo no slot " + slotDestino + "!\n");

        } catch (FalhaSalvamentoException e) {
            System.out.println("\nFalha ao salvar: " + e.getMessage() + "\n");
        }
    }


    public void jogar() {

        while (true) {

            Cena cena = repositorio.buscar(atual);
            view.exibirCena(cena);

            // Ponto de salvamento automático: só ao CHEGAR na praça
            if (cena.id == CenasIds.CAP5_PRACA && ultimaCenaExibida != CenasIds.CAP5_PRACA) {
                System.out.println("\n=== Você chegou a um ponto de salvamento automático ===");
                salvarJogo();
            }
            ultimaCenaExibida = cena.id;

            if (cena.eFim()) {
                System.exit(0);
            }

            if (cena.getEscolhas().isEmpty()) {
                atual = cena.destinoPadrao;
                continue;
            }

            int opcao = view.pedirEscolha(cena.getEscolhas().size());

            if (opcao == 0) {
                view.exibirStatus(jogo);
                continue;
            }

            if (opcao == -1) {
                salvarJogo();
                continue;
            }

            Escolha escolhida = cena.getEscolhas().get(opcao - 1);
            escolhida.aplicarEfeito(jogo);
            atual = escolhida.getDestino(jogo);
        }
    }
}