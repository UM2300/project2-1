package com.mygdx.game.GUI;
import java.util.ArrayList;

import com.mygdx.game.GameLogic.board;
import com.mygdx.game.GameLogic.game;


public class Baseline_Agent {

    public board board;
    public game game;


    public boolean isAStack (int x, int y) {
        if (board.board[x][y].size() > 1 ) {
            return true;
        }
        return false;
    }

    public ArrayList<ArrayList<Integer>> getAvailableTiles (ArrayList<Integer> [][] board, String currentPlayer) {

        ArrayList<ArrayList<Integer>> availableTiles = new ArrayList<>();   //ArrayList of ArrayLists (for easy access later)
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {

                int tileValue = board[i][j].get(board[i][j].size()-1);  //top piece of the stack

                // if the current player is white and the piece at the tile is 0, 1, 2 or it is empty,
                // create a copy of the arraylist and add it to availableTiles
                if (currentPlayer.equals("WHITE")){

                    if((tileValue >= 0 && tileValue <= 2) || board[i][j].isEmpty()) {
                        ArrayList<Integer> newArrayList = board[i][j];
                        availableTiles.add(newArrayList);
                    }
                }

                // if the current player is brown and the piece at the tile is 3, 4, 5 or it is empty,
                // create a copy of the arraylist and add it to availableTiles
                if (currentPlayer.equals("BROWN")) {

                    if((tileValue >= 3 && tileValue <= 5) || board[i][j].isEmpty()) {
                        ArrayList<Integer> newArrayList = board[i][j];
                        availableTiles.add(newArrayList);
                    }
                }
                
            }
            
        }

        return availableTiles;
    }
    
}
