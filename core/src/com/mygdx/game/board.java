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

    // Add these to the board class
    private boolean dfs(int x, int y, int player, boolean[][] visited) {
        if (x < 0 || y < 0 || x >= board.length || y >= board[0].length || visited[x][y]) return false;
        ArrayList<Integer> stack = board[x][y];
        if (stack.isEmpty()) return false;

        int topPiece = stack.get(stack.size() - 1);
        if (player == 0 && (topPiece != 0 && topPiece != 2 && topPiece != 1)) return false;
        if (player == 1 && (topPiece != 3 && topPiece != 5 && topPiece != 4)) return false;

        if ((player == 0 && y == board[0].length - 1) || (player == 1 && x == board.length - 1)) return true;

        visited[x][y] = true;

        if (dfs(x + 1, y, player, visited) || dfs(x - 1, y, player, visited) || dfs(x, y + 1, player, visited) || dfs(x, y - 1, player, visited))
            return true;

        return false;
    }

    public void checkWinCondition() {
        // For white player
        for (int i = 0; i < board.length; i++) {
            if (dfs(i, 0, 0, new boolean[5][5])) {
                System.out.println("White Wins!");
                isGameEnded = true;
                return;
            }
        }

        // For brown player
        for (int j = 0; j < board[0].length; j++) {
            if (dfs(0, j, 1, new boolean[5][5])) {
                System.out.println("Brown Wins!");
                isGameEnded = true;
                return;
            }
        }
    }

    public static void main(String[] args) {
        board board = new board();
        board.addPiece(0, 1, 1);
        System.out.println(board.toString());
    }

}


