package com.company.Classes;

import com.company.Enums.BuffType;
import com.company.Enums.ID;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class BuffCard extends Card {
    protected BuffType buffType;
    protected Integer amount;

    public BuffCard(String name, int manaCost, BufferedImage img, BuffType buffType, Integer amount, boolean mustHaveTarget) {
        super(name, manaCost, ID.Buff, img, mustHaveTarget);
        this.buffType = buffType;
        this.amount = amount;
        this.mustHaveTarget = mustHaveTarget;
    }

    public int getEffectAmount() {
        return this.amount;
    }

    public BuffType getBuffType() {
        return this.buffType;
    }

    public abstract boolean playCard(CardSlot cardSlot, Player player, CardSlot playedCardSlotInHand);

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

    }
}
