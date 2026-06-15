package com.damas.objetos;

public abstract class PecaBase implements Peca {
    private Casa casa;
    private Cor cor;
    private TipoPeca tipoPeca;

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
    public void promover(){
        Casa casaAtual = this.getCasa();

        casaAtual.removerPeca();

        new Dama(casaAtual, this.getCor());
    }

    // metodos especificos para cada classe filha implementar
    public abstract boolean validarRegrasDeDeslocamento(int sentidoY, int distancia);


    // METODOS QUE EVITAM REPETIÇÃO
    // como todos as pecas se movem na diagonal, para evitar
    // repetição nas classes concrentas, o corpo é implementado na classe abstrata
    @Override
    public boolean isMovimentoValido(Casa destino) {
        int distanciaX = Math.abs((destino.getX() - casa.getX()));
        int distanciaY = Math.abs((destino.getY() - casa.getY()));
        int sentidoY = (destino.getY() - casa.getY());

        // garante que o movimento seja na diagonal e que a peca se mova
        if (distanciaX != distanciaY || distanciaX == 0) return false;

        // garante que sentido seja 1 ou -1
        // calcula o sentido da peca
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

    // informa qual é peca que tem a vez no no jogo
    @Override
    public boolean podeMover(Cor vezAtual) {
        return vezAtual == this.cor;
    }

    public Casa getCasa() {
        return casa;
    }
}





