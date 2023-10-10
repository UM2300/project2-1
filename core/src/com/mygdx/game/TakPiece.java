// TakPiece.java
package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class TakPiece {
    public enum Type {
        STONE, STANDINGSTONE, CAPSTONE
    }
    public float getHeight() {
        switch (this.type) {
            case STONE:
                return 0.2f;
            case STANDINGSTONE:
                return 1.0f;
            case CAPSTONE:
                return 0.8f;
            default:
                return 0f;
        }
    }
    public int boardX = -1;
    public int boardZ = -1;
    public Type type;
    public Model model;
    public ModelInstance instance;

    public TakPiece(Type type, Model model) {
        this.type = type;
        this.model = model;
        this.instance = new ModelInstance(model);
    }
}


























































































































































































































