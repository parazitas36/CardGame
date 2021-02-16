package com.company.Classes;

public class Card extends GameObject {
    private String Name;
    private int ManaCost;
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
    public String getName(){
        return this.Name;
    }
    public int getManaCost(){
        return this.ManaCost;
    }
}
