package com.mygdx.game.GameLogic;

import com.mygdx.game.GUI.EvalFunc;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * MCTSAgent class implements the Monte Carlo Tree Search (MCTS) algorithm for decision-making in games.
 * It uses an evaluation function to guide the tree search process and integrates a Baseline_Agent for random move generation during simulations.
 */
public class MCTSAgent {
    private final int MAX_ITERATIONS = 10; // Maximum number of iterations for the MCTS algorithm

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
        

        board currentState = node.getGameState();

        while (legalMoves.size() < 10) {
            // Clone the current state before applying the move
            board clonedState = currentState.clone();

            String currentPlayer = clonedState.getCurrentPlayer();

            Baseline_Agent baselineAgent = new Baseline_Agent(clonedState);

            boolean test = boardsAreEqual(currentState.getBoard(), clonedState.getBoard());

            while(boardsAreEqual(currentState.getBoard(), clonedState.getBoard())){
                baselineAgent.chooseMove(clonedState, currentPlayer);
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


    private boolean containsBoard(ArrayList<board> boards, board nextState) {
        for (board existingBoard : boards) {
            if (existingBoard.equals(nextState)) {
                return true;
            }
        }
        return false;
    }

    private int simulateRandomPlayout(MCTSNode node) {
        board clonedBoard = node.getGameState().clone();
        Baseline_Agent baselineAgent = new Baseline_Agent(clonedBoard);
        clonedBoard.togglePlayer();
        String currentPlayer = clonedBoard.getCurrentPlayer();

        // Simulate the next player move and evaluate it

        //baselineAgent.chooseMove(clonedBoard, currentPlayer);
        //currentPlayer = clonedBoard.getCurrentPlayer();
        //node.setSimulatedMoveScore(new EvalFunc().evaluation(clonedBoard));
        
        int tempLimit=0;

        // Continue simulation to terminal state
        if (!clonedBoard.isGameEnded()) { 
            do {
                //clonedBoard.togglePlayer();
                baselineAgent.chooseMove(clonedBoard, currentPlayer);
                currentPlayer = clonedBoard.getCurrentPlayer();
                clonedBoard.winBoardFull();
            } while (!clonedBoard.isGameEnded());
        }

        int terminalScore = new EvalFunc().evaluation(clonedBoard);
        node.setTerminalStateScore(terminalScore);
        return terminalScore; // Or return a combined score
    }

    private void backPropagate(MCTSNode node, int gameResult) {
        while (node != null) {
            node.incrementVisitCount();
            node.updateScore(gameResult);
            node = node.getParent();
        }
    }

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
