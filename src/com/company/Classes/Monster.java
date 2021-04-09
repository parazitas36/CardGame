package com.company.Classes;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Monster extends Card {
    private int attack,defense;
    public Monster(String name, int manaCost,  ID id,  int atk, int def, BufferedImage img) {
        super(name, manaCost,id, img);
        attack = atk;
        defense = def;
    }

    @Override
    public void tick() {

    }

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
