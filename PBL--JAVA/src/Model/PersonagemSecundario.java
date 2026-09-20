/** Declaração do pacote que agrupa as classes do Model. */
package Model;

/** Declaração da classe pública que representa os personagens secundários. */
public class PersonagemSecundario {

    /** Declaração dos atributos do personagem secundário. */
    private String nome;
    private String tipoRelacionamento;
    private int nivelRelacionamento = 40;

    /** Método construtor de personagem secundário, que não recebe como argumento apenas o nível de relacionamento por ser "constante". */
    public PersonagemSecundario(String nome, String tipoRelacionamento) {
        this.nome = nome;
        this.tipoRelacionamento = tipoRelacionamento;
    }

    /** Métodos getters para alterar/definir os atributos dos personagens secundários. */
    public String getNome() {
        return this.nome;
    }

    public String getTipoRelacionamento() {
        return this.tipoRelacionamento;
    }

    public int getNivelRelacionamento() {
        return this.nivelRelacionamento;
    }

    /** Método para atualizar o nível de relacionamento dos personagens secundários. */
    public void atualizarRelacionamento(int valor) {
        this.nivelRelacionamento = this.nivelRelacionamento + valor;

        if (this.nivelRelacionamento > 100) {
            this.nivelRelacionamento = 100;
        }
        else if (this.nivelRelacionamento < 0) {
            this.nivelRelacionamento = 0;
        }
    }
}
