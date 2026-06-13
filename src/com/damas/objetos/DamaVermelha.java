package com.damas.objetos;

public class DamaVermelha extends PecaBase{

    public DamaVermelha(Casa casa) {
        super(casa, Cor.VERMELHA, TipoPeca.DAMA);
    }

    @Override
    public boolean validarRegrasDeDeslocamento(int sentidoY, int distancia) {
        // como não tem restrições para Damas, o metodo so retorna true
        return true;
    }
}