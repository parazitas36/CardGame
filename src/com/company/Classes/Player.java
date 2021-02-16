package com.company.Classes;

public class Player {
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
}
