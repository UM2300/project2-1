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
                    whitescore=whitescore+1
                elif BoardState[x][y] > 2:
                    brownscore=brownscore+1

        
        if player=="white":
            whitescore=whitescore*-1
        else:
            brownscore=brownscore*-1
                
        return whitescore+brownscore


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
        

