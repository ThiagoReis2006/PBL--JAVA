package Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
}