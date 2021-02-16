package com.company.Classes;

public class Monster extends Card{
    private ID id;
    public Monster(String name, int manaCost) {
        super(name, manaCost);
        id = ID.Monster;
    }
    public ID getID(){
        return this.id;
    }
}
