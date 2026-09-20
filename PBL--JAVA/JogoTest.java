package testes;

import Model.*;
import View.CenasView;
import Control.CenasController;
import Control.MenuController;
import Control.PersonagemController;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JogoTest {

    // ---------------------------------------------------------------
    // Utilitários para simular entrada (System.in) e capturar saída (System.out)
    // ---------------------------------------------------------------

    private InputStream originalIn;
    private PrintStream originalOut;

    @BeforeEach
    void guardarStreamsOriginais() {
        originalIn = System.in;
        originalOut = System.out;
    }

    @AfterEach
    void restaurarStreamsOriginais() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    /** Simula o que o "jogador" digitaria no console, linha por linha. */
    private void simularEntrada(String dadosDigitados) {
        System.setIn(new ByteArrayInputStream(dadosDigitados.getBytes(StandardCharsets.UTF_8)));
    }

    /** Redireciona System.out para um buffer e devolve esse buffer para inspeção. */
    private ByteArrayOutputStream capturarSaida() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        return buffer;
    }

    // =================================================================
    // TESTES DA CLASSE Banda
    // =================================================================
    @Nested
    @DisplayName("Testes da classe Banda")
    class BandaTests {

        @Test
        @DisplayName("Harmonia inicia em 50 e respeita o limite superior de 100")
        void harmoniaNaoPassaDe100() {
            Banda banda = new Banda();
            assertEquals(50, banda.getHarmonia());
            banda.alterarHarmonia(1000);
            assertEquals(100, banda.getHarmonia());
        }

        @Test
        @DisplayName("Harmonia respeita o limite inferior de 0")
        void harmoniaNaoFicaNegativa() {
            Banda banda = new Banda();
            banda.alterarHarmonia(-1000);
            assertEquals(0, banda.getHarmonia());
        }

        @Test
        @DisplayName("Essência fica limitada entre -50 e 50")
        void essenciaRespeitaLimites() {
            Banda banda = new Banda();
            banda.alterarEssencia(1000);
            assertEquals(50, banda.getEssencia());
            banda.alterarEssencia(-1000);
            assertEquals(-50, banda.getEssencia());
        }

        @Test
        @DisplayName("Grana pode ser adicionada e debitada corretamente")
        void granaAdicionarEDebitarComSaldo() {
            Banda banda = new Banda();
            banda.adicionarGrana(200);
            assertEquals(200, banda.getGrana());

            boolean conseguiuDebitar = banda.debitarGrana(150);
            assertTrue(conseguiuDebitar);
            assertEquals(50, banda.getGrana());
        }

        @Test
        @DisplayName("Não é possível debitar mais grana do que a banda possui")
        void debitarGranaSemSaldoFalhaENaoAlteraSaldo() {
            Banda banda = new Banda();
            banda.adicionarGrana(50);

            boolean conseguiuDebitar = banda.debitarGrana(999);
            assertFalse(conseguiuDebitar);
            assertEquals(50, banda.getGrana(), "Saldo não deveria mudar quando o débito falha");
        }

        @Test
        @DisplayName("Flag de oferta aceita e caminho do Jorge começam falsos e podem ser alterados")
        void flagsBooleanasDaBanda() {
            Banda banda = new Banda();
            assertFalse(banda.getaceitou());
            assertFalse(banda.getEscolheuJorge());

            banda.alterarAceitouOferta(true);
            banda.definirCaminhoJorge(true);

            assertTrue(banda.getaceitou());
            assertTrue(banda.getEscolheuJorge());
        }
    }

    // =================================================================
    // TESTES DA CLASSE PersonagemSecundario
    // =================================================================
    @Nested
    @DisplayName("Testes da classe PersonagemSecundario")
    class PersonagemSecundarioTests {

        @Test
        @DisplayName("Nome, tipo de relacionamento e valor inicial (40) estão corretos")
        void estadoInicial() {
            PersonagemSecundario jorge = new PersonagemSecundario("Jorge", "Confiança");
            assertEquals("Jorge", jorge.getNome());
            assertEquals("Confiança", jorge.getTipoRelacionamento());
            assertEquals(40, jorge.getNivelRelacionamento());
        }

        @Test
        @DisplayName("Relacionamento não ultrapassa 100")
        void relacionamentoNaoPassaDe100() {
            PersonagemSecundario manuela = new PersonagemSecundario("Manuela", "Afinidade");
            manuela.atualizarRelacionamento(1000);
            assertEquals(100, manuela.getNivelRelacionamento());
        }

        @Test
        @DisplayName("Relacionamento não fica negativo")
        void relacionamentoNaoFicaNegativo() {
            PersonagemSecundario betinho = new PersonagemSecundario("Betinho", "Amizade");
            betinho.atualizarRelacionamento(-1000);
            assertEquals(0, betinho.getNivelRelacionamento());
        }
    }

    // =================================================================
    // TESTES DE Inventario e Item
    // =================================================================
    @Nested
    @DisplayName("Testes de Inventario e Item")
    class InventarioItemTests {

        @Test
        @DisplayName("Inventário novo começa vazio")
        void inventarioComecaVazio() {
            Inventario inv = new Inventario();
            assertTrue(inv.getItens().isEmpty());
        }

        @Test
        @DisplayName("Adicionar item faz temItem retornar true e inspecionarItem retornar o item certo")
        void adicionarEInspecionarItem() {
            Inventario inv = new Inventario();
            Item cavaquinho = new Item("Cavaquinho de Feira", "Lembra a motivação inicial.");
            inv.adicionarItem(cavaquinho);

            assertTrue(inv.temItem("Cavaquinho de Feira"));
            assertEquals(cavaquinho, inv.inspecionarItem("Cavaquinho de Feira"));
            assertEquals("Lembra a motivação inicial.", inv.inspecionarItem("Cavaquinho de Feira").getDescricao());
        }

        @Test
        @DisplayName("Item inexistente retorna false em temItem e null em inspecionarItem")
        void itemInexistente() {
            Inventario inv = new Inventario();
            assertFalse(inv.temItem("Item Fantasma"));
            assertNull(inv.inspecionarItem("Item Fantasma"));
        }

        @Test
        @DisplayName("Remover item faz ele desaparecer do inventário")
        void removerItem() {
            Inventario inv = new Inventario();
            Item panfleto = new Item("Panfleto do Concurso", "Regras da gravadora.");
            inv.adicionarItem(panfleto);
            assertTrue(inv.temItem("Panfleto do Concurso"));

            inv.removerItem(panfleto);
            assertFalse(inv.temItem("Panfleto do Concurso"));
        }
    }

    // =================================================================
    // TESTES DE PersonagemPrincipal
    // =================================================================
    @Nested
    @DisplayName("Testes de PersonagemPrincipal")
    class PersonagemPrincipalTests {

        @Test
        @DisplayName("Atributos começam em zero e podem ser definidos livremente")
        void setattersEGetters() {
            PersonagemPrincipal toin = new PersonagemPrincipal();
            assertEquals(0, toin.getCarisma());
            assertEquals(0, toin.getSamba());
            assertEquals(0, toin.getOuvido());

            toin.setCarisma(50);
            toin.setSamba(40);
            toin.setOuvido(30);

            assertEquals(50, toin.getCarisma());
            assertEquals(40, toin.getSamba());
            assertEquals(30, toin.getOuvido());
        }
    }

    // =================================================================
    // TESTES DE IntegracaoJogo
    // =================================================================
    @Nested
    @DisplayName("Testes de IntegracaoJogo (estado inicial)")
    class IntegracaoJogoTests {

        @Test
        @DisplayName("Todos os sub-objetos do jogo são criados corretamente e com os nomes certos")
        void estadoInicialDoJogo() {
            IntegracaoJogo jogo = new IntegracaoJogo();

            assertNotNull(jogo.getProtagonista());
            assertNotNull(jogo.getBanda());
            assertNotNull(jogo.getInventario());

            assertEquals("Jorge", jogo.getJorge().getNome());
            assertEquals("Confiança", jogo.getJorge().getTipoRelacionamento());

            assertEquals("Manuela", jogo.getManuela().getNome());
            assertEquals("Afinidade", jogo.getManuela().getTipoRelacionamento());

            assertEquals("Betinho", jogo.getBetinho().getNome());
            assertEquals("Amizade", jogo.getBetinho().getTipoRelacionamento());

            assertEquals(50, jogo.getBanda().getHarmonia());
            assertTrue(jogo.getInventario().getItens().isEmpty(),
                    "IntegracaoJogo não deve inserir itens sozinha (isso é feito no Main)");
        }
    }

    // =================================================================
    // TESTES DE Escolha (isoladamente, sem depender do RepositorioCenas)
    // =================================================================
    @Nested
    @DisplayName("Testes da classe Escolha")
    class EscolhaTests {

        @Test
        @DisplayName("Destino fixo é retornado independente do estado do jogo")
        void destinoFixoIgnoraEstadoDoJogo() {
            Escolha escolha = new Escolha("Ir para o capítulo 2", CenasIds.CAP2_CASA_JORGE);
            IntegracaoJogo jogo = new IntegracaoJogo();

            assertEquals(CenasIds.CAP2_CASA_JORGE, escolha.getDestino(jogo));
        }

        @Test
        @DisplayName("Escolha sem efeito não lança exceção ao aplicar efeito")
        void escolhaSemEfeitoNaoQuebra() {
            Escolha escolha = new Escolha("Só avançar", CenasIds.CAP1_BAR);
            IntegracaoJogo jogo = new IntegracaoJogo();

            assertDoesNotThrow(() -> escolha.aplicarEfeito(jogo));
        }

        @Test
        @DisplayName("Efeito da escolha altera o estado do jogo corretamente")
        void efeitoAlteraEstadoDoJogo() {
            Escolha escolha = new Escolha("Ganhar dinheiro", CenasIds.CAP1_BAR,
                    j -> j.getBanda().adicionarGrana(300));
            IntegracaoJogo jogo = new IntegracaoJogo();

            escolha.aplicarEfeito(jogo);

            assertEquals(300, jogo.getBanda().getGrana());
        }

        @Test
        @DisplayName("Destino dinâmico calcula o próximo capítulo com base no estado atual do jogo")
        void destinoDinamicoDependeDoEstado() {
            Escolha escolha = new Escolha(
                    "Testar destino dinâmico",
                    j -> { /* sem efeito colateral */ },
                    j -> (j.getBanda().getGrana() > 0) ? CenasIds.CAP2_CASA_JORGE : CenasIds.CAP1_BAR
            );

            IntegracaoJogo jogoSemGrana = new IntegracaoJogo();
            assertEquals(CenasIds.CAP1_BAR, escolha.getDestino(jogoSemGrana));

            IntegracaoJogo jogoComGrana = new IntegracaoJogo();
            jogoComGrana.getBanda().adicionarGrana(10);
            assertEquals(CenasIds.CAP2_CASA_JORGE, escolha.getDestino(jogoComGrana));
        }
    }

    // =================================================================
    // TESTES DA CLASSE Cena (os 4 construtores)
    // =================================================================
    @Nested
    @DisplayName("Testes da classe Cena (construtores)")
    class CenaTests {

        @Test
        @DisplayName("Construtor de cena interativa (id, texto, escolhas)")
        void construtorCenaInterativa() {
            Escolha escolha = new Escolha("Ir", CenasIds.CAP2_CASA_JORGE);
            Cena cena = new Cena(CenasIds.CAP1_BAR, "Texto de teste", List.of(escolha));

            assertEquals(CenasIds.CAP1_BAR, cena.getId());
            assertEquals("Texto de teste", cena.getTexto());
            assertEquals(1, cena.getEscolhas().size());
            assertFalse(cena.eFim());
        }

        @Test
        @DisplayName("Construtor de cena automática (id, texto, destinoPadrao) não tem escolhas")
        void construtorCenaAutomatica() {
            Cena cena = new Cena(CenasIds.CAP8_JORGE, "Texto automático", CenasIds.CAP9_CAMARIM_FESTIVAL);

            assertTrue(cena.getEscolhas().isEmpty());
            assertFalse(cena.eFim());
            assertEquals(CenasIds.CAP9_CAMARIM_FESTIVAL, cena.destinoPadrao);
        }

        @Test
        @DisplayName("Construtor de cena de fim (id, texto, escolhas, fim) marca eFim() como true")
        void construtorCenaDeFim() {
            Cena cena = new Cena(CenasIds.CAP10_FINAL_HARMONIOSO, "Fim feliz", List.of(), true);

            assertTrue(cena.eFim());
            assertTrue(cena.getEscolhas().isEmpty());
        }

        @Test
        @DisplayName("Construtor completo (5 argumentos) define todos os campos corretamente")
        void construtorCompleto() {
            Cena cena = new Cena(CenasIds.CAP7_GAMEOVER_HARMONIA, "Game over", List.of(), true, null);

            assertEquals(CenasIds.CAP7_GAMEOVER_HARMONIA, cena.getId());
            assertTrue(cena.eFim());
            assertNull(cena.destinoPadrao);
        }
    }

    // =================================================================
    // TESTES DO RepositorioCenas (usando a história real do jogo)
    // =================================================================
    @Nested
    @DisplayName("Testes do RepositorioCenas e da história do jogo")
    class RepositorioCenasTests {

        private RepositorioCenas repositorio;

        @BeforeEach
        void criarRepositorio() {
            repositorio = new RepositorioCenas();
        }

        @Test
        @DisplayName("Cena inicial (CAP1_BAR) existe e tem duas escolhas")
        void cenaInicialExiste() {
            Cena cap1 = repositorio.buscar(CenasIds.CAP1_BAR);
            assertNotNull(cap1);
            assertEquals(2, cap1.getEscolhas().size());
            assertFalse(cap1.eFim());
        }

        @Test
        @DisplayName("Buscar uma CenasIds que nunca foi registrada lança IllegalStateException")
        void buscarCenaNaoRegistradaLancaExcecao() {
            // CAP7_ESTUDIO_GARAGEM existe no enum, mas nunca é passada para registrar()
            // dentro do construtor de RepositorioCenas — então buscar() deve estourar.
            assertThrows(IllegalStateException.class, () -> repositorio.buscar(CenasIds.CAP7_ESTUDIO_GARAGEM));
        }

        @Test
        @DisplayName("Escolha 'pagodeRaiz' do capítulo 1 aumenta essência e relação com Jorge")
        void escolhaPagodeRaizAlteraEstado() {
            IntegracaoJogo jogo = new IntegracaoJogo();
            Cena cap1 = repositorio.buscar(CenasIds.CAP1_BAR);
            Escolha pagodeRaiz = cap1.getEscolhas().get(0);

            pagodeRaiz.aplicarEfeito(jogo);

            assertEquals(20, jogo.getBanda().getEssencia());
            assertEquals(50, jogo.getJorge().getNivelRelacionamento()); // 40 + 10
            assertEquals(CenasIds.CAP2_CASA_JORGE, pagodeRaiz.getDestino(jogo));
        }

        @Test
        @DisplayName("Escolha 'dinheiro' do capítulo 1 reduz essência e aumenta relação com Manuela")
        void escolhaDinheiroAlteraEstado() {
            IntegracaoJogo jogo = new IntegracaoJogo();
            Cena cap1 = repositorio.buscar(CenasIds.CAP1_BAR);
            Escolha dinheiro = cap1.getEscolhas().get(1);

            dinheiro.aplicarEfeito(jogo);

            assertEquals(-20, jogo.getBanda().getEssencia());
            assertEquals(50, jogo.getManuela().getNivelRelacionamento()); // 40 + 10
        }

        @Test
        @DisplayName("Capítulo 6: destino dinâmico manda para GRANA_BAIXA quando grana < 150")
        void capitulo6DestinoGranaBaixa() {
            IntegracaoJogo jogo = new IntegracaoJogo();
            jogo.getBanda().adicionarGrana(100); // < 150

            Escolha testeAmizade = repositorio.buscar(CenasIds.CAP6_ESCRITORIO).getEscolhas().get(0);

            assertEquals(CenasIds.CAP7_ESTUDIO_GARAGEM_GRANA_BAIXA, testeAmizade.getDestino(jogo));
        }

        @Test
        @DisplayName("Capítulo 6: destino dinâmico manda para GRANA_ALTA quando grana >= 150")
        void capitulo6DestinoGranaAlta() {
            IntegracaoJogo jogo = new IntegracaoJogo();
            jogo.getBanda().adicionarGrana(200); // >= 150

            Escolha testeAmizade = repositorio.buscar(CenasIds.CAP6_ESCRITORIO).getEscolhas().get(0);

            assertEquals(CenasIds.CAP7_ESTUDIO_GARAGEM_GRANA_ALTA, testeAmizade.getDestino(jogo));
        }

        @Test
        @DisplayName("Capítulo 7 (sobreviveu): harmonia chega a 0 e o destino é o Game Over")
        void capitulo7LevaAoGameOverQuandoHarmoniaZera() {
            IntegracaoJogo jogo = new IntegracaoJogo();
            jogo.getBanda().alterarHarmonia(-30); // harmonia: 50 -> 20

            Escolha sobreviveu = repositorio.buscar(CenasIds.CAP7_SOBREVIVEU).getEscolhas().get(0);
            sobreviveu.aplicarEfeito(jogo); // harmonia: 20 -> 0

            assertEquals(0, jogo.getBanda().getHarmonia());
            assertEquals(CenasIds.CAP7_GAMEOVER_HARMONIA, sobreviveu.getDestino(jogo));
        }

        @Test
        @DisplayName("Capítulo 7 (sobreviveu): com harmonia suficiente, o jogo continua para a escolha final")
        void capitulo7ContinuaQuandoHarmoniaPositiva() {
            IntegracaoJogo jogo = new IntegracaoJogo(); // harmonia inicial = 50

            Escolha sobreviveu = repositorio.buscar(CenasIds.CAP7_SOBREVIVEU).getEscolhas().get(0);
            sobreviveu.aplicarEfeito(jogo); // harmonia: 50 -> 30

            assertEquals(30, jogo.getBanda().getHarmonia());
            assertEquals(CenasIds.CAP7_ESCOLHA_FINAL, sobreviveu.getDestino(jogo));
        }

        @Test
        @DisplayName("Capítulo 9 (Cenario): sem aceitar oferta do agiota, vai para o caminho B")
        void capitulo9CaminhoBQuandoNaoAceitouOferta() {
            IntegracaoJogo jogo = new IntegracaoJogo(); // aceitouAOferta = false por padrão

            Escolha cenario = repositorio.buscar(CenasIds.CAP9_CAMARIM_FESTIVAL).getEscolhas().get(0);

            assertEquals(CenasIds.CAP9_CAMARIM_FESTIVAL_B, cenario.getDestino(jogo));
        }

        @Test
        @DisplayName("Capítulo 9 (Cenario): tendo aceitado a oferta do agiota, vai para o caminho A")
        void capitulo9CaminhoAQuandoAceitouOferta() {
            IntegracaoJogo jogo = new IntegracaoJogo();
            jogo.getBanda().alterarAceitouOferta(true);

            Escolha cenario = repositorio.buscar(CenasIds.CAP9_CAMARIM_FESTIVAL).getEscolhas().get(0);

            assertEquals(CenasIds.CAP9_CAMARIM_FESTIVAL_A, cenario.getDestino(jogo));
        }

        @Test
        @DisplayName("Capítulo 9B: sem ter escolhido o caminho do Jorge, final é o Harmonioso")
        void capitulo9BFinalHarmoniosoSemCaminhoJorge() {
            IntegracaoJogo jogo = new IntegracaoJogo(); // escolheuJorge = false por padrão

            Escolha decisaoFinalB = repositorio.buscar(CenasIds.CAP9_CAMARIM_FESTIVAL_B).getEscolhas().get(0);

            assertEquals(CenasIds.CAP10_FINAL_HARMONIOSO, decisaoFinalB.getDestino(jogo));
        }

        @Test
        @DisplayName("Capítulo 9B: tendo escolhido o caminho do Jorge, final é o Essencialista")
        void capitulo9BFinalEssencialistaComCaminhoJorge() {
            IntegracaoJogo jogo = new IntegracaoJogo();
            jogo.getBanda().definirCaminhoJorge(true);

            Escolha decisaoFinalB = repositorio.buscar(CenasIds.CAP9_CAMARIM_FESTIVAL_B).getEscolhas().get(0);

            assertEquals(CenasIds.CAP10_FINAL_ESSENCIALISTA, decisaoFinalB.getDestino(jogo));
        }

        @Test
        @DisplayName("Todos os finais do jogo (CAP10 e Game Over) estão marcados como eFim() = true")
        void todosOsFinaisEstaoMarcadosComoFim() {
            assertTrue(repositorio.buscar(CenasIds.CAP10_FINAL_FALSO).eFim());
            assertTrue(repositorio.buscar(CenasIds.CAP10_FINAL_ESSENCIALISTA).eFim());
            assertTrue(repositorio.buscar(CenasIds.CAP10_FINAL_HARMONIOSO).eFim());
            assertTrue(repositorio.buscar(CenasIds.CAP7_GAMEOVER_HARMONIA).eFim());
        }
    }

    // =================================================================
    // TESTES DE PersonagemController (usa entrada simulada)
    // =================================================================
    @Nested
    @DisplayName("Testes de PersonagemController")
    class PersonagemControllerTests {

        @Test
        @DisplayName("Criação de personagem com soma correta (120) na primeira tentativa")
        void criarPersonagemComSomaValida() {
            simularEntrada("50\n40\n30\n"); // soma = 120
            CenasView view = new CenasView();
            PersonagemPrincipal toin = new PersonagemPrincipal();
            PersonagemController controller = new PersonagemController(toin, view);

            ByteArrayOutputStream saida = capturarSaida();
            controller.criarPersonagem();

            assertEquals(50, toin.getCarisma());
            assertEquals(40, toin.getSamba());
            assertEquals(30, toin.getOuvido());
            assertTrue(saida.toString(StandardCharsets.UTF_8).contains("Personagem criado com sucesso"));
        }

        @Test
        @DisplayName("Criação de personagem pede novamente quando a soma é diferente de 120")
        void criarPersonagemComSomaInvalidaDepoisValida() {
            // Primeira tentativa soma 150 (inválida), segunda tentativa soma 120 (válida)
            simularEntrada("50\n50\n50\n" + "40\n40\n40\n");
            CenasView view = new CenasView();
            PersonagemPrincipal toin = new PersonagemPrincipal();
            PersonagemController controller = new PersonagemController(toin, view);

            ByteArrayOutputStream saida = capturarSaida();
            controller.criarPersonagem();

            assertEquals(40, toin.getCarisma());
            assertEquals(40, toin.getSamba());
            assertEquals(40, toin.getOuvido());
            assertTrue(saida.toString(StandardCharsets.UTF_8).contains("distribuiu 150 pontos"));
        }
    }

    // =================================================================
    // TESTES DE MenuController (apenas opções que não exigem uma partida completa)
    // =================================================================
    @Nested
    @DisplayName("Testes de MenuController")
    class MenuControllerTests {

        private MenuController criarMenuControllerDummy() {
            IntegracaoJogo jogo = new IntegracaoJogo();
            CenasView view = new CenasView();
            PersonagemController personagemController = new PersonagemController(jogo.getProtagonista(), view);
            RepositorioCenas repositorio = new RepositorioCenas();
            CenasController cenasController = new CenasController(repositorio, view, jogo);
            return new MenuController(personagemController, cenasController);
        }

        @Test
        @DisplayName("Opção 2 exibe as instruções do jogo")
        void opcaoInstrucoes() {
            simularEntrada(""); // não deve precisar ler nada
            MenuController menu = criarMenuControllerDummy();

            ByteArrayOutputStream saida = capturarSaida();
            menu.processamentoOpcao(2);

            assertTrue(saida.toString(StandardCharsets.UTF_8).contains("O Show Tem de Continuar"));
        }

        @Test
        @DisplayName("Opção 3 exibe os créditos")
        void opcaoCreditos() {
            simularEntrada("");
            MenuController menu = criarMenuControllerDummy();

            ByteArrayOutputStream saida = capturarSaida();
            menu.processamentoOpcao(3);

            String textoSaida = saida.toString(StandardCharsets.UTF_8);
            assertTrue(textoSaida.contains("Créditos"));
            assertTrue(textoSaida.contains("Felipe Vieira Aquino"));
            assertTrue(textoSaida.contains("Thiago Marques Reis"));
        }

        @Test
        @DisplayName("Opção 4 exibe a mensagem de saída")
        void opcaoSaida() {
            simularEntrada("");
            MenuController menu = criarMenuControllerDummy();

            ByteArrayOutputStream saida = capturarSaida();
            menu.processamentoOpcao(4);

            assertTrue(saida.toString(StandardCharsets.UTF_8).contains("Jogo encerrado!"));
        }

        @Test
        @DisplayName("Opção inválida (fora de 1-4) exibe mensagem de erro")
        void opcaoInvalida() {
            simularEntrada("");
            MenuController menu = criarMenuControllerDummy();

            ByteArrayOutputStream saida = capturarSaida();
            menu.processamentoOpcao(99);

            assertTrue(saida.toString(StandardCharsets.UTF_8).contains("Opção inválida"));
        }
    }

    // =================================================================
    // TESTE PONTUAL: efeito de uma escolha isolada do capítulo 5
    // =================================================================
    @Nested
    @DisplayName("Teste pontual: efeito da escolha do capítulo 5")
    class Capitulo5Tests {

        @Test
        @DisplayName("Escolha 'aceitar visual pop' do capítulo 5 altera essência, relação com Manuela e dá o item certo")
        void escolhaAceitarPopAlteraEstadoEDaItem() {
            // Testa a escolha "aceitarPop" do capítulo 5 isoladamente (sem simular o resto
            // da partida): aplica o efeito e confere que a essência caiu, a relação com
            // Manuela subiu, o item "Cartão da Manuela" foi adicionado ao inventário, e o
            // destino aponta corretamente para o capítulo 6.
            IntegracaoJogo jogo = new IntegracaoJogo();
            RepositorioCenas repositorio = new RepositorioCenas();

            Escolha aceitarPop = repositorio.buscar(CenasIds.CAP5_PRACA).getEscolhas().get(0);
            aceitarPop.aplicarEfeito(jogo);

            assertEquals(70, jogo.getManuela().getNivelRelacionamento()); // 40 + 30
            assertEquals(-20, jogo.getBanda().getEssencia());
            assertTrue(jogo.getInventario().temItem("Cartão da Manuela"));
            assertEquals(CenasIds.CAP6_ESCRITORIO, aceitarPop.getDestino(jogo));
        }
    }
}