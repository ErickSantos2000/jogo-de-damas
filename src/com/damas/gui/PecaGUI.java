package com.damas.gui;

import com.damas.objetos.Peca;
import com.damas.objetos.Cor;
import com.damas.objetos.TipoPeca;

public class PecaGUI {
    private Peca peca;

    public PecaGUI(Peca peca) {
        this.peca = peca;
    }

    public void desenhar(CasaGUI casaGUI) {
        TipoPeca tipo = peca.getTipo();
        Cor cor = peca.getCor();

        if (tipo == TipoPeca.PEDRA) {
            if (cor == Cor.BRANCA) casaGUI.desenharPedraBranca();
            else casaGUI.desenharPedraVermelha();
        }
        else if (tipo == TipoPeca.DAMA) {
            if (cor == Cor.BRANCA) casaGUI.desenharDamaBranca();
            else casaGUI.desenharDamaVermelha();
        }
    }
}