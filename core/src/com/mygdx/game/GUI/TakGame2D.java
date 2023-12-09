package com.mygdx.game.GUI;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.basic.BasicButtonUI;

import com.mygdx.game.GameLogic.Baseline_Agent;
import com.mygdx.game.GameLogic.MCTSAgent;
import com.mygdx.game.GameLogic.board;
import com.mygdx.game.GameLogic.game;
import com.mygdx.game.GameLogic.MCTSAgent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class represents the 2D GUI version of the game
 */

public class TakGame2D {

    private JButton colorButton, menuOption;
    private final ImageIcon whiteFlatStone = loadAndResizeImage("assets/WhitePiece.png", 0.1);
    private final ImageIcon whiteStandingStone = loadAndResizeImage("assets/WhiteStanding.png", 0.1);
    private final ImageIcon whiteCapstone = loadAndResizeImage("assets/WhiteCapstone.png", 0.1);
    private final ImageIcon brownFlatStone = loadAndResizeImage("assets/BlackPiece.png", 0.1);
    private final ImageIcon brownStandingStone = loadAndResizeImage("assets/BlackStanding.png", 0.1);
    private final ImageIcon brownCapstone = loadAndResizeImage("assets/BlackCapstone.png", 0.1);
    public ImageIcon icon = new ImageIcon("assets/takIcon.png"); 
    public String instructions = "Instructions";
    private JButton instructionsButton = new JButton(instructions);
    private JButton instructionsNewButton = new JButton(instructions);
    private JFrame frame, moveFrame;
    private JLabel optionLabel, leftStonesLabel, leftCapstoneLabel, rightStonesLabel, rightCapstoneLabel;
    public JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    private boardButton[][] boardButtons;
    private JPanel leftPanel, rightPanel, boardPanel;
    private int pieceQuantity = 1;
    private int dropNum;
    private final int BOARD_SIZE = 5;
    private int dir;
    private int dropIteration;
    private int[] currentChords;
    private int[] moveToChords;
    private boolean midTurn = false;
    private boolean baseline=false;
    private boolean mcts = false;

    public int stones = 21;
    public int capstone = 1;
    public boolean wCapstone = true;
    public int stones2 = 21;
    public int capstone2 = 1;
    public boolean bCapstone = true;

    private int moveCounter = 0;


    Draw draw = new Draw();
    board logicBoard = new board();
    boardButton boardButton;
    Baseline_Agent baseline_Agent;
    MCTSAgent mctsAgent;
    Font radioButtonFont = new Font("Algerian", Font.PLAIN, 14);
    JRadioButton flatButton = new JRadioButton("Flat Stone");
    JRadioButton standingButton = new JRadioButton("Standing Stone");
    JRadioButton capstoneButton = new JRadioButton("Capstone");
    JRadioButton flatButtonR = new JRadioButton("Flat Stone");
    JRadioButton standingButtonR = new JRadioButton("Standing Stone");
    JRadioButton capstoneButtonR = new JRadioButton("Capstone");
        

    public boolean getBaseLine(){
        return baseline;
    }

    public void setBaseline(boolean baseline){
        this.baseline=baseline;
    }

    public void setMCTS(boolean mcts){
        this.mcts=mcts;
    }

