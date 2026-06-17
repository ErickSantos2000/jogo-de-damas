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
        if (casaBloqueadaOrigem != null && !origem.equals(casaBloqueadaOrigem)) return;

        // 1. Pergunta para a PEÇA se a intenção geométrica base é válida (ex: se pedra anda para trás)
        if (peca.podeMover(vezAtual) && peca.isMovimentoValido(origem, destino)) {

            // 2. Pede para o TABULEIRO calcular o trajeto físico real das casas
            ArrayList<Casa> pecasInimigas = tabuleiro.simularMovimentoEValidar(origem, destino);

            // Se o trajeto for geometricamente possível e não nulo
            if (pecasInimigas != null) {

                // 3. Pergunta para a PEÇA se a quantidade de capturas encontradas confere com o limite dela
                int distancia = Math.abs(destino.getX() - origem.getX());
                if (peca.podeCapturar(distancia, pecasInimigas.size())) {

                    // Se estiver em um combo, proíbe movimentos simples (com tamanho 0)
                    if (casaBloqueadaOrigem != null && pecasInimigas.isEmpty()) return;

                    // Passa a lista limpa para a coleção global e executa
                    this.pecasAComer = pecasInimigas;
                    executarMovimento(origem, destino);
                }
            }
        }
    }

    private void executarMovimento(Casa origem, Casa destino) {
        tabuleiro.mover(origem, destino);

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

    private boolean deveContinuarJogando(Casa origem) {
        int[] direcoes = {-1, 1};

        for (int dx : direcoes) {
            for (int dy : direcoes) {
                // Testa um salto padrão de captura (distância 2) nas 4 diagonais
                int xDestino = origem.getX() + (dx * 2);
                int yDestino = origem.getY() + (dy * 2);

                if (xDestino >= 0 && xDestino <= 7 && yDestino >= 0 && yDestino <= 7) {
                    Casa destino = tabuleiro.getCasa(xDestino, yDestino);

                    // Pergunta ao tabuleiro o que tem nesse mini-trajeto de salto
                    ArrayList<Casa> resultado = tabuleiro.simularMovimentoEValidar(origem, destino);

                    // Se o tabuleiro disser que o salto é possível e contiver exatamente 1 peça inimiga
                    if (resultado != null && resultado.size() == 1) {
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
        Peca peca = casa.getPeca();

        if(peca == null) return;

        // instancia a Dama passando a cor exata da pedra que chegou la
        peca.promover(casa);
    }

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
