package com.company.Classes;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Buff extends Card{
    private boolean special; // Special card like "unstun" or something like that
    private String effect;
    private int amount;
    public Buff(String name, int manaCost,  ID id, BufferedImage img, String eff, int _amount) {
        super(name, manaCost, id, img);
        effect = eff;
        amount = _amount;
        special = false;
    }

    public Buff(String name, int manaCost,  ID id, BufferedImage img, String eff) {
        super(name, manaCost, id, img);
        effect = eff;
        special = true;
    }
    public int getEffectNum(){
        return this.amount;
    }
    public String getEffect(){
        return this.effect;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

    }
}
