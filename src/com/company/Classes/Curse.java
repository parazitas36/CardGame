package com.company.Classes;

import java.awt.image.BufferedImage;

public class Curse extends Card{
    private boolean special; // Special card like "unstun" or something like that
    private String effect;
    private int amount;
    public Curse(String name, int manaCost, ID id, BufferedImage img, String eff) {
        super(name, manaCost, id, img);
        effect = eff;
        special = true;
    }
    public Curse(String name, int manaCost, ID id, BufferedImage img, String eff, int _amount) {
        super(name, manaCost, id, img);
        effect = eff;
        special = false;
        amount = _amount;
    }
}
