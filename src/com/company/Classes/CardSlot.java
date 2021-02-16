package com.company.Classes;

public class CardSlot {
    private int CardSlotID;
    private Card card;
    public CardSlot(Card card1, int id){
        this.card = card1;
        this.CardSlotID = id;
    }
    public Card getCard(){
        return this.card;
    }
    public int getCardSlotID(){
        return this.CardSlotID;
    }
}
