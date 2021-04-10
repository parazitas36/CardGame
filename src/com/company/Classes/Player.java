package com.company.Classes;

import java.awt.*;
import java.util.ArrayList;

public class Player  extends GameObject{
    private int HP;
    private int Mana;
    private int ManaStack;
    private ID id;
    private Deck deck;
    private ArrayList<CardSlot> playerSlots;
    private int handSizeLimit, cardsInHand;
    public Player(int hp, int mana, int manaStack, ID _id, Deck _deck, ArrayList<CardSlot> slots){
        HP = hp;
        Mana = mana;
        ManaStack = manaStack;
        id = _id;
        deck = _deck;
        playerSlots = slots;
        handSizeLimit = 7;
        cardsInHand = 0;
    }
    public ID getID(){
        return this.id;
    }
    public int getHP(){
        return this.HP;
    }
    public int getMana(){
        return this.Mana;
    }
    public int getManaStack(){
        return this.ManaStack;
    }
    public Deck getDeck() { return this.deck; }
    public int getCardsInHand() { return this.cardsInHand; }

    public void drawCard(){
        Card card = deck.drawCard();
        for(int i = 0; i < playerSlots.size(); i++){
            CardSlot slot = playerSlots.get(i);
            if(slot.getId().toString().contains(String.format("%s_HandSlot", this.id))){
                if(!slot.cardOnBoard()&& handSizeLimit > cardsInHand){
                    slot.setCard(card);
                    cardsInHand++;
                    System.out.println(String.format("Player: %s cards: %d", this.id.toString(), this.cardsInHand));
                    break;
                }
            }
        }
    }
    //--------------------------
    //Use only for AI opponent
    //--------------------------
    public void setCardOnBoard(){
        for(int i = 0; i < playerSlots.size(); i++){
            CardSlot slot = playerSlots.get(i);
            if(slot.getId().toString().contains(String.format("%s_HandSlot", this.id)) && slot.cardOnBoard()){
                for(int j = 0; j < playerSlots.size(); j++){
                    CardSlot tempSlot = playerSlots.get(j);
                    if(tempSlot.getId().toString().contains(String.format("%s_Slot", this.id))){
                        if(!tempSlot.cardOnBoard()){
                            tempSlot.setCard(slot.getCard());
                            slot.removeCard();
                            cardsInHand--;
                            return;
                        }
                    }
                }
            }
        }
    }
    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        // draw mana in board
        Font prevFont = g.getFont();
        if(id == ID.Player1){
            Font font = new Font("", Font.BOLD, 22);
            g.setFont(font);
            g.drawString(String.format("%s", getMana()), 10, 590);
            g.drawString(String.format("%s", getHP()), 7, 493);
            g.drawString(String.format("%s", getManaStack()) + "/3", 76, 600);
            g.setFont(prevFont);
        }else{
            Font font = new Font("", Font.BOLD, 22);
            g.setFont(font);
            g.drawString(String.format("%s", getMana()), 10, 195);
            g.drawString(String.format("%s", getHP()), 7, 294);
            g.drawString(String.format("%s", getManaStack()) + "/3", 77, 190);
            g.setFont(prevFont);
        }
    }
}
