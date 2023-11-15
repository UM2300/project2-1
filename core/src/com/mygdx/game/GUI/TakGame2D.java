package com.mygdx.game.GUI;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.basic.BasicButtonUI;

import com.mygdx.game.GameLogic.board;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class represents the 2D GUI version of the game
 */

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
    private int dir;

    private int dropIteration;

    Draw draw = new Draw();

    private int[] currentChords;
    private int[] moveToChords;

    private boolean midTurn = false;

    board logicBoard = new board();

    boardButton boardButton;

    public int stones = 21;
    public int capstone = 1;
    public boolean wCapstone = true;
    public int stones2 = 21;
    public int capstone2 = 1;
    public boolean bCapstone = true;

    private JButton instructionsButton = new JButton("Instructions");
    private JButton instructionsNewButton = new JButton("Instructions");

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

    public int getDir(){
        return dir;
    }

    public void setDir(int dir){
        this.dir=dir;
    }

    public int getDropIteration(){
        return dropIteration;
    }

    public void setDropIteration(int dropIteration){
        this.dropIteration=dropIteration;
    }

    /**
     * Calculates the direction of a move using the current coordinates and the destination coordinates of a piece
     * @param currentCoords current coordinates of a piece
     * @param moveToCoords destination coordinates of a piece
     * @return
     */
    public int targetDir(int[] currentCoords, int[] moveToCoords){

        if(currentCoords[0]!=moveToCoords[0]){
            if(moveToCoords[0]>currentCoords[0])
                return 2;
            else
                return 0;
        }
        else{
            if(moveToCoords[1]>currentCoords[1])
                return 1;
            else
                return 3;
        }

    }

    /**
     * Displays the starting window of the GUI with start and instructions button
     */
    public void startingWindow() {

        if (frame != null) {
            frame.dispose(); // Dispose of the existing frame if it exists
        }
        final JFrame startFrame = new JFrame("Tak Menu");

        // Set up the title label
        JLabel startLabel = new JLabel("Tak");
        Font titleFont = new Font("Algerian", Font.BOLD, 60);
        startLabel.setFont(titleFont);
        startLabel.setForeground(new Color(226, 199, 153));
    
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20)); // Add vertical padding
        topPanel.setBackground(new Color(192, 130, 97));
        topPanel.add(startLabel);
    
        // Create a button panel for the menu buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 0)); // Added vertical spacing
        buttonPanel.setBackground(new Color(226, 199, 153)); // Set background color

        // Define a custom ButtonUI for highlighting
        ButtonUI highlightUI = new BasicButtonUI() {
            private Color hoverColor = new Color(242, 236, 190); // Color when hovering

            @Override
            public void paint(Graphics g, JComponent c) {
                AbstractButton button = (AbstractButton) c;
                ButtonModel model = button.getModel();

                if (model.isRollover()) {
                    button.setBackground(hoverColor); // Change the background color on hover
                } else {
                    button.setBackground(new Color(226, 199, 153)); // Restore the default color
                }
                super.paint(g, c);
            }
        };
    
        // Create and style the "Start" button
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Algerian", Font.BOLD, 24));
        startButton.setUI(highlightUI);
        startButton.setBackground(new Color(226, 199, 153)); // Button color
        startButton.setForeground(new Color(192, 130, 97)); // Text color
        startButton.setFocusPainted(false); // Remove the border around the text
        startButton.setPreferredSize(new Dimension(200, 50)); // Button size
    
        // Add action listener for the "Start" button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TakGame2D();
                frame.setVisible(true);
                startFrame.dispose();
            }
        });
        // Create and style the "Instructions" button
        instructionsButton.setFont(new Font("Algerian", Font.BOLD, 24));
        instructionsButton.setUI(highlightUI);
        instructionsButton.setBackground(new Color(226, 199, 153)); // Button color
        instructionsButton.setForeground(new Color(192, 130, 97)); // Text color
        instructionsButton.setFocusPainted(false); // Remove the border around the text
        instructionsButton.setPreferredSize(new Dimension(200, 50)); // Button size
    
        // Add action listener for the "Instructions" button
        instructionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a panel to display the instructions
                JPanel instructionsPanel = new JPanel();
                instructionsPanel.setLayout(new BorderLayout());

                // Create a JTextArea to display the instructions text
                JTextArea instructionsText = new JTextArea(20, 50);
                instructionsText.setFont(new Font("Arial", Font.PLAIN, 16));
                instructionsText.setLineWrap(true);
                instructionsText.setWrapStyleWord(true);
                instructionsText.setEditable(false); // Make it read-only

                // Add a JScrollPane to the JTextArea for scrolling if needed
                JScrollPane scrollPane = new JScrollPane(instructionsText);

                // Load instructions from a file or set the text directly
                try {
                    File instructionsFile = new File("core\\src\\com\\mygdx\\game\\Instructions.txt");
                    if (instructionsFile.exists()) {
                        // Load instructions from the file
                        BufferedReader reader = new BufferedReader(new FileReader(instructionsFile));
                        String line;
                        StringBuilder instructions = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            instructions.append(line).append("\n");
                        }
                        reader.close();
                        instructionsText.setText(instructions.toString());
                        instructionsText.setCaretPosition(0);
                    } else {
                        // If the file doesn't exist, set some default instructions
                        instructionsText.setText("No instructions found.");
                    }
                } catch (IOException exception) {
                    instructionsText.setText("Error opening instructions.");
                    exception.printStackTrace();
                }

                // Add the scrollPane to the instructions panel
                instructionsPanel.add(scrollPane, BorderLayout.CENTER);

                // Show the instructions in a dialog
                JOptionPane.showMessageDialog(null, instructionsPanel, "Instructions", JOptionPane.PLAIN_MESSAGE);
            }
        });
    
        // Add buttons to the button panel
        buttonPanel.add(startButton);
        buttonPanel.add(instructionsButton);
    
        // Add components to the startFrame
        startFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        startFrame.setSize(400, 300);
        startFrame.setLocationRelativeTo(null);
        startFrame.setLayout(new BorderLayout()); // Use BorderLayout for more control
    
        // Add the title label to the top
        startFrame.add(topPanel, BorderLayout.NORTH);
    
        // Add the button panel to the center with padding
        startFrame.add(buttonPanel, BorderLayout.CENTER);
    
        startFrame.setVisible(true);
    }
    
    /**
     * Constructor for the class which is the frame displaying the game board, pieces and whose turn it is
     */
    public TakGame2D() {
        ToolTipManager.sharedInstance().setInitialDelay(0);

/////////////////////////////////////////////////////////////////////////////////////////////////////////
        colorButton = new JButton("WHITE TURN");
        colorButton.setForeground(new Color(192, 130, 97));
        colorButton.setFont(new Font("Algerian", Font.PLAIN, 14));
        colorButton.setBackground(Color.WHITE);
        instructionsNewButton.setFont(new Font("Algerian", Font.PLAIN, 14));
        instructionsNewButton.setBackground(new Color(226, 199, 153)); 
        instructionsNewButton.setForeground(new Color(192, 130, 97)); 
        instructionsNewButton.setFocusPainted(false); 

        // Add action listener for the "Instructions" button
        instructionsNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a panel to display the instructions
                JPanel instructionsPanel = new JPanel();
                instructionsPanel.setLayout(new BorderLayout());

                // Create a JTextArea to display the instructions text
                JTextArea instructionsText = new JTextArea(20, 50);
                instructionsText.setFont(new Font("Arial", Font.PLAIN, 16));
                instructionsText.setLineWrap(true);
                instructionsText.setWrapStyleWord(true);
                instructionsText.setEditable(false); // Make it read-only

                // Add a JScrollPane to the JTextArea for scrolling if needed
                JScrollPane scrollPane = new JScrollPane(instructionsText);

                // Load instructions from a file or set the text directly
                try {
                    File instructionsFile = new File("core\\src\\com\\mygdx\\game\\Instructions.txt");                    
                        if (instructionsFile.exists()) {
                        // Load instructions from the file
                        BufferedReader reader = new BufferedReader(new FileReader(instructionsFile));
                        String line;
                        StringBuilder instructions = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            instructions.append(line).append("\n");
                        }
                        reader.close();
                        instructionsText.setText(instructions.toString());
                        instructionsText.setCaretPosition(0);
                    } else {
                        // If the file doesn't exist, set some default instructions
                        instructionsText.setText("No instructions found.");
                    }
                } catch (IOException exception) {
                    instructionsText.setText("Error opening instructions.");
                    exception.printStackTrace();
                }

                // Add the scrollPane to the instructions panel
                instructionsPanel.add(scrollPane, BorderLayout.CENTER);

                // Show the instructions in a dialog
                JOptionPane.showMessageDialog(null, instructionsPanel, "Instructions", JOptionPane.PLAIN_MESSAGE);
            }
        });


        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.add(instructionsNewButton);
        topRightPanel.add(colorButton);

        leftStonesLabel = new JLabel(" White Stones: " + stones);
        leftStonesLabel.setForeground(new Color(192, 130, 97));
        leftStonesLabel.setFont(new Font("Algerian", Font.PLAIN, 14));
        leftCapstoneLabel = new JLabel(" White Capstone: " + capstone);
        leftCapstoneLabel.setForeground(new Color(192, 130, 97));
        leftCapstoneLabel.setFont(new Font("Algerian", Font.PLAIN, 14));
        rightStonesLabel = new JLabel(" Brown Stones: " + stones2);
        rightStonesLabel.setForeground(new Color(192, 130, 97));
        rightStonesLabel.setFont(new Font("Algerian", Font.PLAIN, 14));
        rightCapstoneLabel = new JLabel(" Brown Capstone: " + capstone2);
        rightCapstoneLabel.setForeground(new Color(192, 130, 97));
        rightCapstoneLabel.setFont(new Font("Algerian", Font.PLAIN, 14));


        frame = new JFrame("Tak");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 450);
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
        colorButton.setBackground(Color.WHITE);

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

                        boolean checker = boardButtons[xChord-1][yChord-1].getIsEmpty();

                        if(checker||midTurn||(logicBoard.getBoard()[xChord-1][yChord-1].get(logicBoard.getBoard()[xChord-1][yChord-1].size()-1)<=2 && logicBoard.getCurrentPlayer().equals("WHITE"))||
                        (logicBoard.getBoard()[xChord-1][yChord-1].get(logicBoard.getBoard()[xChord-1][yChord-1].size()-1)>2 && logicBoard.getCurrentPlayer().equals("BROWN"))){
                            
                            if(getMidTurn()){
                            
                                setMoveToChords(buttonChords);
                                //int dir = targetDir(getCurrentChords(), getMoveToChords());
                                
                                dropPiece(1);

                                //logicBoard.move(currentChords[0], currentChords[1], pieceQuantity, dir, pieceQuantity);
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

                            System.out.println(xChord+" "+yChord);

                            if(!getMidTurn()){

                                if(logicBoard.getCurrentPlayer().equals("WHITE")){
                                    colorButton.setForeground(Color.WHITE);
                                    colorButton.setBackground(new Color(192, 130, 97));
                                    colorButton.setText("BROWN TURN");
                                    colorButton.setFont(new Font("Algerian", Font.PLAIN, 14));
                                }
                                else{
                                    colorButton.setForeground(new Color(192, 130, 97));
                                    colorButton.setBackground(Color.WHITE);
                                    colorButton.setText("WHITE TURN");
                                    colorButton.setFont(new Font("Algerian", Font.PLAIN, 14));
                                }
                            }
                        }
                    }
                });

                boardPanel.add(boardButtons[i][j]);
                if ((i + j) % 2 == 0) {
                    boardButtons[i][j].setBackground(new Color(226, 199, 153));
                } else {
                    boardButtons[i][j].setBackground(new Color(192, 130, 97));
                }
            }
        }

        // Adding panels to the main frame
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.EAST);
        frame.add(boardPanel, BorderLayout.CENTER);

        // Optional: Setting some preferred sizes and colors for visualization purposes
        leftPanel.setPreferredSize(new Dimension(150, 400));
        rightPanel.setPreferredSize(new Dimension(150, 400));
        leftPanel.setBackground(new Color(242, 236, 190));
        rightPanel.setBackground(new Color(242, 236, 190));

        frame.setLocationRelativeTo(null);
        frame.setVisible(false);
    }



    /**
     * Responsible for loading and resizing the images of the pieces dislayed on the board
     * @param path file name of the respective image
     * @param scale scale factor
     * @return image of piece
     */
    private ImageIcon loadAndResizeImage(String path, double scale) {
        ImageIcon originalIcon = new ImageIcon(path);
        int newWidth = (int) (originalIcon.getIconWidth() * scale);
        int newHeight = (int) (originalIcon.getIconHeight() * scale);
        Image resizedImage = originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    /**
     * Displays popup when adding a piece to the board
     */
    public void addPiece() {

        // Create and configure optionFrame
        optionFrame = new JFrame("Choose Piece");
        optionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        optionFrame.setSize(300, 200);
        optionFrame.setLayout(new BorderLayout());
        optionFrame.setLocationRelativeTo(null);

        // Create a top panel for the title label
        JPanel topOptionPanel = new JPanel();
        topOptionPanel.setBackground(new Color(242, 236, 190)); // Set background color
        topOptionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        optionFrame.add(topOptionPanel, BorderLayout.NORTH);

        // Create a label with a custom font and add it to the top panel
        optionLabel = new JLabel("Choose a piece to add");
        optionLabel.setFont(new Font("Algerian", Font.PLAIN, 18)); // Custom font
        optionLabel.setForeground(new Color(192, 130, 97));
        optionLabel.setBackground(new Color(192, 130, 97));
        topOptionPanel.add(optionLabel);

        // Create a bottom panel for the radio buttons
        JPanel bottomOptionPanel = new JPanel();
        bottomOptionPanel.setBackground(new Color(242, 236, 190)); // Set background color
        bottomOptionPanel.setLayout(new GridLayout(3, 1));
        bottomOptionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        optionFrame.add(bottomOptionPanel, BorderLayout.SOUTH);

        // Create and style the radio buttons
        JRadioButton flatButton = new JRadioButton("Add Flat Stone");
        JRadioButton standingButton = new JRadioButton("Add Standing Stone");
        final JRadioButton capstoneButton = new JRadioButton("Add Capstone");

        Font radioButtonFont = new Font("Algerian", Font.PLAIN, 16); // Custom font for radio buttons
        flatButton.setBackground(new Color(242, 236, 190));
        flatButton.setFont(radioButtonFont);
        flatButton.setForeground(new Color(192, 130, 97));
        standingButton.setBackground(new Color(242, 236, 190));
        standingButton.setFont(radioButtonFont);
        standingButton.setForeground(new Color(192, 130, 97));
        capstoneButton.setBackground(new Color(242, 236, 190));
        capstoneButton.setFont(radioButtonFont);
        capstoneButton.setForeground(new Color(192, 130, 97));

        // Add radio buttons to the bottom panel
        bottomOptionPanel.add(flatButton);
        bottomOptionPanel.add(standingButton);
        bottomOptionPanel.add(capstoneButton);

        optionFrame.setVisible(true);

        flatButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){

                if(logicBoard.getCurrentPlayer().equals("WHITE")){
                    logicBoard.addPiece(0, getCurrentChords()[0], getCurrentChords()[1]);
                    boardButtons[getCurrentChords()[0]-1][getCurrentChords()[1]-1].setIcon(whiteFlatStone);
                    stones--;
                    leftStonesLabel.setText("White Stones: " + stones);
                    optionFrame.dispose();
                    
                } else {
                    logicBoard.addPiece(3, getCurrentChords()[0], getCurrentChords()[1]);
                    boardButtons[getCurrentChords()[0]-1][getCurrentChords()[1]-1].setIcon(brownFlatStone);
                    stones2--;
                    rightStonesLabel.setText("Brown Stones: " + stones2);
                    optionFrame.dispose();
                }
                logicBoard.checkWinCondition();
                logicBoard.winBoardFull();
                logicBoard.checkState();
                System.out.println(logicBoard.isGameEnded());;

                if(logicBoard.isGameEnded() && logicBoard.getCurrentPlayer() == "WHITE") {
                    endScreen("WHITE");
                }
                else if(logicBoard.isGameEnded() && logicBoard.getCurrentPlayer() == "BROWN") {
                    endScreen("BROWN");
                }
             }
            
        });


        standingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(logicBoard.getCurrentPlayer().equals("WHITE")){     

                    logicBoard.addPiece(1, getCurrentChords()[0], getCurrentChords()[1]);
                    boardButtons[getCurrentChords()[0]-1][getCurrentChords()[1]-1].setIcon(whiteStandingStone);
                    stones--;
                    leftStonesLabel.setText("White Stones: " + stones);
                    optionFrame.dispose();
                    
                } else {

                    logicBoard.addPiece(4, getCurrentChords()[0], getCurrentChords()[1]);
                    boardButtons[getCurrentChords()[0]-1][getCurrentChords()[1]-1].setIcon(brownStandingStone);
                    stones2--;
                    rightStonesLabel.setText("Brown Stones: " + stones2);
                    optionFrame.dispose();

                }
                logicBoard.checkWinCondition();
                logicBoard.winBoardFull();
                logicBoard.checkState();

                if(logicBoard.isGameEnded() && logicBoard.getCurrentPlayer() == "WHITE") {
                    endScreen("WHITE");
                }
                else if(logicBoard.isGameEnded() && logicBoard.getCurrentPlayer() == "BROWN") {
                    endScreen("BROWN");
                }

            }
        });

        capstoneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){

                if(logicBoard.getCurrentPlayer().equals("WHITE")){

                    if (!wCapstone) capstoneButton.setVisible(false);

                    else {

                        logicBoard.addPiece(2, getCurrentChords()[0], getCurrentChords()[1]);
                        boardButtons[getCurrentChords()[0] - 1][getCurrentChords()[1] - 1].setIcon(whiteCapstone);
                        wCapstone = false;
                        capstone--;
                        leftCapstoneLabel.setText(" White Capstone: " + capstone);
                        optionFrame.dispose();

                    }

                } else {

                    if (!bCapstone) capstoneButton.setVisible(false);

                    else {

                        logicBoard.addPiece(5, getCurrentChords()[0], getCurrentChords()[1]);
                        boardButtons[getCurrentChords()[0] - 1][getCurrentChords()[1] - 1].setIcon(brownCapstone);
                        bCapstone = false;
                        capstone2--;
                        rightCapstoneLabel.setText(" Brown Capstone: " + capstone2);
                        optionFrame.dispose();

                    }
                }
                logicBoard.checkWinCondition();
                logicBoard.winBoardFull();
                logicBoard.checkState();

                if(logicBoard.isGameEnded() && logicBoard.getCurrentPlayer() == "WHITE") {
                    endScreen("WHITE");
                }
                else if(logicBoard.isGameEnded() && logicBoard.getCurrentPlayer() == "BROWN") {
                    endScreen("BROWN");
                }
                
            }
        });

        /*JPanel topOptionPanel = new JPanel();
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


        optionFrame.setVisible(true);*/

        
    }

    /**
     * Displays popup window when moving a piece on the board
     */
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
        moveFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        moveFrame.setSize(300, 200);
        moveFrame.setLayout(new BorderLayout());
        moveFrame.setLocationRelativeTo(null);
        moveFrame.add(topMovePanel);

        moveFrame.setVisible(true);
    }

    /**
     * Displays popup window to specify how many pieces of a stack (or a single piece) to drop off at the current tile
     */
    public void dropPiece(int iteration) {
        
        JLabel moveLabel = new JLabel("HOW MANY PIECES DO YOU WANT TO DROP?");
        final JTextField textField = new JTextField();
        JButton button = new JButton("Confirm");

        setDropIteration(iteration);

        button.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e){

                String txt = textField.getText();
                int drop = Integer.parseInt(txt);
                setDropNum(drop);

                int dir = targetDir(getCurrentChords(), getMoveToChords());

                if(getPieceQuantity()-getDropNum()<=0){
                    setDropNum(getPieceQuantity());
                    setMidTurn(false);
                }

                //System.out.println("Coodinates of the button are "+currentChords[0]+" "+currentChords[1]);

                if(logicBoard.getHeldPieces().isEmpty()){
                    logicBoard.move(currentChords[0], currentChords[1], getPieceQuantity(), dir, getDropNum());
                    updateVisualBoard();
                    logicBoard.checkState();
                    moveFrame.dispose();
                }
                else{
                    System.out.println("Last piece is above "+currentChords[0]+" "+currentChords[1]+" going "+getDir());
                    logicBoard.move(currentChords[0], currentChords[1], logicBoard.getHeldPieces(), getDir(), getDropNum());
                    updateVisualBoard();
                    logicBoard.checkState();
                    moveFrame.dispose();
                }

                if(!logicBoard.getHeldPieces().isEmpty()){
                    System.out.println("Coming from "+currentChords[0]+" "+currentChords[1]+" "+dir);
                    
                    
                    if(getDropIteration()==1){
                        setDir(dir);
                        alterChords(currentChords[0]-1, currentChords[1]-1, getDir());
                    }
                    else{
                        alterChords(currentChords[0], currentChords[1], getDir());
                    }
                    moveFrame.dispose();
                    dropPiece(getDropIteration()+1);
                }
            }

        });
        

        JPanel topMovePanel = new JPanel();
        topMovePanel.setLayout(new GridLayout(3, 1));
        topMovePanel.add(moveLabel);
        topMovePanel.add(textField);
        topMovePanel.add(button);
        
        
        moveFrame = new JFrame("Move Piece");
        moveFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        moveFrame.setSize(300, 200);
        moveFrame.setLayout(new BorderLayout());
        moveFrame.setLocationRelativeTo(null);
        moveFrame.add(topMovePanel);

        moveFrame.setVisible(true);
    }


    public void alterChords(int x, int y, int dir){
        
        switch(dir){
            case 0:
                x--;
                break;
            case 1:
                y++;
                break;
            case 2:
                x++;
                break;
            case 3:
                y--;
                break;
        }
        setCurrentChords(new int[]{x,y});
    }


    /**
     * Resets the game board 
     */
    public void resetGame() {
        // Clear game state and update the board
        logicBoard = new board();
        pieceQuantity = 1;
        dropNum = 0;
        setMidTurn(false);

        stones = 21;
        capstone = 1;
        wCapstone = true;
        stones2 = 21;
        capstone2 = 1;
        bCapstone = true;

        // Clear the icons on the board buttons
        for (int i = 0; i < boardButtons.length; i++) {
            for (int j = 0; j < boardButtons[i].length; j++) {
                boardButtons[i][j].setIcon(null);
            }
        }
        frame.dispose();

        // Update the logicBoard with its initial state if necessary
        logicBoard = new board();

        // Update the JLabels
        leftStonesLabel.setText("White Stones: " + stones);
        leftCapstoneLabel.setText("White Capstone: " + capstone);
        rightStonesLabel.setText("Brown Stones: " + stones2);
        rightCapstoneLabel.setText("Brown Capstone: " + capstone2);

    }

    /**
     * Displays the endscreen when a winning condition is met
     * @param currentPlayer the player who just performed a move (ie. the current player whose turn it is)
     */
    public void endScreen(String currentPlayer) {
        final JFrame endFrame = new JFrame("Game Over");
    
        JLabel winLabel = new JLabel("Congratulations!");
        Font titleFont = new Font("Algerian", Font.BOLD, 35);
        winLabel.setFont(titleFont);
    
        JLabel winMessageLabel;
    
        if (currentPlayer.equals("WHITE")) {
            winMessageLabel = new JLabel("Brown wins!");
        } else if (currentPlayer.equals("BROWN")) {
            winMessageLabel = new JLabel("White wins!");
        } else {
            winMessageLabel = new JLabel("It's a draw!");
        }
    
        Font messageFont = new Font("Algerian", Font.BOLD, 24);
        winMessageLabel.setFont(messageFont);
    
        // Create a panel for the messages
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        messagePanel.setBackground(new Color(192, 130, 97));
        messagePanel.add(winLabel);
        messagePanel.add(winMessageLabel);
        winLabel.setForeground(new Color(226, 199, 153));
        winMessageLabel.setForeground(new Color(226, 199, 153));
    
        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(226, 199, 153));

        ButtonUI highlightUI = new BasicButtonUI() {
            private Color hoverColor = new Color(242, 236, 190); // Color when hovering

            @Override
            public void paint(Graphics g, JComponent c) {
                AbstractButton button = (AbstractButton) c;
                ButtonModel model = button.getModel();

                if (model.isRollover()) {
                    button.setBackground(hoverColor); // Change the background color on hover
                } else {
                    button.setBackground(new Color(226, 199, 153)); // Restore the default color
                }
                super.paint(g, c);
            }
        };

        JButton startOverButton = new JButton("Start Over");
        startOverButton.setFont(new Font("Algerian", Font.BOLD, 24));
        startOverButton.setUI(highlightUI);
        startOverButton.setBackground(new Color(226, 199, 153)); 
        startOverButton.setForeground(new Color(192, 130, 97)); 
        startOverButton.setFocusPainted(false); 
        startOverButton.setPreferredSize(new Dimension(200, 50)); 
    
        // Add action listener for the "Start" button
        startOverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame(); 
                optionFrame.dispose();
                startingWindow();
                endFrame.dispose();

            }
        });

        buttonPanel.add(startOverButton);
    
        // Add components to the endFrame
        endFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        endFrame.setSize(400, 300);
        endFrame.setLocationRelativeTo(null);
        endFrame.setLayout(new GridLayout(2, 1));
    
        // Add the message panel to the top
        endFrame.add(messagePanel);
    
        // Add the button panel to the bottom
        endFrame.add(buttonPanel);
    
        endFrame.setVisible(true);
    }
    

    /**
     * Updates the game board on the GUI
     * Assuming logicBoard has a method like getTileState(x, y) that returns the numerical state of the tile
     * Assuming boardButtons is a 2D array of JButtons representing the GUI board
     */
    public void updateVisualBoard() {
    
        for (int i = 0; i < boardButtons.length; i++) {
            for (int j = 0; j < boardButtons[i].length; j++) {
                int state = logicBoard.getPieceAt(i, j);

                String stackDetails = logicBoard.getStackDetails(i, j);
                boardButtons[i][j].setToolTipText(stackDetails);

                switch(state) {
                    case 0: boardButtons[i][j].setIcon(whiteFlatStone);
                        break;
                    case 1: boardButtons[i][j].setIcon(whiteStandingStone);
                        break;
                    case 2: boardButtons[i][j].setIcon(whiteCapstone);
                        break;
                    case 3: boardButtons[i][j].setIcon(brownFlatStone);
                        break;
                    case 4: boardButtons[i][j].setIcon(brownStandingStone);
                        break;
                    case 5: boardButtons[i][j].setIcon(brownCapstone);
                        break;
                    default: boardButtons[i][j].setIcon(null); // Default to no icon or some default image
                }
            }
        }
    }



    public static void main(String[] args) {

        TakGame2D game = new TakGame2D();
        game.startingWindow();
    }
}