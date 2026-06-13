package com.damas.objetos;

public class PedraVermelha extends PecaBase {

    public PedraVermelha(Casa casa) {
        super(casa, Cor.VERMELHA, TipoPeca.PEDRA);
    }

    @Override
    public boolean validarRegrasDeDeslocamento(int sentidoY, int distancia) {
        // logica de movimento para comer peca no caminho
        // aqui no caso a verificação em relação a distancia é 2 para qualquer sentido
        if(distancia == 2) return true;

        // logica de movimento padrão da peca
        // pecas brancas se movem apenas para cima(no sentido do Y)
        return sentidoY == -1 && distancia == 1;
    }
}