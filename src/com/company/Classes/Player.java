package com.company.Classes;

import java.awt.*;

public class Player  extends GameObject{
    private int HP;
    private int Mana;
    private int ManaStack;
    private ID id;
    public Player(int hp, int mana, int manaStack, ID _id){
        HP = hp;
        Mana = mana;
        ManaStack = manaStack;
        id = _id; // Player1 ir Player2 tures skirtingus id
    }
    public ID getID(){
        return this.id;
    }
    public int getHP(){
        return this.HP;
    }
    public int getMana(){
        return this.Mana;
    }
    public int getManaStack(){
        return this.ManaStack;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        // draw mana in board
        Font prevFont = g.getFont();
        if(id == ID.Player1){
            Font font = new Font("", Font.BOLD, 22);
            g.setFont(font);
            g.drawString(String.format("%s", getMana()), 10, 590);
            g.drawString(String.format("%s", getHP()), 7, 493);
            g.drawString(String.format("%s", getManaStack()) + "/3", 76, 600);
            g.setFont(prevFont);
        }else{
            Font font = new Font("", Font.BOLD, 22);
            g.setFont(font);
            g.drawString(String.format("%s", getMana()), 10, 195);
            g.drawString(String.format("%s", getHP()), 7, 294);
            g.drawString(String.format("%s", getManaStack()) + "/3", 77, 190);
            g.setFont(prevFont);
        }
    }
}
