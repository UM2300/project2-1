package com.mygdx.game;

import javax.swing.JButton;


public class boardButton extends JButton{
    
    private int[] chords;
    private boolean isEmpty;

    boardButton(){
        super();
        isEmpty = true;
    }

    boardButton(int[] chords, boolean isEmpty){
        super();
        this.chords=chords;
        this.isEmpty = isEmpty;
    }


    public int getXChord(){
        return chords[0];
    }

    public int getYChord(){
        return chords[1];
    }
}
