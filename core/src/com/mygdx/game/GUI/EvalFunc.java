package com.mygdx.game.GUI;
import com.mygdx.game.GameLogic.board;

public class EvalFunc {
 
    private board logicBoard;

    public int evaluation(board logicBoard){
        
        int value = 0;
        int piecesValues = pieceCount(logicBoard);



        return 0;
    }

    public int pieceCount(board logicBoard){

        int boardSum=0;
        for(int i=0; i<logicBoard.getBoard().length; i++){
            for(int j=0; j<logicBoard.getBoard().length; j++){

                if(!logicBoard.getBoard()[i][j].isEmpty()){
                    int topNum = logicBoard.getBoard()[i][j].get(logicBoard.getBoard()[i][j].size());
                    int pieceValue=0;

                    switch (topNum) {
                        case 0:
                            pieceValue=-2;
                            break;
                        case 1:
                            pieceValue=-1;
                            break;
                        case 2:
                            pieceValue=-3;
                            break;
                        case 3:
                            pieceValue=2;
                            break;
                        case 4:
                            pieceValue=1;
                            break;
                        case 5:
                            pieceValue=3;
                            break;
                    }

                    if(i==0&&(j==0||j==4)){
                        //we just do nothing
                    }
                    else if((i==0&&(j!=0||j!=4))||(j==0&&(i!=0||i!=4))||(i==4&&(j!=0||j!=4))||(j==4&&(i!=0||i!=4))){
                        pieceValue=pieceValue*2;
                    }
                    else{
                        pieceValue=pieceValue*3;
                    }
                    boardSum=boardSum+pieceValue;   
                }
            }
        }
        return boardSum;
    }

    public int roadProgress(board logicBoard){
        return 0;
    }

}
