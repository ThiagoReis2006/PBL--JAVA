/** Declaração do pacote que agrupa as classes do Model. */
package Model;

/** Declaração da classe pública que representa o personagem principal. */
public class PersonagemPrincipal {

    /** Declaração dos atributos do personagem principal. */
    String nome = "Toin";
    private int carismaDeBotequim = 0;
    private int sambaNoPe = 0;
    private int ouvidoAbsoluto = 0;

    /** Métodos setters para alterar/definir os atributos do personagem principal. */
    public void setCarisma(int carismaDeBotequim) {
        this.carismaDeBotequim = carismaDeBotequim;
    }

    public void setSamba(int sambaNoPe){
        this.sambaNoPe = sambaNoPe;
    }

    public void setOuvido(int ouvidoAbsoluto){
        this.ouvidoAbsoluto = ouvidoAbsoluto;
    }

    /** Métodos getters para ler/recuperar os atributos do personagem principal. */
    public int getCarisma() {
        return this.carismaDeBotequim;
    }

    public int getSamba() {
        return this.sambaNoPe;
    }

    public int getOuvido() {
        return this.ouvidoAbsoluto;
    }
}