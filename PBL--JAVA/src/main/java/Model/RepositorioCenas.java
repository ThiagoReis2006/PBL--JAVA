package Model;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class RepositorioCenas {
    private Map<CenasIds, Cena> cenas = new EnumMap<>(CenasIds.class);

    private void registrar(Cena cena) {
        cenas.put(cena.getId(), cena);
    }

    public RepositorioCenas() {
        // CAPÍTULO 1: O BAR DA DONA GERTRUDES
        Escolha pagodeRaiz = new Escolha("O Pagode Raiz, aquele que toca na alma da comunidade.", CenasIds.CAP2_CASA_JORGE, j -> {
            j.getBanda().alterarEssencia(20);
            j.getJorge().atualizarRelacionamento(10);
        });

        Escolha dinheiro = new Escolha("O que der dinheiro. Quero sair do aluguel.", CenasIds.CAP2_CASA_JORGE, j -> {
            j.getBanda().alterarEssencia(-20);
            j.getManuela().atualizarRelacionamento(10);
        });

        registrar(new Cena(CenasIds.CAP1_BAR,
                "\n--- CAPÍTULO 1: O BAR DA DONA GERTRUDES ---\n\n" +
                        "Chove lá fora e o letreiro neon pisca falho.\n" +
                        "A exaustiva viagem de ônibus de Feira de Santana até o Rio de Janeiro cobra seu preço, mas Toin desembarca \n" +
                        "abraçado ao cavaquinho e à esperança do Grande Concurso Musical. No Bar da Dona Gertrudes, enquanto dedilha para afastar a ansiedade, \n" +
                        "ele atrai o olhar de Betinho, um produtor falido e mediano no pandeiro. Betinho propõe formarem um grupo.\n\n" +
                        "[Betinho] Baiano, tu tem o dom! Eu conheço os becos desse Rio de Janeiro. Se a gente montar um grupo pro Concurso,\n" +
                        "eu arrumo os contatos e tu entra com o talento.\n\n" +
                        "Qual vai ser o nosso foco?\n",
                List.of(pagodeRaiz, dinheiro)));


        // CAPÍTULO 2: A CASA DO JORGE
        Escolha testeOuvido = new Escolha("[Avançar] Responder a pergunta técnica de Jorge", CenasIds.CAP3_ESTUDIO, j -> {
            int ouvido = j.getProtagonista().getOuvido();

            if (ouvido < 40) {
                System.out.println("\n[Toin] Eu sigo o pandeiro e deixo o coração falar.");
                System.out.println("[Jorge] Coração não segura andamento. Eu vou, mas não me faça passar vergonha.");
                j.getBanda().alterarHarmonia(-10);
                j.getJorge().atualizarRelacionamento(-15);
            } else if (ouvido > 70) {
                System.out.println("\n[Toin] Jogo uma sétima maior e deixo a dissonância pedir a resolução.");
                System.out.println("[Jorge] Pelo menos você não é surdo. Apareço no ensaio.");
                j.getBanda().alterarHarmonia(10);
                j.getJorge().atualizarRelacionamento(20);
            } else {
                System.out.println("\n[Toin] Eu faço o básico ali no tempo da marcação.");
                System.out.println("[Jorge] É, dá pra cumprir tabela. Apareço no ensaio.");
            }
        });

        registrar(new Cena(CenasIds.CAP2_CASA_JORGE,
                "\n--- CAPÍTULO 2: A CASA DO JORGE ---\n\n" +
                        "Betinho diz que, antes de buscar um cantor, a banda precisa de peso musical. Ele\n" +
                        "guia Toin pelos becos da Lapa até a casa empoeirada do veterano Jorge.\n\n" +
                        "[Jorge] A música acabou com minha vida, moleque. Esse concurso aí é coisa de\n" +
                        "maluco. Mas me diz, pra puxar um partido-alto, como você prepara a entrada pro refrão?\n",
                List.of(testeOuvido)));


        // CAPÍTULO 3: ESTÚDIO DE GARAGEM
        Escolha testeCarisma = new Escolha("[Avançar] Lidar com o ego do Léo", CenasIds.CAP4_BAR, j -> {
            int carisma = j.getProtagonista().getCarisma();

            if (carisma < 40) {
                System.out.println("\n[Toin] Deixa o nome pra lá por enquanto, só canta aí, por favor.");
                System.out.println("[Léo] Sabia que estavam desesperados. O repertório quem puxa sou eu.");
                j.getBanda().alterarHarmonia(-15);
            } else if (carisma > 70) {
                System.out.println("\n[Toin] Léo, sua voz é de ouro, mas aqui a estrela é o pagode. Se entrar no compasso, a gente te faz brilhar de verdade. Topa?");
                System.out.println("[Léo] Eu amasso. Bora.");
                j.getBanda().alterarHarmonia(15);
            } else {
                System.out.println("\n[Toin] O nome da banda a gente decide depois de ensaiar a primeira música, Léo.");
                System.out.println("[Léo] Que seja. Mas eu fico no centro do palco.");
            }
        });

        registrar(new Cena(CenasIds.CAP3_ESTUDIO,
                "\n--- CAPÍTULO 3: ESTÚDIO DE GARAGEM ---\n\n" +
                        "Com o violão garantido, a banda ainda é muda. Betinho passa três dias\n" +
                        "vasculhando as rodas de pagode da cidade até retornar ao estúdio de garagem\n" +
                        "trazendo sua grande promessa. Léo entra no calor sufocante exigindo que seu\n" +
                        "nome vá para o letreiro.\n\n" +
                        "[Léo] (Mascando chiclete) A minha voz carrega a banda. A gente só precisa\n" +
                        "botar meu nome no letreiro. 'Léo e os Batucadas', que tal?\n\n" +
                        "[Jorge] (Fecha a cara) O microfone não vai aguentar o peso do seu ego. Toin, resolve isso.\n",
                List.of(testeCarisma)));


        // CAPÍTULO 4: BAR DA DONA GERTRUDES
        Escolha testeSamba = new Escolha("[Avançar] Reagir à pista vazia", CenasIds.CAP5_PRACA, j -> {
            int samba = j.getProtagonista().getSamba();

            if (samba < 40) {
                System.out.println("\nToin insiste no repertório inicial. O show morre e metade do bar vai embora.");
                j.getBanda().adicionarGrana(50);
                j.getBanda().alterarHarmonia(-10);
            } else if (samba > 70) {
                System.out.println("\nToin acelera o ritmo e puxa um hit dos anos 90. O bar inteiro levanta para batucar nas mesas.");
                j.getBanda().adicionarGrana(300);
                j.getBanda().alterarEssencia(10);
            } else {
                System.out.println("\nToin mantém o ritmo. O bar continua neutro, mas vocês ganham o cachê base.");
                j.getBanda().adicionarGrana(150);
            }
        });

        registrar(new Cena(CenasIds.CAP4_BAR,
                "\n--- CAPÍTULO 4: A ESTREIA ---\n\n" +
                        "A banda estreia no bar para testar o som, mas o público foca nos celulares. A\n" +
                        "apresentação, porém, atraiu a atenção de uma mulher que observava tudo silenciosamente do balcão...\n\n" +
                        "[Dona Gertrudes] Toin, esse pagode lento é lindo, mas não vende cerveja. Eles tão pedindo a conta.\n",
                List.of(testeSamba)));


        // CAPÍTULO 5: PRAÇA DA VILA
        Escolha aceitarPop = new Escolha("Aceitar a mudança comercial.", CenasIds.CAP6_ESCRITORIO, j -> {
            j.getManuela().atualizarRelacionamento(30);
            j.getBanda().alterarEssencia(-20);
            j.getJorge().atualizarRelacionamento(-15);
            j.getInventario().adicionarItem(new Item("Cartão da Manuela", "Permite criar o Plano Moderno no Capítulo 8."));
        });

        Escolha recusarPop = new Escolha("Recusar e manter a tradição.", CenasIds.CAP6_ESCRITORIO, j -> {
            j.getManuela().atualizarRelacionamento(-30);
            j.getBanda().alterarEssencia(20);
            j.getJorge().atualizarRelacionamento(15);
        });

        registrar(new Cena(CenasIds.CAP5_PRACA,
                "\n--- CAPÍTULO 5: PRAÇA DA VILA ---\n\n" +
                        "Manuela aborda Toin na praça logo após o show. Como produtora, ela oferece a\n" +
                        "chave para a final do concurso, exigindo em troca que o grupo adote um visual pop.\n\n" +
                        "[Manuela] Vocês têm química, mas as roupas e o repertório são velhos. Eu boto\n" +
                        "vocês no Festival de Pagode, mas preciso mudar a imagem de vocês pra um pop romântico.\n",
                List.of(aceitarPop, recusarPop)));


        // CAPÍTULO 6: ESCRITORIO DE VITOR
        Escolha testeAmizade = new Escolha(
                "[Avançar]",
                // efeito: continua igual, só os prints de diálogo
                j -> {
                    int amizade = j.getBetinho().getNivelRelacionamento();

                    if (amizade > 70) {
                        System.out.println("Betinho puxa Toin para um canto no estúdio\n" +
                                "[Betinho] Irmão, me perdoa. Eu tava desesperado pra fazer a banda virar e peguei um dinheiro sujo pelas suas costas.\n " +
                                "O Vitor tá cobrando e vai tomar o pouco que restou se a gente não for falar com ele.\n");
                    } else if (amizade > 41 || amizade < 70) {
                        System.out.println("Sem muitas explicações, Betinho arrasta Toin até o escritório gélido de Vitor. O agiota joga\n " +
                                "a verdade na mesa, enquanto Betinho apenas abaixa a cabeça, envergonhado e em silêncio");
                    } else {
                        System.out.println(" Betinho age de má-fé e arrasta Toin até o escritório sob falsos pretextos. [Betinho] Relaxa, baiano.\n " +
                                "Marquei uma reunião com um investidor anjo que vai bancar nossos equipamentos novos hoje mesmo. [Vitor] Investidor?\n " +
                                "Você não contou pro seu amigo que a dívida antiga está no seu nome e que o grupo agora é a minha garantia?\n");
                    }
                },
                // destinoDinamico: SÓ AGORA a grana real do jogador é consultada, no
                // momento em que o jogador de fato avança dessa cena
                j -> (j.getBanda().getGrana() < 150)
                        ? CenasIds.CAP7_ESTUDIO_GARAGEM_GRANA_BAIXA
                        : CenasIds.CAP7_ESTUDIO_GARAGEM_GRANA_ALTA
        );

        registrar(new Cena(CenasIds.CAP6_ESCRITORIO,
                "\n--- CAPÍTULO 6: ESCRITORIO DE VITOR ---\n" +
                        " Ar-condicionado congelante e um clima sombrio\n",
                List.of(testeAmizade)));


        //Escolha obrigatoria ou n dependendo do nivbel de grana
        Escolha aceitarOferta = new Escolha("Aceitar", CenasIds.CAP7_ESTUDIO_GARAGEM_CAOS_ACEITOU, j -> {
            j.getBanda().adicionarGrana(1000);
            j.getBanda().alterarEssencia(-40);
            j.getJorge().atualizarRelacionamento(-50);
            j.getBanda().alterarAceitouOferta(true);
            j.getInventario().adicionarItem(new Item("Contrato do Vitor", "Dá ao agiota o controle total sobre a banda no festival."));
        });

        Escolha recusarOferta = new Escolha("Recusar", CenasIds.CAP7_ESTUDIO_GARAGEM_CAOS_RECUSOU, j -> {
            j.getBanda().adicionarGrana(1000);
            j.getBanda().alterarEssencia(-40);
            j.getJorge().atualizarRelacionamento(-50);
            j.getInventario().adicionarItem(new Item("Microfone com Fita Isolante", "Equipamento que causa discussões na banda."));
        });

        registrar(new Cena(CenasIds.CAP7_ESTUDIO_GARAGEM_GRANA_BAIXA,
                "[Vitor] O Betinho já é parceiro antigo da casa. Eu dou os equipamentos e quito a dívida dele. Em troca,\n " +
                        "fico com 50% dos cachês e escolho a música que vão tocar no festival.\n",
                List.of(aceitarOferta)));

        registrar(new Cena(CenasIds.CAP7_ESTUDIO_GARAGEM_GRANA_ALTA,
                "[Vitor] O Betinho já é parceiro antigo da casa. Eu dou os equipamentos e quito a dívida dele. Em troca,\n " +
                        "fico com 50% dos cachês e escolho a música que vão tocar no festival.\n",
                List.of(aceitarOferta, recusarOferta)));


        //CAPITULO 7:Estúdio de Garagem
        Escolha razaoLeo = new Escolha("Léo tem razão. Precisamos pensar grande e ser profissionais se quisermos os R$ 10.000, Jorge. " +
                "Chega de amadorismo.", CenasIds.CAP8_RUAS_VILA, j -> {
            j.getManuela().atualizarRelacionamento(15);
            j.getJorge().atualizarRelacionamento(-15);
        });

        Escolha razaoJorge = new Escolha("Jorge é nosso mestre. O pagode é maior que o seu ego, Léo. E Betinho, " +
                "você vai consertar a burrada que fez", CenasIds.CAP8_RUAS_VILA, j -> {
            j.getJorge().atualizarRelacionamento(15);
            j.getBetinho().atualizarRelacionamento(-15);
        });

        Escolha sobreviver = new Escolha("Ninguém solta a mão de ninguém! Betinho errou, mas é família. " +
                "Vamos engolir o choro e tocar!", CenasIds.CAP8_RUAS_VILA, j -> {
            j.getBetinho().atualizarRelacionamento(15);
            j.getBanda().alterarEssencia(-10);
        });


        registrar(new Cena(CenasIds.CAP7_ESTUDIO_GARAGEM_CAOS_ACEITOU,
                "\n--- CAPÍTULO 7: ESTUDIO DE GARAGEM ---\n" +
                        " Caos absoluto e instrumentos jogados\n" +
                        "[Léo] Eu não vou cantar num festival com fita isolante segurando o microfone! É humilhante!\n" +
                        "[Jorge] Humilhante é o Betinho quase afundar a gente perdendo nosso caixa. Não toco com moleque\n" +
                        ",  nem com ladrão.\n",
                CenasIds.CAP7_SOBREVIVEU));

        registrar(new Cena(CenasIds.CAP7_ESTUDIO_GARAGEM_CAOS_RECUSOU,
                "\n--- CAPÍTULO 7: ESTUDIO DE GARAGEM ---\n" +
                        " Caos absoluto e instrumentos jogados\n" +
                        "[Léo] Finalmente um microfone de primeira! Mas o Jorge tá aí ameaçando ir embora só\n " +
                        "porque o Vitor mandou a música do festival. Se decidam, ou eu meto o pé! \n" +
                        "[Jorge] E você, Betinho? Vendeu a nossa alma pra cobrir sua dívida com bandido? \n" +
                        "Não toco com ladrão.\n",
                CenasIds.CAP7_SOBREVIVEU));

        registrar(new Cena(CenasIds.CAP7_GAMEOVER_HARMONIA,
                "GAME OVER- A harmonia da banda é de 0.", List.of(), true));

        Escolha sobreviveu = new Escolha(
                "[AVANÇAR]",
                j -> {
                },
                j -> {
                    if (j.getBanda().getHarmonia() <= 0) {
                        return CenasIds.CAP7_GAMEOVER_HARMONIA;
                    } else {
                        j.getBanda().alterarHarmonia(-20);
                        return CenasIds.CAP7_ESCOLHA_FINAL;
                    }
                }
        );

        registrar(new Cena(CenasIds.CAP7_SOBREVIVEU,
                "Após evitar que a banda acabe, Toin precisa decidir para quem direcionar " +
                        "seu apoio para colar os cacos do grupo.\n",
                List.of(sobreviveu)));

        registrar(new Cena(CenasIds.CAP7_ESCOLHA_FINAL,
                "Escolha:\n", List.of(razaoLeo, razaoJorge, sobreviver)));


        //CAPITULO 8:Ruas da Vila
        Escolha vistarJorge = new Escolha("Visitar Jorge", CenasIds.CAP8_JORGE, j -> {
            j.getBanda().definirCaminhoJorge(true);
            j.getInventario().adicionarItem(new Item("Caderno do Jorge", "Contém os acordes perfeitos para o Show Mágico."));
        });

        Escolha visitarManuela = new Escolha("Visitar Manuela", CenasIds.CAP8_MANUELA, j -> {
            j.getBanda().definirCaminhoJorge(false);
        });

        registrar(new Cena(CenasIds.CAP8_RUAS_VILA, "\n--- CAPÍTULO 8: RUAS DA VILA ---\n" +
                "Na véspera da decisão, você caminha pela cidade para buscar sua última cartada:\n" +
                " visitar Jorge para aprender o \"Show Mágico\" \n" +
                "ou procurar Manuela para fechar o \"Plano Moderno\". \n", List.of(vistarJorge, visitarManuela)));

        //NAO COLOQUEI SE CONFIANLÇA OU AFINCIADE > QUE AGUMA COISA
        //SE SCOLHER VISTAR JORGE
        registrar(new Cena(CenasIds.CAP8_JORGE, " Ele perdoa o grupo e ensina o \"Show Mágico\", a progressão de \n" +
                "acordes perfeita.\n", CenasIds.CAP9_CAMARIM_FESTIVAL));

        //SE ESCOLHER VISITAR MANUELA
        registrar(new Cena(CenasIds.CAP8_MANUELA, "Ela aceita o seu argumento de manter o cavaco e cria o \"Plano Moderno\", " +
                "unindo o som tradicional com a produção de estúdio.\n", CenasIds.CAP9_CAMARIM_FESTIVAL));


        //CAPITULO 9:Camarim Festival

        Escolha Cenario = new Escolha(
                "[AVANÇAR]",
                j -> {
                },
                j -> (j.getBanda().getaceitou() == true)
                        ? CenasIds.CAP9_CAMARIM_FESTIVAL_A
                        : CenasIds.CAP9_CAMARIM_FESTIVAL_B
        );

        registrar(new Cena(CenasIds.CAP9_CAMARIM_FESTIVAL, "\n--- CAPÍTULO 9: ESTUDIO DE GARAGEM ---\n", List.of(Cenario)));

        registrar(new Cena(CenasIds.CAP9_CAMARIM_FESTIVAL_A, "Vitor invade o camarim com postura de dono, cobrando o contrato que financiou a banda.\n" +
                "[Vitor] Chegou a hora. Eu paguei a inscrição e os equipamentos de vocês. Se não tocarem a música pop da minha gravadora parceira, eu tomo a banda e quebro as pernas do Betinho. \n" +
                "[Toin] (Baixando a cabeça) A gente não tem escolha... \n", CenasIds.CAP10_FINAL_FALSO));

        Escolha decisaoFinalB = new Escolha(
                "[AVANÇAR]",
                j -> {
                },   // sem efeito, só decide o destino
                j -> (j.getBanda().getEscolheuJorge())
                        ? CenasIds.CAP10_FINAL_ESSENCIALISTA
                        : CenasIds.CAP10_FINAL_HARMONIOSO
        );

        registrar(new Cena(CenasIds.CAP9_CAMARIM_FESTIVAL_B,
                "Vitor tenta invadir o camarim para fazer uma chantagem desesperada cobrando a dívida pessoal antiga de Betinho, mas não tem poder sobre a banda.\n" +
                        "[Vitor] O concurso é hoje, Betinho. Acha que eu esqueci o que você me deve só porque o seu amiguinho não quis meu patrocínio? \n" +
                        "[Toin] (Ficando na frente de Betinho, junto com Jorge e Léo) A banda não é sua, Vitor. E o Betinho tá com a gente. Nós vamos ganhar esse prêmio de R$ 10.000 por mérito próprio e ele vai quitar o \n" +
                        "que te deve à vista. Agora vaza do nosso camarim! \n",
                List.of(decisaoFinalB)));


        // CAPÍTULO 10
        registrar(new Cena(CenasIds.CAP10_FINAL_FALSO,
                "\n--- CAPÍTULO 10: O PALCO PRINCIPAL (FINAL FALSO SUCESSO) ---\n\n" +
                        "Vocês tocam o pop genérico imposto pelo agiota. O público vai à loucura e o som viraliza. Nos bastidores, Jorge junta seu violão, olha para Toin com decepção \n" +
                        "profunda e vai embora na chuva. Manuela despede Betinho para limpar a imagem do grupo para os executivos. O jogo termina com você rico, morando em um apartamento de luxo, mas cantando músicas sem alma ao lado\n" +
                        "do arrogante Léo. \n",
                List.of(), true));

        registrar(new Cena(CenasIds.CAP10_FINAL_ESSENCIALISTA,
                "\n--- CAPÍTULO 10: O PALCO PRINCIPAL (FINAL ESSENCIALISTA) ---\n\n" +
                        "A banda ignora as pressões comerciais e executa o Show Mágico em um partido-alto monumental. A plateia delira, mas os jurados engravatados desclassificam o grupo \n" +
                        "por \"falta de apelo pop\". Vocês perdem o contrato. O jogo avança no tempo e retorna ao bar da Dona Gertrudes, completamente lotado. A banda não ganhou o concurso, mas com o bar lotado todas as noites, arrecadaram o\n" +
                        "suficiente na raça para livrar Betinho do agiota. Vocês continuam sem dinheiro de gravadora, mas viraram lendas locais, donos da própria arte e com uma amizade inquebrável.\n\n",
                List.of(), true));

        registrar(new Cena(CenasIds.CAP10_FINAL_HARMONIOSO,
                "\n--- CAPÍTULO 10: O PALCO PRINCIPAL (FINAL HARMONIOSO) ---\n\n" +
                        "Toin aplica o Plano Moderno, unindo a técnica raiz de Jorge com a produção brilhante de Manuela em um arranjo inovador. Os jurados ficam em choque com a originalidade.\n" +
                        "A banda vence o festival por mérito puro e técnico. Vocês assinam com a gravadora gigante, mas conseguem manter o controle criativo. O jogo termina no palco principal, dominando o mercado sem nunca esquecer de onde vieram,\n" +
                        "todos unidos.\n",
                List.of(), true));
    }

    public Cena buscar(CenasIds id) {
        Cena c = cenas.get(id);
        if (c == null) throw new IllegalStateException("Cena não encontrada: " + id);
        return c;
    }
}