package com.company.Classes;

public class Curse extends Card{
    private ID id;
    public Curse(String name, int manaCost) {
        super(name, manaCost);
        id = ID.Curse;
    }
    public ID getID(){
        return this.id;
    }
}
