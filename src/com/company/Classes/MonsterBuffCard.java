package com.company.Classes;

import com.company.Enums.BuffType;

import java.awt.image.BufferedImage;

public class MonsterBuffCard extends BuffCard{
    public MonsterBuffCard(String name, int manaCost, BufferedImage img, BuffType buffType, Integer amount) {
        super(name, manaCost, img, buffType, amount, true);
    }

    @Override
    public boolean playCard(CardSlot cardSlot, Player player, CardSlot playedCardSlotInHand) {
        if (!this.doesPlayerHaveEnoughMana(player)) {
            return false;
        }

        MonsterCard monsterCard = (MonsterCard)cardSlot.getCard();

        if (buffType.equals(BuffType.PlusAtk)) {
            monsterCard.IncreaseAtk(this.getEffectAmount());
        } else if (buffType.equals(BuffType.PlusDef)) {
            monsterCard.IncreaseDef(this.getEffectAmount());
        } else {
            return false;
        }

        this.decreaseManaAndCardsAfterPlayingCard(player);
        this.removedPlayedCardFromHand(playedCardSlotInHand);
        return true;
    }

    @Override
    public boolean canCardBePlayed(Player player, CardSlot targetCardSlot) {
        return doesPlayerHaveEnoughMana(player)
               && !this.isTargetSlotEmpty(targetCardSlot)
               && targetCardSlot.getCard().isMonsterCard()
               && this.isTargetSlotPlayerSlot(player, targetCardSlot);
    }
}
