package com.company.Classes;

import java.awt.*;

public class CardSlot extends GameObject{
    private Card card;
    private Deck deck;
    private ID id;
    private int width;
    private int height;
    private boolean hasCard = false;
    public CardSlot(Card _card, int x, int y, ID slotID){
        super();
        setX(x);
        setY(y);
        card = _card;
        id = slotID;
        hasCard = card == null ? false : true;
    }
    public CardSlot(Deck _deck, int x, int y, ID slotID){
        super();
        deck = _deck;
        setX(x);
        setY(y);
        id = slotID;
    }
    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics g) {
        if(this.hasCard ) {
            if(this.id.toString().contains("Player1_HandSlot") || this.id.toString().contains("Dragging_Slot")) {
                g.drawImage(card.getImage(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), null);
            }else if (card.getID().toString() == ID.Monster.toString() && this.id.toString().contains("Player1_Slot"))
            {
                g.drawImage(card.getImage(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), null);
                g.drawString("ATK: " + String.format("%s", ((Monster)card).getAttack()), this.getX() + 16, this.getY() + 155);
                g.drawString("DEF: " + String.format("%s", ((Monster)card).getDef()), this.getX() + 88, this.getY() + 155);
            } else if(card.getID().toString() != ID.Monster.toString() && this.id.toString().contains("Player1_Slot")){
                g.drawImage(card.getImage(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), null);
            }
            else{
                g.drawImage(card.getImage(), this.getX(), this.getY() + this.getHeight(), this.getWidth(), -this.getHeight(), null);
            }
        }else if(deck != null){
            if(id.toString().contains("Player1")){
                g.drawImage(deck.getImage(),this.getX(), this.getY(), this.getWidth(), this.getHeight(), null);
            }else{
                g.drawImage(deck.getImage(),this.getX(), this.getY() + this.getHeight(), this.getWidth(), -this.getHeight(), null);
            }
        }
    }
    public void setCard(Card _card){
        this.card = _card;
        if(_card != null) { this.hasCard = true; }
    }
    public void setDeck(Deck _deck){
        this.deck = _deck;
    }
    public void removeCard(){
        this.card = null;
        hasCard = false;
    }
    public ID getId() {
        return this.id;
    }
    public void setWidth(int _width){
        this.width = _width;
    }
    public void setHeight(int _height){
        this.height = _height;
    }
    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }
    public boolean cardOnBoard(){
        return this.hasCard = this.card == null ? false : true;
    }
    public Card getCard(){
        return this.card;
    }
    public Deck getDeck(){
        return this.deck;
    }
}
