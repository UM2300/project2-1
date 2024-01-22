import time

import numpy as np
from gym import spaces

import custEnv
from mainPython import DQNAgent

env = custEnv.TakEnv()
state_size = env.observation_space.shape[0]

action_size = env.action_space[0].n if isinstance(env.action_space, spaces.Tuple) else env.action_space.n

agentWhite = DQNAgent(state_size, action_size)

agentBrown = DQNAgent(state_size, action_size)

current_player = "white"

num_episodes = 300

total_time_taken = 0

for episode in range(num_episodes):
    start_time = time.time()
    state = env.reset()
    state = np.reshape(state, [1, state_size])

    while not env.is_game_over():
        # Choose action based on the current player
        if current_player == "white":
            action = agentWhite.act(state, current_player, env, current_player)
        else:
            action = agentBrown.act(state, current_player, env, current_player)

        # Execute the chosen action
        next_state, reward, done, _ = env.step(action, current_player)

        # Update the agent's knowledge based on the transition
        if current_player == "white":
            agentWhite.remember(state, action, reward, next_state, done)
            agentWhite.train()
        else:
            agentBrown.remember(state, action, reward, next_state, done)
            agentBrown.train()

        # Move to the next player
        if current_player == "white":
            current_player = "brown"
        else:
            current_player = "white"

        # Set the next state as the current state for the next iteration
        state = next_state

    end_time = time.time()
    duration = end_time - start_time
    total_time_taken += duration
    print(f"Episode {episode + 1} completed in {duration:.2f} seconds")

print(f"Total time taken for {num_episodes} episodes: {total_time_taken:.2f} seconds")

agentWhite.model.save('core\\python\\agentBrown_model.h5')
agentBrown.model.save('core\\python\\agentWhite_model.h5')
