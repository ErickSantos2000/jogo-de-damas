package com.damas.objetos;

public class PecaFactory {

    public static Peca createPeca(TipoPeca tipo, Cor cor) {
        switch (tipo) {
            case PEDRA:
                return new Pedra(cor);
            case DAMA:
                return new Dama(cor);
            default:
                throw new IllegalArgumentException("Tipo de peça desconhecido: " + tipo);
        }
    }
}
