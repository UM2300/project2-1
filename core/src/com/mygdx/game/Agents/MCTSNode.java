package com.mygdx.game.Agents;
import com.mygdx.game.GameLogic.board;

import java.util.ArrayList;

public class MCTSNode {
    private board gameState; // The game state at this node
    private MCTSNode parent; // The parent node
    private ArrayList<MCTSNode> children; // Child nodes
    private int evalScore;
    private int expandedNodeScore;
    private int terminalStateScore;


    public MCTSNode(board gameState) {
        this.gameState = gameState;
        this.children = new ArrayList<>();
        this.evalScore = new EvalFunc().evaluation(getGameState()); // Calculate evaluation score
    }


    public void setParent(MCTSNode parent) {
        this.parent = parent;
    }

    public MCTSNode getParent() {
        return this.parent;
    }

    public board getGameState() {
        return this.gameState;
    }

    public ArrayList<MCTSNode> getChildren() {
        return this.children;
    }

    public void addChild(MCTSNode child) {
        this.children.add(child);
    }

    public MCTSNode getRandomChildNode() {
        int randomIndex = (int) (Math.random() * this.children.size());
        return this.children.get(randomIndex);
    }

    public MCTSNode getChildWithMaxScore() {
        MCTSNode bestChild = null;
        double maxScore = Double.NEGATIVE_INFINITY;

        ArrayList<MCTSNode> mctsNodes = this.children;
        for (int i = 0; i < mctsNodes.size(); i++) {
            MCTSNode child = mctsNodes.get(i);
            if (child.getWinScore() > maxScore) {
                maxScore = child.getWinScore();
                bestChild = child;
            }
        }

        return bestChild;
    }

    public int getWinScore() {
        return this.evalScore;
    }
    public void setExpandedNodeScore(int score) {
        this.expandedNodeScore = score;
    }
    public int getExpandedNodeScore(){
        return expandedNodeScore;
    }

    public void setTerminalStateScore(int score) {
        this.terminalStateScore = score;
    }

    public int getTerminalStateScore() {
        return terminalStateScore;
    }



}


