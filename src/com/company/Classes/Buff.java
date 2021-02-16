package com.company.Classes;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Buff extends Card{

    private ID id;
    public Buff(String name, int manaCost, int x, int y, BufferedImage img) {
        super(name, manaCost, x, y, img);
        id = ID.Buff;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

    }
}
