package com.mygdx.game.Agents;

import com.mygdx.game.GameLogic.board;

public class MCTS_VS_Hybrid {
    private board logicBoard;
    private MCTSAgent mctsAgentWhite;
    private MCTSAgent mctsAgentBrown;
    private boolean turn;
    private int counter;
    private int gameCounter = 0;
    private int whiteWinCount = 0;
    private int brownWinCount = 0;

    public MCTS_VS_Hybrid() {
        this.logicBoard = new board();
        this.mctsAgentWhite = new MCTSAgent();
        this.mctsAgentBrown = new MCTSAgent();
    }

    public void playGame() {
        turn = true; // Assuming WHITE starts first
        logicBoard = new board();
        counter = 0;

        while (!logicBoard.isGameEnded()) {
            if (turn) {
                // Perform MCTS move for White
                MCTSNode mctsMoveWhite = mctsAgentWhite.findNextMove(logicBoard);
                logicBoard = mctsMoveWhite.getGameState();
      System.out.println(" (WHITE) made a move.");

            } else {
                // Perform MCTS move for Brown
                MCTSNode mctsMoveBrown = mctsAgentBrown.findNextMove(logicBoard);
                logicBoard = mctsMoveBrown.getGameState();
                System.out.println("(BROWN) made a move.");
            }

            logicBoard.checkFinalState();

            counter++;
            turn = !turn; // Toggle the turn

            logicBoard.checkWinCondition(); // Check if the game has ended

            if (logicBoard.isGameEnded()) {
                System.out.println("Game Ended: " + logicBoard.getWinner());
                if (logicBoard.getWinner().equals("BROWN won!")) {
                    brownWinCount++;
                    System.out.println("Brown win count: " + brownWinCount + " out of 100");
                    System.out.println("White win count: " + whiteWinCount + " out of 100");
                }
                else {
                    whiteWinCount++;
                    System.out.println("Brown win count: " + brownWinCount + " out of 100");
                    System.out.println("White win count: " + whiteWinCount + " out of 100");
                }
                System.out.println("Number of Plays by both agents is: " + counter);
                break; // Exit the loop if the game has ended
            }
        }
        logicBoard.checkFinalState();
    }

    public void experiment() {

        while (gameCounter < 1) {
            playGame();
            gameCounter++;
        }
        // System.out.println("White FINAL win count: " + whiteWinCount + " out of 100");
        // System.out.println("Brown FINAL win count: " + brownWinCount + " out of 100");

    }

    public static void main(String[] args) {
        MCTS_VS_Hybrid gameController = new MCTS_VS_Hybrid();
        //gameController.playGame();
        gameController.experiment();
    }
}

