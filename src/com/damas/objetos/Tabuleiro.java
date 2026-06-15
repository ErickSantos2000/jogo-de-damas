package com.damas.objetos;

public class Tabuleiro {
    
    public static final int MAX_LINHAS = 8;
    public static final int MAX_COLUNAS = 8;

    public static final int X_ESQUERDA = -1;
    public static final int X_DIREITA = 1;
    public static final int Y_BAIXO = -1;
    public static final int Y_CIMA = 1;

 
    private Casa[][] casas;

    public Tabuleiro() {
        montarTabuleiro();
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
                    new Pedra(casa, Cor.BRANCA);
                }

                else if ((x % 2 != 0) && (y % 2 != 0)){
                    Casa casa = getCasa(x, y);
                    new Pedra(casa, Cor.BRANCA);
                }
            }

        }
        // CRIA E POE AS PEÇAS NA PARTE SUPERIOR DO TABULEIRO
        for (int x = 0; x < 8; x++) {
            for (int y = 5; y < 8; y++) {
                if ((x % 2 != 0) && (y % 2 != 0)) {
                    Casa casa = getCasa(x, y);
                    new Pedra(casa, Cor.VERMELHA);
                }
                else if ((x % 2 == 0) && (y % 2 == 0)) {
                    Casa casa = getCasa(x, y);
                    new Pedra(casa, Cor.VERMELHA);
                }
            }
        }
    }
}