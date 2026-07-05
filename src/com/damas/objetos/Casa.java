package com.damas.objetos;

public class Casa {

    private int x;
    private int y;
    private Peca peca;

    public Casa(int x, int y) {
        this.x = x;
        this.y = y;
        this.peca = null;
    }

    public void colocarPeca(Peca peca) {
        this.peca = peca;
    }

    public void removerPeca() {
        peca = null;
    }

    public Peca getPeca() {
        return peca;
    }

    public boolean possuiPeca() {
        return peca != null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
