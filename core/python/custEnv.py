import gym
from gym import spaces
import numpy as np
import actionConv
import rewardHelpers

class TakEnv(gym.Env):
    def __init__(self, board_size=25):
        super(TakEnv, self).__init__()

        # Define the action and observation space
        self.action_space = spaces.Discrete(board_size * 3 + 9*4 + 12*3 + 4*2)

        self.observation_space = spaces.Box(low=-1, high=5, shape=(25,), dtype=np.int32)  # Specify the state space

        self.board_size=board_size
        self.state=np.full(board_size, -1, dtype=np.int32)
        self.forbidden_actions=[]
        # Keep track of the number of capstones placed for each player
        self.white_capstone_placed = False
        self.brown_capstone_placed = False

    def reset(self):
        self.state = np.full(self.board_size, -1, dtype=np.int32)
        self.white_capstone_placed = False
        self.brown_capstone_placed = False
        return self.state.copy()

    def is_action_allowed(self, player, action):
        action_type, pieceType, place1, place2 = actionConv.conversion(action, player)

        result = True

        

        

        if action_type == 0:
            if self.state[place1] != -1:  # Check if the target tile is already occupied
                result = False

            
            elif pieceType == 2 and self.white_capstone_placed:  # Check if white's capstone is already placed
                result = False
            elif pieceType == 5 and self.brown_capstone_placed:  # Check if brown's capstone is already placed
                result = False
            else:
                result = True
        elif action_type == 1:
            if self.state[place1] == -1:
                result = False
            elif self.state[place2] == 2 or self.state[place2] == 5:
                result = False
            elif (player == "white" and self.state[place1] > 2) or (player == "brown" and self.state[place1] < 3):
                result = False

        if result:
            # If the action is placing a capstone, update the respective flag
            if pieceType == 2 and player == "white":
                self.white_capstone_used = True
            elif pieceType == 5 and player == "brown":
                self.brown_capstone_used = True
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

        if pieceType == 2:
            self.white_capstone_placed = True
        elif pieceType == 5:
            self.brown_capstone_placed = True

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

                    BoardState=self.state.reshape((5,5))

                    #ws, wb, wbb, bs, bb, bbw = rewardHelpers.road_Score(player, visited, x, y, BoardState)

                    ws, wb, wbb, bs, bb, bbw = rewardHelpers.road_Score(player, visited, x, y, BoardState)


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


    def is_game_over(self):
        print("checking")
        BoardState = self.state.reshape((5, 5))
    # Check if the board is full
        if np.all(BoardState != -1):
            print("board is full")
            self.print_game_state()
            return True
    # Check for road creation
        if self.check_road_creation():
            print("A road was created")
            self.print_game_state()
            return True
        return False


    def check_road_creation(self):
        BoardState = self.state.reshape((5, 5))
        for player_pieces in [[0, 2], [3, 5]]:  # Including stones and capstones for white and brown
            if self.has_player_formed_road(BoardState, player_pieces):
                return True
        return False

    def has_player_formed_road(self, board, player_pieces):
        for row in range(5):
            if self.is_continuous_path(board, (row, 0), player_pieces, 'horizontal') or \
                    self.is_continuous_path(board, (0, row), player_pieces, 'vertical'):
                return True
        return False

    def is_continuous_path(self, board, start, player_pieces, direction):
        visited = set()
        return self.dfs(board, start, player_pieces, direction, visited)


    def dfs(self, board, position, player_pieces, direction, visited):
        x, y = position
         # Check if position is out of bounds or already visited
        if x < 0 or x >= 5 or y < 0 or y >= 5 or (x, y) in visited or board[x][y] not in player_pieces:
            return False

         # Mark the current position as visited
        visited.add((x, y))

    # Check if the edge of the board is reached in the given direction
        if (direction == 'horizontal' and y == 4) or (direction == 'vertical' and x == 4):
            return True

    # Explore adjacent cells based on the direction
        if direction == 'horizontal':
        # Check right, above, and below
            return self.dfs(board, (x, y + 1), player_pieces, direction, visited) or \
                self.dfs(board, (x + 1, y), player_pieces, direction, visited) or \
                self.dfs(board, (x - 1, y), player_pieces, direction, visited)
        elif direction == 'vertical':
        # Check below, right, and left
            return self.dfs(board, (x + 1, y), player_pieces, direction, visited) or \
                self.dfs(board, (x, y + 1), player_pieces, direction, visited) or \
                self.dfs(board, (x, y - 1), player_pieces, direction, visited)

    def print_game_state(self):
        # Reshape the state into a 5x5 matrix and print it
        board_size = int(np.sqrt(self.board_size))
        board_matrix = self.state.reshape((board_size, board_size))
        print("\nGame Board State:")
        print(board_matrix)


    def checkAdj(self,x,y,color, visited):

        BoardState=self.state.reshape((5,5))

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
    
   




