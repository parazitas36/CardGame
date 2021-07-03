package com.company.Classes;

import javax.swing.*;
import java.awt.*;

import java.io.Serializable;

public class Monster extends Card  implements Serializable {
    private int attack,defense;
    public int stunTime;
    public ImageIcon stunnedImg;
    private boolean wasAttacked;
    public Monster(String name, int manaCost,  ID id,  int atk, int def, ImageIcon img) {
        super(name, manaCost,id, img);
        attack = atk;
        defense = def;
        stunTime = 0;
        wasAttacked = false;
    }
    @Override
    public void tick() {

    }
    public boolean getWasAttacked() { return wasAttacked; }
    public void setWasAttacked(boolean attacked) { wasAttacked = attacked; }
    public void IncreaseAtk(int increase){
        attack += increase;
    }
    public void addStun(){ stunTime++; }
    public int getStunTime(){return stunTime; }
    public void IncreaseDef(int increase){
        defense += increase;
    }

    public void removeStun() { stunTime--; }


    @Override
    public void render(Graphics g) {

    }
    public int getAttack(){
        return this.attack;
    }
    public int getDef(){
        return this.defense;
    }
    public void setStunnedImg(ImageIcon img){
        stunnedImg = img;
    }
    @Override
    public String toString() {
        return "Monster{" +
                "attack=" + attack +
                ", defense=" + defense +
                '}';
    }
}
