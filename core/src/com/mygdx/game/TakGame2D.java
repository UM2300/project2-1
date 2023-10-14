package com.mygdx.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TakGame2D {


    private JButton colorButton;


    private final ImageIcon whiteFlatStone = loadAndResizeImage("assets/WhitePiece.png", 0.1);
    private final ImageIcon whiteStandingStone = loadAndResizeImage("assets/WhiteStanding.png", 0.1);
    private final ImageIcon whiteCapstone = loadAndResizeImage("assets/WhiteCapstone.png", 0.1);
    private final ImageIcon brownFlatStone = loadAndResizeImage("assets/BlackPiece.png", 0.1);
    private final ImageIcon brownStandingStone = loadAndResizeImage("assets/BlackStanding.png", 0.1);
    private final ImageIcon brownCapstone = loadAndResizeImage("assets/BlackCapstone.png", 0.1);



    private JLabel leftStonesLabel, leftCapstoneLabel, rightStonesLabel, rightCapstoneLabel;

    private JFrame frame;
    private JFrame optionFrame;
    private JLabel optionLabel;
    private JFrame moveFrame;
    private boardButton[][] boardButtons;
    private JPanel leftPanel, rightPanel, boardPanel;
    private int pieceQuantity = 1;
    private int dropNum;
    private final int BOARD_SIZE = 5;

    Draw draw = new Draw();

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

    public int getPieceQuantity(){
        return pieceQuantity;
    }

    public void setPieceQuantity(int pieceQuantity){
        this.pieceQuantity = pieceQuantity;
    }

    public int getDropNum(){
        return dropNum;
    }

    public void setDropNum(int dropNum){
        this.dropNum = dropNum;
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

    public int stones = 21;
    public int capstone = 1;
    public int stones2 = 21;
    public int capstone2 = 1;

    public void startingWindow() {
        
        final JFrame startFrame = new JFrame();

        JLabel startLabel = new JLabel("Tak");
        Font newFont = new Font("Georgia", Font.BOLD, 60);
        startLabel.setFont(newFont);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(Color.LIGHT_GRAY);
        topPanel.add(startLabel);


        JButton startButton = new JButton("Start");
        startButton.setSize(100, 50);

        startButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new TakGame2D();
                frame.setVisible(true);
                startFrame.dispose();
            }

            
        });
        
        JButton instructionsButton = new JButton("Instructions");
        startButton.setSize(100, 50);

        instructionsButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File instructionsFile = new File("Instructions.txt");

                    if (Desktop.isDesktopSupported() && instructionsFile.exists()) {
                        Desktop desktop = Desktop.getDesktop();
                        desktop.open(instructionsFile);
                    } else {
                        JOptionPane.showMessageDialog(null, "Unable to open instructions.");
                    }
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(null, "Error opening instructions.");
                    exception.printStackTrace();
                }
            }
        });
    
        JPanel bottomPanel1 = new JPanel();
        bottomPanel1.setLayout(new FlowLayout());
        bottomPanel1.add(startButton);
        bottomPanel1.add(instructionsButton);
        
        startFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        startFrame.setSize(400, 300);
        startFrame.setLocationRelativeTo(null);
        startFrame.setLayout(new GridLayout(2, 1));
        startFrame.add(topPanel);
        startFrame.add(bottomPanel1);
        startFrame.setVisible(true);

    }

    public TakGame2D() {

        colorButton = new JButton("PLAYER TURN");
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.add(colorButton);

        leftStonesLabel = new JLabel(stones + " Stones");
        leftCapstoneLabel = new JLabel(capstone + " Capstone");
        rightStonesLabel = new JLabel(stones2 + " Stones");
        rightCapstoneLabel = new JLabel(capstone2 + " Capstone");


        frame = new JFrame("TakGame2D");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        frame.add(topRightPanel, BorderLayout.NORTH);

        leftPanel = new JPanel();
        rightPanel = new JPanel();
        boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(leftStonesLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(leftCapstoneLabel);
        leftPanel.add(Box.createVerticalGlue());
        colorButton.setBackground(Color.BLUE);

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

                        for(int i=0; i< boardButtons.length; i++){
                            for(int j=0; j<boardButtons.length; j++){
                                if(logicBoard.getBoard()[i][j].isEmpty())
                                    boardButtons[i][j].setIsEmpty(true);
                            }
                        }


                        boardButton source = (boardButton) e.getSource();
                        int xChord = source.getXChord();
                        int yChord = source.getYChord();
                        int[] buttonChords = {xChord,yChord};

                        
                        if(getMidTurn()){
                            
                            setMoveToChords(buttonChords);
                            int dir = targetDir(getCurrentChords(), getMoveToChords());
                            
                            //dropPiece();

                            logicBoard.move(currentChords[0], currentChords[1], pieceQuantity, dir, pieceQuantity);
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
                                movePiece();
                                setCurrentChords(buttonChords);
                                setMidTurn(true);
                            }
                        }

                        
                        //logicBoard.checkWinCondition();
                        //logicBoard.winBoardFull();
                        //logicBoard.checkState();
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

        frame.setLocationRelativeTo(null);
        frame.setVisible(false);
    }



    private ImageIcon loadAndResizeImage(String path, double scale) {
        ImageIcon originalIcon = new ImageIcon(path);
        int newWidth = (int) (originalIcon.getIconWidth() * scale);
        int newHeight = (int) (originalIcon.getIconHeight() * scale);
        Image resizedImage = originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
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
                        colorButton.setBackground(Color.RED);
                        stones--;
                        leftStonesLabel.setText(stones + " Stones");
                        logicBoard.addPiece(0, getCurrentChords()[0], getCurrentChords()[1]);
                        boardButtons[getCurrentChords()[0]-1][getCurrentChords()[1]-1].setIcon(whiteFlatStone);
                        optionFrame.dispose();
                    }
                } else {
                    if (stones2 > 0) {
                        colorButton.setBackground(Color.BLUE);
                        stones2--;
                        rightStonesLabel.setText(stones2 + " Stones");
                        logicBoard.addPiece(3, getCurrentChords()[0], getCurrentChords()[1]);
                        boardButtons[getCurrentChords()[0]-1][getCurrentChords()[1]-1].setIcon(brownFlatStone);
                        optionFrame.dispose();
                    }
                }
                logicBoard.checkWinCondition();
                logicBoard.winBoardFull();
                logicBoard.checkState();
            }
            
        });


        standingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){

                if(logicBoard.getCurrentPlayer().equals("WHITE")){
                    if (stones > 0) {
                        colorButton.setBackground(Color.RED);
                        stones--;  // Decrease stone count
                        leftStonesLabel.setText(stones + " Stones");  // Update label
                        logicBoard.addPiece(1, getCurrentChords()[0], getCurrentChords()[1]);
                        boardButtons[getCurrentChords()[0]-1][getCurrentChords()[1]-1].setIcon(whiteStandingStone);
                        optionFrame.dispose();
                    }
                } else {
                    if (stones2 > 0) {
                        colorButton.setBackground(Color.BLUE);
                        stones2--;
                        rightStonesLabel.setText(stones2 + " Stones");
                        logicBoard.addPiece(4, getCurrentChords()[0], getCurrentChords()[1]);
                        boardButtons[getCurrentChords()[0]-1][getCurrentChords()[1]-1].setIcon(brownStandingStone);
                        optionFrame.dispose();
                    }
                }
                logicBoard.checkWinCondition();
                logicBoard.winBoardFull();
                logicBoard.checkState();

            }
        });

        capstoneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){

                if(logicBoard.getCurrentPlayer().equals("WHITE")){
                    if (capstone > 0) {
                        colorButton.setBackground(Color.RED);
                        capstone--;
                        leftCapstoneLabel.setText(capstone + " Capstone");
                        logicBoard.addPiece(2, getCurrentChords()[0], getCurrentChords()[1]);
                        boardButtons[getCurrentChords()[0]-1][getCurrentChords()[1]-1].setIcon(whiteCapstone);
                        optionFrame.dispose();
                    }
                } else {
                    if (capstone2 > 0) {
                        colorButton.setBackground(Color.BLUE);
                        capstone2--;
                        rightCapstoneLabel.setText(capstone2 + " Capstone");
                        logicBoard.addPiece(5, getCurrentChords()[0], getCurrentChords()[1]);
                        boardButtons[getCurrentChords()[0]-1][getCurrentChords()[1]-1].setIcon(brownCapstone);
                        optionFrame.dispose();
                    }
                }
                logicBoard.checkWinCondition();
                logicBoard.winBoardFull();
                logicBoard.checkState();

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
        optionFrame.setLocationRelativeTo(null);
        optionFrame.add(topOptionPanel, BorderLayout.NORTH);
        optionFrame.add(bottomOptionPanel, BorderLayout.SOUTH);


        optionFrame.setVisible(true);

        
    }

    public void movePiece() {

        JLabel moveLabel = new JLabel("HOW MANY PIECES DO YOU WANT TO MOVE?");
        final JTextField textField = new JTextField();
        JButton button = new JButton("Confirm");


        button.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e){

                String txt = textField.getText();
                int quantity = Integer.parseInt(txt);
                setPieceQuantity(quantity);
                moveFrame.dispose();
            }

        });

        JPanel topMovePanel = new JPanel();
        topMovePanel.setLayout(new GridLayout(3, 1));
        topMovePanel.add(moveLabel);
        topMovePanel.add(textField);
        topMovePanel.add(button);


        moveFrame = new JFrame("Move Piece");
        moveFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        moveFrame.setSize(300, 200);
        moveFrame.setLayout(new BorderLayout());
        moveFrame.setLocationRelativeTo(null);
        moveFrame.add(topMovePanel);

        moveFrame.setVisible(true);
    }

    public void dropPiece() {
        
        JLabel moveLabel = new JLabel("HOW MANY PIECES DO YOU WANT TO DROP?");
        final JTextField textField = new JTextField();
        JButton button = new JButton("Confirm");


        button.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e){

                String txt = textField.getText();
                int drop = Integer.parseInt(txt);
                setDropNum(drop);
                moveFrame.dispose();
            }

        });

        JPanel topMovePanel = new JPanel();
        topMovePanel.setLayout(new GridLayout(3, 1));
        topMovePanel.add(moveLabel);
        topMovePanel.add(textField);
        topMovePanel.add(button);
        
        
        moveFrame = new JFrame("Move Piece");
        moveFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        moveFrame.setSize(300, 200);
        moveFrame.setLayout(new BorderLayout());
        moveFrame.setLocationRelativeTo(null);
        moveFrame.add(topMovePanel);

        moveFrame.setVisible(true);
    }

    public void endScreen() {

        JLabel winLabel = new JLabel("Congratulations!");
        Font font = new Font("Georgia", Font.BOLD, 40);
        winLabel.setFont(font);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(Color.LIGHT_GRAY);
        topPanel.add(winLabel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.setBackground(Color.LIGHT_GRAY);

        JFrame endFrame = new JFrame();
        endFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        endFrame.setSize(400,400);
        endFrame.setLocationRelativeTo(null);
        endFrame.setLayout(new GridLayout(2, 1));
        endFrame.add(topPanel);
        endFrame.add(bottomPanel);
        endFrame.setVisible(true);


    }

    public static void main(String[] args) {

        TakGame2D game = new TakGame2D();
        game.startingWindow();
    }
}
