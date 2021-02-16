package com.company.Classes;

public class Buff extends Card{
    private ID id;
    public Buff(String name, int manaCost) {
        super(name, manaCost);
        id = ID.Buff;
    }
    public ID getID(){
        return this.id;
    }
}
