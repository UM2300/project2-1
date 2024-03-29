package com.mygdx.game.Agents;

import com.mygdx.game.GameLogic.board;

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

    /**
     * Simulates a game of Baseline agent vs MCTS agent
     */
    public void playGame() {
        turn = true; // Assuming Baseline (WHITE) starts first
        logicBoard = new board();
        counter = 0;

        while (!logicBoard.isGameEnded()) {

            if (turn) {
                //Perform Baseline Move
                baselineAgent.chooseMove(logicBoard, "WHITE");
            } else {
                // Perform MCTS move
                MCTSNode mctsMove = mctsAgent.findNextMove(logicBoard);
                logicBoard = mctsMove.getGameState();
            }
            logicBoard.checkFinalState();
            counter++;
            turn = !turn; // Toggle the turn

            logicBoard.checkWinCondition();

            if (logicBoard.isGameEnded()) {
                System.out.println("Game Ended: " + logicBoard.getWinner());
                if (logicBoard.getWinner().equals("BROWN won!")) {
                    MCTSwinCount++;
                    System.out.println("MCTS win count: " + MCTSwinCount + " out of 100");
                    System.out.println("Baseline win count: " + BaselineWinCount + " out of 100");
                } else {
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

    /**
     * Simulates 100 games of baseline vs MCTS for experimental purposes
     */
    public void experiment() {

        while (gameCounter < 100) {
            playGame();
            gameCounter++;
        }
    }

    public static void main(String[] args) {
        Baseline_VS_MCTS gameController = new Baseline_VS_MCTS();
        gameController.playGame();
        //gameController.experiment();

    }

}