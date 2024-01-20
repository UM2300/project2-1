import numpy as np
from keras.models import load_model

from mainPython import QNetwork

import custEnv

from gym import spaces


def load_trained_model(model_path):

    return load_model(model_path, custom_objects={'QNetwork': QNetwork.from_config})

def convert_game_state(game_state_text):
    
    game_state_array = np.array([int(x) for x in game_state_text.split() if x.isdigit()])
    return game_state_array.reshape(1, -1)  

def predict_next_move(model, game_state):

    return model.predict(game_state)

def main(model_path, game_state_file):


    model = load_trained_model(model_path)

    with open(game_state_file, 'r') as file:
        game_state_text = file.read()

    game_state = convert_game_state(game_state_text)

    predicted_move = predict_next_move(model, game_state)
    print("Predicted Move:", predicted_move)

    env = custEnv.TakEnv()

    next_state, reward, done, _ = env.step(predicted_move, "brown")
    state=next_state
    env.print_game_state()


if __name__ == "__main__":
    # Paths can be adjusted as needed
    model_path = 'core\\python\\agentBrown_model.keras'
    game_state_file = 'core\\src\\com\\mygdx\\game\\Agents\\GameState.txt'
    main(model_path, game_state_file)


