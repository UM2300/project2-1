package com.mygdx.game.Agents;

import com.mygdx.game.GameLogic.board;

public class MCTS_VS_Hybrid {
    private board logicBoard;
    private MCTSAgent mctsAgent;
    private boolean isWhiteTurn;

    public MCTS_VS_Hybrid() {
        this.logicBoard = new board();
        this.mctsAgent = new MCTSAgent();
        this.isWhiteTurn = true; // Start with White
    }

    public void playGame() {
        while (!logicBoard.isGameEnded()) {
            // Print statement to indicate which player is making the move
            System.out.println(isWhiteTurn ? "White's turn" : "Brown's turn");

            MCTSNode nextMove = mctsAgent.findNextMove(logicBoard);
            logicBoard = nextMove.getGameState();

            // Check win conditions
            logicBoard.checkWinCondition();

            if (logicBoard.isGameEnded()) {
                System.out.println("Game Ended: " + logicBoard.getWinner());
                break;
            }

            // Toggle turn
            isWhiteTurn = !isWhiteTurn;
            logicBoard.setCurrentPlayer(isWhiteTurn ? "WHITE" : "BROWN");
        }
        logicBoard.checkFinalState();
    }

    public static void main(String[] args) {
        MCTS_VS_Hybrid game = new MCTS_VS_Hybrid();
        game.playGame();
    }
}
