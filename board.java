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

    //public ArrayList<Integer> height = new ArrayList<Integer>(null);


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

        if(board[x][y].size()>=quant && quant<5){
            while(quant>0){
                temp.add(popFromTop(x, y),quant-1);
            }

        ArrayList<Integer> target = new ArrayList<Integer>();
        int xChord=x;
        int yChord=y;

            switch(dir){
                case 0:
                    //board[x-1][y].addAll(temp);
                    target = board[x-1][y];
                    xChord--;
                case 1:
                    //board[x][y+1].addAll(temp);
                    target = board[x][y+1];
                    yChord++;
                case 2:
                    //board[x+1][y].addAll(temp);
                    target = board[x+1][y];
                    xChord++;
                case 3:
                    //board[x][y-1].addAll(temp);
                    target = board[x][y-1];
                    yChord--;
            }

            if(checkMove(temp, target)){
                board[xChord][yChord].addAll(temp);
            }

        }
        else
            System.out.println("invalid quantity");
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
        int result=-1;
        for(int i=0; i<board[x][y].size(); i++){
            if(board[x][y].get(i+1)==null)
                result = board[x][y].get(i);
        }
        return result;
    }

    public board(){
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board.length; j++){
                board[i][j] = new ArrayList<Integer>();
            }
        }
    }

    public static void main(String[] args) {
        board board = new board();
        board.addPiece(0, 1, 1);
        System.out.println(board.toString());
    }

}


