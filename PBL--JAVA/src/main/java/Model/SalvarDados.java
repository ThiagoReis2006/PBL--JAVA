package Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SalvarDados {
    private String nomeJogador;
    private String dataHora;
    private String nomeCapitulo;
    private String resumoProgressao;

    private IntegracaoJogo estadoJogo;
    private CenasIds idCenaAtual;

    public SalvarDados(String nomeJogador, String nomeCapitulo, String resumoProgressao, IntegracaoJogo estadoJogo, CenasIds idCenaAtual) {
        this.nomeJogador = nomeJogador;
        this.nomeCapitulo = nomeCapitulo;
        this.resumoProgressao = resumoProgressao;
        this.estadoJogo = estadoJogo;
        this.idCenaAtual = idCenaAtual;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.dataHora = dtf.format(LocalDateTime.now());
    }

    // Getters
    public String getNomeJogador() {
        return nomeJogador;
    }
    public String getDataHora() {
        return dataHora;
    }
    public String getNomeCapitulo() {
        return nomeCapitulo;
    }
    public String getResumoProgressao() {
        return resumoProgressao;
    }
    public IntegracaoJogo getEstadoJogo() {
        return estadoJogo;
    }
    public CenasIds getIdCenaAtual() {
        return idCenaAtual;
    }
}