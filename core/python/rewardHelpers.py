def road_Score(player, visited, x, y, BoardState):


        whiteStraight=0
        whiteBranch=0
        whiteBlockedBrown=0

        brownStraight=0
        brownBranch=0
        brownBlockedWhite=0

        prevRoadChords=[]


        if(x==0):

            if(BoardState[x][y]==0 or BoardState[x][y]==2):
                whiteStraight += 1

                while (checkAdj(x,y,"white",visited, BoardState) and x!=4):


                    visited[x][y]=True
                    prevRoadChords.append((x,y))

                    if(BoardState[x+1][y]==0 or BoardState[x+1][y]==2):
                        x=x+1
                        whiteStraight += 1
                    elif(y<4 and (BoardState[x][y+1]==0 or BoardState[x][y+1]==2)):
                        y=y+1
                        whiteBranch+=1
                    elif(y>0 and (BoardState[x][y-1]==0 or BoardState[x][y-1]==2)):
                        y=y-1
                        whiteBranch+=1
                    elif(BoardState[x+1][y]==4):
                        brownBlockedWhite+=1

            elif(BoardState[x][y]==3 or BoardState[x][y]==5):
                brownStraight += 1

                while (checkAdj(x,y,"brown",visited, BoardState) and x!=4):



                    visited[x][y]=True
                    prevRoadChords.append((x,y))

                    if(BoardState[x+1][y]==3 or BoardState[x+1][y]==5):
                        x=x+1
                        brownStraight += 1
                    elif(y<4 and (BoardState[x][y+1]==3 or BoardState[x][y+1]==5)):
                        y=y+1
                        brownBranch+=1
                    elif(y>0 and (BoardState[x][y-1]==3 or BoardState[x][y-1]==5)):
                        y=y-1
                        brownBranch+=1
                    elif(BoardState[x+1][y]==1):
                        whiteBlockedBrown+=1

        elif(x==4):

            if(BoardState[x][y]==0 or BoardState[x][y]==2):
                whiteStraight += 1

                while (checkAdj(x,y,"white",visited, BoardState) and x!=0):


                    visited[x][y]=True
                    prevRoadChords.append((x,y))

                    if(BoardState[x-1][y]==0 or BoardState[x-1][y]==2):
                        x=x-1
                        whiteStraight += 1
                    elif(y<4 and (BoardState[x][y+1]==0 or BoardState[x][y+1]==2)):
                        y=y+1
                        whiteBranch+=1
                    elif(y>0 and (BoardState[x][y-1]==0 or BoardState[x][y-1]==2)):
                        y=y-1
                        whiteBranch+=1
                    elif(BoardState[x-1][y]==4):
                        brownBlockedWhite+=1

            elif(BoardState[x][y]==3 or BoardState[x][y]==5):
                brownStraight += 1

                while (checkAdj(x,y,"brown",visited, BoardState) and x!=0):



                    visited[x][y]=True
                    prevRoadChords.append((x,y))

                    if(BoardState[x-1][y]==3 or BoardState[x-1][y]==5):
                        x=x-1
                        brownStraight += 1
                    elif(y<4 and (BoardState[x][y+1]==3 or BoardState[x][y+1]==5)):
                        y=y+1
                        brownBranch+=1
                    elif(y>0 and (BoardState[x][y-1]==3 or BoardState[x][y-1]==5)):
                        y=y-1
                        brownBranch+=1
                    elif(BoardState[x-1][y]==1):
                        whiteBlockedBrown+=1

        elif(y==0):

            if(BoardState[x][y]==0 or BoardState[x][y]==2):
                whiteStraight += 1

                while (checkAdj(x,y,"white",visited, BoardState) and y!=4):
                    

                    
                    visited[x][y]=True
                    prevRoadChords.append((x,y))

                    if(BoardState[x][y+1]==0 or BoardState[x][y+1]==2):
                        y=y+1
                        whiteStraight += 1
                    elif(x<4 and (BoardState[x+1][y]==0 or BoardState[x+1][y]==2)):
                        x=x+1
                        whiteBranch+=1
                    elif(x>0 and (BoardState[x-1][y]==0 or BoardState[x-1][y]==2)):
                        x=x-1
                        whiteBranch+=1
                    elif(BoardState[x][y+1]==4):
                        brownBlockedWhite+=1

            elif(BoardState[x][y]==3 or BoardState[x][y]==5):
                brownStraight += 1

                while (checkAdj(x,y,"brown",visited, BoardState) and y!=4):



                    visited[x][y]=True
                    prevRoadChords.append((x,y))

                    if(BoardState[x][y+1]==3 or BoardState[x][y+1]==5):
                        y=y+1
                        brownStraight += 1
                    elif(x<4 and (BoardState[x+1][y]==3 or BoardState[x+1][y]==5)):
                        x=x+1
                        brownBranch+=1
                    elif(x>0 and (BoardState[x-1][y]==3 or BoardState[x-1][y]==5)):
                        x=x-1
                        brownBranch+=1
                    elif(BoardState[x][y+1]==1):
                        whiteBlockedBrown+=1

        elif(y==4):

            if(BoardState[x][y]==0 or BoardState[x][y]==2):
                whiteStraight += 1

                while (checkAdj(x,y,"white",visited, BoardState) and y!=0):



                    visited[x][y]=True
                    prevRoadChords.append((x,y))

                    if(BoardState[x][y-1]==0 or BoardState[x][y-1]==2):
                        y=y-1
                        whiteStraight += 1
                    elif(x<4 and (BoardState[x+1][y]==0 or BoardState[x+1][y]==2)):
                        x=x+1
                        whiteBranch+=1
                    elif(x>0 and (BoardState[x-1][y]==0 or BoardState[x-1][y]==2)):
                        x=x-1
                        whiteBranch+=1
                    elif(BoardState[x][y-1]==4):
                        brownBlockedWhite+=1

            elif(BoardState[x][y]==3 or BoardState[x][y]==5):
                brownStraight += 1

                while (checkAdj(x,y,"brown",visited, BoardState) and y!=0):



                    visited[x][y]=True
                    prevRoadChords.append((x,y))

                    if(BoardState[x][y-1]==3 or BoardState[x][y-1]==5):
                        y=y-1
                        brownStraight += 1
                    elif(x<4 and (BoardState[x+1][y]==3 or BoardState[x+1][y]==5)):
                        x=x+1
                        brownBranch+=1
                    elif(x>0 and (BoardState[x-1][y]==3 or BoardState[x-1][y]==5)):
                        x=x-1
                        brownBranch+=1
                    elif(BoardState[x][y-1]==1):
                        whiteBlockedBrown+=1





        ws2 = 0
        wb2 = 0
        wbb2 = 0
        bs2 = 0
        bb2 = 0
        bbw2 = 0

        if(whiteStraight>0):
            for x, y in prevRoadChords:
                oldX=x
                oldY=y

                whiteRoad=[0,2]
                brownRoad=[3,5]

                

                if(BoardState[oldX][oldY] in whiteRoad):


                
                    if(0<=x+1<=4 and BoardState[x+1][y] in whiteRoad and visited[x+1][y]==False):
                        ws2, wb2, wbb2, bs2, bb2, bbw2 = road_Score(player, visited, x+1, y, BoardState)
                    elif(0<=x-1<=4 and BoardState[x-1][y] in whiteRoad and visited[x-1][y]==False):
                        ws2, wb2, wbb2, bs2, bb2, bbw2 = road_Score(player, visited, x-1, y, BoardState)
                    elif(0<=y+1<=4 and BoardState[x][y+1] in whiteRoad and visited[x][y+1]==False):
                        ws2, wb2, wbb2, bs2, bb2, bbw2 = road_Score(player, visited, x, y+1, BoardState)
                    elif(0<=y-1<=4 and BoardState[x][y-1] in whiteRoad and visited[x][y-1]==False):
                        ws2, wb2, wbb2, bs2, bb2, bbw2 = road_Score(player, visited, x, y-1, BoardState)

                elif(BoardState[oldX][oldY] in brownRoad):
                    if(0<=x+1<=4 and BoardState[x+1][y] in brownRoad and visited[x+1][y]==False):
                        ws2, wb2, wbb2, bs2, bb2, bbw2 = road_Score(player, visited, x+1, y, BoardState)
                    elif(0<=x-1<=4 and BoardState[x-1][y] in brownRoad and visited[x-1][y]==False):
                        ws2, wb2, wbb2, bs2, bb2, bbw2 = road_Score(player, visited, x-1, y, BoardState)
                    elif(0<=y+1<=4 and BoardState[x][y+1] in brownRoad and visited[x][y+1]==False):
                        ws2, wb2, wbb2, bs2, bb2, bbw2 = road_Score(player, visited, x, y+1, BoardState)
                    elif(0<=y-1<=4 and BoardState[x][y-1] in brownRoad and visited[x][y-1]==False):
                        ws2, wb2, wbb2, bs2, bb2, bbw2 = road_Score(player, visited, x, y-1, BoardState)



        return whiteStraight+ws2, whiteBranch+wb2, whiteBlockedBrown+wbb2, brownStraight+bs2, brownBranch+bb2, brownBlockedWhite+bbw2


def checkAdj(x,y,color, visited, BoardState):


    road=0
    cap=2

    if(color=="brown"):
        road+=3
        cap+=3

    if(x>0 and (BoardState[x-1][y]==road or BoardState[x-1][y]==cap)and(visited[x-1][y]==False)):
        above=True
    else:
        above=False
    if(x<4 and (BoardState[x+1][y]==road or BoardState[x+1][y]==cap)and(visited[x+1][y]==False)):
        below=True
    else:
        below=False
    if(y>0 and (BoardState[x][y-1]==road or BoardState[x][y-1]==cap)and(visited[x][y-1]==False)):
        left=True
    else:
        left=False
    if(y<4 and (BoardState[x][y+1]==road or BoardState[x][y+1]==cap)and(visited[x][y+1]==False)):
        right=True
    else:
        right=False

    return above or below or left or right