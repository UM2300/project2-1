package com.mygdx.game.Agents;

import com.mygdx.game.GameLogic.board;

import java.util.ArrayList;

public class HelperFunctions {
    /**
     * Checks whether two 2D arrays of ArrayLists representing game boards are equal.
     * Equality is determined based on the dimensions and the content of the ArrayLists in corresponding positions.
     *
     * @param board1 The first 2D array representing a game board.
     * @param board2 The second 2D array representing another game board for comparison.
     * @return true if the dimensions and content of corresponding ArrayLists in both boards are equal, false otherwise.
     */

    public static boolean boardsAreEqual(ArrayList<Integer>[][] board1, ArrayList<Integer>[][] board2) {

        if (board1.length != board2.length || board1[0].length != board2[0].length) {
            return false;
        }

        for (int i = 0; i < board1.length; i++) {
            for (int j = 0; j < board1[0].length; j++) {
                if (!board1[i][j].equals(board2[i][j])) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks whether a given game state (represented by a board object) is already present in a list of existing game states.
     *
     * @param boards    The list of existing game states to check against.
     * @param nextState The game state to check for existence in the list.
     * @return true if the given game state is found in the list, false otherwise.
     */
    // Complexity: O(n)
    // n is the number of elements in the boards list
    static boolean containsBoard(ArrayList<board> boards, board nextState) {
        for (board existingBoard : boards) {
            if (existingBoard.equals(nextState)) {
                return true;
            }
        }
        return false;
    }
}
