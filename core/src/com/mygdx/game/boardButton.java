package com.mygdx.game;

import javax.swing.JButton;


public class boardButton extends JButton{
    
    private int[] chords;

    boardButton(){super();}
    boardButton(int[] chords){super();this.chords=chords;}


    public int getXChord(){
        return chords[0];
    }

    public int getYChord(){
        return chords[1];
    }
}
