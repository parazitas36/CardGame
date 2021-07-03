package com.company.Classes;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Card extends GameObject  implements Serializable {
    private String Name;
    private int ManaCost;
    private ID id;
    private ImageIcon img;
    public Card(String name, int manaCost, ID _id, ImageIcon image){
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
    public Image getImage(){return this.img.getImage();}
    public ID getID() { return this.id; }
}
