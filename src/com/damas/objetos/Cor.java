package com.damas.objetos;

public enum Cor {
    BRANCA(1, 7),
    VERMELHA(-1, 0);

    private final int sentido;
    private final int linhaPromocao;

    Cor(int sentido, int linhaPromocao) {
        this.sentido = sentido;
        this.linhaPromocao = linhaPromocao;
    }

    public int getLinhaPromocao() {
        return linhaPromocao;
    }

    public int getSentido() {
        return sentido;
    }
}
