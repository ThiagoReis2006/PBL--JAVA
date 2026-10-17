package Model;

import java.util.ArrayList;
import java.util.List;

public class DadosGlobais {
    private Configuracoes configuracoes;
    private final List<String> finaisAlcancados;
    private boolean cenaSecretaDesbloqueada; // Nova variável para o conteúdo extra

    public DadosGlobais() {
        this.configuracoes = new Configuracoes();
        this.finaisAlcancados = new ArrayList<>();
        this.cenaSecretaDesbloqueada = false;
    }

    public void adicionarFinal(String nomeFinal) {
        // Adiciona qualquer final (incluindo o Game Over) se ainda não estiver na lista
        if (!finaisAlcancados.contains(nomeFinal)) {
            finaisAlcancados.add(nomeFinal);
        }

        // Conta apenas os finais principais do Capítulo 10
        int contadorFinaisReais = 0;
        for (String finalSalvo : finaisAlcancados) {
            if (finalSalvo.startsWith("CAP10_")) {
                contadorFinaisReais++;
            }
        }

        // Se o jogador alcançou os 3 finais verdadeiros, libera o epílogo!
        if (contadorFinaisReais >= 3) {
            cenaSecretaDesbloqueada = true;
        }
    }

    public Configuracoes getConfiguracoes() {
        return configuracoes;
    }

    public List<String> getFinaisAlcancados() {
        return finaisAlcancados;
    }

    public boolean isCenaSecretaDesbloqueada() {
        return cenaSecretaDesbloqueada;
    }
}