package com.mygdx.game;

import java.util.ArrayList;
import java.util.Scanner;

/*
 * Piece keys are as follows:
 * 
 * 0 = white road
 * 1 = white standing
 * 2 = white capstone
 * 
 * 3 = brown road
 * 4 = brown standing
 * 5 = brown capstone
 * 
 * 
 * Direction keys are as follows:
 * 0 = up
 * 1 = right
 * 2 = down
 * 3 = left
 * 
 */

public class board {

    public TakPiece piece;
    public ArrayList<Integer>[][] board = new ArrayList[5][5];
    private int turn = 0;
    private boolean isGameEnded = false;
    private static String currentPlayer = "WHITE";

    public boolean isGameEnded() {
        return isGameEnded;
    }

    public void setTurn(int turn){
        this.turn = turn;
    }

    public String getCurrentPlayer(){
        return currentPlayer;
    }

    public ArrayList<Integer>[][] getBoard(){
        return board;
    }

    public void togglePlayer() {
        if (currentPlayer.equals("WHITE")) {
            currentPlayer = "BROWN";
        } else {
            currentPlayer = "WHITE";
        }
    }

    public board(){
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board.length; j++){
                board[i][j] = new ArrayList<Integer>();
            }
        }
    }


    public void addPiece(int num,int x, int y){
        System.out.println("this method ran: "+x+" "+y);
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board.length; j++){
                if(x-1==i && y-1==j){

                    if(currentPlayer.equals("WHITE")&&(num==3||num==4||num==5))
                        System.out.println("Not browns turn");
                    else if(currentPlayer.equals("BROWN")&&(num==0||num==1||num==2))
                        System.out.println("Not whites turn");
                    else if(!board[i][j].isEmpty()){
                        System.out.println("Not empty space");
                    }
                    else{
                        board[i][j].add(num);
                        togglePlayer();
                        int inum= i+1;
                        int jnum=j+1;
                        System.out.println("added "+num+" at: ["+inum+"]["+jnum+"]");
                    }
                }
            }
        }

    }

    

    public void move(int x, int y, int quant, int dir, int dropNum){
        ArrayList<Integer> temp = new ArrayList<Integer>();

        x--;
        y--;


        if(board[x][y].size()>=quant && quant<=5){
            while(quant>0){
                temp.add(0, popFromTop(x, y));
                quant--;
            }

            if(currentPlayer.equals("WHITE") && (temp.get(temp.size()-1)==3||temp.get(temp.size()-1)==4||temp.get(temp.size()-1)==5)){
                System.out.println("Not browns turn");
            }
            else if(currentPlayer.equals("BROWN") && (temp.get(temp.size()-1)==0||temp.get(temp.size()-1)==1||temp.get(temp.size()-1)==2)){
                System.out.println("Not whites turn");
            }
            else{
                ArrayList<Integer> target = new ArrayList<Integer>();
                int xChord=x;
                int yChord=y;

                switch(dir){
                    case 0:
                        xChord--;
                        break;
                    case 1:
                        yChord++;
                        break;
                    case 2:
                        xChord++;
                        break;
                    case 3:
                        yChord--;
                        break;
                }

                if(checkSpace(xChord, yChord)){

                    target = board[xChord][yChord];

                    if(checkMove(temp, target)){

                        if(!target.isEmpty()){
                            if(target.get(target.size()-1)==1)
                                board[xChord][yChord].set(target.size()-1, 0);
                            else if(target.get(target.size()-1)==4)
                                board[xChord][yChord].set(target.size()-1, 3);
                        }
                
                        ArrayList<Integer> drop = dropOff(temp, dropNum);

                        board[xChord][yChord].addAll(drop);
                        System.out.println("moved");

                        if(temp.size()>0){
                            continueMove(xChord, yChord, temp, dir, 0);
                        }
                        togglePlayer();
                    }
                    else{
                        System.out.println("Invalid move");
                        board[x][y].addAll(temp);
                    }
                }
                else{
                    System.out.println("Invalid space");
                    board[x][y].addAll(temp);
                }
            }            
        }
        else
            System.out.println("Invalid quantity");
    }

    public void continueMove(int x, int y, ArrayList<Integer> temp, int dir, int dropNum){
        System.out.println("continue move");
                 
        Scanner in = new Scanner(System.in);
        System.out.println("please give drop number");
        System.out.print(">");
        dropNum = Integer.parseInt(in.nextLine());

        ArrayList<Integer> target = new ArrayList<Integer>();
        int xChord=x;
        int yChord=y;

            switch(dir){
                case 0:
                    xChord--;
                    break;
                case 1:
                    yChord++;
                    break;
                case 2:
                    xChord++;
                    break;
                case 3:
                    yChord--;
                    break;
            }

            if(checkSpace(xChord, yChord)){

                target = board[xChord][yChord];

                if(checkMove(temp, target)){
                    if(!target.isEmpty()){
                        if(target.get(target.size()-1)==1){
                            board[xChord][yChord].set(target.size()-1, 0);
                        }
                        else if(target.get(target.size()-1)==4)
                            board[xChord][yChord].set(target.size()-1, 3);
                    }

                    ArrayList<Integer> drop = dropOff(temp, dropNum);
                    board[xChord][yChord].addAll(drop);
                    System.out.println("moved");

                    if(temp.size()>0){
                        continueMove(xChord, yChord, temp, dir, 0);   
                    }
                }
                else{
                    System.out.println("Invalid move");
                    board[x][y].addAll(temp);
                }
            }
            else{
                System.out.println("Invalid space");
                board[x][y].addAll(temp);
            }      
    }


    public ArrayList<Integer> dropOff(ArrayList<Integer> temp, int quant){

        if(quant>temp.size()){
            System.out.println("invalid quantity");
            return null;
        }
        else{
            ArrayList<Integer> drop = new ArrayList<Integer>();
            for(int i=0; i<quant; i++){
                drop.add(temp.get(0));
                temp.remove(0);
            }
            return drop;
        }
    }

    public boolean checkSpace(int x, int y){

        if(x<0||y<0||x>4||y>4)
            return false;
        else
            return true;
    }
    

    public boolean checkMove(ArrayList<Integer> pile, ArrayList<Integer> target){

        if(target.isEmpty()){
            return true;
        }
        else{
            int top = target.get(target.size()-1);

            if(top==0||top==3)
                return true;
            else if(top==2||top==5)
                return false;
            else{
                if(pile.get(0)==2||pile.get(0)==5)
                    return true;
                else
                    return false;
            }            

        }
    }


    public int popFromTop(int x, int y){

        if(!board[x][y].isEmpty()){
            int result = board[x][y].get(board[x][y].size()-1);

            board[x][y].remove(board[x][y].size()-1);

            return result;
        }
        else
            return -1;
    }

    public void checkState(){

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                ArrayList<Integer> list = board[i][j];

                if (!list.isEmpty()) {
                    System.out.print("[");
                    for (Integer item : list) {
                        System.out.print(item + " ");
                    }
                    System.out.print("]");
                } else System.out.print("[]");
            }
            System.out.println();
        }
    }

    public boolean checkIfFull(ArrayList<Integer>[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void winBoardFull() {

        int wCounter = 0;
        int bCounter = 0;

        if (checkIfFull(board)) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {

                    ArrayList<Integer> arrayList = board[i][j];
                    int lastElement = arrayList.get(arrayList.size() - 1);

                    if (lastElement == 0 || lastElement == 2)
                        wCounter += 1;
                    else if (lastElement == 3 || lastElement == 5)
                        bCounter += 1;
                }
            }
            if (wCounter > bCounter) 
                System.out.println("White Wins.");
            else if (wCounter < bCounter) 
                System.out.println("Brown Wins.");
            else 
                System.out.println("It's a Draw.");
        }
        isGameEnded = true;
    }


    /**
     * Checks if the top piece at the specified coordinates belongs to the given player and forms
     * part of a potential road.
     *
     * @param x the x-coordinate on the board.
     * @param y the y-coordinate on the board.
     * @param player the player ("WHITE" or other) to check the piece for.
     * @return true if the top piece at (x, y) belongs to the given player and is part of a potential road, false otherwise.
     */
    public boolean isPartOfRoad(int x, int y, String player) {
        ArrayList<Integer> stack = board[x][y];
        if (stack.isEmpty()) {
            return false;
        }

        int topPiece = stack.get(stack.size() - 1);
        if (player.equals("WHITE")) {
            return topPiece == 0 || topPiece == 2;  // Checking for white flat stone or capstone.
        } else {
            return topPiece == 3 || topPiece == 5;  // Checking for brown flat stone or capstone.
        }
    }


    /**
     * Checks if the given player has successfully formed a road on the board.
     *
     * @param player the player ("WHITE" or "Brown") to check for a winning road.
     * @return true if the player has made a road either horizontally or vertically, false otherwise.
     */
    public boolean checkRoadForPlayer(String player) {
        for (int i = 0; i < board.length; i++) {
            if (checkRoadRecursive(0, i, player, new boolean[5][5], "vertical") ||
                    checkRoadRecursive(i, 0, player, new boolean[5][5], "horizontal")) {
                return true;
            }
        }
        return false;
    }


    /**
     * Recursively checks for a road in the given direction starting from the specified coordinates.
     *
     * @param x the x-coordinate to start checking from.
     * @param y the y-coordinate to start checking from.
     * @param player the player ("WHITE" or other) to check the road for.
     * @param visited a 2D boolean array marking the cells that have been visited during the search.
     * @param direction the direction ("vertical" or "horizontal") to check for the road.
     * @return true if a road is found in the given direction starting from (x, y), false otherwise.
     */
    public boolean checkRoadRecursive(int x, int y, String player, boolean[][] visited, String direction) {
        if (x < 0 || x >= 5 || y < 0 || y >= 5 || visited[x][y] || !isPartOfRoad(x, y, player)) {
            return false;
        }
        if (direction.equals("vertical") && x == 4) {
            return true;
        }
        if (direction.equals("horizontal") && y == 4) {
            return true;
        }
        visited[x][y] = true;
        return checkRoadRecursive(x - 1, y, player, visited, direction) ||
                checkRoadRecursive(x + 1, y, player, visited, direction) ||
                checkRoadRecursive(x, y - 1, player, visited, direction) ||
                checkRoadRecursive(x, y + 1, player, visited, direction);
    }

    /**
     * Checks if either player ("WHITE" or "BROWN") has won the game by forming a road.
     * If a player has won, an appropriate message is printed to the console, and the game ends.
     */

    public void checkWinCondition() {
        boolean whiteWins = checkRoadForPlayer("WHITE");
        boolean brownWins = checkRoadForPlayer("BROWN");

        if (whiteWins && brownWins) {
            System.out.println("WHITE Wins, as the player who made the move wins."); //based on the rules
            isGameEnded = true;
        } else if (whiteWins) {
            System.out.println("WHITE Wins by making a road.");
            isGameEnded = true;
        } else if (brownWins) {
            System.out.println("BROWN Wins by making a road.");
            isGameEnded = true;
        }
    }

}


