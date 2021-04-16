package com.company.Classes;

import com.company.Engine.Display;

import java.awt.*;
import java.util.ArrayList;

public class Player  extends GameObject{
    private int HP;
    private int Mana;
    private int ManaCapacity;
    private int ManaStack;
    private int ManaStackCapacity;
    private ID id;
    private Deck deck;
    private ArrayList<CardSlot> playerSlots;
    private int handSizeLimit, cardsInHand;
    private Phase phase;
    private Display display;



    public Player(ID _id, Deck _deck, ArrayList<CardSlot> slots, Display _display){
        HP = 30;
        Mana = 1;
        ManaCapacity = 1;
        ManaStack = 0;
        ManaStackCapacity = 3;
        id = _id;
        deck = _deck;
        playerSlots = slots;
        handSizeLimit = 7;
        cardsInHand = 0;
        this.display = _display;

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
    public void setPhase(Phase _phase){
        this.phase = _phase;
    }
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
    public boolean placeCard(CardSlot slot, Card card){
        if(Mana >= card.getManaCost()) {
            slot.setCard(card);
            cardsInHand--;
            Mana = Mana - card.getManaCost();
            return true;
        }else{
            return false;
        }
    }
    public void addMana(){
        if(ManaCapacity < 5){
            ManaCapacity++;
        }
    }
    public void addHP(int amount){
        HP += amount;
    }
    public void decreaseHP(int amount){
        HP -= amount;
    }
    public void refillMana(){
        Mana = ManaCapacity;
    }
    public void takeDamage(int damage){
        HP-=damage;
    }
    public boolean attack(Monster attacker, Monster defender){
        if(attacker.getAttack() >= defender.getDef()){
            phase.getOpponent().takeDamage(attacker.getAttack() - defender.getDef());
            return true;
        }else{
            return false;
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
        int width = display.getWidth();
        int Height = display.getHeight();
        // draw mana in board
        Font prevFont = g.getFont();
        if(id == ID.Player1){
            Font font = new Font("", Font.BOLD, (int)((Height + width) * 0.00933));
            g.setFont(font);
            g.drawString(String.format("%s", getMana()), (int)(width * 0.007), (int)(Height*0.605));
            g.drawString(String.format("%s", getHP()), (int)(width * 0.005), (int)(Height*0.505));
            g.drawString(String.format("%s", getManaStack()) + String.format("/%s", ManaStackCapacity), (int)(width * 0.054), (int)(Height*0.615));
            g.setFont(prevFont);
        }else{
            Font font = new Font("", Font.BOLD, (int)((Height + width) * 0.00933));
            g.setFont(font);
            g.drawString(String.format("%s", getMana()), (int)(width * 0.007), (int)(Height*0.199));
            g.drawString(String.format("%s", getHP()), (int)(width * 0.005), (int)(Height*0.3));
            g.drawString(String.format("%s", getManaStack()) + "/3", (int)(width * 0.054), (int)(Height*0.194));
            g.setFont(prevFont);
        }
    }
}
