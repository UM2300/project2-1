import numpy as np
from keras.models import load_model

from mainPython import QNetwork

import custEnv

from gym import spaces


def load_custom_model(model_path):
    custom_objects = {'QNetwork': QNetwork}
    return load_model(model_path, custom_objects=custom_objects)

def convert_game_state(game_state_text):
    
    game_state_array = np.array([int(x) for x in game_state_text.split()])
    return game_state_array.reshape(1, -1)  

def predict_next_move(model, game_state):

    return model.predict(game_state)

def main(model_path, game_state_file):


    model = load_custom_model(model_path)

    with open(game_state_file, 'r') as file:
        game_state_text = file.read()

    game_state = convert_game_state(game_state_text)

    #predicted_move = predict_next_move(model, game_state)
    #print("Predicted Move:", predicted_move)

    env = custEnv.TakEnv()

    env.readBoard()

    capstoneAvailable = env.checkBrownCap()

    action = actFinal(game_state, "brown", env, "brown", model, capstoneAvailable)

    

    next_state, reward, done, _ = env.step(action, "brown")
    state=next_state
    env.print_game_state()
    print("done")


def actFinal(state, player, env, current_player, model, capstone):

        act_values = model.predict(state.reshape(1, -1))
        chosenAction = np.argmax(act_values[0])

        if (capstone==True and env.is_action_allowed(player, chosenAction) == True):
            return chosenAction
        elif (capstone==False and env.is_action_allowed_no_cap(player, chosenAction) == True):
            return chosenAction
        else:
            for next_action in np.argsort(act_values[0])[::-1]:
                if capstone==True and env.is_action_allowed(current_player, next_action):
                    return next_action
                elif capstone==False and env.is_action_allowed_no_cap(current_player, next_action):
                    return next_action


if __name__ == "__main__":
    # Paths can be adjusted as needed
    model_path = 'core\\python\\agentBrown_model.h5'
    game_state_file = 'core\\src\\com\\mygdx\\game\\Agents\\GameState.txt'
    main(model_path, game_state_file)


