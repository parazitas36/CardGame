package com.company.Classes;

import com.company.Enums.CurseType;
import com.company.Enums.ID;

import java.awt.image.BufferedImage;

public class DestroyCard extends CurseCard{
    public DestroyCard(String name, int manaCost, BufferedImage img) {
        super(name, manaCost, img, CurseType.Destroy, null, true);
    }

    @Override
    public boolean playCard(Player player, Player opponent, CardSlot playedCardSlotInHand, CardSlot targettedCardSlot) {
        if (!this.doesPlayerHaveEnoughMana(player)) {
            return false;
        }

        targettedCardSlot.removeCard();
        this.removedPlayedCardFromHand(playedCardSlotInHand);
        this.decreaseManaAndCardsAfterPlayingCard(player);
        return true;
    }

    @Override
    public boolean canCardBePlayed(Player player, CardSlot targetCardSlot) {
        return this.doesPlayerHaveEnoughMana(player)
               && !this.isTargetSlotEmpty(targetCardSlot)
               && targetCardSlot.getCard().isMonsterCard();
    }
}
