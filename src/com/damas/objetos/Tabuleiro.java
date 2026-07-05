package com.damas.objetos;

import java.util.ArrayList;

public class Tabuleiro {

    public static final int MAX_LINHAS = 8;
    public static final int MAX_COLUNAS = 8;

    private Casa[][] casas;

    public Tabuleiro() {
        montarTabuleiro();
    }

    public void mover(Casa origem, Casa destino) {
        Peca peca = origem.getPeca();

        origem.removerPeca();
        destino.removerPeca();
        destino.colocarPeca(peca);
    }

    private void montarTabuleiro() {
        casas = new Casa[MAX_LINHAS][MAX_COLUNAS];
        for (int x = 0; x < MAX_LINHAS; x++) {
            for (int y = 0; y < MAX_COLUNAS; y++) {
                Casa casa = new Casa(x, y);
                casas[x][y] = casa;
            }
        }
    }

    public Casa getCasa(int x, int y) {
        return casas[x][y];
    }

    public void colocarPecas() {

        // CRIA E PÕE AS PEÇAS NA PARTE INFERIOR DO TABULEIRO
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 3; y++) {
                if((x % 2 == 0) && (y % 2 == 0)) {
                    Casa casa = getCasa(x, y);
                    casa.colocarPeca(new Pedra(Cor.BRANCA));
                }

                else if ((x % 2 != 0) && (y % 2 != 0)){
                    Casa casa = getCasa(x, y);
                    casa.colocarPeca(new Pedra(Cor.BRANCA));
                }
            }

        }
        // CRIA E POE AS PEÇAS NA PARTE SUPERIOR DO TABULEIRO
        for (int x = 0; x < 8; x++) {
            for (int y = 5; y < 8; y++) {
                if ((x % 2 != 0) && (y % 2 != 0)) {
                    Casa casa = getCasa(x, y);
                    casa.colocarPeca(new Pedra(Cor.VERMELHA));
                }
                else if ((x % 2 == 0) && (y % 2 == 0)) {
                    Casa casa = getCasa(x, y);
                    casa.colocarPeca(new Pedra(Cor.VERMELHA));
                }
            }
        }
    }

    ArrayList<Casa> simularMovimentoEValidar(Casa origem, Casa destino) {
        if (destino.getPeca() != null) return null;

        int sentidoX = (destino.getX() - origem.getX());
        int sentidoY = (destino.getY() - origem.getY());
        int distanciaX = Math.abs(sentidoX);
        int distanciaY = Math.abs(sentidoY);

        if (distanciaX == 0) return null;

        sentidoX = sentidoX / distanciaX;
        sentidoY = sentidoY / distanciaY;

        ArrayList<Casa> pecasNoCaminho = new ArrayList<>();
        int pecasSeguidas = 0;

        int i = origem.getX();
        int j = origem.getY();

        // Varre a diagonal entre a origem e o destino
        while (i != destino.getX() && j != destino.getY()) {
            i += sentidoX;
            j += sentidoY;

            Casa alvo = getCasa(i, j);
            Peca pecaAlvo = alvo.getPeca();

            if (pecaAlvo != null) {
                pecasSeguidas++;
                // Se encontrar uma peça da mesma cor no caminho, o movimento é inválido
                if (origem.getPeca().getCor() == pecaAlvo.getCor()) {
                    return null;
                }
                pecasNoCaminho.add(alvo);
            } else {
                pecasSeguidas = 0; // Reseta se encontrar uma casa vazia
            }

            // Regra universal do jogo de damas: nunca se pula duas peças juntas
            if (pecasSeguidas == 2) {
                return null;
            }
        }

        return pecasNoCaminho;
    }

    boolean deveContinuarJogando(Casa origem) {
        int[] direcoes = {-1, 1};

        for (int dx : direcoes) {
            for (int dy : direcoes) {
                // Testa um salto padrão de captura (distância 2) nas 4 diagonais
                int xDestino = origem.getX() + (dx * 2);
                int yDestino = origem.getY() + (dy * 2);

                if (xDestino >= 0 && xDestino <= 7 && yDestino >= 0 && yDestino <= 7) {
                    Casa destino = getCasa(xDestino, yDestino);

                    // Pergunta ao tabuleiro o que tem nesse mini-trajeto de salto
                    ArrayList<Casa> resultado = simularMovimentoEValidar(origem, destino);

                    // Se o tabuleiro disser que o salto é possível e contiver exatamente 1 peça inimiga
                    if (resultado != null && resultado.size() == 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}