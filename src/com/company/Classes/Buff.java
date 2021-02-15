package com.company.Classes;

public class Buff extends Card{
    ID id;
    public Buff(String name, int manaCost) {
        super(name, manaCost);
        id = ID.Buff;
    }
}
