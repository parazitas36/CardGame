package com.company.Classes;


public class GameObject {
    int x, y;
    public GameObject(int _x, int _y){
        x = _x;
        y = _y;
    }

    public void tick(){}
    public void render(){}

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}
