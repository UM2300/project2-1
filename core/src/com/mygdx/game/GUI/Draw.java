package com.mygdx.game.GUI;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * This class is responsible for placing the images of the pieces onto the board
 */

public class Draw extends JPanel{

    Image whiteFlat;
    Image whiteStanding;
    Image whiteCapstone;

    Image blackFlat;
    Image blackStanding;
    Image blackCapstone;

    public Draw() {

        //white flat
        ImageIcon temp = new ImageIcon("WhitePiece.png");
        Image edit = temp.getImage();
        Image finalImg = edit.getScaledInstance(25,25,java.awt.Image.SCALE_SMOOTH);
        whiteFlat = new ImageIcon(finalImg).getImage();
        
        //black flat
        temp = new ImageIcon("BlackPiece.png");
        Image edit2 = temp.getImage();
        Image finalImg2 = edit2.getScaledInstance(25,25,java.awt.Image.SCALE_SMOOTH);
        blackFlat = new ImageIcon(finalImg2).getImage();

        //white standing
        temp = new ImageIcon("WhiteStanding.png");
        Image edit3 = temp.getImage();
        Image finalImg3 = edit3.getScaledInstance(25,25,java.awt.Image.SCALE_SMOOTH);
        whiteStanding = new ImageIcon(finalImg3).getImage();
        
        //black standing
        temp = new ImageIcon("BlackStanding.png");
        Image edit4 = temp.getImage();
        Image finalImg4 = edit4.getScaledInstance(25,25,java.awt.Image.SCALE_SMOOTH);
        blackStanding = new ImageIcon(finalImg4).getImage();

        //white capstone
        temp = new ImageIcon("WhiteCapstone.png");
        Image edit5 = temp.getImage();
        Image finalImg5 = edit5.getScaledInstance(25,25,java.awt.Image.SCALE_SMOOTH);
        whiteCapstone = new ImageIcon(finalImg5).getImage();
        
        //black capstone
        temp = new ImageIcon("BlackCapstone.png");
        Image edit6 = temp.getImage();
        Image finalImg6 = edit6.getScaledInstance(25,25,java.awt.Image.SCALE_SMOOTH);
        blackCapstone = new ImageIcon(finalImg6).getImage();
        


        setFocusable(true);
        requestFocus();

    }

    /**
     * Repaints the panel to update the displayed image
     * @param image image of piece to be displayed
     */
    public void setCurrentImage(Image image) {
        repaint(); 
    }


}
