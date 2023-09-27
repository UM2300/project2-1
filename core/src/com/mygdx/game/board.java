package com.mygdx.game;

import java.util.ArrayList;

/*
 * Piece keys are as follows:
 * 
 * 0 = white road
 * 1 = white standing
 * 2 = white capstone
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
    public ArrayList<Integer>[][] board = new ArrayList[5][5];

    public board(){
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board.length; j++){
                board[i][j] = new ArrayList<Integer>();
            }
        }
    }


    public void addPiece(int num,int x, int y){
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board.length; j++){
                if(x-1==i && y-1==j){
                    board[i][j].add(num);
                    System.out.println("done");
                }
            }
        }

    }

    public void move(int x, int y, int quant, int dir){
        ArrayList<Integer> temp = new ArrayList<Integer>();

        x--;
        y--;

        int big = board[x][y].size();

        if(board[x][y].size()>=quant && quant<5){
            while(quant>0){
                //temp.add(popFromTop(x-1, y-1),quant-1);
                temp.add(quant-1, popFromTop(x, y));
                quant--;
            }

        ArrayList<Integer> target = new ArrayList<Integer>();
        int xChord=x;
        int yChord=y;

            switch(dir){
                case 0:
                System.out.println(xChord);
                System.out.println(yChord);
                    target = board[x-1][y];
                    xChord--;
                    break;
                case 1:
                    target = board[x][y+1];
                    yChord++;
                    break;
                case 2:
                    target = board[x+1][y];
                    xChord++;
                    break;
                case 3:
                    target = board[x][y-1];
                    yChord--;
                    break;
            }

            if(checkMove(temp, target)){
                if(!target.isEmpty()){
                    if(target.get(target.size())==1)
                        board[xChord][yChord].set(target.size(), 0);
                    else if(target.get(target.size())==4)
                        board[xChord][yChord].set(target.size(), 3);
                }
                

                board[xChord][yChord].addAll(temp);
            }

        }
        else{
            System.out.println("invalid quantity");
        }

        System.out.println("moved");
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
                //if (j < 2) System.out.print("\t");
            } 
            System.out.println();
        }
    }

    public static void main(String[] args) {
        board board = new board();
        board.addPiece(0, 1, 1);
        System.out.println(board.toString());
    }

}


