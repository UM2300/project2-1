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

    public board getLogicBoard(){
        return this.logicBoard;
    }

    public void setLogicBoard(board logicBoard){
        this.logicBoard=logicBoard;
    }


    /**
     * This method is the main call of the evaluation function that adds up the different scoring elements
     * defined below and returns the final sum.
     * 
     * @param logicBoard The state of the game board
     * @return score for the state of the board from the view of the AI player
     */
    public int evaluation(board logicBoard){

        setLogicBoard(logicBoard);

        boolean[][] newVisited = new boolean[5][5];

        for (int i = 0; i < newVisited.length; i++) {
            for (int j = 0; j < newVisited[i].length; j++) {
                newVisited[i][j] = false;
            }
        }

        setVisited(newVisited);
        
        int finalScore = 0;
        int piecesValues = pieceCount(getLogicBoard());
        int roadValuesBrown = checkRoadScoreForPlayer("BROWN", getLogicBoard());
        int roadValuesWhite = checkRoadScoreForPlayer("WHITE", getLogicBoard());

        finalScore = finalScore+piecesValues+roadValuesBrown+roadValuesWhite;
        
        return finalScore;
    }

    /**
     * This method finds an element of the final evaluation score by assigning value to each piece on the board and 
     * returning the sum of their values.
     * 
     * @param logicBoard the state of the game board
     * @return score added for each piece present on the board
     */
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




    /**
     * This method iterates over the edge spaces of the board and checks for roads in the four cardinal directions. Any found will have their
     * score saved. The final return is the sum of the scores of any found roads on the board.
     * 
     * @param player player that has the current turn
     * @param logicBoard current state of the game board
     * @return evaluation score for any existing roads on the board
     */
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

            ArrayList<Integer>[] vertDown = checkRoadIncremental(0, j, player, "vd", new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<int[]>());
            setVisited(newVisited);
            ArrayList<Integer>[] horiLeft = checkRoadIncremental(j, 0, player, "hl", new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<int[]>());
            setVisited(newVisited);


            int vertUpScoreBest = vertUp[0].size()*(vertUp[0].size()+1)*2;
            int vertDownScoreBest = vertDown[0].size()*(vertDown[0].size()+1)*2;
            int horiRightScoreBest = horiRight[0].size()*(horiRight[0].size()+1)*2;
            int horiLeftScoreBest = horiLeft[0].size()*(horiLeft[0].size()+1)*2;

            int vertUpScoreGood = vertUp[1].size()*(vertUp[1].size()+1);
            int vertDownScoreGood = vertDown[1].size()*(vertDown[1].size()+1);
            int horiRightScoreGood = horiRight[1].size()*(horiRight[1].size()+1);
            int horiLeftScoreGood = horiLeft[1].size()*(horiLeft[1].size()+1);

            score=score+vertDownScoreBest+vertUpScoreBest+horiLeftScoreBest+horiRightScoreBest+vertUpScoreGood+vertDownScoreGood+horiLeftScoreGood+horiRightScoreGood;

        }
        
        if(player.equals("WHITE")){
            score = score*-1;
        }

        return score;
    }

    /**
     * This method takes a starting coordinate and attempts to find a road from it to the other side. Whenever a piece is
     * found to be part of the road and taking the shortest path to the oppsite side it is added to the best list, if it 
     * is part of the road but is not taking the shortest path it is added to the good list. So long as adjascent pieces 
     * that can be part of the road exist and have not been visited the method will recursively travel back to iterate over
     * those as well.
     * 
     * @param x x coordinate of current road piece
     * @param y y coordinate of current road piece
     * @param player player who has current turn
     * @param direction direction for which the road is being checked
     * @param best list of pieces part of a road but not on the shortest path to the opposite side
     * @param good list of pieces part of a road and on the shortest path to the opposite side
     * @param roadChords a list of pieces that are part of the current road
     * @return score of the current found road
     */
    public ArrayList<Integer>[] checkRoadIncremental(int x, int y, String player, String direction, ArrayList<Integer> best, ArrayList<Integer> good, ArrayList<int[]> roadChords){

        //ArrayList<Integer> best = new ArrayList<Integer>();
        //ArrayList<Integer> good = new ArrayList<Integer>();

        //ArrayList<int[]> roadChords=new ArrayList<int[]>();

        boolean[][] visited=getVisited();

        if(!logicBoard.getBoard()[x][y].isEmpty()){
            best.add(logicBoard.getBoard()[x][y].get(logicBoard.getBoard()[x][y].size()-1));
            roadChords.add(new int[]{x,y});
            visited[x][y]=true;

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

            


            //setVisited(visited);
            if(!best.isEmpty()){
                for (int i=0; i<best.size(); i++){
                    int oldX=roadChords.get(i)[0];
                    int oldY=roadChords.get(i)[1];

                    if(isValidSpace(oldX-1, oldY)&&!visited[oldX-1][oldY]&&isPartOfRoad(oldX-1, oldY, player)){
                        ArrayList[] list = checkRoadIncremental(oldX-1, oldY, player, direction, best, good, roadChords);
                        best = list[0];
                        good = list[1];
                        roadChords = list[2];
                    }
                    if(isValidSpace(oldX+1, oldY)&&!visited[oldX+1][oldY]&&isPartOfRoad(oldX-1, oldY, player)){
                        ArrayList[] list = checkRoadIncremental(oldX+1, oldY, player, direction, best, good, roadChords);
                        best = list[0];
                        good = list[1];
                        roadChords = list[2];
                    }
                    if(isValidSpace(oldX, oldY-1)&&!visited[oldX][oldY-1]&&isPartOfRoad(oldX-1, oldY, player)){
                        ArrayList[] list = checkRoadIncremental(oldX, oldY-1, player, direction, best, good, roadChords);
                        best = list[0];
                        good = list[1];
                        roadChords = list[2];
                    }
                    if(isValidSpace(oldX, oldY+1)&&!visited[oldX][oldY+1]&&isPartOfRoad(oldX-1, oldY, player)){
                        ArrayList[] list = checkRoadIncremental(oldX, oldY+1, player, direction, best, good, roadChords);
                        best = list[0];
                        good = list[1];
                        roadChords = list[2];
                    }

                }
            }

            
            
            ArrayList[] lists = {best, good, roadChords};
            return lists;
        }
        else{

            ArrayList[] lists = {best, good, roadChords};
            return lists;
        }
        
    }


    /**
     * This method seeks to check all adjascent spaces of the x and y inputs to check if there is a space that has
     * not been visited and could be part of road of the current player
     * 
     * @param x x coordinate of board space
     * @param y y coordinate of board space
     * @param visited 2d array of booleans denoting previously visited spaces with true
     * @param player player who has the current turn
     * @return true if there exists an adjascent space that has not been visited and could be a part of a road
     */
    public boolean checkAdj(int x, int y, boolean[][] visited, String player){

        boolean left=false;
        boolean right=false;
        boolean up=false;
        boolean down=false;

        if(isValidSpace(x-1, y)&&!visited[x-1][y]&&isPartOfRoad(x-1, y, player)){
            up=true;
        }
        if(isValidSpace(x+1, y)&&!visited[x+1][y]&&isPartOfRoad(x+1, y, player)){
            down=true;
        }
        if(isValidSpace(x, y-1)&&!visited[x][y-1]&&isPartOfRoad(x, y-1, player)){
            left=true;
        }
        if(isValidSpace(x, y+1)&&!visited[x][y+1]&&isPartOfRoad(x, y+1, player)){
            right=true;
        }

        if(left&&right&&up&&down){
            return true;
        }
        else{
            return false;
        }

    }

    /**
     * This method simply checks the top piece at coordinate x,y to see if the piece belongs to the current
     * player and could be part of their road. It will return true if it is the case and false otherwise.
     * 
     * @param x x coordinate of board space
     * @param y y coordinate of board space
     * @param player player who has current turn
     * @return true if the space x, y has a piece that could be a part of the road for current player
     */
    public boolean isPartOfRoad(int x, int y, String player) {

        if(!isValidSpace(x, y)){
            return false;
        }

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

    public boolean isValidSpace(int x, int y){
        if(x<0||x>4||y<0||y>4){
            return false;
        }
        else{
            return true;
        }
    }

}
