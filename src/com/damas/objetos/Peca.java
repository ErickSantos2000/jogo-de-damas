package com.damas.objetos;

/**
 * Interface com os métodos abstratos das peças
 * @author João Victor da S. Cirilo {@link joao.cirilo@academico.ufpb.br}
 */
public interface Peca {

    abstract public boolean isMovimentoValido(Casa origem, Casa destino);
    abstract public boolean podeMover(Cor vezAtual); // informa qual é peca que tem a vez no no jogo
    abstract  public boolean podeCapturar(int distancia, int capturas);
    abstract public void promover(Casa casaAtual);
    abstract public TipoPeca getTipo();
    abstract public Cor getCor();
}