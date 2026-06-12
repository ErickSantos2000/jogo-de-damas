package com.damas.objetos;

public class DamaVermelha extends PecaBase{

    public DamaVermelha(Casa casa) {
        super(casa, Cor.VERMELHA);
    }

    @Override
    public boolean isMovimentoValido(Casa destino) {
        int distanciaX = Math.abs((destino.getX() - casa.getX()));
        int distanciaY = Math.abs((destino.getY() - casa.getY()));

        if (distanciaX == distanciaY) return true;

        return false;
    }
}