    public void setLogicBoard(board board){
        this.logicBoard=board;
    }

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
    ButtonUI highlightUI = new BasicButtonUI() {
        private Color hoverColor = new Color(242, 236, 190); 
        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton button = (AbstractButton) c;
            ButtonModel model = button.getModel();
            if (model.isRollover()) {
                button.setBackground(hoverColor); 
            } else {
                button.setBackground(new Color(226, 199, 153)); 
            }
            super.paint(g, c);
        }
    };

    ActionListener instructionsActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showInstructions();
        }
    };

    /**
     * Displays the starting window of the GUI with start and instructions button
     */
    public void startingWindow() {

        if (frame != null) {
            frame.dispose(); 
        }
        final JFrame startFrame = new JFrame("Tak Menu");
        final JFrame gameModeFrame = new JFrame("Game Mode");
        startFrame.setIconImage(icon.getImage());
        gameModeFrame.setIconImage(icon.getImage());
        JLabel startLabel = new JLabel("Tak");
        Font titleFont = new Font("Algerian", Font.BOLD, 60);
        startLabel.setFont(titleFont);
        startLabel.setForeground(new Color(226, 199, 153));
    
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20)); 
        topPanel.setBackground(new Color(192, 130, 97));
        topPanel.add(startLabel);
    
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 0)); 
        buttonPanel.setBackground(new Color(226, 199, 153)); 

        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Algerian", Font.BOLD, 24));
        startButton.setUI(highlightUI);
        startButton.setBackground(new Color(226, 199, 153)); 
        startButton.setForeground(new Color(192, 130, 97)); 
        startButton.setFocusPainted(false); 
        startButton.setPreferredSize(new Dimension(200, 50)); 
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameModeFrame.setVisible(true);
                startFrame.dispose();
            }
        });

        JPanel modeButtonPanel = new JPanel();
        modeButtonPanel.setLayout(new GridLayout(3, 1, 0, 0)); 
        modeButtonPanel.setBackground(new Color(226, 199, 153)); 

        JButton hhButton = new JButton("Multiplayer");
        hhButton.setFont(new Font("Algerian", Font.BOLD, 24));
        hhButton.setUI(highlightUI);
        hhButton.setBackground(new Color(226, 199, 153)); 
        hhButton.setForeground(new Color(192, 130, 97)); 
        hhButton.setFocusPainted(false); 
        hhButton.setPreferredSize(new Dimension(200, 50)); 
        hhButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TakGame2D();
                rightPanel.add(flatButtonR);
                rightPanel.add(Box.createVerticalStrut(1)); // Adjust the vertical spacing as needed
                rightPanel.add(standingButtonR);
                rightPanel.add(Box.createVerticalStrut(1)); // Adjust the vertical spacing as needed
                rightPanel.add(capstoneButtonR);
                leftPanel.add(flatButton);
                leftPanel.add(Box.createVerticalStrut(1)); // Adjust the vertical spacing as needed
                leftPanel.add(standingButton);
                leftPanel.add(Box.createVerticalStrut(1)); // Adjust the vertical spacing as needed
                leftPanel.add(capstoneButton);
                topRightPanel.add(colorButton);
                frame.revalidate(); 
                frame.repaint();    
                frame.setVisible(true);
                gameModeFrame.dispose();
            }
        });

        JButton hbeButton = new JButton("Easy Mode");
        hbeButton.setFont(new Font("Algerian", Font.BOLD, 24));
        hbeButton.setUI(highlightUI);
        hbeButton.setBackground(new Color(226, 199, 153));
        hbeButton.setForeground(new Color(192, 130, 97)); 
        hbeButton.setFocusPainted(false); 
        hbeButton.setPreferredSize(new Dimension(200, 50)); 
        hbeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setBaseline(true);
                setMCTS(false);
                baseline_Agent = new Baseline_Agent(logicBoard);
                new TakGame2D();
                rightStonesLabel.setText("");
                rightCapstoneLabel.setText("");
                rightPanel.remove(flatButtonR);
                rightPanel.remove(standingButtonR);
                rightPanel.remove(capstoneButtonR);
                topRightPanel.remove(colorButton);
                frame.revalidate(); 
                frame.repaint();    
                frame.setVisible(true);
                gameModeFrame.dispose();
            }
        });

        JButton hbhButton = new JButton("Hard Mode");
        hbhButton.setFont(new Font("Algerian", Font.BOLD, 24));
        hbhButton.setUI(highlightUI);
        hbhButton.setBackground(new Color(226, 199, 153));
        hbhButton.setForeground(new Color(192, 130, 97)); 
        hbhButton.setFocusPainted(false); 
        hbhButton.setPreferredSize(new Dimension(200, 50)); 
        hbhButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMCTS(true);
                mctsAgent = new MCTSAgent();
                new TakGame2D();
                rightStonesLabel.setText("");
                rightCapstoneLabel.setText("");
                rightPanel.remove(flatButtonR);
                rightPanel.remove(standingButtonR);
                rightPanel.remove(capstoneButtonR);
                topRightPanel.remove(colorButton);
                frame.revalidate(); 
                frame.repaint();    
                frame.setVisible(true);
                gameModeFrame.dispose();
            }
        });

        if (instructionsButton.getActionListeners().length == 0) {
            instructionsButton.addActionListener(instructionsActionListener);
        }

        instructionsButton.setFont(new Font("Algerian", Font.BOLD, 24));
        instructionsButton.setUI(highlightUI);
        instructionsButton.setBackground(new Color(226, 199, 153)); 
        instructionsButton.setForeground(new Color(192, 130, 97)); 
        instructionsButton.setFocusPainted(false); 
        instructionsButton.setPreferredSize(new Dimension(200, 50));

        buttonPanel.add(startButton);
        buttonPanel.add(instructionsButton);
        modeButtonPanel.add(hhButton);
        modeButtonPanel.add(hbeButton);
        modeButtonPanel.add(hbhButton);

        gameModeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameModeFrame.setSize(400, 300);
        gameModeFrame.setLocationRelativeTo(null);
        gameModeFrame.setLayout(new BorderLayout()); 
        gameModeFrame.add(modeButtonPanel, BorderLayout.CENTER);
        gameModeFrame.setVisible(false);

        startFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        startFrame.setSize(400, 300);
        startFrame.setLocationRelativeTo(null);
        startFrame.setLayout(new BorderLayout()); 
        startFrame.add(topPanel, BorderLayout.NORTH);
        startFrame.add(buttonPanel, BorderLayout.CENTER);
        startFrame.setVisible(true);
        
    }
    public enum StoneType {
        FLAT,
        STANDING,
        CAPSTONE
    }
    StoneType selectedStoneType;
    ActionListener stoneButtonsListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == flatButton) {
                selectedStoneType = StoneType.FLAT;
                flatButton.setSelected(true); 
                standingButton.setSelected(false); 
                capstoneButton.setSelected(false);
                flatButtonR.setSelected(false); 
                standingButtonR.setSelected(false); 
                capstoneButtonR.setSelected(false);
            } else if (e.getSource() == standingButton) {
                selectedStoneType = StoneType.STANDING;
                flatButton.setSelected(false); 
                standingButton.setSelected(true); 
                capstoneButton.setSelected(false);
                flatButtonR.setSelected(false); 
                standingButtonR.setSelected(false); 
                capstoneButtonR.setSelected(false);
            } else if (e.getSource() == capstoneButton) {
                selectedStoneType = StoneType.CAPSTONE;
                flatButton.setSelected(false); 
                standingButton.setSelected(false); 
                capstoneButton.setSelected(true);
                flatButtonR.setSelected(false); 
                standingButtonR.setSelected(false); 
                capstoneButtonR.setSelected(false);
            }else if (e.getSource() == standingButtonR) {
                selectedStoneType = StoneType.STANDING;
                flatButton.setSelected(false); 
                standingButton.setSelected(false); 
                capstoneButton.setSelected(false);
                flatButtonR.setSelected(false); 
                standingButtonR.setSelected(true); 
                capstoneButtonR.setSelected(false);
            }else if (e.getSource() == flatButtonR) {
                selectedStoneType = StoneType.FLAT;
                flatButton.setSelected(false); 
                standingButton.setSelected(false); 
                capstoneButton.setSelected(false);
                flatButtonR.setSelected(true); 
                standingButtonR.setSelected(false); 
                capstoneButtonR.setSelected(false);
            }else if (e.getSource() == capstoneButtonR) {
                selectedStoneType = StoneType.CAPSTONE;
                flatButton.setSelected(false); 
                standingButton.setSelected(false); 
                capstoneButton.setSelected(false);
                flatButtonR.setSelected(false); 
                standingButtonR.setSelected(false); 
                capstoneButtonR.setSelected(true);
            }
        }
    };
    

    public void capstonesListener() {
        if (logicBoard.getCurrentPlayer().equals("WHITE")) {
            if (!wCapstone) {
                capstoneButton.setVisible(false);
            } else {
                logicBoard.addPiece(2, getCurrentChords()[0], getCurrentChords()[1]);
                boardButtons[getCurrentChords()[0] - 1][getCurrentChords()[1] - 1].setIcon(whiteCapstone);
                wCapstone = false;
                capstone--;
                leftCapstoneLabel.setText(" White Capstone: " + capstone);
                switchTurnLabel();

                logicBoard.checkWinCondition();
                logicBoard.winBoardFull();
                logicBoard.checkState();

                if(logicBoard.getWinner().equals("NONE")){
                    baselineCall();
                    MCTSCall();
                }
                
                updateVisualBoard();
                
                // Check if WHITE player has no capstones left
                if (capstone == 0) {
                    capstoneButton.setVisible(false); // Hide the capstoneButton
                }
            }
        } else {
            if (!bCapstone) {
                capstoneButtonR.setVisible(false);
            } else {
                logicBoard.addPiece(5, getCurrentChords()[0], getCurrentChords()[1]);
                boardButtons[getCurrentChords()[0] - 1][getCurrentChords()[1] - 1].setIcon(brownCapstone);
                bCapstone = false;
                capstone2--;
                rightCapstoneLabel.setText(" Brown Capstone: " + capstone2);
                switchTurnLabel();
                
                // Check if BROWN player has no capstones left
                if (capstone2 == 0) {
                    capstoneButtonR.setVisible(false); // Hide the capstoneButtonR
                } 
            }
        }

            flatButton.setSelected(false); 
            standingButton.setSelected(false); 
            capstoneButton.setSelected(false);
            flatButtonR.setSelected(false); 
            standingButtonR.setSelected(false); 
            capstoneButtonR.setSelected(false);
            logicBoard.checkWinCondition();
            logicBoard.winBoardFull();
            logicBoard.checkState();
            
    };  

    public void flatListener() {
            if(logicBoard.getCurrentPlayer().equals("WHITE")){
                logicBoard.addPiece(0, getCurrentChords()[0], getCurrentChords()[1]);
                boardButtons[getCurrentChords()[0]-1][getCurrentChords()[1]-1].setIcon(whiteFlatStone);
                stones--;
                leftStonesLabel.setText("White Stones: " + stones);
                switchTurnLabel();
                
                logicBoard.checkWinCondition();
                logicBoard.winBoardFull();
                logicBoard.checkState();

                if(logicBoard.getWinner().equals("NONE")){
                    baselineCall();
                    MCTSCall();
                }
                updateVisualBoard();
            
            } else {
                logicBoard.addPiece(3, getCurrentChords()[0], getCurrentChords()[1]);
                boardButtons[getCurrentChords()[0]-1][getCurrentChords()[1]-1].setIcon(brownFlatStone);
                stones2--;
                rightStonesLabel.setText("Brown Stones: " + stones2);
                switchTurnLabel();

            }
            flatButton.setSelected(false); 
            standingButton.setSelected(false); 
            capstoneButton.setSelected(false);
            flatButtonR.setSelected(false); 
            standingButtonR.setSelected(false); 
            capstoneButtonR.setSelected(false);
            logicBoard.checkWinCondition();
            logicBoard.winBoardFull();
            logicBoard.checkState();
            System.out.println(logicBoard.isGameEnded());
          
    };

    public void standingListener() {
        if(logicBoard.getCurrentPlayer().equals("WHITE")){     
            logicBoard.addPiece(1, getCurrentChords()[0], getCurrentChords()[1]);
            boardButtons[getCurrentChords()[0]-1][getCurrentChords()[1]-1].setIcon(whiteStandingStone);
            stones--;
            leftStonesLabel.setText("White Stones: " + stones);
            switchTurnLabel();
            
            logicBoard.checkWinCondition();
            logicBoard.winBoardFull();
            logicBoard.checkState();
            if(logicBoard.getWinner().equals("NONE")){
                baselineCall();
                MCTSCall();
            }
            updateVisualBoard();
            
        } else {
            logicBoard.addPiece(4, getCurrentChords()[0], getCurrentChords()[1]);
            boardButtons[getCurrentChords()[0]-1][getCurrentChords()[1]-1].setIcon(brownStandingStone);
            stones2--;
            rightStonesLabel.setText("Brown Stones: " + stones2);
            switchTurnLabel();
            
        }
        flatButton.setSelected(false); 
        standingButton.setSelected(false); 
        capstoneButton.setSelected(false);
        flatButtonR.setSelected(false); 
        standingButtonR.setSelected(false); 
        capstoneButtonR.setSelected(false);
        logicBoard.checkWinCondition();
        logicBoard.winBoardFull();
        logicBoard.checkState();
        System.out.println(logicBoard.isGameEnded());
   
    };
  
    /**
     * This method shows used to check the Instructions file and show it when button is pressed
     */

    public void showInstructions() {
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new BorderLayout());

        JTextArea instructionsText = new JTextArea(20, 50);
        instructionsText.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionsText.setLineWrap(true);
        instructionsText.setWrapStyleWord(true);
        instructionsText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(instructionsText);
        try {
            File instructionsFile = new File("core\\src\\com\\mygdx\\game\\Instructions.txt");                    
                if (instructionsFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(instructionsFile));
                String line;
                StringBuilder instructions = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    instructions.append(line).append("\n");
                }
                reader.close();
                instructionsText.setText(instructions.toString());
                instructionsText.setCaretPosition(0);
                instructionsPanel.add(scrollPane, BorderLayout.CENTER);
                JOptionPane.showMessageDialog(null, instructionsPanel, "Instructions", JOptionPane.PLAIN_MESSAGE);
            } else {
                instructionsText.setText("No instructions found.");
            }
        } catch (IOException exception) {
            instructionsText.setText("Error opening instructions.");
            exception.printStackTrace();
        }
    }
    ActionListener buttonsListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof boardButton) {

                for(int i=0; i< boardButtons.length; i++){
                    for(int j=0; j<boardButtons.length; j++){
                        if(logicBoard.getBoard()[i][j].isEmpty())
                            boardButtons[i][j].setIsEmpty(true);
                    }
                }

                boardButton source = (boardButton) e.getSource();
                int xChord = source.getXChord();
                int yChord = source.getYChord();
                int[] buttonChords = { xChord, yChord };
                boolean checker = boardButtons[xChord-1][yChord-1].getIsEmpty();
                if(checker||midTurn||(logicBoard.getBoard()[xChord-1][yChord-1].get(logicBoard.getBoard()[xChord-1][yChord-1].size()-1)<=2 && logicBoard.getCurrentPlayer().equals("WHITE"))||
                (logicBoard.getBoard()[xChord-1][yChord-1].get(logicBoard.getBoard()[xChord-1][yChord-1].size()-1)>2 && logicBoard.getCurrentPlayer().equals("BROWN"))){
                    if (getMidTurn()) {
                        setMoveToChords(buttonChords);
                        dropPiece(1);
                        setMidTurn(false);
                        source.setIsEmpty(false);
                    } else {
                        if (source.getIsEmpty()) {
                            setCurrentChords(buttonChords);
                            switch (selectedStoneType) {
                                case FLAT:
                                    flatListener();
                                    break;
                                case STANDING:
                                    standingListener();
                                    break;
                                case CAPSTONE:
                                    capstonesListener();
                                    break;
                                default:
                                    break;
                            }
                            source.setIsEmpty(false);
                        } else {
                            movePiece();
                            setCurrentChords(buttonChords);
                            setMidTurn(true);
                        }
                    }
                    // Perform other relevant actions after placing stones/capstones
                    System.out.println(xChord + " " + yChord);
                    if (!getMidTurn()) {
                        // Handle other cases if needed
                    }
                }
                callForEndScreen();
            }
    }
    };
    

    
    /**
     * Constructor for the class which is the frame displaying the game board, pieces and whose turn it is
     */
    public TakGame2D() {
        ToolTipManager.sharedInstance().setInitialDelay(0);


        colorButton = new JButton("WHITE TURN");
        colorButton.setForeground(new Color(192, 130, 97));
        colorButton.setFont(new Font("Algerian", Font.PLAIN, 14));
        colorButton.setBackground(Color.WHITE);
        instructionsNewButton.setFont(new Font("Algerian", Font.PLAIN, 14));
        instructionsNewButton.setBackground(new Color(226, 199, 153)); 
        instructionsNewButton.setForeground(new Color(192, 130, 97)); 
        instructionsNewButton.setFocusPainted(false); 
        if (instructionsNewButton.getActionListeners().length == 0) {
            instructionsNewButton.addActionListener(instructionsActionListener);
        }
        menuOption = new JButton("Menu");
        menuOption.setFont(new Font("Algerian", Font.PLAIN, 14));
        menuOption.setBackground(new Color(226, 199, 153)); 
        menuOption.setForeground(new Color(192, 130, 97)); 
        menuOption.setFocusPainted(false);
        menuOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuOption();
            }
        });

        topRightPanel.add(menuOption);
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
        frame.setIconImage(icon.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 450);
        frame.setLayout(new BorderLayout());
        frame.add(topRightPanel, BorderLayout.NORTH);

        leftPanel = new JPanel();
        rightPanel = new JPanel();
        boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(leftStonesLabel);
        leftPanel.add(leftCapstoneLabel);
        leftPanel.add(Box.createVerticalGlue());
        
        flatButton.setBackground(new Color(242, 236, 190));
        flatButton.setFont(radioButtonFont);
        flatButton.setForeground(new Color(192, 130, 97));
        standingButton.setBackground(new Color(242, 236, 190));
        standingButton.setFont(radioButtonFont);
        standingButton.setForeground(new Color(192, 130, 97));
        capstoneButton.setBackground(new Color(242, 236, 190));
        capstoneButton.setFont(radioButtonFont);
        capstoneButton.setForeground(new Color(192, 130, 97));

        flatButtonR.setBackground(new Color(242, 236, 190));
        flatButtonR.setFont(radioButtonFont);
        flatButtonR.setForeground(new Color(192, 130, 97));
        standingButtonR.setBackground(new Color(242, 236, 190));
        standingButtonR.setFont(radioButtonFont);
        standingButtonR.setForeground(new Color(192, 130, 97));
        capstoneButtonR.setBackground(new Color(242, 236, 190));
        capstoneButtonR.setFont(radioButtonFont);
        capstoneButtonR.setForeground(new Color(192, 130, 97));

        if (flatButton.getActionListeners().length == 0) {
            flatButton.addActionListener(stoneButtonsListener);
        }
        if (flatButtonR.getActionListeners().length == 0) {
            flatButtonR.addActionListener(stoneButtonsListener);
        }
        if (standingButton.getActionListeners().length == 0) {
            standingButton.addActionListener(stoneButtonsListener);
        }
        if (standingButtonR.getActionListeners().length == 0) {
            standingButtonR.addActionListener(stoneButtonsListener);
        }
        if (capstoneButton.getActionListeners().length == 0) {
            capstoneButton.addActionListener(stoneButtonsListener);
        }
        if (capstoneButtonR.getActionListeners().length == 0) {
            capstoneButtonR.addActionListener(stoneButtonsListener);
        }
        
        leftPanel.add(flatButton);
        leftPanel.add(Box.createVerticalStrut(1)); // Adjust the vertical spacing as needed
        leftPanel.add(standingButton);
        leftPanel.add(Box.createVerticalStrut(1)); // Adjust the vertical spacing as needed
        leftPanel.add(capstoneButton);
    
        colorButton.setBackground(Color.WHITE);

        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(rightStonesLabel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(rightCapstoneLabel);
        rightPanel.add(Box.createVerticalGlue());

        rightPanel.add(flatButtonR);
        rightPanel.add(Box.createVerticalStrut(1)); // Adjust the vertical spacing as needed
        rightPanel.add(standingButtonR);
        rightPanel.add(Box.createVerticalStrut(1)); // Adjust the vertical spacing as needed
        rightPanel.add(capstoneButtonR);


        boardButtons = new boardButton[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {

                int[] chords = {i+1,j+1};
                boardButtons[i][j] = new boardButton(chords,true);
                boardButtons[i][j].setOpaque(true); 
                boardButtons[i][j].addActionListener(buttonsListener);

                boardPanel.add(boardButtons[i][j]);
                if ((i + j) % 2 == 0) {
                    boardButtons[i][j].setBackground(new Color(226, 199, 153));
                } else {
                    boardButtons[i][j].setBackground(new Color(192, 130, 97));
                }
            }
        }
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.EAST);
        frame.add(boardPanel, BorderLayout.CENTER);

        leftPanel.setPreferredSize(new Dimension(150, 400));
        rightPanel.setPreferredSize(new Dimension(150, 400));
        leftPanel.setBackground(new Color(242, 236, 190));
        rightPanel.setBackground(new Color(242, 236, 190));

        frame.setLocationRelativeTo(null);
        frame.setVisible(false);
    }

    public void switchTurnLabel(){
        if(!logicBoard.getCurrentPlayer().equals("WHITE")){
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

    /**
     * Checks the winning condition and shows pop-up end screen once the game is over.
     */
    public void callForEndScreen() {
        logicBoard.checkWinCondition();
        String winner = logicBoard.getWinner();
        if(!winner.equals("NONE")){
            endScreen(winner);
        }
        
        /* if(logicBoard.isGameEnded() && logicBoard.getCurrentPlayer() == "WHITE") {
            endScreen("WHITE");
        }
        else if(logicBoard.isGameEnded() && logicBoard.getCurrentPlayer() == "BROWN") {
            endScreen("BROWN");
        }*/
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
    //public void addPiece() {      
    //}

    /**
     * Displays popup window when moving a piece on the board
     */
    public void movePiece() {
        Color backgroundColor = new Color(226, 199, 153);
        Color textColor = new Color(192, 130, 97);
        Font labelFont = new Font("Algerian", Font.BOLD, 18);
        Font buttonFont = new Font("Algerian", Font.PLAIN, 14);
    
        JLabel moveLabel = new JLabel("How many to move?");
        moveLabel.setFont(labelFont);
        moveLabel.setForeground(textColor);
    
        final JTextField textField = new JTextField();
        textField.setFont(buttonFont);
        textField.setBackground(backgroundColor);
        textField.setForeground(textColor);
    
        final JButton button = new JButton("Confirm");
        button.setFont(buttonFont);
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
   
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String txt = textField.getText();
                int quantity = Integer.parseInt(txt);
                setPieceQuantity(quantity);
                moveFrame.dispose();
            }
        });
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    button.doClick(); // Simulate a button click
                }
            }
        });
    
        JPanel topMovePanel = new JPanel();
        topMovePanel.setLayout(new GridLayout(3, 1));
        topMovePanel.setBackground(backgroundColor);
    
        topMovePanel.add(moveLabel);
        topMovePanel.add(textField);
        topMovePanel.add(button);

        moveFrame = new JFrame("Move Piece");
        moveFrame.setIconImage(icon.getImage());
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

        Color backgroundColor = new Color(226, 199, 153);
        Color textColor = new Color(192, 130, 97);
        Font labelFont = new Font("Algerian", Font.BOLD, 18);
        Font buttonFont = new Font("Algerian", Font.PLAIN, 14);
    
        JLabel moveLabel = new JLabel("How many to drop?");
        moveLabel.setFont(labelFont);
        moveLabel.setForeground(textColor);
    
        final JTextField textField = new JTextField();
        textField.setFont(buttonFont);
        textField.setBackground(backgroundColor);
        textField.setForeground(textColor);
    
        final JButton button = new JButton("Confirm");
        button.setFont(buttonFont);
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
    
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String txt = textField.getText();
                int drop = Integer.parseInt(txt);
                setDropNum(drop);
                int dir = targetDir(getCurrentChords(), getMoveToChords());
    
                if (getPieceQuantity() - getDropNum() <= 0) {
                    setDropNum(getPieceQuantity());
                    setMidTurn(false);
                }
    
                if(logicBoard.getHeldPieces().isEmpty()){
                    logicBoard.move(currentChords[0], currentChords[1], getPieceQuantity(), dir, getDropNum());
                    updateVisualBoard();
                    logicBoard.checkState();
                    moveFrame.dispose();
                }
                else{
                    logicBoard.move(currentChords[0], currentChords[1], logicBoard.getHeldPieces(), getDir(), getDropNum());
                    updateVisualBoard();
                    logicBoard.checkState();
                    moveFrame.dispose();
                }

                if(!logicBoard.getHeldPieces().isEmpty()){
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
                else{
                    switchTurnLabel();
                    logicBoard.checkWinCondition();
                    logicBoard.winBoardFull();
                    logicBoard.checkState();

                    if(logicBoard.getWinner().equals("NONE")){
                        baselineCall();
                        MCTSCall();
                    }
                    
                    updateVisualBoard();
                }
                callForEndScreen();
            }
            
        });
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    button.doClick(); // Simulate a button click
                }
            }
        });
        JPanel topMovePanel = new JPanel();
        topMovePanel.setLayout(new GridLayout(3, 1));
        topMovePanel.setBackground(backgroundColor);
        topMovePanel.add(moveLabel);
        topMovePanel.add(textField);
        topMovePanel.add(button);
    
        moveFrame = new JFrame("Drop Piece");
        moveFrame.setIconImage(icon.getImage());
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

        setMCTS(false);
        setBaseline(false);

        //logicBoard = new board();
        for(int i=0; i< boardButtons.length; i++){
            for(int j=0; j<boardButtons.length; j++){
                if(logicBoard.getBoard()[i][j].isEmpty())
                    boardButtons[i][j].setIsEmpty(true);
            }
        }
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
        logicBoard = new board();
        capstoneButton.setVisible(true);
        capstoneButtonR.setVisible(true);
        flatButton.setSelected(false); 
        standingButton.setSelected(false); 
        capstoneButton.setSelected(false);
        flatButtonR.setSelected(false); 
        standingButtonR.setSelected(false); 
        capstoneButtonR.setSelected(false);
        
        leftStonesLabel.setText("White Stones: " + stones);
        leftCapstoneLabel.setText("White Capstone: " + capstone);
        rightStonesLabel.setText("Brown Stones: " + stones2);
        rightCapstoneLabel.setText("Brown Capstone: " + capstone2);

    }

    /**
     * Displays the endscreen when a winning condition is met
     * @param winner the player who just performed a move (ie. the current player whose turn it is)
     */
    public void endScreen(String winner) {
        final JFrame endFrame = new JFrame("Game Over");
    
        JLabel winLabel = new JLabel("Congratulations!");
        Font titleFont = new Font("Algerian", Font.BOLD, 35);
        winLabel.setFont(titleFont);
    
        JLabel winMessageLabel = new JLabel(winner);
    
        /*if (currentPlayer.equals("WHITE")) {
            winMessageLabel = new JLabel("White wins!");
        } else if (currentPlayer.equals("BROWN")) {
            winMessageLabel = new JLabel("Brown wins!");
        } else {
            winMessageLabel = new JLabel("It's a draw!");
        }*/
    
        Font messageFont = new Font("Algerian", Font.BOLD, 24);
        winMessageLabel.setFont(messageFont);
    
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        messagePanel.setBackground(new Color(192, 130, 97));
        messagePanel.add(winLabel);
        messagePanel.add(winMessageLabel);
        winLabel.setForeground(new Color(226, 199, 153));
        winMessageLabel.setForeground(new Color(226, 199, 153));
    
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(226, 199, 153));

        JButton startOverButton = new JButton("Start Over");
        startOverButton.setFont(new Font("Algerian", Font.BOLD, 24));
        startOverButton.setUI(highlightUI);
        startOverButton.setBackground(new Color(226, 199, 153)); 
        startOverButton.setForeground(new Color(192, 130, 97)); 
        startOverButton.setFocusPainted(false); 
        startOverButton.setPreferredSize(new Dimension(200, 50)); 
    
        startOverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame(); 
                startingWindow();
                endFrame.dispose();
                moveCounter = 0;

            }
        });

        buttonPanel.add(startOverButton);

        endFrame.setIconImage(icon.getImage());
        endFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        endFrame.setSize(400, 300);
        endFrame.setLocationRelativeTo(null);
        endFrame.setLayout(new GridLayout(2, 1));
        endFrame.add(messagePanel);
        endFrame.add(buttonPanel);
        endFrame.setVisible(true);
    }

    public void menuOption() {
        final JFrame menuFrame = new JFrame("Tak Menu");
        menuFrame.setIconImage(icon.getImage());

        JLabel startLabel = new JLabel("Tak");
        Font titleFont = new Font("Algerian", Font.BOLD, 60);
        startLabel.setFont(titleFont);
        startLabel.setForeground(new Color(226, 199, 153));
    
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20)); 
        topPanel.setBackground(new Color(192, 130, 97));
        topPanel.add(startLabel);
    
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 0, 0)); 
        buttonPanel.setBackground(new Color(226, 199, 153)); 

        JButton restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Algerian", Font.BOLD, 24));
        restartButton.setUI(highlightUI);
        restartButton.setBackground(new Color(226, 199, 153)); 
        restartButton.setForeground(new Color(192, 130, 97)); 
        restartButton.setFocusPainted(false); 
        restartButton.setPreferredSize(new Dimension(200, 50)); 
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
                startingWindow();
                menuFrame.dispose();
            }
        });
        JButton continueButton = new JButton("Continue");
        continueButton.setFont(new Font("Algerian", Font.BOLD, 24));
        continueButton.setUI(highlightUI);
        continueButton.setBackground(new Color(226, 199, 153)); 
        continueButton.setForeground(new Color(192, 130, 97)); 
        continueButton.setFocusPainted(false); 
        continueButton.setPreferredSize(new Dimension(200, 50));
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Algerian", Font.BOLD, 24));
        exitButton.setUI(highlightUI);
        exitButton.setBackground(new Color(226, 199, 153)); 
        exitButton.setForeground(new Color(192, 130, 97)); 
        exitButton.setFocusPainted(false); 
        exitButton.setPreferredSize(new Dimension(200, 50)); 
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();
                System.exit(0);
            }
        });
    
        buttonPanel.add(restartButton);
        buttonPanel.add(continueButton);
        buttonPanel.add(exitButton);

        menuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        menuFrame.setSize(400, 300);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setLayout(new BorderLayout()); 
        menuFrame.add(topPanel, BorderLayout.NORTH);
        menuFrame.add(buttonPanel, BorderLayout.CENTER);
        menuFrame.setVisible(true);
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

    public void baselineCall(){
        if(baseline){
            baseline_Agent.chooseMove(logicBoard, "BROWN");
            moveCounter++;
            System.out.println("Move count: " + moveCounter);
            if(logicBoard.getCurrentPlayer().equals("BROWN")){
                baselineCall();
            }
        }
    }

    public void MCTSCall(){
        if(mcts){
            board NewState = mctsAgent.findNextMove(logicBoard).getGameState();
            setLogicBoard(NewState);
            // add a line to assign the MCTS resulting board to the actual playing boardd
            if(logicBoard.getCurrentPlayer().equals("BROWN")){
                logicBoard.togglePlayer();
            }
        }
    }

    public static void main(String[] args) {
        TakGame2D game = new TakGame2D();
        game.startingWindow();
       
    }
}
