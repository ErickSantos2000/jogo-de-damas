package com.damas.objetos;
import com.damas.objetos.Casa;
import com.damas.objetos.Cor;
import com.damas.objetos.TipoPeca;

public class Pedra extends PecaBase {

    public Pedra(Casa casa, Cor cor) {
        super(casa, cor, TipoPeca.PEDRA);
    }

    // faz velidação de movimentação especifica para Pedra, definindo a quantidade de casas percorridas
    // No caso a Pedra pode se mover por 1 ou 2 (caso seja um movimento de captura) casas
    @Override
    public boolean validarRegrasDeDeslocamento(int sentidoY, int distancia) {

        if (distancia == 2) return true;

        // remove a necessidade pela cor, ao inves disso é perguntado diretamente ao enum
        int direcaoPermitida = this.getCor().getSentido();

        // verifica se a distancia da pedra é de apenas um e se esta no sentido correto
        return distancia == 1 && sentidoY == direcaoPermitida;
    }

    // define as regras de captura de Pedra
    @Override
    public boolean podeCapturar(int distancia, int capturas) {
        if(distancia == 2){
            return capturas == 1;
        }
        return capturas == 0;
    }
}