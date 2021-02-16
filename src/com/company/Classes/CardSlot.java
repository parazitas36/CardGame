package com.company.Classes;

import java.awt.*;

public class CardSlot extends GameObject{
    private Card card;
    private ID id;
    private int width;
    private int height;
    public CardSlot(Card _card, int x, int y, ID slotID){
        super(x, y);
        card = _card;
        id = slotID;
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics g) {
        if(card != null) {
            g.drawImage(card.getImage(),this.getX(), this.getY(), this.getWidth(), this.getHeight(), null);
        }else{
            g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        }
    }
    public void setCard(Card _card){
        card = _card;
    }
    public ID getId() {
        return id;
    }
    public void setWidth(int _width){
        width = _width;
    }
    public void setHeight(int _height){
        height = _height;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public boolean cardOnBoard(){
        return card == null ? false : true;
    }
}
