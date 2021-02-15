package com.company.Classes;

public class Player {
    int HP;
    int Mana;
    int ManaStack;
    ID id;
    public Player(int hp, int mana, int manaStack, ID _id){
        HP = hp;
        Mana = mana;
        ManaStack = manaStack;
        id = _id; // Player1 ir Player2 tures skirtingus id
    }
}
