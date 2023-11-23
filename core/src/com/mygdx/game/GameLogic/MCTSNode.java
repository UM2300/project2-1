package com.mygdx.game.GameLogic;

import com.mygdx.game.GUI.EvalFunc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MCTSNode {
    private board gameState; // The game state at this node
    private MCTSNode parent; // The parent node
    private ArrayList<MCTSNode> children; // Child nodes
    private int visitCount; // Number of times the node has been visited
    private int evalScore;

    public MCTSNode(board gameState) {
        this.gameState = gameState;
        this.children = new ArrayList<>();
        this.evalScore = new EvalFunc().evaluation(gameState); // Calculate evaluation score
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

        for (MCTSNode child : this.children) {
            if (child.getWinScore() > maxScore) {
                maxScore = child.getWinScore();
                bestChild = child;
            }
        }

        return bestChild;
    }

    public double getWinScore() {
        return this.evalScore;
    }

    public int getVisitCount() {
        return this.visitCount;
    }

    public void incrementVisitCount() {
        this.visitCount++;
    }



}
