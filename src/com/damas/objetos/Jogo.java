package com.damas.objetos;

import java.util.ArrayList;

/**
 * Armazena o tabuleiro e responsavel por posicionar as pecas.
 *
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;

 */
public class Jogo {

    private Tabuleiro tabuleiro;
    private Jogador jogadorUm;
    private Jogador jogadorDois;
    private Cor vezAtual; // é necessário iniciar com o valor o valor 1 ou 2
    private int jogadas = 0;
    private int jogadasSemComerPeca = 0;
    private ArrayList<Casa> pecasAComer;
    private Casa casaBloqueadaOrigem;

    public Jogo() {
        tabuleiro = new Tabuleiro();
        pecasAComer = new ArrayList<Casa>();
        jogadorUm = new Jogador("player branco");
        jogadorDois = new Jogador("player vermelho");

        vezAtual = Cor.BRANCA;
        jogadas = 0;
        jogadasSemComerPeca = 0;
        casaBloqueadaOrigem = null;

        colocarPecas(tabuleiro);
    }

    /**
     * Realiza uma serie de paços para comandar uma peça na posicão
     * (origemX, origemY) fazer um movimento para (destinoX, destinoY).
     *
     * @param origemX - {@code int} linha da Casa de origem.
     * @param origemY - {@code int} coluna da Casa de origem.
     * @param destinoX - {@code int} linha da Casa de destino.
     * @param destinoY - {@code int} coluna da Casa de destino.
     */
    public void moverPeca(int origemX, int origemY, int destinoX, int destinoY) {
        // casa de origem
        Casa origem = tabuleiro.getCasa(origemX, origemY);

        // casa de destino
        Casa destino = tabuleiro.getCasa(destinoX, destinoY);
        Peca peca = origem.getPeca(); // pega a peça de origem

        // indentifica
        if (casaBloqueadaOrigem == null) {
            if (peca.podeMover(vezAtual)) {

                if (peca.isMovimentoValido(destino)) {

                    if (simularMovimentoEValidar(origem, destino)) {

                        peca.mover(destino);

                        if (pecasAComer.size() > 0) {
                            this.comerPecas();
                            if (deveContinuarJogando(destino)) {
                                this.casaBloqueadaOrigem = destino;
                            } else {
                                trocarDeVez();
                            }
                        } else {
                            jogadasSemComerPeca++;
                            trocarDeVez();
                        }

                        jogadas++;
                        if (podeTransformarParaDama(destino)) transformarPedraParaDama(destino);
                    }
                }
            }
        } else {
            if ((origem.equals(casaBloqueadaOrigem))) {
                if(simularMovimentoEValidar(origem, destino)) {
                    if (pecasAComer.size() != 0) {
                        casaBloqueadaOrigem = null;
                        moverPeca(origemX, origemY, destinoX, destinoY);
                    }
                }
            }
        }
    }

