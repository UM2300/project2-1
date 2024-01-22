import numpy as np
from keras.models import load_model

import custEnv
from mainPython import QNetwork


def load_custom_model(model_path):
    """
    Loads a Keras model from a specified path, including custom objects necessary for the model.

    Parameters:
        model_path (str): The filesystem path to the model to be loaded.

    Returns:
        A Keras model instance loaded with the specified custom objects.
    """
    custom_objects = {'QNetwork': QNetwork}
    return load_model(model_path, custom_objects=custom_objects)


def convert_game_state(game_state_text):
    """
    Converts a textual representation of a game state into a numpy array suitable for model prediction.

    Parameters:
        game_state_text (str): A space-separated string representation of game state integers.

    Returns:
        numpy.array: A reshaped array representing the game state, ready for input into the model.
    """

    game_state_array = np.array([int(x) for x in game_state_text.split()])
    return game_state_array.reshape(1, -1)


def predict_next_move(model, game_state):
    """
    Uses the loaded model to predict the next move given a game state.

    Parameters:
        model: The trained Keras model for predicting the next move.
        game_state (numpy.array): The current game state as input for the model.

    Returns:
        The predicted move as determined by the model.
    """

    return model.predict(game_state)


def main(model_path, game_state_file):
    """
    Main function to load the model, read the game state, and determine the next move.

    Parameters:
        model_path (str): Path to the trained model file.
        game_state_file (str): Path to the file containing the current game state.
    """

    model = load_custom_model(model_path)

    with open(game_state_file, 'r') as file:
        game_state_text = file.read()

    game_state = convert_game_state(game_state_text)

    env = custEnv.TakEnv()

    env.readBoard()

    capstoneAvailable = env.checkBrownCap()

    action = actFinal(game_state, "brown", env, "brown", model, capstoneAvailable)

    next_state, reward, done, _ = env.step(action, "brown")
    state = next_state
    env.print_game_state()
    print("done")


def actFinal(state, player, env, current_player, model, capstone):
    act_values = model.predict(state.reshape(1, -1))
    chosenAction = np.argmax(act_values[0])

    if (capstone == True and env.is_action_allowed(player, chosenAction) == True):
        return chosenAction
    elif (capstone == False and env.is_action_allowed_no_cap(player, chosenAction) == True):
        return chosenAction
    else:
        for next_action in np.argsort(act_values[0])[::-1]:
            if env.is_action_allowed(current_player, next_action):
                return next_action


if __name__ == "__main__":
    model_path = 'core\\python\\agentBrown_model.h5'
    game_state_file = 'core\\src\\com\\mygdx\\game\\Agents\\GameState.txt'
    main(model_path, game_state_file)
