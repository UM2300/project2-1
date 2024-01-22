package com.mygdx.game.Agents;

import com.badlogic.gdx.Files;
import com.mygdx.game.GameLogic.board;
import java.nio.file.StandardCopyOption;

import java.nio.file.CopyOption;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


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

    public static void resetFile(){

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("core\\src\\com\\mygdx\\game\\Agents\\GameState.txt"))) {
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {
                    writer.write("-1");
                    if (col < 4) {
                        writer.write(" "); 
                    }
                }
                writer.newLine(); 
            }
        }catch (Exception e) {
            System.out.println("write error");
        }
    }

    public void playGame() {

        callPython pyCall = new callPython();
        resetFile();

        boolean wasMLTurn = false;
        boolean MCTSPlays = false;

        while (!logicBoard.isGameEnded()) {
            // Print statement to indicate which player is making the move
            System.out.println(isWhiteTurn ? "White's turn" : "Brown's turn");

            if(checkStack()){
                MCTSPlays=true;
            }

            if(!isWhiteTurn){

                if(MCTSPlays){
                    MCTSNode nextMove = mctsAgent.findNextMove(logicBoard);
                    logicBoard = nextMove.getGameState();
                    logicBoard.checkMoveState();  //this step writes the move made by the java agent into the file
                }
                else{
                    pyCall.callPythonPredict(); //board reading into python happens automatically in this step
                                                //writing to file after the move is done as well
                    wasMLTurn = true;
                }
            }
            else{
                if(wasMLTurn){   // this part is to get the new game state from text file if the prev move was made by DQN
                    MCTSNode temp = new MCTSNode(logicBoard);
                    temp.readGameStateFromFile();
                    logicBoard = temp.getGameState();
                    wasMLTurn = false;
                }
                MCTSNode nextMove = mctsAgent.findNextMove(logicBoard);
                logicBoard = nextMove.getGameState();
                logicBoard.checkMoveState();
            }
            //logicBoard.checkMoveState();

            
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
