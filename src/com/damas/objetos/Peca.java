package com.damas.objetos;

/**
 * Interface com os métodos abstratos das peças
 * @author João Victor da S. Cirilo {@link joao.cirilo@academico.ufpb.br}
 */
public interface Peca {

    abstract public void mover(Casa destino);
    abstract public boolean isMovimentoValido(Casa destino);
    abstract public Cor getCor();
    abstract public boolean podeMover(Cor vezAtual);
    abstract public TipoPeca getTipo();
    abstract  public boolean podeCapturar(int distancia, int capturadas);
}