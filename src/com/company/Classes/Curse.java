package com.company.Classes;

public class Curse extends Card{
    ID id;
    public Curse(String name, int manaCost) {
        super(name, manaCost);
        id = ID.Curse;
    }
}
