// TakPiece.java
package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class TakPiece {
    public enum Type {
        STONE, CAPSTONE
    }

    public Type type;
    public Model model;
    public ModelInstance instance;

    public TakPiece(Type type, Model model) {
        this.type = type;
        this.model = model;
        this.instance = new ModelInstance(model);
    }
}
