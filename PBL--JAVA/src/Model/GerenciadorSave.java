package Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class GerenciadorSave {

    private Gson gson;

    public GerenciadorSave() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public SalvarDados carregar(int slot) throws Exception {
        String caminhoArquivo = "save_slot_" + slot + ".json";
        try (FileReader reader = new java.io.FileReader(caminhoArquivo)) {
            return gson.fromJson(reader, SalvarDados.class);
        } catch (Exception e) {
            throw new Exception("Falha ao carregar ou save inexistente no slot " + slot);
        }
    }

    public void salvar(SalvarDados dados, int slot) throws FalhaSalvamentoException {
        String caminhoArquivo = "save_slot_" + slot + ".json";

        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            gson.toJson(dados, writer);
        } catch (IOException e) {
            throw new FalhaSalvamentoException("Erro ao tentar salvar o jogo no slot " + slot + ": " + e.getMessage());
        }
    }

    //Verifica se o arquivo existe e se tem algo dentro dele
    public boolean slotTemSave(int slot) {
        File arquivo = new File("save_slot_" + slot + ".json");
        return arquivo.exists() && arquivo.length() > 0;
    }

    //determina o slot mais antigo
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
}