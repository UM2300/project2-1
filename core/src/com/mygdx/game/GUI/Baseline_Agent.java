package com.mygdx.game.GUI;
import java.util.ArrayList;
import java.util.List;

import com.mygdx.game.GameLogic.board;
import com.mygdx.game.GameLogic.game;


public class Baseline_Agent {

    private board logicBoard;

    public Baseline_Agent(board logicBoard){
        this.logicBoard=logicBoard;
    }


    public boolean isAStack (int x, int y) {
        if (logicBoard.getBoard()[x][y].size() > 1 ) {
            return true;
        }
        return false;
    }

    public ArrayList<int []> getAvailableTiles (board logicBoard, String currentPlayer) {

        ArrayList<int []> availableTiles = new ArrayList<>();   //ArrayList of arrays 
        
        for (int i = 0; i < logicBoard.getBoard().length; i++) {
            for (int j = 0; j <  logicBoard.getBoard().length; j++) {

                int tileValue = logicBoard.getBoard()[i][j].get(logicBoard.getBoard()[i][j].size()-1);  //top piece of the stack

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
    
}
