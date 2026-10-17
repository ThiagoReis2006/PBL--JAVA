package Model;

public class Configuracoes {
    private boolean ocultarUHD = false;
    // 1 = Neutro (Padrão do Terminal), 2 = Modo Escuro (Preto), 3 = Modo Claro (Branco)
    private int temaVisual = 1;
    private String corLetras = "Branco";

    public boolean isOcultarUHD() { return ocultarUHD; }
    public void setOcultarUHD(boolean ocultarUHD) { this.ocultarUHD = ocultarUHD; }

    public int getTemaVisual() { return temaVisual; }
    public void setTemaVisual(int temaVisual) { this.temaVisual = temaVisual; }

    public String getCorLetras() { return corLetras; }
    public void setCorLetras(String corLetras) { this.corLetras = corLetras; }
}