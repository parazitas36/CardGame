package com.company.Classes;


import java.awt.*;

public abstract class GameObject {
    private int x, y;
    public GameObject(){
    }

    public abstract void tick();
    public abstract void render(Graphics g);

    public void setX(int _x){
         this.x = _x;
    }
    public void setY(int _y){
        this.y = _y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}
