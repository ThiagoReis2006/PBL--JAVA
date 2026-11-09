package Model;

import java.util.ArrayList;
import java.util.List;

// Classe que representa o inventário (a mochila) do jogador, 
// onde ficam guardados todos os itens coletados durante a história.
public class Inventario {

    // Cria uma lista vazia para guardar os itens do jogo
    private List<Item> itens = new ArrayList<>();

    // Método para guardar um novo item na mochila
    public void adicionarItem (Item item) {
        this.itens.add(item);
    }

    // Método para jogar fora ou gastar um item da mochila
    public void removerItem (Item item) {
        this.itens.remove(item);
    }

    // Verifica se o jogador tem um item específico guardado (usando o nome do item)
    public boolean temItem(String nomeDoItem) {
        // Passa por cada item que está dentro da lista
        for (Item i : itens) {
            // Se achar algum item com o nome exato que estamos procurando, retorna "verdadeiro" (tem o item)
            if (i.getNome().equals(nomeDoItem)) {
                return true;
            }
        }
        // Se olhar a lista toda e não achar nada, retorna "falso" (não tem o item)
        return false;
    }

    // Busca um item pelo nome para pegar as informações dele (como a descrição, por exemplo)
    public Item inspecionarItem(String nomeDoItem) {
        // Passa por cada item da lista
        for (Item i : itens) {
            // Se achar o item pelo nome, devolve o item inteiro para quem pediu
            if (i.getNome().equals(nomeDoItem)) {
                return i;
            }
        }
        // Se não achar nada, devolve "vazio" (null)
        return null;
    }

    // Devolve a lista completa com todos os itens que estão no inventário agora
    public List<Item> getItens() {
        return this.itens;
    }
}