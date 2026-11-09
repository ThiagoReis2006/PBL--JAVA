/** Declaração do pacote que agrupa as classes do Model. */
package Model;

/** Importa uma interface do Java que recebe um argumento de entrada e não retorna nada.*/
import java.util.function.Consumer;

/** Importa uma interface do Java que recebe um argumento de entrada e RETORNA um valor.
 *  É essa interface que vamos usar para calcular o destino de forma dinâmica. */
import java.util.function.Function;

public class Escolha {

    private final String texto;

    /** Destino fixo, definido na hora em que a Escolha é criada.
     *  Continua existindo, para não quebrar nenhuma Escolha que já usa destino fixo. */
    private final CenasIds destinoFixo;

    /** Destino dinâmico: uma FUNÇÃO que, quando executada, recebe o estado atual do
     *  jogo (IntegracaoJogo) e DEVOLVE qual CenasIds deve ser usado.
     *  Fica null quando a Escolha usa destino fixo. */
    private final Function<IntegracaoJogo, CenasIds> destinoDinamico;

    private final Consumer<IntegracaoJogo> efeito;

    /**
     * Construtor de segurança (sem efeito, destino FIXO).
     * Continua funcionando exatamente como antes.
     */
    public Escolha(String texto, CenasIds destino) {
        this(texto, destino, null);
    }

    /**
     * Construtor real (com efeito, destino FIXO).
     * Continua funcionando exatamente como antes — usado em quase todas as
     * Escolhas do jogo, que sempre vão para o mesmo lugar.
     */
    public Escolha(String texto, CenasIds destino, Consumer<IntegracaoJogo> efeito) {
        this.texto = texto;
        this.destinoFixo = destino;
        this.destinoDinamico = null;
        this.efeito = efeito;
    }

    /**
     * Usado quando o próximo capítulo depende do estado do jogador
     * (grana, harmonia, relacionamento etc.), calculado SOMENTE no momento
     * em que a Escolha é de fato executada, e não na hora em que o
     * RepositorioCenas é construído.
     *
     * @param texto            texto exibido para o jogador
     * @param efeito           efeito aplicado ao jogo quando a Escolha é feita
     * @param destinoDinamico  função que recebe o estado atual do jogo e
     *                         devolve para qual CenasIds o jogo deve ir
     */
    public Escolha(String texto, Consumer<IntegracaoJogo> efeito, Function<IntegracaoJogo, CenasIds> destinoDinamico) {
        this.texto = texto;
        this.destinoFixo = null;
        this.destinoDinamico = destinoDinamico;
        this.efeito = efeito;
    }

    public String getTexto() {
        return texto;
    }



    /**
     * Retorna o destino correto, considerando o estado atual do jogo.
     * - Se a Escolha tem destino dinâmico, executa a função passando o jogo
     *   atual e devolve o CenasIds calculado na hora.
     * - Se a Escolha tem destino fixo, simplesmente devolve o destino fixo,
     *   ignorando o parâmetro jogo.
     * Esse é o método que o game loop deve preferir usar sempre, porque
     * funciona tanto para Escolhas fixas quanto dinâmicas.
     */
    public CenasIds getDestino(IntegracaoJogo jogo) {
        if (destinoDinamico != null) {
            return destinoDinamico.apply(jogo);
        }
        return destinoFixo;
    }

    /** Aplica o efeito colocado no campo, se houver algum. */
    public void aplicarEfeito(IntegracaoJogo jogo) {
        if (efeito != null) {
            efeito.accept(jogo);
        }
    }
}