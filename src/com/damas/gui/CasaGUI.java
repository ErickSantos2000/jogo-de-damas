package com.damas.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class CasaGUI extends JButton {

    // Constantes 
    public static final Color COR_CLARA = new Color(255, 255, 250);
    public static final Color COR_ESCURA = new Color(87, 168, 124);
    private static final Color COR_DESTAQUE = new Color(0, 1, 0, 0.4f);

    // Icones das pecas
    private static final URL PEDRA_BRANCA_URL = CasaGUI.class.getResource("/resources/pedra_branca.png"); 
    private static final URL DAMA_BRANCA_URL = CasaGUI.class.getResource("/resources/dama_branca.png"); 
    private static final URL PEDRA_VERMELHA_URL = CasaGUI.class.getResource("/resources/pedra_vermelha.png"); 
    private static final URL DAMA_VERMELHA_URL = CasaGUI.class.getResource("/resources/dama_vermelha.png"); 

    private static final Icon PEDRA_BRANCA = new ImageIcon(PEDRA_BRANCA_URL);
    private static final Icon DAMA_BRANCA = new ImageIcon(DAMA_BRANCA_URL);
    private static final Icon PEDRA_VERMELHA = new ImageIcon(PEDRA_VERMELHA_URL);
    private static final Icon DAMA_VERMELHA = new ImageIcon(DAMA_VERMELHA_URL);

    // Cores das pecas

    private int x;
    private int y;
    private Color cor;

    public CasaGUI(int x, int y, Color cor, TabuleiroGUI tabuleiro) {
        this.x = x;
        this.y = y;
        this.cor = cor;
        setIcon(null);

        // Layout e cor
        setBackground(cor);
        setOpaque(false);
        setBorder(BorderFactory.createLineBorder(cor, 1));
        setContentAreaFilled(false);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabuleiro.getJanela().reagir((CasaGUI) e.getSource());
            }
        });
    }
    public int getPosicaoX() {
        return this.x;
    }

    public int getPosicaoY() {
        return this.y;
    }

    public void desenharPedraBranca() {
        setIcon(PEDRA_BRANCA);
    }

    public void desenharPedraVermelha() {
        setIcon(PEDRA_VERMELHA);
    }

    public void desenharDamaBranca() {
        setIcon(DAMA_BRANCA);
    }

    public void desenharDamaVermelha() {
        setIcon(DAMA_VERMELHA);
    }

    public void apagarPeca() {
        setIcon(null);
    }

    public boolean possuiPeca() {
        return getIcon() != null;
    }

    
    public void destacar() {
        setBackground(COR_DESTAQUE);
    }

    public void atenuar() {
        setBackground(cor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

}
