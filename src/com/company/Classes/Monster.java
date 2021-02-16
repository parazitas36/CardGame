package com.company.Classes;

public class Monster extends Card{
    private ID id;
    public Monster(String name, int manaCost, int x, int y) {
        super(name, manaCost, x, y);
        id = ID.Monster;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render() {
        super.render();
    public ID getID(){
        return this.id;
    }
}
