package com.damas.objetos;

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

    // metodos especificos para cada classe filha implementar
    public abstract boolean validarRegrasDeDeslocamento(int sentidoY, int distancia);


    // METODOS QUE EVITAM REPETIÇÃO
    // como todos as pecas se movem na diagonal, para evitar repetição implemento esse motodo aqui
    @Override
    public boolean isMovimentoValido(Casa destino) {
        int distanciaX = Math.abs((destino.getX() - casa.getX()));
        int distanciaY = Math.abs((destino.getY() - casa.getY())); // 2 - 4 = 2
        int sentidoY = (destino.getY() - casa.getY());

        // garante que o movimento seja na diagonal
        if (distanciaX != distanciaY || distanciaX == 0) return false;

        // garante que sentido seja 1 ou -1
        sentidoY = sentidoY / distanciaY;

        return validarRegrasDeDeslocamento(sentidoY, distanciaX);
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





