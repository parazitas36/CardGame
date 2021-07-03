package com.company.Classes;

import javax.swing.*;
import java.io.Serializable;

public class Curse extends Card  implements Serializable {
    private boolean special; // Special card like "unstun" or something like that
    private String effect;
    private int amount;
    public Curse(String name, int manaCost, ID id, ImageIcon img, String eff) {
        super(name, manaCost, id, img);
        effect = eff;
        special = true;
    }
    public Curse(String name, int manaCost, ID id, ImageIcon img, String eff, int _amount) {
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
        if (getEffect().equals("destroy")) {
            c.removeCard();
            currentPlayer.decreaseCardsInHandCount();
            currentPlayer.decreaseMana(getManaCost());
            return true;
        }else if (getEffect().equals("stun")) {
            System.out.println("Stunas");
            ((Monster) (c.getCard())).addStun();
            currentPlayer.decreaseCardsInHandCount();
            currentPlayer.decreaseMana(getManaCost());
            return true;
        }
        return  false;
    }
    public boolean hpCurseLogic(Player currentPlayer, Player opponent, CardSlot chosenCardSlot){
        if(getManaCost() <= currentPlayer.getMana() + currentPlayer.getManaStack()){
            System.out.println("Ieina");
            System.out.println( this.amount);
            opponent.decreaseHP(this.amount);
            chosenCardSlot.removeCard();
            chosenCardSlot = null;
            currentPlayer.decreaseCardsInHandCount();
            currentPlayer.decreaseMana(getManaCost());
            return true;
        }
        return false;
    }
}
