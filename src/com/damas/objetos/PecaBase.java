package com.damas.objetos;

// É uma classe "mãe" que guarda o que é igual
 public abstract class PecaBase implements Peca {
    protected Casa casa;
    protected Cor cor;
    protected TipoPeca tipoPeca;

    public PecaBase(Casa casa, Cor cor, TipoPeca tipoPeca) {
        this.casa = casa;
        this.cor = cor;
        this.tipoPeca = tipoPeca;
        casa.colocarPeca(this);
    }

    @Override
    public void mover(Casa destino) {
        casa.removerPeca();
        destino.colocarPeca(this);
        casa = destino;
    }

    @Override
    public Cor getCor() {
        return cor;
    }

    @Override
    public TipoPeca getTipo() {
        return tipoPeca;
    }

    @Override
    public boolean podeMover(Cor vezAtual) {
        return vezAtual == this.cor;
    }


}





