package com.company.Classes;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Card extends GameObject {
    private String Name;
    private int ManaCost;
    private BufferedImage img;
    public Card(String name, int manaCost, int x, int y, BufferedImage image){
        super(x, y);
        Name = name;
        ManaCost = manaCost;
        img = image;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

    }
    public String getName(){
        return this.Name;
    }
    public int getManaCost(){
        return this.ManaCost;
    }
    public BufferedImage getImage(){return this.img;}
}
