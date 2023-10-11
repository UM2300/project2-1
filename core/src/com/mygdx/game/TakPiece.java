// TakPiece.java
package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;
import java.util.List;

public class TakPiece {

    public List<TakPiece> stack = new ArrayList<TakPiece>();
    public enum Owner {
        LEFT, RIGHT, NONE
    }

    public Owner owner = Owner.NONE;

    public TakPiece getTopPiece() {
        return stack.isEmpty() ? this : stack.get(stack.size() - 1);
    }

    public enum Type {
        STONE, CAPSTONE, STAND
    }
    public float getHeight() {
        switch (this.type) {
            case STONE:
                return 0.2f;
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
    public int idNum;

    public TakPiece(Type type, Model model, int idNum) {
        this.type = type;
        this.model = model;
        this.instance = new ModelInstance(model);
        this.idNum = idNum;
    }

    public int getIdNum(){
        return idNum;
    }

    public void setIdNum(int idNum){
        this.idNum=idNum;
    }

    public Type getType(){
        return type;
    }

    public void setType(Type type){
        this.type=type;
    }

    public Model getModel(){
        return model;
    }

    public void setModel(Model model){
        this.model=model;
    }

}