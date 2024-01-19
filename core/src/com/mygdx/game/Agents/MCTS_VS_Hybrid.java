package com.mygdx.game.Agents;

import com.mygdx.game.GameLogic.board;

import com.mygdx.game.Agents.callPython;

public class MCTS_VS_Hybrid {
    private board logicBoard;
    private MCTSAgent mctsAgent;
    private boolean isWhiteTurn;

    public MCTS_VS_Hybrid() {
        this.logicBoard = new board();
        this.mctsAgent = new MCTSAgent();
        this.isWhiteTurn = true; // Start with White
    }

    public boolean checkStack(){

        boolean result=false;
        for (int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                if(!this.logicBoard.getBoard()[i][j].isEmpty() && this.logicBoard.getBoard()[i][j].size()>2){
                    result = true;
                }
            }
        }

        return result;
    }

    public void playGame() {

        callPython pyCall = new callPython();

        while (!logicBoard.isGameEnded()) {
            // Print statement to indicate which player is making the move
            System.out.println(isWhiteTurn ? "White's turn" : "Brown's turn");


            if(isWhiteTurn==false){

                if(checkStack()){
                    MCTSNode nextMove = mctsAgent.findNextMove(logicBoard);
                    logicBoard = nextMove.getGameState();
                }
                else{
                    pyCall.callPythonMove();
                    MCTSNode nextMove = new MCTSNode(logicBoard);
                    nextMove.readGameStateFromFile();
                    logicBoard=nextMove.getGameState();
                }

            }
            else{
                MCTSNode nextMove = mctsAgent.findNextMove(logicBoard);
                logicBoard = nextMove.getGameState();
            }

            

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
