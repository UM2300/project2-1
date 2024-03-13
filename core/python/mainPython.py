import random
from collections import deque

import numpy as np
from keras.layers import Dense
from keras.models import Sequential
from keras.optimizers import Adam


class QNetwork(Sequential):
    def __init__(self, state_size, num_actions):
        super(QNetwork, self).__init__()
        self.add(Dense(64, activation='relu', input_dim=state_size))
        self.add(Dense(num_actions, activation='linear')
        self.compile(optimizer=Adam(lr=0.001), loss='mean_squared_error')

    @classmethod
    def from_config(cls, config):
        state_size = config['state_size'] if 'state_size' in config else 25
        num_actions = config['num_actions'] if 'num_actions' in config else 155
        return cls(state_size=state_size, num_actions=num_actions)


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
        """
        Stores an experience tuple in the agent's memory for experience replay.

        Parameters:
            state (np.array): The observed state.
            action (int): The action taken.
            reward (float): The reward received.
            next_state (np.array): The next observed state.
            done (bool): Whether the episode has ended.
        """
        self.memory.append((state, action, reward, next_state, done))

    def act(self, state, player, env, current_player):
        """
        Decides an action based on the current state using an epsilon-greedy policy.

        Parameters:
            state (np.array): The current state observed by the agent.
            player (str): The player making the decision.
            env: The environment in which the agent is acting.
            current_player (str): The current player from the perspective of the environment.

        Returns:
            int: The chosen action.
        """
        attempt_counter = 0
        max_attempts = 300

        if np.random.rand() <= self.epsilon:
            chosenAction = np.random.choice(self.action_size)

            while (env.is_action_allowed(player, chosenAction) == False) and attempt_counter < max_attempts:
                attempt_counter += 1
                chosenAction = np.random.choice(self.action_size)

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

    def actFinal(self, state, player, env, current_player):

        act_values = self.model.predict(state.reshape(1, -1))
        chosenAction = np.argmax(act_values[0])

        if (env.is_action_allowed(player, chosenAction) == True):
            return chosenAction
        else:
            for next_action in np.argsort(act_values[0])[::-1]:
                if env.is_action_allowed(current_player, next_action):
                    return next_action

    def replay(self, batch_size):
        """
        Trains the network using a minibatch from the agent's memory.

        Parameters:
            batch_size (int): The size of the minibatch to use for training.
        """
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
        """
        Performs a training step for the agent, sampling a minibatch and updating the network.

        Parameters:
            batch_size (int): The size of the minibatch to train on.
        """
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
