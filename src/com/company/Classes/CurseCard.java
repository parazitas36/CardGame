package com.company.Classes;

import com.company.Enums.CurseType;
import com.company.Enums.ID;

import java.awt.image.BufferedImage;

public abstract class CurseCard extends Card{
    protected CurseType curseType;
    protected Integer amount;

    public CurseCard(String name, int manaCost, BufferedImage img, CurseType curseType, Integer _amount, boolean mustHaveTarget) {
        super(name, manaCost, ID.Curse, img, mustHaveTarget);
        this.curseType = curseType;
        this.amount = _amount;
    }

    public int getEffectNum(){
        return this.amount;
    }
    public CurseType getCurseType(){
        return this.curseType;
    }

    public boolean mustHaveTarget() {
        return this.mustHaveTarget;
    }

    public abstract boolean playCard(Player currentPlayer, Player opponent, CardSlot playedCardSlotInHand, CardSlot targettedCardSlot);
}
