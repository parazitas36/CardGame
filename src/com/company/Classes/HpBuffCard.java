package com.company.Classes;

import com.company.Enums.BuffType;
import com.company.Enums.ID;

import java.awt.image.BufferedImage;

public class HpBuffCard extends BuffCard {
    public HpBuffCard(String name, int manaCost, BufferedImage img, Integer amount) {
        super(name, manaCost, img, BuffType.PlusHp, amount, false);
    }

    @Override
    public boolean playCard(CardSlot cardSlot, Player player, CardSlot playedCardSlotInHand) {
        if (!this.doesPlayerHaveEnoughMana(player)) {
            return false;
        }

        player.addHP(this.amount);
        this.removedPlayedCardFromHand(playedCardSlotInHand);
        this.decreaseManaAndCardsAfterPlayingCard(player);
        return true;
    }

    @Override
    public boolean canCardBePlayed(Player player, CardSlot targetCardSlot) {
        return this.doesPlayerHaveEnoughMana(player);
    }
}
