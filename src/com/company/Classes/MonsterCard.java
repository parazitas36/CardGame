package com.company.Classes;

import com.company.Enums.ID;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MonsterCard extends Card {
    private int attack, defense;
    public int stunTime;
    public BufferedImage stunnedImg;
    private boolean wasAttacked;

    public MonsterCard(String name, int manaCost, int atk, int def, BufferedImage cardImage) {
        super(name, manaCost, ID.Monster, cardImage, true);
        attack = atk;
        defense = def;
        stunTime = 0;
        wasAttacked = false;
    }

    @Override
    public void tick() {

    }

    public boolean getWasAttacked() {
        return wasAttacked;
    }

    public void setWasAttacked(boolean attacked) {
        wasAttacked = attacked;
    }

    public void addStun() {
        stunTime++;
    }

    public int getStunTime() {
        return stunTime;
    }

    public void IncreaseAtk(int increase) {
        attack += increase;
    }

    public void IncreaseDef(int increase) {
        defense += increase;
    }

    public void removeStun() {
        stunTime--;
    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public boolean canCardBePlayed(Player player, CardSlot targetCardSlot) {
        return doesPlayerHaveEnoughMana(player)
               && this.isTargetSlotEmpty(targetCardSlot)
               && this.isTargetSlotPlayerSlot(player, targetCardSlot);
    }

    public int getAttack() {
        return this.attack;
    }

    public int getDef() {
        return this.defense;
    }

    public void setStunnedImg(BufferedImage img) {
        stunnedImg = img;
    }

    @Override
    public String toString() {
        return "Monster{" + "attack=" + attack + ", defense=" + defense + '}';
    }
}
