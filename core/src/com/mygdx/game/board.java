package com.mygdx.game;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*
 * This class represents the logic behind the rules of Tak
 */

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
    private JFrame dropFrame;
    private int drop=1;

    private ArrayList<Integer> temp;
    private int[] chordsAndDir;


    public int wCounter = 0;
    public int bCounter = 0;

    public int maxPiecesPerPlayer = 21;

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

    public int getDrop(){
        return drop;
    }

    public void setDrop(int drop){
        this.drop=drop;
    }

    public int[] getChordsAndDir(){
        return chordsAndDir;
    }

    public void setChordsAndDir(int[] chordsAndDir){
        this.chordsAndDir=chordsAndDir;
    }

    public ArrayList<Integer> getTemp(){
        return temp;
    }

    public void setTemp(ArrayList<Integer> temp){
        this.temp=temp;
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


    /**
     * Adds a piece to the board with the colour of the current player, and switches the turn to the opponent after the piece has been added
     * 
     * @param num the number representing the piece (see key above)
     * @param x x coordinate on the board
     * @param y y coordinate on the board
     */
    public void addPiece(int num,int x, int y){

        System.out.println("this method ran: "+x+" "+y);

        if (currentPlayer.equals("WHITE")&&(num < 0 || num > 2)) {
            System.out.println("Not browns turn");
            return;
        } else if (currentPlayer.equals("BROWN")&&(num < 3 || num > 5)) {
            System.out.println("Not whites turn");
            return;
            }

        for(int i=0; i<board.length; i++){
            for(int j=0; j<board.length; j++){
                if(x-1==i && y-1==j){

                    if(!board[i][j].isEmpty()){
                        System.out.println("Not an empty space");
                    }
                    else{
                        board[i][j].add(num);

                        if (currentPlayer.equals("WHITE")) {
                            wCounter++;
                        } else if (currentPlayer.equals("BROWN")) {
                            bCounter++;
                        }

                        togglePlayer();
                        int inum= i+1;
                        int jnum=j+1;
                        System.out.println("added "+num+" at: ["+inum+"]["+jnum+"]");

                    }
                }
            }
        }

    }

    
    /**
     * Moves a piece / stack to a specified cell / block 
     * 
     * @param x current x coordinate on the board
     * @param y current y coordinate on the board
     * @param quant quantity of pieces to move (if a stack, quant > 1, else quant = 1)
     * @param dir direction (up, down, left, right) in which the player wants to move the piece/stack
     * @param dropNum the number of pieces to drop off in the current tile (if a stack dropNum >= 1, else dropNum = 1)
     */
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
                            //continueMove(xChord, yChord, temp, dir, 0);
                            continueDropPiece(xChord, yChord, temp, dir);
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

    /**
     * Continues a move if a STACK is being moved
     * 
     * @param x x coordinate on board
     * @param y y coordinate on board
     * @param temp current stack of pieces 
     * @param dir direction (up, down, left, right) to continue move
     * @param dropNum number of pieces to drop of in the current tile (if a stack dropNum >= 1, else dropNum = 1)
     */

    public void continueMove(int x, int y, ArrayList<Integer> temp, int dir, int dropNum){
        System.out.println("continue move");
                 
        //Scanner in = new Scanner(System.in);
        //System.out.println("please give drop number");
        //System.out.print(">");
        //dropNum = Integer.parseInt(in.nextLine());
        //continueDropPiece();
        //dropNum = getDrop();

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
                        //continueMove(xChord, yChord, temp, dir, 0); 
                        try{
                            Thread.sleep(2000);
                            continueDropPiece(xChord, yChord, temp, dir);
                        }
                        catch(Exception e){
                            System.out.println("Time error");
                        };
                          
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


    /**
     * Drops off a number of pieces as a stack is being moved
     * 
     * @param temp current state of the stack
     * @param quant number of pieces to be dropped off at the current tile
     * @return pieces being dropped off
     */
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

    /**
     * Checks if the coordinates of a tile is valid
     * 
     * @param x x coordinate
     * @param y y coordinate
     * @return true if coordinates are on the board, false if not
     */
    public boolean checkSpace(int x, int y){

        if(x<0||y<0||x>4||y>4)
            return false;
        else
            return true;
    }
    

    /**
     * Checks if a move of a piece or stack is valid
     * 
     * @param pile the current stack / piece
     * @param target the current tile's contents 
     * @return true if there is no capstone / standing stone / the tile is empty, false if otherwise
     */
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


    /**
     * Returns the top piece of a stack
     * 
     * @param x x coordinate on the board
     * @param y y coordinate on the board
     * @return top piece of a stack
     */
    public int popFromTop(int x, int y){

        if(!board[x][y].isEmpty()){
            int result = board[x][y].get(board[x][y].size()-1);
            board[x][y].remove(board[x][y].size()-1);
            return result;
        }
        else
            return -1;
    }

    /**
     * Used to check the current state of the board
     */
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

    /**
     * Checks if the board is full, ie if a player has used up all their pieces or if there are no more empty tiles.
     * 
     * @param board 2D array representing coordinates on the board 
     * @return true if the board is full / player has used up all pieces, false if otherwise
     */
    public boolean checkIfFull(ArrayList<Integer>[][] board) {
        if ((wCounter == maxPiecesPerPlayer) || (bCounter == maxPiecesPerPlayer)) return true;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the board has no empty tiles or if a player has used all pieces, and returns the winner for this condition
     */
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
            
            isGameEnded = true;
        }
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


    public void continueDropPiece(int x, int y, ArrayList<Integer> temp, int dir) {
        System.out.println("Created");

        JLabel moveLabel = new JLabel("HOW MANY PIECES DO YOU WANT TO DROP?");
        final JTextField textField = new JTextField();
        JButton button = new JButton("Confirm");

        int[] chordsAndDir = {x,y,dir};

        setChordsAndDir(chordsAndDir);

        setTemp(temp);

        button.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e){

                String txt = textField.getText();
                int drop = Integer.parseInt(txt);
                
                if(getTemp().isEmpty()){
                    dropFrame.dispose();
                }
                else{
                    
                    dropFrame.dispose();
                    System.out.println("disposed");

                    continueMove(getChordsAndDir()[0], getChordsAndDir()[1], getTemp(), getChordsAndDir()[2], drop);
                }                
            }

        });

        JPanel topMovePanel = new JPanel();
        topMovePanel.setLayout(new GridLayout(3, 1));
        topMovePanel.add(moveLabel);
        topMovePanel.add(textField);
        topMovePanel.add(button);
        
        
        dropFrame = new JFrame("Move Piece (continued)");
        dropFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dropFrame.setSize(300, 200);
        dropFrame.setLayout(new BorderLayout());
        dropFrame.setLocationRelativeTo(null);
        dropFrame.add(topMovePanel);

        dropFrame.setVisible(true);
    }

}


