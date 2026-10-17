package View;

public class EstiloVisual {

    // Códigos ANSI para as cores do texto
    public static final String RESET = "\u001B[0m";
    public static final String LETRA_BRANCA = "\u001B[37m";
    public static final String LETRA_VERDE = "\u001B[32m";
    public static final String LETRA_AMARELA = "\u001B[33m";
    public static final String LETRA_AZUL = "\u001B[34m";

    // Códigos ANSI para o fundo
    public static final String FUNDO_ESCURO = "\u001B[40m"; // Fundo Preto
    public static final String FUNDO_CLARO = "\u001B[47m";  // Fundo Branco
    public static final String LETRA_PRETA_FORCADA = "\u001B[30m";

    public static void aplicarEstilo() {
        try {
            Model.GerenciadorSave ger = new Model.GerenciadorSave();
            Model.Configuracoes config = ger.carregarDadosGlobais().getConfiguracoes();

            String estiloFinal = "";
            int tema = config.getTemaVisual();

            // 1. Aplica o fundo dependendo da escolha do jogador
            if (tema == 2) {
                estiloFinal += FUNDO_ESCURO; // Fundo Preto
            } else if (tema == 3) {
                estiloFinal += FUNDO_CLARO;  // Fundo Branco
            }
            // Se o tema for 1 (Neutro), não adiciona nenhum código de fundo!

            // 2. Aplica a cor da letra
            if (tema == 3 && config.getCorLetras().equals("Branco")) {
                // Se o fundo for branco e a letra for branca, a letra ficaria invisível
                estiloFinal += LETRA_PRETA_FORCADA;
            } else {
                switch (config.getCorLetras()) {
                    case "Verde" -> estiloFinal += LETRA_VERDE;
                    case "Amarelo" -> estiloFinal += LETRA_AMARELA;
                    case "Azul" -> estiloFinal += LETRA_AZUL;
                    default -> estiloFinal += LETRA_BRANCA;
                }
            }

            // Imprime o código ANSI
            System.out.print(estiloFinal);

        } catch (Exception e) {
            // Se houver erro, não aplica estilo (mantém neutro)
        }
    }

    public static void limparEstilo() {
        System.out.print(RESET);
    }
}