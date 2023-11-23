package com.mygdx.game.GUI;
import java.util.ArrayList;

import com.mygdx.game.GameLogic.board;

public class EvalFunc {
 
    private board logicBoard;
    private boolean[][] visited;

    public void setVisited(boolean[][] visited){
        this.visited=visited;
    }

    public boolean[][] getVisited(){
        return this.visited;
    }

    public int evaluation(board logicBoard){

        boolean[][] newVisited = new boolean[5][5];

        for (int i = 0; i < newVisited.length; i++) {
            for (int j = 0; j < newVisited[i].length; j++) {
                newVisited[i][j] = false;
            }
        }

        setVisited(newVisited);
        
        int finalScore = 0;
        int piecesValues = pieceCount(logicBoard);
        int roadValuesBrown = checkRoadScoreForPlayer("BROWN", logicBoard);
        int roadValuesWhite = checkRoadScoreForPlayer("WHITE", logicBoard);

        finalScore = finalScore+piecesValues+roadValuesBrown+roadValuesWhite;
        
        return finalScore;
    }

    public int pieceCount(board logicBoard){

        int boardSum=0;
        for(int i=0; i<logicBoard.getBoard().length; i++){
            for(int j=0; j<logicBoard.getBoard().length; j++){

                if(!logicBoard.getBoard()[i][j].isEmpty()){
                    int topNum = logicBoard.getBoard()[i][j].get(logicBoard.getBoard()[i][j].size()-1);
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



    public int checkRoadScoreForPlayer(String player, board logicBoard) {

        int score=0;

        boolean[][] newVisited = new boolean[5][5];

        for (int i = 0; i < newVisited.length; i++) {
            for (int j = 0; j < newVisited[i].length; j++) {
                newVisited[i][j] = false;
            }
        }

        setVisited(newVisited);

        boolean[][] verticalBool = new boolean[5][5];
        boolean[][] horizontalBool = new boolean[5][5];

        for (int i = 0; i < logicBoard.getBoard().length; i++) {

            ArrayList<Integer>[] vertUp = checkRoadIncremental(0, i, player, "vu", new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<int[]>());
            setVisited(newVisited);
            ArrayList<Integer>[] horiRight = checkRoadIncremental(i, 0, player, "hr", new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<int[]>());
            setVisited(newVisited);

            int j = logicBoard.getBoard().length-1-i;

            ArrayList<Integer>[] vertDown = checkRoadIncremental(0, j, player, "vu", new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<int[]>());
            setVisited(newVisited);
            ArrayList<Integer>[] horiLeft = checkRoadIncremental(j, 0, player, "hr", new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<int[]>());
            setVisited(newVisited);

            int vertUpScore = vertUp[0].size()*vertUp[0].size()+1;
            int vertDownScore = vertDown[0].size()*vertDown[0].size()+1;
            int horiRightScore = horiRight[0].size()*horiRight[0].size()+1;
            int horiLeftScore = horiLeft[0].size()*horiLeft[0].size()+1;

            score=score+vertDownScore+vertUpScore+horiLeftScore+horiRightScore;

        }
        
        if(player.equals("WHITE")){
            score = score*-1;
        }

        return score;
    }


    public ArrayList<Integer>[] checkRoadIncremental(int x, int y, String player, String direction, ArrayList<Integer> best, ArrayList<Integer> good, ArrayList<int[]> roadChords){

        //ArrayList<Integer> best = new ArrayList<Integer>();
        //ArrayList<Integer> good = new ArrayList<Integer>();

        //ArrayList<int[]> roadChords=new ArrayList<int[]>();

        boolean[][] visited=getVisited();

        best.add(logicBoard.getBoard()[x][y].get(logicBoard.getBoard()[x][y].size()-1));

        if(direction.equals("vu")){

            while(checkAdj(x, y, visited, player)) {   
                if(!visited[x-1][y]&&isPartOfRoad(x-1, y, player)){
                    best.add(logicBoard.getBoard()[x-1][y].get(logicBoard.getBoard()[x-1][y].size()-1));
                    roadChords.add(new int[]{x-1,y});
                    visited[x-1][y]=true;
                    x=x-1;
                }
                else if(!visited[x][y-1]&&isPartOfRoad(x, y-1, player)){
                    good.add(logicBoard.getBoard()[x][y-1].get(logicBoard.getBoard()[x][y-1].size()-1));
                    roadChords.add(new int[]{x,y-1});
                    visited[x][y-1]=true;
                    y=y-1;
                }
                else if(!visited[x][y+1]&&isPartOfRoad(x, y+1, player)){
                    good.add(logicBoard.getBoard()[x][y+1].get(logicBoard.getBoard()[x][y+1].size()-1));
                    roadChords.add(new int[]{x,y+1});
                    visited[x][y+1]=true;
                    y=y+1;
                }
            }
        }
        else if(direction.equals("vd")){

            while(checkAdj(x, y, visited, player)) {
                if(!visited[x+1][y]&&isPartOfRoad(x+1, y, player)){
                    best.add(logicBoard.getBoard()[x+1][y].get(logicBoard.getBoard()[x+1][y].size()-1));
                    roadChords.add(new int[]{x+1,y});
                    visited[x+1][y]=true;
                    x=x+1;
                }
                else if(!visited[x][y-1]&&isPartOfRoad(x, y-1, player)){
                    good.add(logicBoard.getBoard()[x][y-1].get(logicBoard.getBoard()[x][y-1].size()-1));
                    roadChords.add(new int[]{x,y-1});
                    visited[x][y-1]=true;
                    y=y-1;
                }
                else if(!visited[x][y+1]&&isPartOfRoad(x, y+1, player)){
                    good.add(logicBoard.getBoard()[x][y+1].get(logicBoard.getBoard()[x][y+1].size()-1));
                    roadChords.add(new int[]{x,y+1});
                    visited[x][y+1]=true;
                    y=y+1;
                }
            }
        }
        else if(direction.equals("hr")){

            while(checkAdj(x, y, visited, player)) {   
                if(!visited[x][y+1]&&isPartOfRoad(x, y+1, player)){
                    best.add(logicBoard.getBoard()[x][y+1].get(logicBoard.getBoard()[x][y+1].size()-1));
                    roadChords.add(new int[]{x,y+1});
                    visited[x][y+1]=true;
                    y=y+1;
                }
                else if(!visited[x-1][y]&&isPartOfRoad(x-1, y, player)){
                    good.add(logicBoard.getBoard()[x-1][y].get(logicBoard.getBoard()[x-1][y].size()-1));
                    roadChords.add(new int[]{x-1,y});
                    visited[x-1][y]=true;
                    x=x-1;
                }
                else if(!visited[x+1][y]&&isPartOfRoad(x+1, y, player)){
                    good.add(logicBoard.getBoard()[x+1][y].get(logicBoard.getBoard()[x+1][y].size()-1));
                    roadChords.add(new int[]{x+1,y});
                    visited[x+1][y]=true;
                    x=x+1;
                }
            }
        }
        else if(direction.equals("hl")){

            while(checkAdj(x, y, visited, player)) {   
                if(!visited[x][y-1]&&isPartOfRoad(x, y-1, player)){
                    best.add(logicBoard.getBoard()[x][y-1].get(logicBoard.getBoard()[x][y-1].size()-1));
                    roadChords.add(new int[]{x,y-1});
                    visited[x][y-1]=true;
                    y=y-1;
                }
                else if(!visited[x-1][y]&&isPartOfRoad(x-1, y, player)){
                    good.add(logicBoard.getBoard()[x-1][y].get(logicBoard.getBoard()[x-1][y].size()-1));
                    roadChords.add(new int[]{x-1,y});
                    visited[x-1][y]=true;
                    x=x-1;
                }
                else if(!visited[x+1][y]&&isPartOfRoad(x+1, y, player)){
                    good.add(logicBoard.getBoard()[x+1][y].get(logicBoard.getBoard()[x+1][y].size()-1));
                    roadChords.add(new int[]{x+1,y});
                    visited[x+1][y]=true;
                    x=x+1;
                }       
            }
        }

        


        setVisited(visited);

        for (int i=0; i<best.size(); i++){
            int oldX=roadChords.get(i)[0];
            int oldY=roadChords.get(i)[1];

            if(!visited[oldX-1][oldY]&&isPartOfRoad(oldX-1, oldY, player)){
                ArrayList[] list = checkRoadIncremental(oldX-1, oldY, player, direction, best, good, roadChords);
                best = list[0];
                good = list[1];
                roadChords = list[2];
            }
            if(!visited[oldX+1][oldY]&&isPartOfRoad(oldX-1, oldY, player)){
                ArrayList[] list = checkRoadIncremental(oldX+1, oldY, player, direction, best, good, roadChords);
                best = list[0];
                good = list[1];
                roadChords = list[2];
            }
            if(!visited[oldX][oldY-1]&&isPartOfRoad(oldX-1, oldY, player)){
                ArrayList[] list = checkRoadIncremental(oldX, oldY-1, player, direction, best, good, roadChords);
                best = list[0];
                good = list[1];
                roadChords = list[2];
            }
            if(!visited[oldX][oldY+1]&&isPartOfRoad(oldX-1, oldY, player)){
                ArrayList[] list = checkRoadIncremental(oldX, oldY+1, player, direction, best, good, roadChords);
                best = list[0];
                good = list[1];
                roadChords = list[2];
            }

        }
        
        ArrayList[] lists = {best, good, roadChords};


        return lists;
    }


    public boolean checkAdj(int x, int y, boolean[][] visited, String player){

        boolean left=false;
        boolean right=false;
        boolean up=false;
        boolean down=false;

        if(!visited[x-1][y]&&isPartOfRoad(x-1, y, player)){
            up=true;
        }
        if(!visited[x+1][y]&&isPartOfRoad(x+1, y, player)){
            down=true;
        }
        if(!visited[x][y-1]&&isPartOfRoad(x, y-1, player)){
            left=true;
        }
        if(!visited[x][y+1]&&isPartOfRoad(x, y+1, player)){
            right=true;
        }

        if(left&&right&&up&&down){
            return true;
        }
        else{
            return false;
        }

    }

    public boolean isPartOfRoad(int x, int y, String player) {
        ArrayList<Integer> stack = logicBoard.getBoard()[x][y];
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

}
