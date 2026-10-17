package Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException; // Importante para detectar JSON quebrado
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class GerenciadorSave {

    private Gson gson;

    public GerenciadorSave() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    // lança as duas exceções
    public SalvarDados carregar(int slot) throws FalhaCarregamentoException, DadosInvalidosException {
        String caminhoArquivo = "save_slot_" + slot + ".json";
        File arquivo = new File(caminhoArquivo);

        // 1. Verifica se o arquivo existe e tem conteúdo
        if (!arquivo.exists() || arquivo.length() == 0) {
            throw new FalhaCarregamentoException("O Slot " + slot + " está vazio ou não existe.");
        }

        try (FileReader reader = new FileReader(arquivo)) {
            SalvarDados dados = gson.fromJson(reader, SalvarDados.class);

            // 2. Verifica se o JSON leu algo válido (Ex: se não faltou o nome do jogador)
            if (dados == null || dados.getNomeJogador() == null || dados.getNomeJogador().trim().isEmpty()) {
                throw new DadosInvalidosException("O save do Slot " + slot + " está corrompido ou com dados ausentes.");
            }

            return dados;

        } catch (JsonSyntaxException e) {
            // Dispara se alguém abrir o JSON no bloco de notas e apagar uma chave sem querer
            throw new DadosInvalidosException("Formato inválido! O arquivo do Slot " + slot + " foi corrompido.");
        } catch (IOException e) {
            // Falha física de leitura (ex: falta de permissão no Windows)
            throw new FalhaCarregamentoException("Erro de leitura no arquivo do Slot " + slot + ": " + e.getMessage());
        }
    }

    public void salvar(SalvarDados dados, int slot) throws FalhaSalvamentoException {
        // Validação extra para não salvar um jogo nulo
        if (dados == null) {
            throw new FalhaSalvamentoException("Os dados do jogo estão vazios e não podem ser salvos.");
        }

        String caminhoArquivo = "save_slot_" + slot + ".json";

        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            gson.toJson(dados, writer);
        } catch (IOException e) {
            throw new FalhaSalvamentoException("Erro ao tentar salvar o jogo no slot " + slot + ": " + e.getMessage());
        }
    }

    // Verifica se o arquivo existe e se tem algo dentro dele
    public boolean slotTemSave(int slot) {
        File arquivo = new File("save_slot_" + slot + ".json");
        return arquivo.exists() && arquivo.length() > 0;
    }

    // Determina o slot mais antigo
    public int slotMaisAntigo() {
        int escolhido = 1;
        long menorData = Long.MAX_VALUE;

        for (int slot = 1; slot <= 3; slot++) {
            long data = new File("save_slot_" + slot + ".json").lastModified();
            if (data < menorData) {
                menorData = data;
                escolhido = slot;
            }
        }
        return escolhido;
    }

    public int slotMaisRecente() {
        int escolhido = -1;
        long maiorData = 0;

        for (int slot = 1; slot <= 3; slot++) {
            File arquivo = new File("save_slot_" + slot + ".json");
            if (arquivo.exists() && arquivo.length() > 0) {
                long data = arquivo.lastModified();
                if (data > maiorData) {
                    maiorData = data;
                    escolhido = slot;
                }
            }
        }
        return escolhido; // -1 se nenhum slot tem save
    }

    public boolean deletar(int slot) {
        File arquivo = new File("save_slot_" + slot + ".json");
        return arquivo.exists() && arquivo.delete();
    }

    public void salvarDadosGlobais(DadosGlobais dadosGlobais) throws FalhaSalvamentoException {
        try (FileWriter writer = new FileWriter("dados_globais.json")) {
            gson.toJson(dadosGlobais, writer);
        } catch (IOException e) {
            throw new FalhaSalvamentoException("Erro ao salvar os dados globais: " + e.getMessage());
        }
    }

    public DadosGlobais carregarDadosGlobais() {
        File arquivo = new File("dados_globais.json");
        if (arquivo.exists() && arquivo.length() > 0) {
            try (FileReader reader = new FileReader(arquivo)) {
                return gson.fromJson(reader, DadosGlobais.class);
            } catch (Exception e) {
                return new DadosGlobais();
            }
        }
        return new DadosGlobais();
    }
}