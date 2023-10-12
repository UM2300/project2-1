package com.mygdx.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TakGame2D {

    private JLabel leftStonesLabel, leftCapstoneLabel, rightStonesLabel, rightCapstoneLabel;


    private JFrame frame;
    private JFrame optionFrame;
    private JLabel[][] boardLabels;
    private JLabel optionLabel;
    private boardButton[][] boardButtons;
    private JPanel leftPanel, rightPanel, boardPanel;
    private final int BOARD_SIZE = 5;

    Draw draw = new Draw();

    private int[] currentChords;

    public void setCurrentChords(int[] chords){
        this.currentChords = chords;
    }

    public int[] getCurrentChords(){
        return currentChords;
    }

    public int stones = 21;
    public int capstone = 1;
    public int stones2 = 21;
    public int capstone2 = 1;
    board logicBoard = new board();

    boardButton boardButton;

    public TakGame2D() {

        leftStonesLabel = new JLabel(stones + " Stones");
        leftCapstoneLabel = new JLabel(capstone + " Capstone");
        rightStonesLabel = new JLabel(stones2 + " Stones");
        rightCapstoneLabel = new JLabel(capstone2 + " Capstone");


        frame = new JFrame("TakGame2D");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Initialize the left, right, and board panels
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(leftStonesLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(leftCapstoneLabel);
        leftPanel.add(Box.createVerticalGlue());


        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(rightStonesLabel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(rightCapstoneLabel);
        rightPanel.add(Box.createVerticalGlue());


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


                        if(source.getIsEmpty()){
                            int[] buttonChords = {xChord,yChord};
                            setCurrentChords(buttonChords);
                            addPiece();
                            source.setIsEmpty(false);
                        }
                        else{
                            int[] buttonChords = {xChord,yChord};
                            setCurrentChords(buttonChords);

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
                    if (stones > 0) {
                        stones--;
                        leftStonesLabel.setText(stones + " Stones");
                        logicBoard.addPiece(1, getCurrentChords()[0], getCurrentChords()[1]);
                        optionFrame.dispose();
                    }
                } else {
                    if (stones2 > 0) {
                        stones2--;
                        rightStonesLabel.setText(stones2 + " Stones");
                        logicBoard.addPiece(4, getCurrentChords()[0], getCurrentChords()[1]);
                        optionFrame.dispose();
                    }
                }
            }
        });


        standingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){

                if(logicBoard.getCurrentPlayer().equals("WHITE")){
                    if (stones > 0) {
                        stones--;  // Decrease stone count
                        leftStonesLabel.setText(stones + " Stones");  // Update label
                        logicBoard.addPiece(0, getCurrentChords()[0], getCurrentChords()[1]);
                        optionFrame.dispose();
                    }
                } else {
                    if (stones2 > 0) {
                        stones2--;
                        rightStonesLabel.setText(stones2 + " Stones");
                        logicBoard.addPiece(3, getCurrentChords()[0], getCurrentChords()[1]);
                        optionFrame.dispose();
                    }
                }

            }
        });

        capstoneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){

                if(logicBoard.getCurrentPlayer().equals("WHITE")){
                    if (capstone > 0) {
                        capstone--;
                        leftCapstoneLabel.setText(capstone + " Capstone");
                        logicBoard.addPiece(2, getCurrentChords()[0], getCurrentChords()[1]);
                        optionFrame.dispose();
                    }
                } else {
                    if (capstone2 > 0) {
                        capstone2--;
                        rightCapstoneLabel.setText(capstone2 + " Capstone");
                        logicBoard.addPiece(5, getCurrentChords()[0], getCurrentChords()[1]);
                        optionFrame.dispose();
                    }
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

    public void movePiece() {
        
        JLabel moveLabel = new JLabel("HOW MANY PIECES DO YOU WANT TO MOVE?");

        JTextField textField = new JTextField();

        JPanel topMovePanel = new JPanel();
        topMovePanel.setLayout(new GridLayout(2, 1));
        topMovePanel.add(moveLabel);
        topMovePanel.add(textField);
        
        
        JFrame moveFrame = new JFrame("Move Piece");
        moveFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        moveFrame.setSize(300, 200);
        moveFrame.setLayout(new BorderLayout());
        moveFrame.add(topMovePanel);

        moveFrame.setVisible(true);

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
