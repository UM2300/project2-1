package com.mygdx.game.Agents;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import com.mygdx.game.GameLogic.board;
import com.mygdx.game.GameLogic.game;

public class Baseline_VS_MCTS {
    private board logicBoard;
    private Baseline_Agent baselineAgent;
    private MCTSAgent mctsAgent;
    private boolean turn;
    private int counter;
    private int gameCounter = 0;
    private int MCTSwinCount = 0;
    private int BaselineWinCount = 0;

    public Baseline_VS_MCTS() {
        this.logicBoard = new board();
        this.baselineAgent = new Baseline_Agent(logicBoard);
        this.mctsAgent = new MCTSAgent();

    }

    public void playGame() {
        turn = true; // Assuming Baseline (WHITE) starts first
        logicBoard = new board();
        counter = 0;
    
        while (!logicBoard.isGameEnded()) {
            //System.out.println("Current Player: " + logicBoard.getCurrentPlayer());
    
            if (turn) {
                //Perform Baseline Move
                baselineAgent.chooseMove(logicBoard, "WHITE");
                //System.out.println("Baseline Agent (WHITE) made a move.");
            } else {
                // Perform MCTS move
                MCTSNode mctsMove = mctsAgent.findNextMove(logicBoard);
                logicBoard = mctsMove.getGameState(); 
                //System.out.println("MCTS Agent (BROWN) made a move.");
            }

            counter++;
            turn = !turn; // Toggle the turn

            logicBoard.checkWinCondition(); // Check if the game has ended
    
            if (logicBoard.isGameEnded()) {
                System.out.println("Game Ended: " + logicBoard.getWinner());
                if (logicBoard.getWinner().equals("BROWN won!")) {
                    MCTSwinCount++;
                    System.out.println("MCTS win count: " + MCTSwinCount + " out of 100");
                    System.out.println("Baseline win count: " + BaselineWinCount + " out of 100");
                }
                else {
                    BaselineWinCount++;
                    System.out.println("MCTS win count: " + MCTSwinCount + " out of 100");
                    System.out.println("Baseline win count: " + BaselineWinCount + " out of 100");
                }
                System.out.println("Number of Plays by both agents is: " + counter);
                break; // Exit the loop if the game has ended
            }
        }
        logicBoard.checkFinalState();
    }

    public void experiment() {

        while (gameCounter < 100) {
            playGame();
            gameCounter++;
        }
        // System.out.println("MCTS FINAL win count: " + MCTSwinCount + " out of 100");
        // System.out.println("Baseline FINAL win count: " + BaselineWinCount + " out of 100");
        
    }
    
    public static void main(String[] args) {
        Baseline_VS_MCTS gameController = new Baseline_VS_MCTS();
        gameController.playGame();
        //gameController.experiment();
        
    }

}