package com.company.Classes;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Card extends GameObject {
    private String Name;
    private int ManaCost;
    private ID id;
    private BufferedImage img;
    public Card(String name, int manaCost, ID _id, BufferedImage image){
        super();
        this.Name = name;
        this.ManaCost = manaCost;
        this.id = _id;
        this.img = image;
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
    public ID getID() { return this.id; }
}
