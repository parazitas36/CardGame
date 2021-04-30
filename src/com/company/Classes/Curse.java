package com.company.Classes;

import java.awt.image.BufferedImage;

public class Curse extends Card{
    private boolean special; // Special card like "unstun" or something like that
    private String effect;
    private int amount;
    public Curse(String name, int manaCost, ID id, BufferedImage img, String eff) {
        super(name, manaCost, id, img);
        effect = eff;
        special = true;
    }
    public Curse(String name, int manaCost, ID id, BufferedImage img, String eff, int _amount) {
        super(name, manaCost, id, img);
        effect = eff;
        special = false;
        amount = _amount;
    }
    public int getEffectNum(){
        return this.amount;
    }
    public String getEffect(){
        return this.effect;
    }
    public boolean curseLogic(CardSlot c, Player currentPlayer, Player opponent){
        System.out.println("Current: " + currentPlayer.getID() + " opp" + opponent.getID());
        if(getEffect().equals("destroy")){
            if(c.getId().toString().contains(opponent.getID().toString())){
                c.removeCard();
                currentPlayer.decreaseCardsInHandCount();
                currentPlayer.decreaseMana(getManaCost());
                return  true;

            }
        }else if(getEffect().equals("stun")){
            if(c.getId().toString().contains(opponent.getID().toString())){
                ((Monster)(c.getCard())).addStun();
                currentPlayer.decreaseCardsInHandCount();
                currentPlayer.decreaseMana(getManaCost());
                return  true;
            }
        }
        return  false;
    }
    public void hpCurseLogic(CardSlot c, Player currentPlayer, Player opponent, CardSlot chosenCardSlot){
        if(getManaCost() <= currentPlayer.getMana()){
            System.out.println("Ieina");
            opponent.decreaseHP(amount);
            chosenCardSlot.removeCard();
            chosenCardSlot = null;
            currentPlayer.decreaseCardsInHandCount();
            currentPlayer.decreaseMana(getManaCost());
        }
    }
}
