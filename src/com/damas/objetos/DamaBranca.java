package com.damas.objetos;

public class DamaBranca extends PecaBase{

    public DamaBranca(Casa casa) {
        super(casa, Cor.BRANCA, TipoPeca.DAMA);
    }

    @Override
    public boolean validarRegrasDeDeslocamento(int sentidoY, int distancia) {
        // como não tem restrições para Damas, o metodo so retorna true
        return true;
    }

    @Override
    public boolean ehCaptura(int distancia) {
        return false;
    }
}