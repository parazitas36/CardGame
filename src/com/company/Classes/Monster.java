package com.company.Classes;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Monster extends Card {
    private ID id;

    public Monster(String name, int manaCost, int x, int y, BufferedImage img) {
        super(name, manaCost, x, y, img);
        id = ID.Monster;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

    }
}
