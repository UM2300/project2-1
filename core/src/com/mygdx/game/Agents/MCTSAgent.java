package com.mygdx.game.Agents;

import com.mygdx.game.GameLogic.board;

import java.util.ArrayList;

import static com.mygdx.game.Agents.HelperFunctions.boardsAreEqual;
import static com.mygdx.game.Agents.HelperFunctions.containsBoard;

/**
 * MCTSAgent class implements the Monte Carlo Tree Search (MCTS) algorithm for decision-making in games.
 * It uses an evaluation function to guide the tree search process and integrates a Baseline_Agent for random move generation during simulations.
 */
public class MCTSAgent {
    private int maxPiece = 3;

    public void setMaxPiece(int maxPiece) {
        this.maxPiece = maxPiece;
    }

    public int getMaxPiece() {
        return this.maxPiece;
    }

    /**
     * Finds the next best move based on the current state of the game board.
     * It performs a Monte Carlo Tree Search by iterating through a series of steps: selection, expansion, simulation, and backpropagation.
     *
     * @param currentBoard The current state of the game board.
     * @return The best move as determined by the MCTS algorithm.
     */

    /**
     * Complexity: O(bh + m + d + n)
     * bh is from selectPromisingNode
     * m is from simulateRandomPlayout
     * d is from backPropagate
     */
    public MCTSNode findNextMove(board currentBoard) {
        MCTSNode rootNode = new MCTSNode(currentBoard);
        rootNode.setParent(null);

        long startTime = System.currentTimeMillis();
        // Time limit in milliseconds (seconds = TIME_LIMIT/1000)
        long TIME_LIMIT = 10000;
        while (System.currentTimeMillis() - startTime < TIME_LIMIT) {

            MCTSNode promisingNode = selectPromisingNode(rootNode);
            if (!promisingNode.getGameState().isGameEnded()) {
                expandNode(promisingNode, currentBoard);
            }

            MCTSNode nodeToExplore = promisingNode;
            if (!promisingNode.getChildren().isEmpty()) {
                nodeToExplore = promisingNode.getRandomChildNode();
            }

            int playoutResult = simulateRandomPlayout(nodeToExplore);
            backPropagate(nodeToExplore, playoutResult);
        }
        return rootNode.getChildWithMaxScore();
    }


    /**
     * Selects the most promising node from the tree starting at the given root node.
     * The selection is based on the evaluation function scores of the nodes.
     *
     * @param rootNode The root node of the tree from which to start the selection process.
     * @return The most promising node as determined by the evaluation function.
     */

    /**
     * Complexity: O(bh)
     * b = max number of children for any node
     * h = the height of the tree
     */
    private MCTSNode selectPromisingNode(MCTSNode rootNode) {

        MCTSNode node = rootNode;

        int childNum = node.getChildren().size();

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

    /**
     * Complexity: O(bh)
     * b = max number of children for any node
     * h = the height of the tree
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

    /**
     * Expands the given MCTS (Monte Carlo Tree Search) node by generating a set of legal moves and creating child nodes for each move.
     * The expansion process involves simulating potential game states by applying moves to the current game state.
     * The legal moves are generated until a specified limit is reached, and child nodes are created for each unique resulting game state.
     *
     * @param node         The MCTS node to be expanded.
     * @param currentBoard The current game board state represented by the MCTS node.
     */
    // Complexity: O(n)
    // n = the number of expanded nodes
    private void expandNode(MCTSNode node, board currentBoard) {
        ArrayList<board> legalMoves = new ArrayList<>();


        board currentState = node.getGameState();

        while (legalMoves.size() < 10) {
            // Clone the current state before applying the move
            board clonedState = currentState.clone();

            String currentPlayer = clonedState.getCurrentPlayer();

            Baseline_Agent baselineAgent = new Baseline_Agent(clonedState, getMaxPiece());

            boolean test = boardsAreEqual(currentState.getBoard(), clonedState.getBoard());

            while (boardsAreEqual(currentState.getBoard(), clonedState.getBoard())) {
                baselineAgent.chooseMove(clonedState, currentPlayer);
                if (baselineAgent.getPieceMultiplier() != getMaxPiece()) {
                    setMaxPiece(baselineAgent.getPieceMultiplier());
                }
            }


            clonedState.togglePlayer();

            // Check if the move is not a repetition
            if (!containsBoard(legalMoves, clonedState)) {
                legalMoves.add(clonedState);
            }


            for (int i = 0; i < legalMoves.size(); i++) {
                board nextState = legalMoves.get(i);
                MCTSNode newNode = new MCTSNode(nextState);
                newNode.setParent(node);
                newNode.setExpandedNodeScore(new EvalFunc().evaluation(nextState));
                node.addChild(newNode);
            }
        }
    }


    /**
     * Simulates a random playout from the given MCTS (Monte Carlo Tree Search) node until a terminal game state is reached.
     * The simulation involves making random moves using a baseline agent until the game reaches a terminal state.
     * The evaluation of the terminal state is calculated using an evaluation function, and the score is assigned to the node.
     *
     * @param node The MCTS node from which the random playout simulation starts.
     * @return The score of the terminal state reached during the random playout.
     */
    // Complexity: O(m)
    // m is the average number of iterations required to reach a terminal game state
    private int simulateRandomPlayout(MCTSNode node) {
        board clonedBoard = node.getGameState().clone();
        Baseline_Agent baselineAgent = new Baseline_Agent(clonedBoard, maxPiece);
        clonedBoard.togglePlayer();
        String currentPlayer = clonedBoard.getCurrentPlayer();

        // Continue simulation to terminal state
        if (!clonedBoard.isGameEnded()) {
            do {
                baselineAgent.chooseMove(clonedBoard, currentPlayer);
                if (baselineAgent.getPieceMultiplier() != getMaxPiece()) {
                    setMaxPiece(baselineAgent.getPieceMultiplier());
                }
                currentPlayer = clonedBoard.getCurrentPlayer();
                clonedBoard.winBoardFull();
            } while (!clonedBoard.isGameEnded());
        }

        int terminalScore = new EvalFunc().evaluation(clonedBoard);
        node.setTerminalStateScore(terminalScore);
        return terminalScore; // Or return a combined score
    }

    /**
     * Backpropagates the result of a simulated playout from a terminal state up the Monte Carlo Tree Search (MCTS) tree.
     * Starting from the given node, the method traverses up the tree to update the scores of all parent nodes based on the terminal state score.
     *
     * @param nodeToExplore      The MCTS node from which the backpropagation starts.
     * @param terminalStateScore The score obtained from the terminal state reached during the simulation.
     */
    // Complexity: O(d)
    // d is the depth of the node in the MCTS tree
    private void backPropagate(MCTSNode nodeToExplore, int terminalStateScore) {
        MCTSNode tempNode = nodeToExplore;
        while (tempNode != null) {
            // Update the score of the node. The updated score is the sum of the existing evaluation score and the terminal state score.
            int updatedScore = tempNode.getExpandedNodeScore() + terminalStateScore;
            tempNode.setExpandedNodeScore(updatedScore);
            tempNode = tempNode.getParent(); // Move to the parent node
        }
    }


}
