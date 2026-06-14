package com.damas.objetos;

public class Dama extends PecaBase {

    public Dama(Casa casa, Cor cor) {
        super(casa, cor, TipoPeca.DAMA);
    }

    @Override
    public boolean validarRegrasDeDeslocamento(int sentidoY, int distancia) {

        return true;
    }

    @Override
    public boolean podeCapturar(int distancia, int capturadas) {
        return capturadas <= 1;
    }
}