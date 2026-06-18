package com.damas.gui;

import com.damas.objetos.Jogo;

public class JogoController {
    private Jogo jogo;
    private boolean primeiroClique;
    private JanelaPrincipal view;
    private CasaGUI casaOrigem;

    public JogoController(Jogo jogo, JanelaPrincipal view){
        this.jogo = jogo;
        this.view = view;
    }

    // reponsavel por gerenciar o estado
    public void lidaComSelecao(int x, int y) {
        // pega a peca clicada atraves da view
        CasaGUI casaClicada = view.getTabuleiroGUI().getCasaGUI(x, y);
        if (casaOrigem == null) {
            if(casaClicada.possuiPeca()){
                casaOrigem = casaClicada;
                casaOrigem.destacar();
            } else {
                System.out.println("Clique em uma peça valida.");
            }
        } else {
            // executa a jogada no jogo
            jogo.processarJogada(casaOrigem.getPosicaoX(), casaOrigem.getPosicaoY(), x, y);

            // verifica se o jogo bloqueou uma casa, então deve continuar
            if(jogo.getCasaBloqueada() != null){
                // remove destaque da casa velha
                casaOrigem.atenuar();

                int novoX = jogo.getCasaBloqueada().getX();
                int novoY = jogo.getCasaBloqueada().getY();
                casaOrigem = view.getTabuleiroGUI().getCasaGUI(novoX, novoY);

                // destaca nova casa
                casaOrigem.destacar();
            } else {
                casaOrigem.atenuar();
                casaOrigem = null; // libera a origem para o proxima clique

            }


        }
    }
}
