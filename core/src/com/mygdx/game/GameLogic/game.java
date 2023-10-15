package com.mygdx.game.GameLogic;

import java.util.Scanner;

/**
 * This class is used to test and check the logic methods and rules of the game by being displayed in the terminal
 */

public class game {
    board board = new board();
    static boolean running = true;
    public static String currentPlayer = "WHITE";

    public static void main(String[] args){

        game game = new game();

        String input;
        boolean choice;

        Scanner scanner = new Scanner(System.in);


        while (!game.isGameOver()) {
            System.out.println(currentPlayer + "'s turn.");  // Displaying the current player's turn
            System.out.print("> ");
            input = scanner.nextLine();

            choice = game.comCheck(input);

            if(choice == false)
                System.out.println("\nI don't recognize that command\n");
            else
                game.comStart(input);
        }

        scanner.close();
    }

    /**
     * Checks if a winning condition is met / game is over
     * @return true is the game is over, false if otherwise
     */
    private boolean isGameOver() {
        return board.isGameEnded();
    }


    /**
     * Checks if the "check" command passed into the terminal is valid
     * @param command command typed in by user
     * @return true if command is valid, false if otherwise
     */
    public boolean comCheck(String command){
        String[] split = command.split("\\s+");
        String[] comType = {"add","move","check","full"};
        boolean stat=false;
        String one=split[0].toLowerCase();
        for (String s : comType) {
            if (one.equals(s)) {
                stat = true;
                break;
            }
        }
        return stat;
    }

    /**
     * Determines which action to take based on input command passed in by user, performs that command, checks the game state and then switches to the next player's turn
     * @param command command passed into terminal by user
     */
    public void comStart(String command){
        String[] split = command.split("\\s+");
        String[] comType = {"add","move","check","full"};
        String one=split[0].toLowerCase();
        int num=0;
        for(int i=0; i<comType.length; i++){
            if(one.equals(comType[i])){
                num=i;
            }  
        }

        switch (num){
            case 0:
                add(command);
                break;
            case 1:
                move(command);
                break;
            case 2:
                check();
                break;
            case 3:
                full();
                break;
        }
        board.checkState();
        togglePlayer();

    }


    /**
     * Adds a piece to the board by using the terminal 
     * @param command command passed into terminal by user
     */
    public void add(String command){
        String[] split = command.split("\\s+");
        String[] comType = {"0","1","2","3","4","5"};
        int two=Integer.parseInt(split[1]);
        boolean check=false;
        for (String s : comType) {
            if (two == Integer.parseInt(s)) {
                check = true;
            }
        }
        if(!check){
            System.out.println("\nInvalid Piece\n");
        }
        else
            board.addPiece(two, Integer.parseInt(split[2]), Integer.parseInt(split[3]));
        board.checkWinCondition();
    }


    /**
     * Moves a piece on the board using the terminal
     * @param command command passed into terminal by user
     */
    public void move(String command){
        String[] split = command.split("\\s+");
        String[] comType = {"up","right","down","left"};
        int two=Integer.parseInt(split[1]);
        int three=Integer.parseInt(split[2]);
        int four=Integer.parseInt(split[3]);
        String five=split[4].toLowerCase();
        int six=Integer.parseInt(split[5]);
        boolean check=false;

        int j=0;
        for(int i=0; i<comType.length; i++){
            if(five.equals(comType[i])){
                j=i;
                check=true;
            }  
        }
        if(!check){
            System.out.println("\nInvalid Chord\n");
        }
        else
            board.move(two, three, four, j, six);
        board.checkWinCondition();
    }


    /**
     * Alternates between the 2 player's turns
     */
    public void togglePlayer() {
        if (currentPlayer.equals("WHITE")) {
            currentPlayer = "BROWN";
        } else {
            currentPlayer = "WHITE";
        }
    }


    /**
     * Checks the current state of the game (displayed in the terminal)
     */
    public void check(){
        board.checkState();
    }
    
    /**
     * Checks if the current state of the board is full
     */
    public void full(){
        board.winBoardFull();
    }

}
