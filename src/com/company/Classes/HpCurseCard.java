package com.company.Classes;

import com.company.Enums.CurseType;

import java.awt.image.BufferedImage;

public class HpCurseCard extends CurseCard {
    public HpCurseCard(String name, int manaCost, BufferedImage img, Integer _amount) {
        super(name, manaCost, img, CurseType.MinusHp, _amount, false);
    }

    @Override
    public boolean playCard(Player player, Player opponent, CardSlot playedCardSlotInHand, CardSlot targettedCardSlot) {
        if(!this.doesPlayerHaveEnoughMana(player)){
            return false;
        }

        opponent.decreaseHP(this.amount);
        this.removedPlayedCardFromHand(playedCardSlotInHand);
        this.decreaseManaAndCardsAfterPlayingCard(player);
        return true;
    }

    @Override
    public boolean canCardBePlayed(Player player, CardSlot targetCardSlot) {
        return this.doesPlayerHaveEnoughMana(player);
    }
}
