package com.damas.objetos;

public class PedraBranca extends PecaBase {

    public PedraBranca(Casa casa) {
        super(casa, Cor.BRANCA);
        casa.colocarPeca(this);
    }

    @Override
    public void mover(Casa destino) {
        casa.removerPeca();
        destino.colocarPeca(this);
        casa = destino;
    }

    @Override
    public boolean isMovimentoValido(Casa destino) {

        // SENTIDO UNITÁRIO E DISTANCIA X E Y DA CASA ATUAL ATÉ A CASA DE DESTINO
        // Cacula a distancia que a peça esta se movimentando: horizontalmete (distanciaX) e verticalmente (distanciaY)
        // O uso de Math.abs garante que o resultado seja sempre um numero positivo, ignorando a direção
        int distanciaX = Math.abs(destino.getX() - casa.getX());
        int distanciaY = Math.abs(destino.getY() - casa.getY());

        // garante que o movimento seja estritamente diagonal
        // em um tabuleiro, andar na diagonal significa avançar o mesmo numero de casas em X e em Y
        if ((distanciaX == 0) || (distanciaY == 0)) return false;

        // REGRA DE MOVIMENTO NO CASO DA DISTÂNCIA SER DE 2 CASAS (MOVIMENTO DE COMER PEÇA)
        // limita o alacance do movimento a no maximo duas casas
        // Como as distancias já são iguais devido a regra anterior, a peça so
        // pode andar exatamente 1 casa (movimento normal) ou exatamente 2 casas (comum para pular/comer uma peça adversária).
        if ((distanciaX <= 2 || distanciaY <= 2) && (distanciaX == distanciaY)) {
            return true;
        }

        return false;
    }

    @Override
    public Cor getCor() {
        return cor;
    }

}