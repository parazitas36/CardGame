package com.company.Classes;

public class Monster extends Card{
    ID id;
    public Monster(String name, int manaCost) {
        super(name, manaCost);
        id = ID.Monster;
    }
}
