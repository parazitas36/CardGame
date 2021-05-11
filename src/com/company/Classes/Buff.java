package com.company.Classes;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Buff extends Card{
    private boolean special; // Special card like "unstun" or something like that
    private String effect;
    private int amount;
    public Buff(String name, int manaCost,  ID id, BufferedImage img, String eff, int _amount) {
        super(name, manaCost, id, img);
        effect = eff;
        amount = _amount;
        special = false;
    }

    public Buff(String name, int manaCost,  ID id, BufferedImage img, String eff) {
        super(name, manaCost, id, img);
        effect = eff;
        special = true;
    }
    public int getEffectNum(){
        return this.amount;
    }
    public String getEffect(){
        return this.effect;
    }
//    public boolean isHP(){
//        if(this.effect.equals("hp")){
//
//        }
//    }

    public Boolean buffLogic(CardSlot c, Player currentPlayer){
        if(effect.equals("atk") && getManaCost() <= currentPlayer.getMana() + currentPlayer.getManaStack()){
            ((Monster)(c.getCard())).IncreaseAtk(this.amount);
            currentPlayer.decreaseCardsInHandCount();
            currentPlayer.decreaseMana(getManaCost());
            return  true;
        }else if(effect.equals("def") && getManaCost() <= currentPlayer.getMana() + currentPlayer.getManaStack()){
            ((Monster)(c.getCard())).IncreaseDef(getEffectNum());
            currentPlayer.decreaseCardsInHandCount();
            currentPlayer.decreaseMana(getManaCost());
            return  true;
        }
        return false;
    }
    public boolean hpBuffLogic(CardSlot c, Player currentPlayer, CardSlot chosenCardSlot){
        if(getManaCost() <= currentPlayer.getMana() + currentPlayer.getManaStack()){
            currentPlayer.addHP(amount);
            chosenCardSlot.removeCard();
            chosenCardSlot = null;
            currentPlayer.decreaseCardsInHandCount();
            currentPlayer.decreaseMana(getManaCost());
            return true;
        }
        return false;
    }
    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

    }
}
