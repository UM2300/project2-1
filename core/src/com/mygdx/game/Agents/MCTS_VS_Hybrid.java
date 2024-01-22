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
    private int MCTSwinCount = 0;
    private int HybridWinCount = 0;

    public MCTS_VS_Hybrid() {
        this.logicBoard = new board();
        this.mctsAgent = new MCTSAgent();
        this.isWhiteTurn = true; // Start with White
    }

    /**
     * Checks if any stack on the board has two or more pieces
     * @return true if any stack on the board has two or more pieces, otherwise false.
     */
    public boolean checkStack(){

        boolean result=false;
        for (int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                if(!this.logicBoard.getBoard()[i][j].isEmpty() && this.logicBoard.getBoard()[i][j].size()>=2){
                    result = true;
                }
            }
        }

        return result;
    }

    /**
     * Resets the game state file to its initial state
     */
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

    /**
     * Simulates a game of the Hybrid agent vs MCTS agent
     */
    public void playGame() {
        int moveCounter1 = 0;
        int moveCounter2 = 0;
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
                moveCounter1++;
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
                moveCounter2++;
            }
            //logicBoard.checkMoveState();
            
            
            // Check win conditions
            logicBoard.checkWinCondition();

            if (logicBoard.isGameEnded()) {
                System.out.println("Game Ended: " + logicBoard.getWinner());
                if (logicBoard.getWinner().equals("WHITE won!")) {
                    MCTSwinCount++;
                    System.out.println("MCTS win count: " + MCTSwinCount);
                    System.out.println("MCTS move count: " + moveCounter1);
                }
                else {
                    HybridWinCount++;
                    System.out.println("Hybrid win count: " + HybridWinCount);
                    System.out.println("Hybrid move count: " + moveCounter2);
                }
                break;
            }

            // Toggle turn
            isWhiteTurn = !isWhiteTurn;
            logicBoard.setCurrentPlayer(isWhiteTurn ? "WHITE" : "BROWN");
            logicBoard.checkFinalState();
        }
        //logicBoard.checkFinalState();
    }

    /**
     * Simulates 100 games of Hybrid agent vs MCTS agent for experimental purposes
     */
    public void experiment() {
        int gameCounter = 0;
        while (gameCounter < 10) {
            System.out.println("Game " + gameCounter + " start");
            playGame();
            System.out.println("Game " + gameCounter + " end");
            gameCounter++;
        }
    }
    

    public static void main(String[] args) {
        MCTS_VS_Hybrid game = new MCTS_VS_Hybrid();
        game.playGame();
       // game.experiment();
    }
}
