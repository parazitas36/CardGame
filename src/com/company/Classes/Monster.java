package com.company.Classes;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Monster extends Card {
    private int attack,defense;
    private int stunTime;
    public Monster(String name, int manaCost,  ID id,  int atk, int def, BufferedImage img) {
        super(name, manaCost,id, img);
        attack = atk;
        defense = def;
        stunTime = 0;
    }
    @Override
    public void tick() {

    }
    public void IncreaseAtk(int increase){
        attack += increase;
    }
    public void addStun(){ stunTime++; }
    public void removeStun(){ stunTime = 0; }
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

    @Override
    public String toString() {
        return "Monster{" +
                "attack=" + attack +
                ", defense=" + defense +
                '}';
    }
}
