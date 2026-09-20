/** Declaração do pacote que agrupa as classes do Model. */
package Model;

/** Declaração da classe pública que representa a banda. */
public class Banda {

    /** Declaração dos atributos da banda. */
    private int harmonia = 25;
    private int essencia = 0;
    private double grana = 0.0;
    private boolean aceitouAOferta = false;
    private boolean escolheuJorge;

    /** Métodos getters para alterar/definir os atributos da banda. */
    public int getHarmonia() {
        return this.harmonia;
    }

    public int getEssencia() {
        return this.essencia;
    }

    public boolean getaceitou(){
        return this.aceitouAOferta;    }

    public void alterarAceitouOferta(boolean aceitou){
        this.aceitouAOferta=aceitou;
    }

    public double getGrana() {
        return this.grana;
    }

    public boolean getEscolheuJorge() {
        return escolheuJorge;
    }

    public void definirCaminhoJorge(boolean valor) {
        this.escolheuJorge = valor;
    }

    /** Métodos para alterar/definir os atributos da banda. */
    public void alterarHarmonia(int valor) {
        this.harmonia = this.harmonia + valor;
        if (this.harmonia > 100) {
            this.harmonia = 100;
        } else if (this.harmonia < 0) {
            this.harmonia = 0;
        }
    }

    public void alterarEssencia(int valor) {
        this.essencia = this.essencia + valor;
        if (this.essencia > 50) {
            this.essencia = 50;
        } else if (this.essencia < -50) {
            this.essencia = -50;
        }
    }

    public void adicionarGrana(double valor) {
        this.grana = this.grana + valor;
    }

    public boolean debitarGrana(double valor) {
        if (this.grana >= valor) {
            this.grana = this.grana - valor;
            return true;
        } else {
            return false;
        }
    }



}
