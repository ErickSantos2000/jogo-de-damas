package com.damas.objetos;
import com.damas.objetos.Casa;
import com.damas.objetos.Cor;
import com.damas.objetos.TipoPeca;

public class Pedra extends PecaBase {

    public Pedra(Casa casa, Cor cor) {
        super(casa, cor, TipoPeca.PEDRA);
    }

    @Override
    public boolean validarRegrasDeDeslocamento(int sentidoY, int distancia) {

        if (distancia == 2)
            return true;

        // remove a necessidade pela cor, ao inves disso é perguntado diretamente ao enum
        int direcaoPermitida = cor.getSentido();

        return distancia == 1 && sentidoY == direcaoPermitida;
    }

    @Override
    public boolean ehCaptura() {
        return true;
    }
}