/** Declaração do pacote que agrupa as classes do Model. */
package Model;

/** Declaração da classe pública que representa os itens. */
public class Item {

    /** Declaração dos atributos dos itens. */
    private String nome;
    private String descricao;

    /** Método construtor para os itens. */
    public Item (String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    /** Métodos getters para ler/recuperar os atributos dos itens. */
    public String getNome() {
        return this.nome;
    }

    public String getDescricao() {
        return this.descricao;
    }
}
