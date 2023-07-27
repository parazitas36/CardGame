package com.company.Classes;

import com.company.Enums.ID;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Card extends GameObject {
    private String Name;
    private int ManaCost;
    private ID id;
    private BufferedImage img;

    protected boolean mustHaveTarget;

    public Card(String name, int manaCost, ID _id, BufferedImage image, boolean mustHaveTarget) {
        super();
        this.Name = name;
        this.ManaCost = manaCost;
        this.id = _id;
        this.img = image;
        this.mustHaveTarget = mustHaveTarget;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
    }

    public String getName() {
        return this.Name;
    }

    public int getManaCost() {
        return this.ManaCost;
    }

    public BufferedImage getImage() {
        return this.img;
    }

    public ID getID() {
        return this.id;
    }

    public boolean isBuffCard() {
        return this.id == ID.Buff;
    }

    public boolean isCurseCard() {
        return this.id == ID.Curse;
    }

    public boolean isMonsterCard() {
        return this.id == ID.Monster;
    }

    public boolean mustHaveTarget() {
        return this.mustHaveTarget;
    }

    public void decreaseManaAndCardsAfterPlayingCard(Player player) {
        player.decreaseCardsInHandCount();
        player.decreaseMana(this.getManaCost());
    }

    public boolean doesPlayerHaveEnoughMana(Player player) {
        return this.getManaCost() <= player.getMana() + player.getManaStack();
    }

    public boolean isTargetSlotPlayerSlot(Player player, CardSlot targetCardSlot) {
        return targetCardSlot != null
               && targetCardSlot.getId().toString()
                .contains(player.getID().toString());
    }

    protected boolean isTargetSlotEmpty(CardSlot targetSlot) {
        return targetSlot == null || !targetSlot.isCardOnBoard();
    }

    public void removedPlayedCardFromHand(CardSlot playedCardSlotInHand) {
        playedCardSlotInHand.removeCard();
        playedCardSlotInHand = null;
    }

    public abstract boolean canCardBePlayed(Player player, CardSlot targetCardSlot);
}
