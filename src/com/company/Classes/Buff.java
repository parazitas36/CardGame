package com.company.Classes;

public class Buff extends Card{

    private ID id;
    public Buff(String name, int manaCost, int x, int y) {
        super(name, manaCost, x, y);
        id = ID.Buff;
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
