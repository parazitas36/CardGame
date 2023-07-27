package com.company.Classes;

import java.awt.image.BufferedImage;

public class BasicCardData {
    private String name;
    private int manaCost;
    private BufferedImage cardImage;

    public BasicCardData(String name, int manaCost, BufferedImage cardImage) {
        this.name = name;
        this.manaCost = manaCost;
        this.cardImage = cardImage;
    }

    public String getName() {
        return name;
    }

    public int getManaCost() {
        return manaCost;
    }

    public BufferedImage getCardImage() {
        return cardImage;
    }
}
