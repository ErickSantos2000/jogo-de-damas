package com.damas.objetos;

public class Dama extends PecaBase {

    public Dama(Cor cor) {
        super(cor, TipoPeca.DAMA);
    }

    // faz velidação de movimentação especifica para Dama, definindo a quantidade de casas percorridas
    // No caso a Dama pode se mover por todo o tabuleiro
    @Override
    public boolean validarRegrasDeDeslocamento(int sentidoY, int distancia) {
        if(distancia >= 1 && distancia < 8) return true;
        return false;
    }

    // define as regras de captura de Dama
    @Override
    public boolean podeCapturar(int distancia, int capturas) {
        return capturas <= 1;
    }

    // Dentro da classe Dama.java
    @Override
    public void promover(Casa casa) {
        return;
    }
}