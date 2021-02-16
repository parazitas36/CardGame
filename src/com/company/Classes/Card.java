package com.company.Classes;

public class Card extends GameObject {
    String Name;
    int ManaCost;
    public Card(String name, int manaCost, int x, int y){
        super(x, y);
        Name = name;
        ManaCost = manaCost;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render() {
        super.render();
    }
}
