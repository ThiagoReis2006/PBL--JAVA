/** Declaração do pacote que agrupa as classes do Model. */
package Model;

/** Declaração que serve para incluir a interface List do Java. */
import java.util.List;

/** Declaração da classe pública das cenas, que basicamente representam momentos do jogo. */
public class Cena {
    /** Declaração dos atributos da classe das cenas, que são públicos, porém inalteráveis. */
    public final CenasIds id;
    public final String texto;
    public final List<Escolha> escolhas;
    public final boolean fim;
    public final CenasIds destinoPadrao;

    /** Nesse momento, está sendo utilizado o método da "Sobrecarga de Construtores".
     * Esse é o único construtor onde ele recebe 5 argumentos. */
    public Cena(CenasIds id, String texto, List<Escolha> escolhas, boolean fim, CenasIds destinoPadrao) {
        this.id = id;
        this.texto = texto;
        this.escolhas = escolhas;
        this.fim = fim;
        this.destinoPadrao = destinoPadrao;
    }
    /** Esse construtor é utilizado para cenas interativas. */
    public Cena(CenasIds id, String texto, List<Escolha> escolhas) {
        this(id, texto, escolhas, false, null);
    }

    /** Esse construtor é utilizado para cenas automáticas. */
    public Cena(CenasIds id, String texto, CenasIds destinoPadrao) {
        this(id, texto, List.of(), false, destinoPadrao);
    }

    /** Esse construtor é utilizado para cena de fim. */
    public Cena(CenasIds id, String texto, List<Escolha> escolhas, boolean fim) {
        this(id, texto, escolhas, fim, null);
    }

    /** Métodos getters para recuperar ou ler os atributos da classe dos itens. */
    public CenasIds getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

    public List<Escolha> getEscolhas() {
        return escolhas;
    }

    public boolean eFim() {
        return fim;
    }
}