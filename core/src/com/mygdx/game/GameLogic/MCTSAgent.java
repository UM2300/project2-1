package com.mygdx.game.GameLogic;

import com.mygdx.game.GUI.Baseline_Agent;
import com.mygdx.game.GUI.EvalFunc;
import java.util.ArrayList;

/**
 * MCTSAgent class implements the Monte Carlo Tree Search (MCTS) algorithm for decision-making in games.
 * It uses an evaluation function to guide the tree search process and integrates a Baseline_Agent for random move generation during simulations.
 */
public class MCTSAgent {
    private final int MAX_ITERATIONS = 10000; // Maximum number of iterations for the MCTS algorithm

    /**
     * Finds the next best move based on the current state of the game board.
     * It performs a Monte Carlo Tree Search by iterating through a series of steps: selection, expansion, simulation, and backpropagation.
     *
     * @param currentBoard The current state of the game board.
     * @return The best move as determined by the MCTS algorithm.
     */
    public MCTSNode findNextMove(board currentBoard) {
        MCTSNode rootNode = new MCTSNode(currentBoard); // Initialize the root node of the MCTS tree
        rootNode.setParent(null);

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            MCTSNode promisingNode = selectPromisingNode(rootNode); // Select the most promising node
            if (!promisingNode.getGameState().isGameEnded()) {
                expandNode(promisingNode, currentBoard);  // Expand the selected node, (artur added currentBoard)
            }

            MCTSNode nodeToExplore = promisingNode;
            if (!promisingNode.getChildren().isEmpty()) {
                nodeToExplore = promisingNode.getRandomChildNode();  // Select a random child node for exploration
            }

            int playoutResult = simulateRandomPlayout(nodeToExplore);  // Simulate a random playout from the selected node
            backPropagate(nodeToExplore, playoutResult);  // Update the tree based on the simulation result
        }

        return rootNode.getChildWithMaxScore(); // Return the child of the root node with the highest score
    }


    /**
     * Selects the most promising node from the tree starting at the given root node.
     * The selection is based on the evaluation function scores of the nodes.
     *
     * @param rootNode The root node of the tree from which to start the selection process.
     * @return The most promising node as determined by the evaluation function.
     */
    private MCTSNode selectPromisingNode(MCTSNode rootNode) {
        MCTSNode node = rootNode;
        if (node.getChildren().size() != 0) {
            do {
                node = findBestNodeWithEvalFunc(node);
            } while (node.getChildren().size() != 0);
        }
        return node;
    }


    /**
     * Finds the best node among the children of the given node based on the evaluation function.
     * The node with the highest evaluation score is considered the best.
     *
     * @param node The node whose children are to be evaluated.
     * @return The child node with the highest evaluation score.
     */
    private MCTSNode findBestNodeWithEvalFunc(MCTSNode node) {
        int maxScore = Integer.MIN_VALUE;
        MCTSNode bestNode = null;
        ArrayList<MCTSNode> children = node.getChildren();
        for (int i = 0; i < children.size(); i++) {
            MCTSNode child = children.get(i);
            int evalScore = new EvalFunc().evaluation(child.getGameState());
            if (evalScore > maxScore) {
                maxScore = evalScore;
                bestNode = child;
            }
        }
        return bestNode;
    }

    private void expandNode(MCTSNode node, board currentBoard) {

        ArrayList<board> legalMoves = new ArrayList<>();

        Baseline_Agent baselineAgent = new Baseline_Agent(currentBoard);

        board currentState = node.getGameState();
        String currentPlayer = currentState.getCurrentPlayer();

        while (legalMoves.size() < MAX_ITERATIONS) {

            baselineAgent.chooseMove(currentState, currentPlayer);

            // Check if the move is not a repetition
            if (!containsBoard(legalMoves, currentState)) {
                legalMoves.add(currentState);
            }
        }

        for (board nextState : legalMoves) {
            MCTSNode newNode = new MCTSNode(nextState);
            newNode.setParent(node);
            node.addChild(newNode);
        }
    }

    private boolean containsBoard(ArrayList<board> boards, board nextState) {
        for (board existingBoard : boards) {
            if (existingBoard.equals(nextState)) {
                return true;
            }
        }
        return false;
    }




    private int simulateRandomPlayout(MCTSNode node) {
return 0;
    }

    private void backPropagate(MCTSNode node, int gameResult) {

    }
}
