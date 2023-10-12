package com.mygdx.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Draw extends JPanel implements MouseListener{

    Image whitePiece;
    Image blackPiece;
    Image whiteStand;
    Image blackStand;
    Image whiteCapstone;
    Image blackCapstone;

    public Draw() {

        ImageIcon temp = new ImageIcon("WhitePiece.png");
        Image edit = temp.getImage();
        Image finalImg = edit.getScaledInstance(25,25,java.awt.Image.SCALE_SMOOTH);
        whitePiece = new ImageIcon(finalImg).getImage();
        
        temp = new ImageIcon("BlackPiece.png");
        Image edit2 = temp.getImage();
        Image finalImg2 = edit2.getScaledInstance(25,25,java.awt.Image.SCALE_SMOOTH);
        blackPiece = new ImageIcon(finalImg2).getImage();


        setFocusable(true);
        requestFocus();

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
