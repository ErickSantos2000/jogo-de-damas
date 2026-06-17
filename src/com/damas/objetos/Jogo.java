package com.damas.objetos;

import java.util.ArrayList;

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

        tabuleiro.colocarPecas();
    }

    public void processarJogada(int origemX, int origemY, int destinoX, int destinoY) {
        Casa origem = tabuleiro.getCasa(origemX, origemY);
        Casa destino = tabuleiro.getCasa(destinoX, destinoY);
        Peca peca = origem.getPeca();

        if (peca == null) return;

        // se houver um combo em andamento, checa se é a peca certa, se for a certa, continua
        if (casaBloqueadaOrigem != null && !origem.equals(casaBloqueadaOrigem)) return;

        // vefica a vez e se a movimentação é valida
        if (peca.podeMover(vezAtual) && peca.isMovimentoValido(destino)) {
            if (simularMovimentoEValidar(origem, destino)) {

                // se estiver em um combo, proíbe movimentos simples
                if (casaBloqueadaOrigem != null && pecasAComer.isEmpty()) return;

                executarMovimento(peca, destino);
            }
        }
    }

    private void executarMovimento(Peca peca, Casa destino) {
        peca.mover(destino);

        if (!pecasAComer.isEmpty()) {
            this.comerPecas();
            if (deveContinuarJogando(destino)) {
                casaBloqueadaOrigem = destino;
            } else {
                casaBloqueadaOrigem = null;
                this.trocarDeVez();
            }
        } else {
            jogadasSemComerPeca++;
            this.trocarDeVez();
        }

        jogadas++;
        if (podeTransformarParaDama(destino)) {
            transformarPedraParaDama(destino);
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

        sentidoX = sentidoX/distanciaX;
        sentidoY = sentidoY/distanciaY;

        //PERCORRER AS CASAS E VERIFICAR:
        // 1 - SE HÁ MAIS DE UMA PEÇA SEGUIDA NO CAMINHO (VERDADEIRO RETORNA FALSO)
        // 2 - SE HÁ uma PEÇA NO CAMINHO E É DA MESMA COR (VERDADEIRO RETORNA FALSO)
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
                    pecasAComer.clear();
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
                if (pecasAComer.size() > 0) pecasAComer.clear();
                return false;
            }

        }

        if (!peca.podeCapturar(distanciaX, pecasAComer.size())) {
            pecasAComer.clear();
            return false;
        }
        return true;
    }

    private boolean deveContinuarJogando(Casa origem) {
        // o jogo testa as 4 direcoes em volta da peca, se tiver uma
        // peca inimiga que de para ser comida, a peca da vez de continuar
        int[] direcoes = {-1, 1};

        for (int dx : direcoes) {
            for (int dy : direcoes) {
                // testa um salto de 2 casas
                int xDestino = origem.getX() + (dx * 2);
                int yDestino = origem.getY() + (dy * 2);

                // verifica os limites do tabuleiro
                if (xDestino >= 0 && xDestino <= 7 && yDestino >= 0 && yDestino <= 7) {
                    Casa destino = tabuleiro.getCasa(xDestino, yDestino);

                    // simula movimento para saber se deve continuar
                    if (simularMovimentoEValidar(origem, destino)) {
                        if (pecasAComer.size() > 0) {
                            pecasAComer.clear();
                            return true;
                        }
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
        Peca peca = casa.getPeca();

        if(peca == null) return;

        // instancia a Dama passando a cor exata da pedra que chegou la
        peca.promover();
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
}
