package com.company.Classes;

public class Curse extends Card{
    private ID id;
    public Curse(String name, int manaCost, int x, int y) {
        super(name, manaCost, x, y);
        id = ID.Curse;
    }
    public ID getID(){
        return this.id;
    }
}
