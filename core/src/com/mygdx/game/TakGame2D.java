package com.mygdx.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TakGame2D {

    private JFrame frame;
    private JFrame optionFrame;
    private JLabel[][] boardLabels;
    private JLabel optionLabel;
    private boardButton[][] boardButtons;
    private JPanel leftPanel, rightPanel, boardPanel;
    private final int BOARD_SIZE = 5;
    private int[] currentChords;
    private int[] moveToChords;

    private boolean midTurn = false;

    board logicBoard = new board();

    boardButton boardButton;

    public boolean getMidTurn(){
        return midTurn;
    }

    public void setMidTurn(boolean midTurn){
        this.midTurn = midTurn;
    }

    public void setCurrentChords(int[] chords){
        this.currentChords = chords;
    }

    public int[] getCurrentChords(){
        return currentChords;
    }

    public void setMoveToChords(int[] chords){
        this.moveToChords = chords;
    }

    public int[] getMoveToChords(){
        return moveToChords;
    }

    


    public int targetDir(int[] currentChords, int[] moveToChords){

        if(currentChords[0]!=moveToChords[0]){
            if(moveToChords[0]>currentChords[0])
                return 2;
            else
                return 0;
        }
        else{
            if(moveToChords[1]>currentChords[1])
                return 1;
            else
                return 3;
        }

    }


    public TakGame2D() {
        frame = new JFrame("TakGame2D");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Initialize the left, right, and board panels
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));

        boardButtons = new boardButton[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {

                int[] chords = {i+1,j+1};

                boardButtons[i][j] = new boardButton(chords,true);

                boardButtons[i][j].setOpaque(true); // This is to make sure the background color is visible

                boardButtons[i][j].addActionListener(new ActionListener() {
                    
                    
                    public void actionPerformed(ActionEvent e){


                        
                        boardButton source = (boardButton) e.getSource();
                        int xChord = source.getXChord();
                        int yChord = source.getYChord();
                        int[] buttonChords = {xChord,yChord};

                        

                        if(getMidTurn()){
                            setMoveToChords(buttonChords);
                            int dir = targetDir(getCurrentChords(), getMoveToChords());
                            
                            logicBoard.move(currentChords[0], currentChords[1], 1, dir, 1);
                            setMidTurn(false);
                            source.setIsEmpty(false);
                        }
                        else{
                            if(source.getIsEmpty()){                             
                                setCurrentChords(buttonChords);
                                addPiece();
                                source.setIsEmpty(false);
                            }
                            else{
                                setCurrentChords(buttonChords);
                                setMidTurn(true);
                            }
                        }

                        

                        System.out.println(xChord+" "+yChord);
                    }

                });

                boardPanel.add(boardButtons[i][j]);
                if ((i + j) % 2 == 0) {
                    boardButtons[i][j].setBackground(Color.LIGHT_GRAY);
                } else {
                    boardButtons[i][j].setBackground(Color.BLACK);
                }
            }
        }

        // Adding panels to the main frame
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.EAST);
        frame.add(boardPanel, BorderLayout.CENTER);

        // Optional: Setting some preferred sizes and colors for visualization purposes
        leftPanel.setPreferredSize(new Dimension(100, 400));
        rightPanel.setPreferredSize(new Dimension(100, 400));
        leftPanel.setBackground(Color.GRAY);
        rightPanel.setBackground(Color.GRAY);

        frame.setVisible(true);
    }

    public void addPiece() {
        
        optionLabel = new JLabel("Choose a piece to add:");

        JRadioButton flatButton = new JRadioButton("add Flat Stone");
        JRadioButton standingButton = new JRadioButton("add Standing Stone");
        JRadioButton capstoneButton = new JRadioButton("add Capstone");
       
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(flatButton);
        buttonGroup.add(standingButton);
        buttonGroup.add(capstoneButton);


        flatButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(logicBoard.getCurrentPlayer().equals("WHITE")){
                    logicBoard.addPiece(0, getCurrentChords()[0], getCurrentChords()[1]);
                    optionFrame.dispose();
                }
                else{
                    logicBoard.addPiece(3, getCurrentChords()[0], getCurrentChords()[1]);
                    optionFrame.dispose();
                }
            }
        });


        standingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(logicBoard.getCurrentPlayer().equals("WHITE")){
                    logicBoard.addPiece(1, getCurrentChords()[0], getCurrentChords()[1]);
                    optionFrame.dispose();
                }
                else{
                    logicBoard.addPiece(4, getCurrentChords()[0], getCurrentChords()[1]);
                    optionFrame.dispose();
                }
            }
        });

        capstoneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(logicBoard.getCurrentPlayer().equals("WHITE")){
                    logicBoard.addPiece(2, getCurrentChords()[0], getCurrentChords()[1]);
                    optionFrame.dispose();
                }
                else{
                    logicBoard.addPiece(5, getCurrentChords()[0], getCurrentChords()[1]);
                    optionFrame.dispose();
                }
            }
        });

        JPanel topOptionPanel = new JPanel();
        topOptionPanel.setBounds(0, 0, 300, 100);
        JPanel bottomOptionPanel = new JPanel();
        bottomOptionPanel.setBounds(0, 0, 300, 100);
        bottomOptionPanel.setLayout(new GridLayout(3, 1)); 
        
        topOptionPanel.add(optionLabel);
        bottomOptionPanel.add(flatButton);
        bottomOptionPanel.add(standingButton);
        bottomOptionPanel.add(capstoneButton);



        optionFrame = new JFrame("Choose Piece");
        optionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        optionFrame.setSize(300, 200);
        optionFrame.setLayout(new BorderLayout());
        optionFrame.add(topOptionPanel, BorderLayout.NORTH);
        optionFrame.add(bottomOptionPanel, BorderLayout.SOUTH);

        
        optionFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TakGame2D();
            }
        });

    }
}
