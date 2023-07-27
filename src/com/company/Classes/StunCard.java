package com.company.Classes;

import com.company.Enums.CurseType;

import java.awt.image.BufferedImage;

public class StunCard extends CurseCard{
    public StunCard(String name, int manaCost, BufferedImage img) {
        super(name, manaCost, img, CurseType.Stun, null, true);
    }

    @Override
    public boolean playCard(Player player, Player opponent, CardSlot playedCardSlotInHand, CardSlot targettedCardSlot) {
        if (!this.doesPlayerHaveEnoughMana(player)) {
            return false;
        }

        MonsterCard monsterCard = (MonsterCard)targettedCardSlot.getCard();
        monsterCard.addStun();
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
