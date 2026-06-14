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

    private boolean simularMovimentoEValidar(Casa origem, Casa destino) {
        Peca peca = origem.getPeca();
        int casasComPecaSeguidas = 0;

        if (destino.getPeca() != null) return false;

        // SENTIDO DO MOVIMENTO E DISTÂNCIA DO MOVIMENTO
        int sentidoX = (destino.getX() - origem.getX());
        int sentidoY = (destino.getY() - origem.getY());

        int distanciaX = Math.abs(sentidoX);
        int distanciaY = Math.abs(sentidoY);

        if ((distanciaX == 0) || (distanciaY == 0)) return false;

        sentidoX = sentidoX/distanciaX;
        sentidoY = sentidoY/distanciaY;

        //PERCORRER AS CASAS E VERIFICAR:
        // 1 - SE HÁ MAIS DE UMA PEÇA SEGUIDA NO CAMINHO (VERDADEIRO RETORNA FALSO)
        // 2 - SE HÁ UMA PEÇA NO CAMINHO E É DA MESMA COR (VERDADEIRO RETORNA FALSO)
        int i = origem.getX();
        int j = origem.getY();

        while (i != destino.getX() && j != destino.getY()) {
            i += sentidoX;
            j += sentidoY;

            Casa alvo = tabuleiro.getCasa(i, j);
            Peca pecaAlvo = alvo.getPeca();

            if (pecaAlvo != null) {
                casasComPecaSeguidas++;

                // VE SE TEM UMA PECA DO MESMO TIPO NO CAMNHO, CASO TENHA, RETORNA FALSE
                if(peca.getCor() == pecaAlvo.getCor()){
                     // limpa a lista de capturas pois o movimento falhou
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


        }

        if (!peca.podeCapturar(distanciaX, pecasAComer.size())) {
            pecasAComer.clear();
            return false;
        }
        return true;
    }

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

    private boolean podeTransformarParaDama(Casa casa) {
        Peca peca = casa.getPeca();

        // se a casa estiver vazia, não ha o que promover
        if (peca == null) return false;

        // o jogo apenas compara a posição y atual da peca com a linha que o Enum daquela cor manda
        return casa.getY() == peca.getCor().getLinhaPromocao();
    }

    private void transformarPedraParaDama(Casa casa) {
        Peca pedra = casa.getPeca();

        if(pedra == null) return;

        // instancia a Dama passando a cor exata da pedra que chegou la
        new Dama(casa, pedra.getCor());
    }

    public void colocarPecas(Tabuleiro tabuleiro) {

        // CRIA E PÕE AS PEÇAS NA PARTE INFERIOR DO TABULEIRO
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 3; y++) {
                if((x % 2 == 0) && (y % 2 == 0)) {
                    Casa casa = tabuleiro.getCasa(x, y);
                    new Pedra(casa, Cor.BRANCA);
                }

                else if ((x % 2 != 0) && (y % 2 != 0)){
                    Casa casa = tabuleiro.getCasa(x, y);
                    new Pedra(casa, Cor.BRANCA);
                }
            }

        }
        // CRIA E POE AS PEÇAS NA PARTE SUPERIOR DO TABULEIRO
        for (int x = 0; x < 8; x++) {
            for (int y = 5; y < 8; y++) {
                if ((x % 2 != 0) && (y % 2 != 0)) {
                    Casa casa = tabuleiro.getCasa(x, y);
                    new Pedra(casa, Cor.VERMELHA);
                }
                else if ((x % 2 == 0) && (y % 2 == 0)) {
                    Casa casa = tabuleiro.getCasa(x, y);
                    new Pedra(casa, Cor.VERMELHA);
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