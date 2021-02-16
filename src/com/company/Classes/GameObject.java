package com.company.Classes;


import java.awt.*;

public abstract class GameObject {
    int x, y;
    public GameObject(int _x, int _y){
        x = _x;
        y = _y;
    }

    public abstract void tick();
    public abstract void render(Graphics g);

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}
