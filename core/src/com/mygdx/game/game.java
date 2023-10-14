package com.mygdx.game;

import java.util.Scanner;

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

    private boolean isGameOver() {
        return board.isGameEnded();
    }


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


    public void togglePlayer() {
        if (currentPlayer.equals("WHITE")) {
            currentPlayer = "BROWN";
        } else {
            currentPlayer = "WHITE";
        }
    }


    public void check(){
        board.checkState();
    }
    
    public void full(){
        board.winBoardFull();
    }

}