    /**
     * <p>
     * Percorre as casas da casa de origem clicada até a casa de destino clicada,
     * verifica se o caminho é valido e adiciona casas a variável {@code pecasAComer}
     * </p>
     *
     * @param origem {@code Casa} de origem
     * @param destino {@code Casa} de destino
     * @return {@code boolean} se a simulação ocorreu bem
     */
    private boolean simularMovimentoEValidar(Casa origem, Casa destino) {
        Peca peca = origem.getPeca();
        int casasComPecaSeguidas = 0;

        if (destino.getPeca() != null) return false;

        // SENTIDO DO MOVIMENTO E DISTÂNCIA DO MOVIMENTO
        //  desntinoX = 6, origemX = 4
        // destinoY = 4, origemY = 2
        int sentidoX = (destino.getX() - origem.getX()); // tem que ser sempre ou mais
        int sentidoY = (destino.getY() - origem.getY());
        // sentidoX = 6 - 4 = 2
        // sentidoY = 4 - 2 = 2
        int distanciaX = Math.abs(sentidoX); // garante valores positivos
        int distanciaY = Math.abs(sentidoY);
        // distanciaY = (2)
        // distanciaX = (2)

        if ((distanciaX == 0) || (distanciaY == 0)) return false;

        sentidoX = sentidoX/distanciaX;
        sentidoY = sentidoY/distanciaY;

        // sentidoY = 1
        // sentidoX = 1

        // REGRA DE MOVIMENTO DAS PEDRAS NO TABULEIRO CASO A DISTÂNCIA ATÉ A CASA CLICADA SEJA DE 2 BLOCOS
        if ((distanciaX == 2 && distanciaY == 2) &&
                ((peca.getCor() == Cor.BRANCA) || (peca.getCor() == Cor.VERMELHA))) {

            Casa casa = tabuleiro.getCasa((destino.getX() - sentidoX), (destino.getY() - sentidoY));
            if (casa.getPeca() == null) return false;
        } else {

            // REGRA DE MOVIMENTO DAS PEDRAS NO TABULEIRO CASO A DISTÂNCIA ATÉ A CASA CLICADA SEJA DE 1 BLOCO
            if (peca.getCor() == Cor.BRANCA) {
                if ((distanciaX == 1 || distanciaY == 1) && (distanciaX == distanciaY) && sentidoY == 1) {
                    return true;
                } else {
                    return false;
                }
            } else {
                // REGRA DE MOVIMENTO DAS PEDRAS VERMELHAS
                if (peca.getCor() == Cor.VERMELHA) {
                    if ((distanciaX == 1 || distanciaY == 1) && (distanciaX == distanciaY) && sentidoY == -1) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }

        //PERCORRER AS CASAS E VERIFICAR:
        // 1 - SE HÁ MAIS DE UMA PEÇA SEGUIDA NO CAMINHO (VERDADEIRO RETORNA FALSO)
        // 2 - SE HÁ UMA PEÇA NO CAMINHO E É DA MESMA COR (VERDADEIRO RETORNA FALSO)
        int i = origem.getX();
        int j = origem.getY();

        while (!((i == destino.getX()) || (j == destino.getY()))) {
            i += sentidoX;
            j += sentidoY;

            Casa alvo = tabuleiro.getCasa(i, j);
            Peca pecaAlvo = alvo.getPeca();

            if (!(pecaAlvo == null)) {
                casasComPecaSeguidas += 1;

                // VE SE TEM UMA PECA DO MESMO TIPO NO CAMNHO, CASO TENHA, RETORNA FALSE
                if ((peca.getCor() == Cor.BRANCA || peca.getCor() == Cor.BRANCA) && (pecaAlvo.getCor() == Cor.BRANCA || pecaAlvo.getCor() == Cor.BRANCA)) {
                    if (pecasAComer.size() > 0) pecasAComer.removeAll(pecasAComer);
                    return false;
                }

                if ((peca.getCor() == Cor.BRANCA || peca.getCor() == Cor.BRANCA) && (pecaAlvo.getCor() == Cor.BRANCA || pecaAlvo.getCor() == Cor.BRANCA)) {
                    if (pecasAComer.size() > 0) pecasAComer.removeAll(pecasAComer);
                    return false;
                }

            } else {

                // VE SE HÁ PEÇA PARA COMER NO CAMINHO E PASSAR A CASA À COLEÇÃO pecasAComer() PARA DEPOIS COME-LAS
                if (casasComPecaSeguidas == 1) {
                    Casa casa = tabuleiro.getCasa((alvo.getX() - sentidoX), (alvo.getY() - sentidoY));
                    pecasAComer.add(casa);
                }
                casasComPecaSeguidas = 0;
            }

            if (casasComPecaSeguidas == 2) {
                if (pecasAComer.size() > 0) pecasAComer.removeAll(pecasAComer);
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Percorre as casas do tabuleirio a partir da casa de origem indicada no sentido dado
     * por {@code sentidoX} e {@code sentidoY} até o limite do tabuleiro.
     * </p>
     * @param origem Casa de origem da peça
     * @param deltaX {@code Tabuleiro.X_ESQUERDA} ou {@code Tabuleiro.X_DIREITA}
     * @param deltaY {@code Tabuleiro.Y_BAIXO} ou {@code Tabuleiro.Y_CIMA}
     * @return
     * {@code false} - se não há peça para comer
     * <li> {@code false} - se houver mais de uma peça no caminho </li>
     * <li> {@code false} - se houver peça de mesma cor no caminho </li>
     * <li> {@code true} - se há peça para comer </li>
     */
    private boolean percorrerEVerificar(Casa origem, int deltaX, int deltaY) {

        Peca peca = origem.getPeca();
        int x = origem.getX();
        int y = origem.getY();
        int pecasSeguidasNoCaminho = 0;

        // SE O TIPO FOR PEDRA
        if ((peca.getCor() == Cor.BRANCA) || (peca.getCor() == Cor.BRANCA)) {

            x += deltaX;
            y += deltaY;

            try {

                Peca pecaAtual = tabuleiro.getCasa(x, y).getPeca();

                if (!( pecaAtual == null)) {

                    if (tabuleiro.getCasa((x + deltaX), (y + deltaY)).getPeca() != null) {
                        return false;
                    }

                    // VERIFICA SE A PEÇA NO CAMINHO É DA MESMA COR
                    if ((peca.getCor() == Cor.BRANCA) &&
                            ((pecaAtual.getCor() == Cor.BRANCA|| pecaAtual.getCor() == Cor.BRANCA))) {
                        return false;
                    } else {
                        if ((peca.getCor() == Cor.VERMELHA) &&
                                ((pecaAtual.getCor() == Cor.VERMELHA|| pecaAtual.getCor() == Cor.VERMELHA))) {
                            return false;
                        }
                    }

                    return true;
                }

            } catch (Exception e) {
                return false;
            }

        } else {
            while (!((x == -1 || x == 8) || (y == -1 || y == 8))) {
                x += deltaX;
                y += deltaY;

                try {
                    Peca pecaAtual = tabuleiro.getCasa(x, y).getPeca();

                    if (!( pecaAtual == null)) {

                        pecasSeguidasNoCaminho += 1;

                        // VERIFICA SE HÁ ALGUMA PEÇA DO MESMO TIPO NO CAMINHO SE SIM, RETORNA FALSE;
                        if ((peca.getCor() == Cor.BRANCA) &&
                                ((pecaAtual.getCor() == Cor.BRANCA) || (pecaAtual.getCor() == Cor.BRANCA))) {
                            return false;
                        } else {
                            if ((peca.getCor() == Cor.VERMELHA) &&
                                    ((pecaAtual.getCor() == Cor.VERMELHA) || (pecaAtual.getCor() == Cor.VERMELHA))) {
                                return false;
                            }
                        }
                    } else {

                        if (pecasSeguidasNoCaminho == 1) {
                            return true;
                        }

                        if (pecasSeguidasNoCaminho == 2) {
                            return false;
                        }
                    }
                } catch (Exception e) {
                    return false;
                }
            }
        }

        return false;
    }

    /**
     * <p>
     * Dispara o método {@code percorrerEVerificar()} no sentido
     * das quatro diagonais a partir da casa indicada.
     * </p>
     * @param origem tipo {@code Casa} de onde vai partir a verifição
     * @return {@code true} Se há peça para comer em alguma diagonal
     */
    private boolean deveContinuarJogando(Casa origem) {

        if (percorrerEVerificar(origem, Tabuleiro.X_ESQUERDA, Tabuleiro.Y_CIMA)) {
            return true;
        } else {

            if (percorrerEVerificar(origem, Tabuleiro.X_DIREITA, Tabuleiro.Y_CIMA)) {
                return true;
            } else {

                if (percorrerEVerificar(origem, Tabuleiro.X_DIREITA, Tabuleiro.Y_BAIXO)) {
                    return true;
                } else {

                    if (percorrerEVerificar(origem, Tabuleiro.X_ESQUERDA, Tabuleiro.Y_BAIXO)) {
                        return true;
                    }
                }
            }

        }

        return false;
    }

    /**
     * Limpa as peças na variável {@code ArrayList pecasAComer}, adiciona pontos ao jogador
     */
    private void comerPecas() {
        int pecasComidas = pecasAComer.size();

        if (getVez() == Cor.BRANCA) jogadorUm.addPonto(pecasComidas);
        if (getVez() == Cor.VERMELHA) jogadorDois.addPonto(pecasComidas);

        for (Casa casa : pecasAComer) {
            casa.removerPeca();
        }

        pecasAComer.removeAll(pecasAComer);

        jogadasSemComerPeca = 0;
    }

    /**
     * Verifica se a pedra da casa pode virar dama.
     * @param casa {@code Casa}
     * @return {@code boolean}
     */
    private boolean podeTransformarParaDama(Casa casa) {

        // REGRA PARA PEÇAS BRANCAS
        if (casa.getPeca().getCor() == Cor.BRANCA) {
            if (casa.getY() == 7) return true;
        }

        // REGRA PARA PEÇAS VERMELHAS
        if (casa.getPeca().getCor() == Cor.VERMELHA) {
            if (casa.getY() == 0) return true;
        }

        return false;
    }

    /**
     * Transforma a pedra da casa passada como parametro em dama
     * @param casa - tipo {@code Casa} contendo a peça a ser ser transformada.
     */
    private void transformarPedraParaDama(Casa casa) {
        Peca pedra = casa.getPeca();

        if (pedra.getCor() == Cor.BRANCA) {
            DamaBranca damaBranca = new DamaBranca(casa);
            pedra = (DamaBranca) damaBranca;
        } else {
            DamaVermelha damaVermelha = new DamaVermelha(casa);
            pedra = (DamaVermelha) damaVermelha;
        }
    }

    /**
     * Posiciona peças no tabuleiro.
     * Utilizado na inicialização do jogo.
     * @param tabuleiro - tipo {@code Tabuleiro} onde as peças serão posicionadas
     */
    public void colocarPecas(Tabuleiro tabuleiro) {

        // CRIA E PÕE AS PEÇAS NA PARTE INFERIOR DO TABULEIRO
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 3; y++) {
                if((x % 2 == 0) && (y % 2 == 0)) {
                    Casa casa = tabuleiro.getCasa(x, y);
                    new PedraBranca(casa);
                }

                else if ((x % 2 != 0) && (y % 2 != 0)){
                    Casa casa = tabuleiro.getCasa(x, y);
                    new PedraBranca(casa);
                }
            }

        }
        // CRIA E POE AS PEÇAS NA PARTE SUPERIOR DO TABULEIRO
        for (int x = 0; x < 8; x++) {
            for (int y = 5; y < 8; y++) {
                if ((x % 2 != 0) && (y % 2 != 0)) {
                    Casa casa = tabuleiro.getCasa(x, y);
                    new PedraVermelha(casa);
                }
                else if ((x % 2 == 0) && (y % 2 == 0)) {
                    Casa casa = tabuleiro.getCasa(x, y);
                    new PedraVermelha(casa);
                }
            }
        }
    }

    /**
     * Troca a vez do jogador que pode mover no tabuleiro
     */
    public void trocarDeVez() {
        if (vezAtual == Cor.BRANCA) {
            vezAtual = Cor.VERMELHA;
        } else {
            vezAtual = Cor.BRANCA;
        }
    }

    /**
     *
     * @return
     * {@code int } 0 - Nenhum jogador
     * <li> {@code int} 1 - Jogador um </li>
     * <li> {@code int} 1 - Jogador dois </li>
     */
    public int getGanhador() {
        if (jogadorUm.getPontos() == 12) return 1;
        if (jogadorDois.getPontos() == 12) return 2;
        return 0;
    }

    /**
     * @return o Tabuleiro em jogo.
     */
    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public void setJogadorUm(Jogador jogador) {
        jogadorUm = jogador;
    }

    public void setJogadorDois(Jogador jogador) {
        jogadorDois = jogador;
    }

    public Jogador getJogadorUm() {
        return jogadorUm;
    }

    public Jogador getJogadorDois() {
        return jogadorDois;
    }

    /**
     * @return
     *      {@code int} 1 - jogador branco
     *  <li>{@code int} 2 - jogador vermelho </li>
     */
    public Cor getVez() {
        return vezAtual;
    }

    public int getJogadasSemComerPecas() {
        return jogadasSemComerPeca;
    }

    public int getJogada() {
        return jogadas;
    }

    public Casa getCasaBloqueada() {
        return casaBloqueadaOrigem;
    }

    @Override
    public String toString() {

        String retorno = "Vez: ";
        if (getVez() == Cor.BRANCA) {
            retorno += jogadorUm.getNome();
            retorno += "\n";
        } else if (getVez() == Cor.VERMELHA) {
            retorno += jogadorDois.getNome();
            retorno += "\n";
        }

        retorno += "Nº de jogadas: " + getJogada() + "\n";
        retorno += "Jogadas sem comer peça: " + getJogadasSemComerPecas() + "\n";
        retorno += "\n";
        retorno += "Informações do(a) jogador(a) " + jogadorUm.getNome() + "\n";
        retorno += "Pontos: " + jogadorUm.getPontos() + "\n";
        retorno += "Nº de peças restantes: " + (12 - jogadorDois.getPontos()) + "\n";
        retorno += "\n";
        retorno += "Informações do(a) jogador(a) " + jogadorDois.getNome() + "\n";
        retorno += "Pontos: " + jogadorDois.getPontos() + "\n";
        retorno += "Nº de peças restantes: " + (12 - jogadorUm.getPontos()) + "\n";

        if (casaBloqueadaOrigem != null) {
            retorno += "\n";
            retorno += "Mova a peça na casa " + casaBloqueadaOrigem.getX() + ":" + casaBloqueadaOrigem.getY() + "!";
        }

        return retorno;
    }
}