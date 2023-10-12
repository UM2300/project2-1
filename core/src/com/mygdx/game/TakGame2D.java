package com.mygdx.game;

import javax.swing.*;
import java.awt.*;

public class TakGame2D {

    private JFrame frame;
    private JFrame optionFrame;
    private JLabel[][] boardLabels;
    private JLabel optionLabel;
    private JPanel leftPanel, rightPanel, boardPanel;
    private final int BOARD_SIZE = 5;

    public TakGame2D() {
        frame = new JFrame("TakGame2D");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Initialize the left, right, and board panels
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));

        boardLabels = new JLabel[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                boardLabels[i][j] = new JLabel();
                boardLabels[i][j].setOpaque(true); // This is to make sure the background color is visible
                boardPanel.add(boardLabels[i][j]);
                if ((i + j) % 2 == 0) {
                    boardLabels[i][j].setBackground(Color.WHITE);
                } else {
                    boardLabels[i][j].setBackground(Color.BLACK);
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

        JRadioButton standingButton = new JRadioButton("add Flat Piece");
        JRadioButton capstoneButton = new JRadioButton("add Capstone");

        JPanel topOptionPanel = new JPanel();
        topOptionPanel.setBounds(0, 0, 300, 100);
        JPanel bottomOptionPanel = new JPanel();
        topOptionPanel.setBounds(0, 0, 300, 100);
        
        topOptionPanel.add(optionLabel);
        bottomOptionPanel.add(standingButton);
        bottomOptionPanel.add(capstoneButton);



        optionFrame = new JFrame("Choose Piece");
        optionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
