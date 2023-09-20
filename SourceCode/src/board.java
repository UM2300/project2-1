
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

    /**
     * Constructor:
     * initialises an ArrayList within each cell of a 5x5 grid, creating a board
     */
    public board(){
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board.length; j++){
                board[i][j] = new ArrayList<Integer>();
            }
        }
    }


    /**
     * Adds a piece (white or brown) to an empty cell on the board.
     *
     * @param num the type of piece described by a number
     * @param x the x coordinate on the board
     * @param y the y coordinate on the board
     */
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


    /**
     * Moves a specific piece / pieces to a new cell on the board
     * @param x new x coordinate
     * @param y new y coordinate
     * @param quant the quantity of pieces to move
     * @param dir the direction the piece is moving (up, down, left, right)
     */
    public void move(int x, int y, int quant, int dir){
        ArrayList<Integer> temp = new ArrayList<Integer>();

        int big = board[x-1][y-1].size();

        if(board[x-1][y-1].size()>=quant && quant<5){
            while(quant>0){
                //temp.add(popFromTop(x-1, y-1),quant-1);
                temp.add(quant-1, popFromTop(x-1, y-1));
                quant--;
            }

        ArrayList<Integer> target = new ArrayList<Integer>();
        int xChord=x-1;
        int yChord=y-1;

            switch(dir){
                case 0:
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

    /**
     * Checks if a move is valid
     * @param pile the pile of pieces to move
     * @param target the target cell
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
     * Removes and returns the top piece from a pile of a specified cell 
     * @param x the x coordinate on the board
     * @param y the y coordinate on the board
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
     * Prints out the current state of the board, including where and how pieces are placed
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


