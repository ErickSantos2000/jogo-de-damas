package com.damas.objetos;

public abstract class PecaBase implements Peca {
    private Cor cor;
    private TipoPeca tipoPeca;

    public PecaBase(Cor cor, TipoPeca tipoPeca) {
        this.cor = cor;
        this.tipoPeca = tipoPeca;
    }

    @Override
    public void promover(Casa casaAtual){
        Peca peca = casaAtual.getPeca();

        // se a casa estiver vazia, não ha o que promover
        if (peca == null) return;

        // o jogo apenas compara a posição y atual da peca com a linha que o Enum daquela cor manda
        if(casaAtual.getY() == peca.getCor().getLinhaPromocao()){
            casaAtual.removerPeca();
            casaAtual.colocarPeca(PecaFactory.createPeca(TipoPeca.DAMA, this.getCor()));
        }
    }

    // como todos as pecas se movem na diagonal, para evitar
    // repetição nas classes concrentas, o corpo é implementado na classe abstrata
    @Override
    public boolean isMovimentoValido(Casa origem, Casa destino) {
        int distanciaX = Math.abs((destino.getX() - origem.getX()));
        int distanciaY = Math.abs((destino.getY() - origem.getY()));
        int sentidoY = (destino.getY() - origem.getY());

        // garante que o movimento seja na diagonal e que a peca se mova
        if (distanciaX != distanciaY || distanciaX == 0) return false;

        // garante que sentido seja 1 ou -1
        // calcula o sentido da peca
        sentidoY = sentidoY / distanciaY;

        return validarRegrasDeDeslocamento(sentidoY, distanciaX);
    }

    // metodos especificos para cada classe filha implementar
    public abstract boolean validarRegrasDeDeslocamento(int sentidoY, int distancia);


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
}





