import numpy as np
import tensorflow as tf
import time
import keras

from keras.layers import Dense
from keras.models import Sequential
from keras.optimizers import Adam
from collections import deque
import random

import custEnv
from gym import spaces


class QNetwork(Sequential):
    def __init__(self, state_size, num_actions):
        super(QNetwork, self).__init__()
        self.add(Dense(64, activation='relu', input_dim=state_size))
        self.add(Dense(num_actions, activation='linear'))
        self.compile(optimizer=Adam(lr=0.001), loss='mean_squared_error')


class DQNAgent:
    def __init__(self, state_size, action_size):
        self.state_size = state_size
        self.action_size = action_size
        self.memory = deque(maxlen=2000)
        self.gamma = 0.95  # discount factor
        self.epsilon = 1.0  # exploration-exploitation trade-off
        self.epsilon_decay = 0.995
        self.epsilon_min = 0.01
        self.model = QNetwork(state_size, action_size)

    def remember(self, state, action, reward, next_state, done):
        self.memory.append((state, action, reward, next_state, done))

    def act(self, state, player):
        attempt_counter = 0
        max_attempts = 300

        if np.random.rand() <= self.epsilon:
            chosenAction = np.random.choice(self.action_size)

            while (env.is_action_allowed(player, chosenAction) == False) and attempt_counter < max_attempts:
                attempt_counter += 1
                chosenAction = np.random.choice(self.action_size)

            if attempt_counter >= max_attempts:
                    # Handle no-action-available scenario
                return np.random.choice(self.action_size)  # or appropriate action/signaling

            return chosenAction

        else:
            act_values = self.model.predict(state.reshape(1, -1))
            chosenAction = np.argmax(act_values[0])

            if (env.is_action_allowed(player, chosenAction) == True):
                return chosenAction
            else:
                for next_action in np.argsort(act_values[0])[::-1]:
                    if env.is_action_allowed(current_player, next_action):
                        return next_action
                    

    def actFinal(self, state, player):

        act_values = self.model.predict(state.reshape(1, -1))
        chosenAction = np.argmax(act_values[0])

        if (env.is_action_allowed(player, chosenAction) == True):
            return chosenAction
        else:
            for next_action in np.argsort(act_values[0])[::-1]:
                if env.is_action_allowed(current_player, next_action):
                    return next_action

    def replay(self, batch_size):
        minibatch = random.sample(self.memory, batch_size)
        for state, action, reward, next_state, done in minibatch:
            target = reward
            if not done:
                target = (reward + self.gamma * np.amax(self.model.predict(next_state)[0]))
            target_f = self.model.predict(state)
            target_f[0][action] = target
            self.model.fit(state, target_f, epochs=1, verbose=0, batch_size=1)
        if self.epsilon > self.epsilon_min:
            self.epsilon *= self.epsilon_decay


    def train(self, batch_size=32):
        if len(self.memory) < batch_size:
            return

        minibatch = random.sample(self.memory, batch_size)

        states, targets = [], []
        for state, action, reward, next_state, done in minibatch:
            # Reshape state and next_state to be 2-dimensional
            state_reshaped = np.reshape(state, [1, -1])
            next_state_reshaped = np.reshape(next_state, [1, -1])

            target = reward
            if not done:
                target = (reward + self.gamma * np.amax(self.model.predict(next_state.reshape(1, -1))[0]))

            target_f = self.model.predict(state_reshaped)
            target_f[0][action] = target

            states.append(state.flatten())
            targets.append(target_f.flatten())

        self.model.fit(np.array(states), np.array(targets), epochs=1, verbose=0, batch_size=batch_size)

        if self.epsilon > self.epsilon_min:
            self.epsilon *= self.epsilon_decay


    def chooseMove(self, state):

        env.readBoard()
        state_reshaped = np.reshape(state, [1, -1])
        action = agentBrown.actFinal(state_reshaped, "brown")
        next_state, reward, done, _ = env.step(action, "brown")
        state=next_state
        env.print_game_state()


env = custEnv.TakEnv()
state_size = env.observation_space.shape[0]

action_size = env.action_space[0].n if isinstance(env.action_space, spaces.Tuple) else env.action_space.n

agentWhite = DQNAgent(state_size, action_size)

agentBrown = DQNAgent(state_size, action_size)

current_player = "white"

num_episodes = 2

for episode in range(num_episodes):
    start_time = time.time()
    state = env.reset()
    state = np.reshape(state, [1, state_size])

    while not env.is_game_over():
        # Choose action based on the current player
        if current_player == "white":
            action = agentWhite.act(state, current_player)
        else:
            action = agentBrown.act(state, current_player)

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

    end_time = time.time()  # End the timer
    duration = end_time - start_time  # Calculate the duration
    print(f"Episode {episode + 1} completed in {duration:.2f} seconds")  # Print the duration for each episode

agentBrown.chooseMove(state)
