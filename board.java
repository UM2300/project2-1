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
 */

public class board {
    public ArrayList<Integer>[][] board = new ArrayList[5][5];


    public void addPiece(int num,int x, int y){
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board.length; j++){
                if(x==i && y==j){
                    board[i][j].add(num);
                }
            }
        }

    }

    public void move(int x, int y, int quant, int dir){
        ArrayList<Integer> temp = new ArrayList<Integer>();

        if(board[x][y].size()>=quant && quant<5){
            while(quant>0){
                temp.add(pop(x, y),quant-1);
            }


            switch(dir){
                case 0:
                    board[x-1][y].addAll(temp);
                case 1:
                    board[x][y+1].addAll(temp);
                case 2:
                    board[x+1][y].addAll(temp);
                case 3:
                    board[x][y-1].addAll(temp);
            }

        }
        else
            System.out.println("invalid quantity");
    }



    public int pop(int x, int y){
        int result=-1;
        for(int i=0; i<board[x][y].size(); i++){
            if(board[x][y].get(i+1)==null)
                result = board[x][y].get(i);
        }
        return result;
    }



    public board(){


    }

    public static void main(String[] args) {
        board board = new board();
        board.addPiece(0, 1, 1);
        System.out.println(board.toString());
    }

}


