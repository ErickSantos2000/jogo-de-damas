package com.damas.objetos;

/**
 * Factory para criação de peças (Pedra e Dama).
 * Centraliza a instância de peças para evitar acoplamento direto com as classes concretas.
 */
public class PecaFactory {

    /**
     * Cria uma nova peça com base no tipo e cor fornecidos.
     * @param tipo Tipo da peça (PEDRA ou DAMA)
     * @param cor Cor da peça
     * @return Uma instância de Peca
     */
    public static Peca createPeca(TipoPeca tipo, Cor cor) {
        switch (tipo) {
            case PEDRA:
                return new Pedra(cor);
            case DAMA:
                return new Dama(cor);
            default:
                throw new IllegalArgumentException("Tipo de peça desconhecido: " + tipo);
        }
    }
}
