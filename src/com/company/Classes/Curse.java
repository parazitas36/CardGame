package com.company.Classes;

import java.awt.image.BufferedImage;

public class Curse extends Card{
    private ID id;
    public Curse(String name, int manaCost, int x, int y, ID id, BufferedImage img) {
        super(name, manaCost, x, y, id, img);
    }

}
