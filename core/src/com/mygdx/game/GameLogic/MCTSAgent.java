package com.mygdx.game.GameLogic;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * MCTSAgent class implements the Monte Carlo Tree Search (MCTS) algorithm for decision-making in games.
 * It uses an evaluation function to guide the tree search process and integrates a Baseline_Agent for random move generation during simulations.
 */
public class MCTSAgent {
    private final int MAX_ITERATIONS = 10; // Maximum number of iterations for the MCTS algorithm

    private int maxPiece=3;

    public void setMaxPiece(int maxPiece){
        this.maxPiece=maxPiece;
    }

    public int getMaxPiece(){
        return this.maxPiece;
    }

    /**
     * Finds the next best move based on the current state of the game board.
     * It performs a Monte Carlo Tree Search by iterating through a series of steps: selection, expansion, simulation, and backpropagation.
     *
     * @param currentBoard The current state of the game board.
     * @return The best move as determined by the MCTS algorithm.
     */

     /** Complexity: O(bh + m + d)
      * bh is from selectPromisingNode
      * m is from simulateRandomPlayout
      * d is from backPropagate
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

      /**Complexity: O(bh)
     * b = max number of children for any node
     * h = the height of the tree
     */ 
    private MCTSNode selectPromisingNode(MCTSNode rootNode) {
        
        MCTSNode node = rootNode;

        int childNum=node.getChildren().size();

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

     /**Complexity: O(bh)
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
     * @param node The MCTS node to be expanded.
     * @param currentBoard The current game board state represented by the MCTS node.
     */
    // Complexity: O(1)
    private void expandNode(MCTSNode node, board currentBoard) {
        ArrayList<board> legalMoves = new ArrayList<>();
        

        board currentState = node.getGameState();

        while (legalMoves.size() < 10) {
            // Clone the current state before applying the move
            board clonedState = currentState.clone();

            String currentPlayer = clonedState.getCurrentPlayer();

            Baseline_Agent baselineAgent = new Baseline_Agent(clonedState,getMaxPiece());

            boolean test = boardsAreEqual(currentState.getBoard(), clonedState.getBoard());

            while(boardsAreEqual(currentState.getBoard(), clonedState.getBoard())){
                baselineAgent.chooseMove(clonedState, currentPlayer);
                if(baselineAgent.getPieceMultiplier()!=getMaxPiece()){
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
     * Checks whether a given game state (represented by a board object) is already present in a list of existing game states.
     *
     * @param boards The list of existing game states to check against.
     * @param nextState The game state to check for existence in the list.
     * @return true if the given game state is found in the list, false otherwise.
     */
    // Complexity: O(n)
    // n is the number of elements in the boards list
    private boolean containsBoard(ArrayList<board> boards, board nextState) {
        for (board existingBoard : boards) {
            if (existingBoard.equals(nextState)) {
                return true;
            }
        }
        return false;
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
        Baseline_Agent baselineAgent = new Baseline_Agent(clonedBoard,maxPiece);
        clonedBoard.togglePlayer();
        String currentPlayer = clonedBoard.getCurrentPlayer();

        // Continue simulation to terminal state
        if (!clonedBoard.isGameEnded()) { 
            do {
                //clonedBoard.togglePlayer();
                baselineAgent.chooseMove(clonedBoard, currentPlayer);
                if(baselineAgent.getPieceMultiplier()!=getMaxPiece()){
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
     * @param nodeToExplore The MCTS node from which the backpropagation starts.
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

    /**
     * Checks whether two 2D arrays of ArrayLists representing game boards are equal.
     * Equality is determined based on the dimensions and the content of the ArrayLists in corresponding positions.
     *
     * @param board1 The first 2D array representing a game board.
     * @param board2 The second 2D array representing another game board for comparison.
     * @return true if the dimensions and content of corresponding ArrayLists in both boards are equal, false otherwise.
     */
    // Complexity: O(1)
    // As stated before we are looping over a nxn board where n is always 5 and remains unchanged hence O(1) not O(n^2)
    public boolean boardsAreEqual(ArrayList<Integer>[][] board1, ArrayList<Integer>[][] board2){

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

}
