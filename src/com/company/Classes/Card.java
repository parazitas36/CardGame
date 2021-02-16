package com.company.Classes;

public class Card {
    private String Name;
    private int ManaCost;
    public Card(String name, int manaCost){
        Name = name;
        ManaCost = manaCost;
    }
    public String getName(){
        return this.Name;
    }
    public int getManaCost(){
        return this.ManaCost;
    }
}
