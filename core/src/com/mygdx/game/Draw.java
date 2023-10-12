package com.mygdx.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Draw extends JPanel implements MouseListener{

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

    public void setCurrentImage(Image image) {
        ;
        repaint(); // Repaint the panel to update the displayed image
    }



    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;




    }

   

    @Override
    public void mouseClicked(MouseEvent e) {
        
        throw new UnsupportedOperationException("Unimplemented method 'mouseClicked'");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mousePressed'");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseEntered'");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseExited'");
    }


    
}
