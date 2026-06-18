package com.damas.gui;

import com.damas.objetos.Tabuleiro;

public interface JogoOuvinte {
    abstract public void aoMover(Tabuleiro t);
    abstract public void aoVencer(String player);
    abstract public void aoMovimentoInvalido(String reason);

}
