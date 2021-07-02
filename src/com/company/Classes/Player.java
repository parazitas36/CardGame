package com.company.Classes;

import com.company.Engine.Display;
import com.company.Engine.Game;

import java.awt.*;
import java.util.ArrayList;

public class Player  extends GameObject{
    public int HP;
    public int Mana;
    public int ManaCapacity;
    public int ManaStack;
    public int ManaStackCapacity;
    public ID id;
    public Deck deck;
    public ArrayList<CardSlot> playerSlots;
    public ArrayList<CardSlot> playerHandSlots;
    public ArrayList<CardSlot> playerBoardSlots;
    public CardSlot deckSlot;
    public int handSizeLimit, cardsInHand;
    public Phase phase;
    public Display display;
    public Player opponent;
    public boolean possibleToDefeat;
    public Game game;
    public boolean supper;
    public Player(ID _id, Deck _deck, ArrayList<CardSlot> slots, Display _display, Game _game){
        HP = 30;
        Mana = 1;
        ManaCapacity = 1;
        ManaStack = 0;
        ManaStackCapacity = 3;
        id = _id;
        deck = _deck;
        playerSlots = slots;
        if(slots != null) {
            filterSlots();
        }
        handSizeLimit = 7;
        cardsInHand = 0;
        this.display = _display;
        possibleToDefeat = false;
        game = _game;
        supper = false;
    }
    public void setOpponent(Player opp){ this.opponent = opp;}
    public ID getID(){
        return this.id;
    }
    public int getHP(){
        return this.HP;
    }
    public int getMana(){
        return this.Mana;
    }
    public int getManaStack(){
        return this.ManaStack;
    }
    public Deck getDeck() { return this.deck; }
    public int getCardsInHand() { return this.cardsInHand; }
    public void setPhase(Phase _phase){
        this.phase = _phase;
    }
    /*
        Filters Hand and Board slots from "playerSlots" array into two different arrays.
     */
    public void filterSlots(){
        this.playerBoardSlots = new ArrayList<>();
        this.playerHandSlots = new ArrayList<>();
        for(int i = 0; i < playerSlots.size(); i++){
            CardSlot slot = playerSlots.get(i);
            if(slot.getId().toString().contains(String.format("%s_HandSlot", this.id))){
                playerHandSlots.add(slot);
            }else if(slot.getId().toString().contains(String.format("%s_Slot", this.id))){
                playerBoardSlots.add(slot);
            }else if(slot.getId().toString().contains(String.format("%s_Deck", this.id))){
                deckSlot = slot;
            }
        }
    }
    public void drawCard(){
        Card card = deck.drawCard();
        if(card == null){
            HP--;
        }else{
            System.out.println("Drawn card:\t" + card.getName());
        }
        for(int i = 0; i < playerHandSlots.size(); i++){
            CardSlot slot = playerHandSlots.get(i);
            System.out.println("\tChecking slot:\t" + slot.getId().toString());
            System.out.println("\tCards in the hand:\t" + cardsInHand);
            if(!slot.cardOnBoard() && handSizeLimit > cardsInHand){
                slot.setCard(card);
                System.out.println("\tCard placed on: " + slot.getId());
                if(game.sound_drawEffectClip == null) {
                    game.sound_drawEffectClip = game.musicPlayer.playSound(game.sound_drawEffect);
                    game.musicPlayer.repeatSound(game.sound_drawEffectClip);
                }else{
                    game.musicPlayer.repeatSound(game.sound_drawEffectClip);
                }
                cardsInHand++;
                break;
            }
        }
    }
    public boolean placeCard(CardSlot slot, Card card){
        if(enoughManaForCard(card)) {
            slot.setCard(card);
            if(!isSuper()) {
                slot.setAttackedThisTurn();
            }
            if(game.sound_placedEffectClip == null) {
                game.sound_placedEffectClip = game.musicPlayer.playSound(game.sound_placedEffect);
                game.musicPlayer.repeatSound(game.sound_placedEffectClip);
            }else{
                game.musicPlayer.repeatSound(game.sound_placedEffectClip);
            }
            cardsInHand--;
            decreaseMana(card.getManaCost());
            //Mana = Mana - card.getManaCost();
            return true;
        }else{
            return false;
        }
    }
    public void addMana(){
        if(ManaCapacity < 5){
            ManaCapacity++;
        }
    }
    public void addHP(int amount){
        HP += amount;
    }
    public void decreaseHP(int amount){
        HP = HP - amount;
    }
    public void refillMana(){
        if(Mana != 0 && ManaStackCapacity > ManaStack){
            ManaStack += Mana;
            if(ManaStack > 3){
                ManaStack = 3;
            }
        }
        Mana = ManaCapacity;
        if(ManaStack == ManaStackCapacity && ManaCapacity == 5){
            supper = true;
        }else{
            supper = false;
        }
    }
    public boolean isSuper(){
        return this.supper;
    }
    public void takeDamage(int damage){
        HP-=damage;
    }
    public boolean attack(CardSlot attacker, CardSlot defender){
        Monster attMonster = (Monster)attacker.getCard();
        Monster defMonster = (Monster)defender.getCard();
        int damage = attMonster.getAttack() - defMonster.getDef();
        if(damage > 0){
            game.setAttacking(attacker.getIndex(), false);
            game.targetX = defender.getX() - attacker.getX();
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            phase.getOpponent().takeDamage(damage);
                            attacker.setAttackedThisTurn();
                            defender.removeCard();
                            if(game.sound_deathClip == null) {
                                game.sound_deathClip = game.musicPlayer.playSound(game.sound_death);
                                game.musicPlayer.repeatSound(game.sound_deathClip);
                            }else{
                                game.musicPlayer.repeatSound(game.sound_deathClip);
                            }
                        }
                    },
                    500
            );
            return true;
        }else if (damage == 0){
            game.setAttacking(attacker.getIndex(), false);
            game.targetX = defender.getX() - attacker.getX();
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            attacker.setAttackedThisTurn();
                            attacker.removeCard();
                            if(game.sound_deathClip == null) {
                                game.sound_deathClip = game.musicPlayer.playSound(game.sound_death);
                                game.musicPlayer.repeatSound(game.sound_deathClip);
                            }else{
                                game.musicPlayer.repeatSound(game.sound_deathClip);
                            }
                            defender.removeCard();
                        }
                    },
                    500
            );

            return true;
        }else if(damage < 0){
            game.setAttacking(attacker.getIndex(), false);
            game.targetX = defender.getX() - attacker.getX();
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            attacker.setAttackedThisTurn();
                            attacker.removeCard();
                            if(game.sound_deathClip == null) {
                                game.sound_deathClip = game.musicPlayer.playSound(game.sound_death);
                                game.musicPlayer.repeatSound(game.sound_deathClip);
                            }else{
                                game.musicPlayer.repeatSound(game.sound_deathClip);
                            }
                        }
                    },
                    500
            );

            return true;
        }else{
            return false;
        }
    }
    public boolean enoughManaForCard(Card card){
        if(card.getManaCost() <= this.getMana() + this.ManaStack){
            return true;
        }else{
            return false;
        }
    }

    public void decreaseCardsInHandCount(){
        cardsInHand--;
    }
    public void decreaseMana(int decrease){
        int temp = decrease;

        while(ManaStack != 0 && temp > 0){
            ManaStack--;
            temp--;
        }
        this.Mana -= temp;
    }
    public boolean opponentHasMonsterOnTheBoard(){
        for(CardSlot slot : this.opponent.playerBoardSlots){
            if(slot.cardOnBoard() && slot.getCard().getID() == ID.Monster){
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        int width = display.getWidth();
        int Height = display.getHeight();
        // draw mana in board
        Font prevFont = g.getFont();
        Font font1 = new Font( Font.SANS_SERIF, 3, (int)((Height + width) * 0.02));
        Color clr = Color.MAGENTA;
        Color clrop = Color.GRAY;
        Color clrprev = Color.WHITE;
        if(id == ID.Player1){
            Font font = new Font( Font.SANS_SERIF, 3, (int)((Height + width) * 0.00933));

            g.setFont(font);
            g.drawString(String.format("%s", getMana()), (int)(width * 0.007), (int)(Height*0.605));
            g.drawString(String.format("%s", getHP()), (int)(width * 0.005), (int)(Height*0.505));
            g.drawString(String.format("%s", getManaStack()) + String.format("/%s", ManaStackCapacity), (int)(width * 0.054), (int)(Height*0.615));
            if(this.isSuper()){
                g.setColor(clr);
                g.setFont(font1);
                g.drawString(String.format("%s", "S"), (int)(width * 0.053), (int)(Height*0.515));
            }
            g.setColor(clrprev);
            g.setFont(prevFont);
        }else{
            Font font = new Font( Font.SANS_SERIF, 3, (int)((Height + width) * 0.00933));
            g.setFont(font);
            g.drawString(String.format("%s", getMana()), (int)(width * 0.007), (int)(Height*0.199));
            g.drawString(String.format("%s", getHP()), (int)(width * 0.005), (int)(Height*0.3));
            g.drawString(String.format("%s", getManaStack()) + "/3", (int)(width * 0.054), (int)(Height*0.194));
            g.setFont(font1);
            if(this.isSuper()){
                g.setColor(clrop);
                g.setFont(font1);
                g.drawString(String.format("%s", "S"), (int)(width * 0.0545), (int)(Height*0.31));
            }
            g.setColor(clrprev);
            g.setFont(prevFont);
        }
    }
}
