package com.company.Classes;

public class Curse extends Card{
    ID id;
    public Curse(String name, int manaCost, int x, int y) {
        super(name, manaCost, x, y);
        id = ID.Curse;
    }
}
