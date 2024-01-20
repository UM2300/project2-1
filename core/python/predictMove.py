import numpy as np
from keras.models import load_model


def load_trained_model(model_path):
    """
    Load the trained neural network model from the specified path.
    """
    return load_model(model_path)

def convert_game_state(game_state_text):
    """
    Convert the text-based game state into a NumPy array suitable for the neural network.
    """
    # Example conversion logic; this needs to be adapted to your specific case
    game_state_array = np.array([int(x) for x in game_state_text.split() if x.isdigit()])
    return game_state_array.reshape(1, -1)  # Reshape as needed for the model

def predict_next_move(model, game_state):
    """
    Predict the next move using the loaded model and the provided game state.
    """
    return model.predict(game_state)

def main(model_path, game_state_file):
    """
    Main method to load the model, read game state from a file, and predict the next move.
    """
    # Load the model
    model = load_trained_model(model_path)

    # Read game state from file
    with open(game_state_file, 'r') as file:
        game_state_text = file.read()

    # Convert and reshape the game state
    game_state = convert_game_state(game_state_text)

    # Predict the next move
    predicted_move = predict_next_move(model, game_state)
    print("Predicted Move:", predicted_move)


if __name__ == "__main__":
    # Paths can be adjusted as needed
    model_path = 'core/python/agentBrown_model.h5'
    game_state_file = 'com/mygdx/game/Agents/GameState.txt'
    main(model_path, game_state_file)


