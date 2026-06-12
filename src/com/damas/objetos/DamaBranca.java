package com.damas.objetos;

public class DamaBranca extends PecaBase{

    public DamaBranca(Casa casa) {
        super(casa, Cor.BRANCA);
    }

    @Override
    public boolean isMovimentoValido(Casa destino) {
        int distanciaX = Math.abs((destino.getX() - casa.getX()));
        int distanciaY = Math.abs((destino.getY() - casa.getY()));

        if (distanciaX == distanciaY) return true;

        return false;
    }
}