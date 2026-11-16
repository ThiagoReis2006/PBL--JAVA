package Model;

public class SessaoJogo {

    private static SessaoJogo instancia;

    private IntegracaoJogo jogoAtual;
    private CenasIds cenaAtual;
    private int slotAtual;

    private SessaoJogo() {
        iniciarNovaSessao();
    }

    public static SessaoJogo getInstancia() {
        if (instancia == null) {
            instancia = new SessaoJogo();
        }
        return instancia;
    }

    public void iniciarNovaSessao() {
        this.jogoAtual = new IntegracaoJogo();
        this.jogoAtual.getInventario().adicionarItem(
                new Item("Cavaquinho de Feira", "Relembra a motivação e o peso das economias gastas na passagem de ônibus.")
        );
        this.jogoAtual.getInventario().adicionarItem(
                new Item("Panfleto do Concurso", "Lembrete do objetivo principal: prêmio de R$ 10.000 e as regras da gravadora.")
        );
        this.cenaAtual = CenasIds.CAP1_BAR;
        this.slotAtual = -1;
    }

    public void carregarSessao(SalvarDados dados, int slot) {
        this.jogoAtual = dados.getEstadoJogo();
        this.cenaAtual = dados.getIdCenaAtual();
        this.slotAtual = slot;
    }

    public IntegracaoJogo getJogoAtual() {
        return jogoAtual;
    }

    public CenasIds getCenaAtual() {
        return cenaAtual;
    }

    public void setCenaAtual(CenasIds cenaAtual) {
        this.cenaAtual = cenaAtual;
    }

    public int getSlotAtual() {
        return slotAtual;
    }

    public void setSlotAtual(int slotAtual) {
        this.slotAtual = slotAtual;
    }
}