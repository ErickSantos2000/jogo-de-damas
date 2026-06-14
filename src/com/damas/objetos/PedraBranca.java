package com.damas.objetos;

public class PedraBranca extends PecaBase {

    public PedraBranca(Casa casa) {
        super(casa, Cor.BRANCA, TipoPeca.PEDRA);
    }

    @Override
    public boolean validarRegrasDeDeslocamento(int sentidoY, int distancia) {
        // logica de movimento para comer peca no caminho
        // aqui no caso a verificação em relação a distancia é 2 para qualquer sentido
        if(distancia == 2) return true;

        // logica de movimento padrão da peca
        // pecas brancas se movem apenas para cima(no sentido do Y)
        return sentidoY == 1 && distancia == 1;
    }

    @Override
    public boolean ehCaptura(int distancia) {
        return distancia == 2 ;
    }
}