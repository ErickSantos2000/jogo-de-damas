package com.damas.objetos;

// É uma classe "mãe" que guarda o que é igual
 public abstract class PecaBase implements Peca {
    protected Casa casa;
    protected Cor cor;
    public PecaBase(Casa casa, Cor cor) {
        this.casa = casa;
        casa.colocarPeca(this);
    }

    @Override
    public void mover(Casa destino) {
        casa.removerPeca();
        destino.colocarPeca(this);
        casa = destino;
    }
 }





