package com.mygdx.game.GameLogic;
import java.util.ArrayList;

/**
 * This class represents the implementation of the baseline agent
 */

public class Baseline_Agent {

    private board logicBoard;

    private int pieceMultiplier=3;

    public int getPieceMultiplier(){
        return this.pieceMultiplier;
    }

    public void setPieceMultiplier(int pieceMultiplier){
        this.pieceMultiplier=pieceMultiplier;
    }

    public Baseline_Agent(board logicBoard){
        this.logicBoard=logicBoard;
    }

    public Baseline_Agent(board logicBoard, int pieceMultiplier){
        this.logicBoard=logicBoard;
        this.pieceMultiplier=pieceMultiplier;
    }

    /**
     * Checks if a tile on the board contains a stack
     * @param x x coordinate on the board
     * @param y y coordinate on the board
     * @return true if the tile has a stack, false otherwise
     */
    public boolean isAStack (int x, int y) {
        if (logicBoard.getBoard()[x][y].size() > 1 ) {
            return true;
        }
        return false;
    }

    
    /**
     * Returns a list of tiles on the game board that are available to the current player, ie. tiles that are
     * empty or tiles that have stacks which the current player controls
     * @param logicBoard the current state of the board
     * @param currentPlayer the current player (brown or white)
     * @return a list of tiles available for the current player to make a move
     */
    /**  Complexity: O(1) 
    Even though there are two nested loops, the number of iterations they perform is constant 
    and does not depend on any variable input size. They will always perform 25 iterations in total
    */
    public ArrayList<int []> getAvailableTiles (board logicBoard, String currentPlayer) {

        ArrayList<int []> availableTiles = new ArrayList<>();   //ArrayList of arrays 
        
        for (int i = 0; i < logicBoard.getBoard().length; i++) {
            for (int j = 0; j <  logicBoard.getBoard().length; j++) {

                int tileValue=-1;
                if(!logicBoard.getBoard()[i][j].isEmpty()){
                    tileValue = logicBoard.getBoard()[i][j].get(logicBoard.getBoard()[i][j].size()-1);  //top piece of the stack
                }

                // if the current player is white and the piece at the tile is 0, 1, 2 or it is empty,
                // create a copy of the arraylist and add it to availableTiles
                if (currentPlayer.equals("WHITE")){

                    if((tileValue >= 0 && tileValue <= 2) || logicBoard.getBoard()[i][j].isEmpty()) {
                        int [] coordinates = {i, j};   
                        availableTiles.add(coordinates);
                    }
                }

                // if the current player is brown and the piece at the tile is 3, 4, 5 or it is empty,
                // create a copy of the arraylist and add it to availableTiles
                if (currentPlayer.equals("BROWN")) {

                    if((tileValue >= 3 && tileValue <= 5) || logicBoard.getBoard()[i][j].isEmpty()) {
                        int[] coordinates = {i, j};    
                        availableTiles.add(coordinates);
                    }
                }
                
            }
            
        }

        return availableTiles;
    }

    /**
     * Randomly chooses a move using the Math.random function
     * @param logicBoard current state of the board
     * @param currentPlayer the current player making the move
     */
    // Complexity: O(1) 
    public void chooseMove(board logicBoard, String currentPlayer){
        ArrayList<int[]> chords = new ArrayList<int[]>();
        chords = getAvailableTiles(logicBoard, currentPlayer);

        int[] moveChords = chords.get((int)(Math.random()*chords.size()));

        if(logicBoard.getBoard()[moveChords[0]][moveChords[1]].isEmpty()){

            int pieceAdded = (int)(Math.random()*getPieceMultiplier());

            if(pieceAdded==2){
                setPieceMultiplier(2);
            }

            if(currentPlayer.equals("BROWN")){
                pieceAdded=pieceAdded+3;
            }
            //System.out.println("Adding at machine chords "+(moveChords[0])+" "+(moveChords[1]));
            logicBoard.addPiece(pieceAdded, moveChords[0]+1, moveChords[1]+1);
        }
        else{
            int moveDir = (int)(Math.random()*3);
            int quant = (int)(Math.random()*logicBoard.getBoard()[moveChords[0]][moveChords[1]].size()-1)+1;

            int dropNum = (int)(Math.random()*(quant-1)+1);

            //System.out.println("Moving at machine chords "+(moveChords[0])+" "+(moveChords[1]));
            logicBoard.move(moveChords[0]+1, moveChords[1]+1, quant, moveDir, dropNum);

            if(!logicBoard.getHeldPieces().isEmpty()){
                dropRecursion(moveChords[0],moveChords[1],moveDir);
            }

        }

        logicBoard.checkState();

    }

    /**
     * Handles the process of dropping off pieces while moving a stack  
     * @param x x coordinate on the board
     * @param y y coordinate on the board
     * @param dir direction the stack is moving
     */
    // Complexity: O(1) As the size of getHeldPieces is always reasonably small < 5
    public void dropRecursion(int x, int y,int dir){

        int newDropNum = (int)Math.random()*(logicBoard.getHeldPieces().size()-1)+1;

        logicBoard.move(x, y, logicBoard.getHeldPieces(), dir, newDropNum);

        if(!logicBoard.getHeldPieces().isEmpty()){
            dropRecursion(x, y, dir);
        }

    }
    
}
