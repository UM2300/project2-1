import gym
from gym import spaces
import numpy as np
import actionConv

class TakEnv(gym.Env):
    def __init__(self, board_size=25):
        super(TakEnv, self).__init__()

        # Define the action and observation space
        self.action_space = spaces.Discrete(board_size * 3 + 9*4 + 12*3 + 4*2)

        self.observation_space = spaces.Box(low=-1, high=5, shape=(25,), dtype=np.int32)  # Specify the state space

        self.board_size=board_size
        self.state=np.full(board_size, -1, dtype=np.int32)
        self.forbidden_actions=[]

    def reset(self):
        self.state = np.full(self.board_size, -1, dtype=np.int32)
        return self.state.copy()
    
    def is_action_allowed(self, player, action):

        action_type, pieceType, place1, place2 = actionConv.conversion(action, player)

        result = True
        
        if(action_type==0):
            if self.state[place1]!=-1:
                result = False
            else:
                result = True
            
        elif(action_type==1):
            if self.state[place1]==-1:
                result = False
            elif self.state[place2]==2 or self.state[place2]==5:
                result = False
            elif (player=="white" and self.state[place1] > 2):
                result = False
            elif (player=="brown" and self.state[place1] < 3):
                result = False
            else: 
                result = True

        if result==True:
            return True
        else:
            self.add_forbidden_action(action)
            return False
            
    def get_all_actions(self):
        return list(range(self.action_space.n))
    
    def get_forbidden_actions(self):
        return self.forbidden_actions
            
    def add_forbidden_action(self, action):
        self.forbidden_actions.append(action)


    def get_allowed_actions(self, player):
        # Get a list of all possible actions
        all_actions = self.get_all_actions()

        # Get a list of forbidden actions based on the current state and player
        forbidden_actions = self.get_forbidden_actions()

        # Return the list of allowed actions
        allowed_actions = [action for action in all_actions if action not in forbidden_actions]

        return allowed_actions


    def step(self, action, player):
        # Execute the given action and update the state
        # Return the next state, reward, whether the episode is done, and additional info

        action_type, pieceType, place1, place2 = actionConv.conversion(action, player)


        if action_type==0:
            if 0<= place1 < self.board_size:
                self.state[place1]=pieceType
        elif action_type==1:
            self.state[place2]=self.state[place1]
            self.state[place1]=-1


        reward = self.calculate_reward(player)
        done = self.is_game_over()

        print(action_type)

        return self.state, reward, done, {}


    def calculate_reward(self, player):
        # Implement your reward calculation logic
        # Return a scalar value representing the reward

        whitescore=0
        brownscore=0

        BoardState=self.state.reshape((5,5))

        for x in range(5):
            for y in range(5):

                if BoardState[x][y] >=0 and BoardState[x][y] <= 2:

                    if(BoardState[x][y]==0):
                        whitescore=whitescore+2
                    elif(BoardState[x][y]==1):
                        whitescore=whitescore+1
                    elif(BoardState[x][y]==2):
                        whitescore=whitescore+3

                elif BoardState[x][y] > 2:

                    if(BoardState[x][y]==2):
                        brownscore=brownscore+2
                    elif(BoardState[x][y]==4):
                        brownscore=brownscore+1
                    elif(BoardState[x][y]==5):
                        brownscore=brownscore+2


        for x in range(5):
            for y in range(5):

                visited = [[False] * 5 for _ in range(5)]

                if(x==0 or x==4 or y==0 or y==4):
                    ws, wb, wbb, bs, bb, bbw = self.road_Score(player, visited, x, y)

                    ws*=3
                    wb*=2
                    wbb*=10
                    bs*=3
                    bb*=2
                    bbw*=10

                    whitescore=whitescore+ws+wb+wbb
                    brownscore=brownscore+bs+bb+bbw

                

        
        if player=="brown":
            whitescore=whitescore*-1
        else:
            brownscore=brownscore*-1
                
        return whitescore+brownscore
    

    def checkAdj(self,x,y,color, visited):

        BoardState=self.state.reshape((5,5))

        road=0
        cap=2

        if(color=="brown"):
            road+=3
            cap+=3

        if(x>0 and (BoardState[x-1][y]==road or BoardState[x-1][y]==cap)and(visited[x-1][y]==False)):
            above=True
        elif(x<4 and (BoardState[x+1][y]==road or BoardState[x+1][y]==cap)and(visited[x+1][y]==False)):
            below=True
        elif(y>0 and (BoardState[x][y-1]==road or BoardState[x][y-1]==cap)and(visited[x][y-1]==False)):
            left=True
        elif(y<4 and (BoardState[x][y+1]==road or BoardState[x][y+1]==cap)and(visited[x][y+1]==False)):
            right=True
            
        return above or below or left or right



    

    def is_game_over(self):

        print("checking")
        BoardState=self.state.reshape((5,5))

        pieceCount=0

        for x in range(5):
            for y in range(5):

                if(BoardState[x][y]==-1):
                    print("maybe")
                else:
                    pieceCount=pieceCount+1

        print("ended")

        if pieceCount>=10:
            return True
        else:
            return False
        


    def road_Score(self, player, visited, x, y):

        BoardState=self.state.reshape((5,5))

        whiteStraight=0
        whiteBranch=0
        whiteBlockedBrown=0

        brownStraight=0
        borwnBranch=0
        brownBlockedWhite=0

        prevRoadChords=[]

        if(x==0):

            if(BoardState[x][y]==0 or BoardState[x][y]==2):
                whiteStraight += 1

                while (self.checkAdj(x,y,player,visited) or x!=4):
                    visited[x][y]=True
                    prevRoadChords.append((x,y))

                    if(BoardState[x+1][y]==0 or BoardState[x+1][y]==2):
                        x=x+1
                        whiteStraight += 1
                    elif(y<4 and (BoardState[x][y+1]==0 or BoardState[x][y+1]==2)):
                        y=y+1
                        whiteBranch+=1
                    elif(y>0 and (BoardState[x][y-1]==0 or BoardState[x][y-1]==2)):
                        y=y+1
                        whiteBranch+=1
                    elif(BoardState[x+1][y]==4):
                        brownBlockedWhite+=1

            elif(BoardState[x][y]==3 or BoardState[x][y]==5):
                brownStraight += 1

                while (self.checkAdj(x,y,player,visited) or x!=4):
                    visited[x][y]=True
                    prevRoadChords.append((x,y))

                    if(BoardState[x+1][y]==3 or BoardState[x+1][y]==5):
                        x=x+1
                        brownStraight += 1
                    elif(y<4 and (BoardState[x][y+1]==3 or BoardState[x][y+1]==5)):
                        y=y+1
                        borwnBranch+=1
                    elif(y>0 and (BoardState[x][y-1]==3 or BoardState[x][y-1]==5)):
                        y=y+1
                        borwnBranch+=1
                    elif(BoardState[x+1][y]==1):
                        whiteBlockedBrown+=1

        elif(x==4):

            if(BoardState[x][y]==0 or BoardState[x][y]==2):
                whiteStraight += 1

                while (self.checkAdj(x,y,player,visited) or x!=0):
                    visited[x][y]=True
                    prevRoadChords.append((x,y))

                    if(BoardState[x-1][y]==0 or BoardState[x-1][y]==2):
                        x=x-1
                        whiteStraight += 1
                    elif(y<4 and (BoardState[x][y+1]==0 or BoardState[x][y+1]==2)):
                        y=y+1
                        whiteBranch+=1
                    elif(y>0 and (BoardState[x][y-1]==0 or BoardState[x][y-1]==2)):
                        y=y+1
                        whiteBranch+=1
                    elif(BoardState[x-1][y]==4):
                        brownBlockedWhite+=1

            elif(BoardState[x][y]==3 or BoardState[x][y]==5):
                brownStraight += 1

                while (self.checkAdj(x,y,player,visited) or x!=0):
                    visited[x][y]=True
                    prevRoadChords.append((x,y))

                    if(BoardState[x-1][y]==3 or BoardState[x-1][y]==5):
                        x=x-1
                        brownStraight += 1
                    elif(y<4 and (BoardState[x][y+1]==3 or BoardState[x][y+1]==5)):
                        y=y+1
                        borwnBranch+=1
                    elif(y>0 and (BoardState[x][y-1]==3 or BoardState[x][y-1]==5)):
                        y=y+1
                        borwnBranch+=1
                    elif(BoardState[x-1][y]==1):
                        whiteBlockedBrown+=1

        elif(y==0):

            if(BoardState[x][y]==0 or BoardState[x][y]==2):
                whiteStraight += 1

                while (self.checkAdj(x,y,player,visited) or y!=4):
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

                while (self.checkAdj(x,y,player,visited) or x!=0):
                    visited[x][y]=True
                    prevRoadChords.append((x,y))

                    if(BoardState[x][y+1]==3 or BoardState[x][y+1]==5):
                        y=y+1
                        brownStraight += 1
                    elif(x<4 and (BoardState[x+1][y]==3 or BoardState[x+1][y]==5)):
                        x=x+1
                        borwnBranch+=1
                    elif(x>0 and (BoardState[x-1][y]==3 or BoardState[x-1][y]==5)):
                        x=x+1
                        borwnBranch+=1
                    elif(BoardState[x][y+1]==1):
                        whiteBlockedBrown+=1

        elif(y==4):

            if(BoardState[x][y]==0 or BoardState[x][y]==2):
                whiteStraight += 1

                while (self.checkAdj(x,y,player,visited) or y!=0):
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

                while (self.checkAdj(x,y,player,visited) or x!=0):
                    visited[x][y]=True
                    prevRoadChords.append((x,y))

                    if(BoardState[x][y-1]==3 or BoardState[x][y-1]==5):
                        y=y-1
                        brownStraight += 1
                    elif(x<4 and (BoardState[x+1][y]==3 or BoardState[x+1][y]==5)):
                        x=x+1
                        borwnBranch+=1
                    elif(x>0 and (BoardState[x-1][y]==3 or BoardState[x-1][y]==5)):
                        x=x+1
                        borwnBranch+=1
                    elif(BoardState[x][y-1]==1):
                        whiteBlockedBrown+=1



        if(len(whiteStraight>0)):
            for x, y in prevRoadChords:
                oldX=x
                oldY=y

                whiteRoad=[0,2]
                brownRoad=[3,5]

                if(BoardState[oldX][oldY] in whiteRoad):
                
                    if(0<=x+1<=4 and BoardState[x+1][y] in whiteRoad and visited[x+1][y]==False):
                        ws2, wb2, wbb2, bs2, bb2, bbw2 = self.road_Score(player, visited, x+1, y)
                    elif(0<=x-1<=4 and BoardState[x-1][y] in whiteRoad and visited[x-1][y]==False):
                        ws2, wb2, wbb2, bs2, bb2, bbw2 = self.road_Score(player, visited, x-1, y)
                    elif(0<=y+1<=4 and BoardState[x][y+1] in whiteRoad and visited[x][y+1]==False):
                        ws2, wb2, wbb2, bs2, bb2, bbw2 = self.road_Score(player, visited, x, y+1)
                    elif(0<=y-1<=4 and BoardState[x][y-1] in whiteRoad and visited[x][y-1]==False):
                        ws2, wb2, wbb2, bs2, bb2, bbw2 = self.road_Score(player, visited, x, y-1)

                elif(BoardState[oldX][oldY] in brownRoad):
                    if(0<=x+1<=4 and BoardState[x+1][y] in brownRoad and visited[x+1][y]==False):
                        ws2, wb2, wbb2, bs2, bb2, bbw2 = self.road_Score(player, visited, x+1, y)
                    elif(0<=x-1<=4 and BoardState[x-1][y] in brownRoad and visited[x-1][y]==False):
                        ws2, wb2, wbb2, bs2, bb2, bbw2 = self.road_Score(player, visited, x-1, y)
                    elif(0<=y+1<=4 and BoardState[x][y+1] in brownRoad and visited[x][y+1]==False):
                        ws2, wb2, wbb2, bs2, bb2, bbw2 = self.road_Score(player, visited, x, y+1)
                    elif(0<=y-1<=4 and BoardState[x][y-1] in brownRoad and visited[x][y-1]==False):
                        ws2, wb2, wbb2, bs2, bb2, bbw2 = self.road_Score(player, visited, x, y-1)



        return whiteStraight+ws2, whiteBranch+wb2, whiteBlockedBrown+wbb2, brownStraight+bs2, borwnBranch+bb2, brownBlockedWhite+bbw2

        

