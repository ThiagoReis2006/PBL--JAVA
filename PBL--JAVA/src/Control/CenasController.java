package Control;

import Model.*;
import View.CenasView;

public class CenasController {

    private final RepositorioCenas repositorio;
    private final CenasView view;
    private final IntegracaoJogo jogo;
    private CenasIds ultimaCenaExibida = null;

    private int slotAtual = -1;

    private CenasIds atual = CenasIds.CAP1_BAR;

    public CenasController(RepositorioCenas repositorio, CenasView view, IntegracaoJogo jogo) {
        this.repositorio = repositorio;
        this.view = view;
        this.jogo = jogo;
    }

    public void setJogoCarregado(IntegracaoJogo jogoSalvo, CenasIds cenaSalva, int slot) {
        this.atual = cenaSalva;
        this.ultimaCenaExibida = cenaSalva;
        this.slotAtual = slot;
    }

    private void salvarJogo() {
        GerenciadorSave gerenciador = new GerenciadorSave();

        try {
            int slotDestino = this.slotAtual;

            if (slotDestino == -1) {
                for (int slot = 1; slot <= 3; slot++) {
                    if (!gerenciador.slotTemSave(slot)) {
                        slotDestino = slot;
                        break;
                    }
                }

                if (slotDestino == -1) {
                    slotDestino = gerenciador.slotMaisAntigo();
                    if (!view.confirmarSobrescrita(slotDestino)) {
                        System.out.println("\nSave cancelado.\n");
                        return;
                    }
                }

                this.slotAtual = slotDestino;
            }


            String capituloAtual = atual.name();

            String resumo = "Harmonia: " + jogo.getBanda().getHarmonia() +
                    " | Grana: R$ " + jogo.getBanda().getGrana() +
                    " | Essência: " + jogo.getBanda().getEssencia();

            SalvarDados dadosDoSave = new SalvarDados(
                    "Toin",
                    capituloAtual,
                    resumo,
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

            boolean chegouNaPraca = (cena.id == CenasIds.CAP5_PRACA && ultimaCenaExibida != CenasIds.CAP5_PRACA);
            boolean chegouNoFinal = ((cena.id == CenasIds.CAP10_FINAL_FALSO ||
                    cena.id == CenasIds.CAP10_FINAL_ESSENCIALISTA ||
                    cena.id == CenasIds.CAP10_FINAL_HARMONIOSO) &&
                    ultimaCenaExibida != cena.id);

            if (chegouNaPraca || chegouNoFinal) {
                System.out.println("\n=== Você chegou a um ponto de salvamento automático ===");
                salvarJogo();
            }

            ultimaCenaExibida = cena.id;

            if (cena.eFim()) {
                GerenciadorSave gerGlobal = new GerenciadorSave();
                DadosGlobais globais = gerGlobal.carregarDadosGlobais();

                globais.adicionarFinal(cena.id.name());

                try {
                    gerGlobal.salvarDadosGlobais(globais);
                    System.out.println("\n*** Progresso Global: Final (" + cena.id.name() + ") registrado com sucesso! ***\n");
                } catch (FalhaSalvamentoException e) {
                    System.out.println("\nErro ao guardar progresso global: " + e.getMessage() + "\n");
                }

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