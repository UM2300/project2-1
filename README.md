# project2-1

README

-> First of all run "pip install -r requirements.txt" in IDE terminal.
-> Run TakGame2D to test the second version of the game.
    project2-1\core\src\com\mygdx\game\GUI\TakGame2D.java

A 2D version of a human vs human, human vs agent playable board game of Tak - Group 02
Requirements: Installation of Java and Python on laptop


This repo is split into: 
- GUI
- GameLogic
- Agents
- seperate png files for images
- README


--- In order to run the GAME (GUI): ---

Download the .zip or clone this repository to Desktop by using the command below.

git clone <https://github.com/UM2300/project2-1>

If .zip is downloaded extract the folder into an easily accesible directory.


Follow option A to run using the terminal/cmd and option B to run using an IDE or code compiler

Option A:

1. Using terminal/cmd navigate to the directory where the repository is

2. When in the folder for the project write the following in the terminal
    - javac ./GUI/TakGame2D.java

3. Followed by
    - java GUI.TakGame2D

4. You will now have a starting screen showing "Tak" with the start button and the game instruction button

5. After pressing start, another window is displayed with the types of game modes

6. Option 1 is Multiplayer, which is basically your human player vs another human player

7. Option 2 is Easy Mode, where you can play against our baseline agent

8. Option 3 is Hard Mode, where you can play against our more advanced MCTS agent
   *** PLEASE NOTE: In this game mode, it should take 10 seconds before your piece is placed onto the board, this is because MCTS is given 10 seconds to "think" before making a move.

9. On each game mode you have a menu button where you can choose to exit game (exit), return to main menu (restart), or continue (continue)

10. Once a player/agent has met a winning condition, the game is over and an end screen is displayed.



Option B:


1. Open the folder using your preferred IDE or code compiler

2. In order to run the space simulation select:
- GUI
    - TakGame2D.java

3. You will now have a starting screen showing "Tak" with the start button and the game instruction button

4. After pressing start, another window is displayed with the types of game modes

5. Option 1 is Multiplayer, which is baisically your human player vs another human player

6. Option 2 is Easy Mode, where you can play against our baseline agent

7. Option 3 is Hard Mode, where you can play against our more advanced MCTS agent

8. On each game mode you have a menu button where you can choose to exit game (exit), return to main menu (restart), or continue (continue)

9. Once a player/agent has met a winning condition, the game is over and an end screen is displayed.


--- In order to run EXPERIMENTS of the different agents playing against each other:---

Follow the instructions given above regarding opening the project.

1. For Baseline vs MCTS, run the "Baseline_VS_MCTS.java" file.

2. For MCTS vs Hybrid, run the "MCTS_VS_Hybrid.java" file.

3. For Hybrid vs Baseline, run the "Hybrid_VS_Baseline.java" file.

Enjoy!
