package com.company.Classes;

import com.company.Engine.Display;

import java.awt.*;
import java.util.ArrayList;

public class Player  extends GameObject{
    private int HP;
    private int Mana;
    private int ManaCapacity;
    private int ManaStack;
    private int ManaStackCapacity;
    private ID id;
    private Deck deck;
    public ArrayList<CardSlot> playerSlots;
    private int handSizeLimit, cardsInHand;
    private Phase phase;
    private Display display;
    public Player(ID _id, Deck _deck, ArrayList<CardSlot> slots, Display _display){
        HP = 30;
        Mana = 1;
        ManaCapacity = 1;
        ManaStack = 0;
        ManaStackCapacity = 3;
        id = _id;
        deck = _deck;
        playerSlots = slots;
        handSizeLimit = 7;
        cardsInHand = 0;
        this.display = _display;

    }
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
    public void drawCard(){
        Card card = deck.drawCard();
        for(int i = 0; i < playerSlots.size(); i++){
            CardSlot slot = playerSlots.get(i);
            if(slot.getId().toString().contains(String.format("%s_HandSlot", this.id))){
                if(!slot.cardOnBoard()&& handSizeLimit > cardsInHand){
                    slot.setCard(card);
                    cardsInHand++;
                    System.out.println(String.format("Player: %s cards: %d", this.id.toString(), this.cardsInHand));
                    break;
                }
            }
        }
    }
    public boolean placeCard(CardSlot slot, Card card){
        if(enoughManaForCard(card)) {
            slot.setCard(card);
            cardsInHand--;
            Mana = Mana - card.getManaCost();
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
        HP -= amount;
    }
    public void refillMana(){
        Mana = ManaCapacity;
    }
    public void takeDamage(int damage){
        HP-=damage;
    }
    public boolean attack(CardSlot attacker, CardSlot defender){
        Monster attMonster = (Monster)attacker.getCard();
        Monster defMonster = (Monster)defender.getCard();
        if(attMonster.getAttack() >= defMonster.getDef()){
            phase.getOpponent().takeDamage(attMonster.getAttack() - defMonster.getDef());
            attacker.setAttackedThisTurn();
            return true;
        }else{
            return false;
        }
    }
    private boolean enoughManaForCard(Card card){
        if(card.getManaCost() <= this.getMana()){
            return true;
        }else{
            return false;
        }
    }
    public void decreaseCardsInHandCount(){
        cardsInHand--;
    }
    public void decreaseMana(int decrease){
        this.Mana -= decrease;
    }
    //--------------------------
    //Use only for AI opponent
    //--------------------------
    public void setCardOnBoardAI(CardSlot handSlot){
        if(handSlot != null) {
            for (int i = 0; i < playerSlots.size(); i++) {
                CardSlot slot = playerSlots.get(i);
                if (slot.getId().toString().contains(String.format("%s_Slot", this.id)) && !slot.cardOnBoard()) { // If its AI board slot which doesn't have a card, card is placed on this slot
                    slot.setCard(handSlot.getCard());
                    handSlot.removeCard();
                    cardsInHand--;
                }
            }
        }
    }
    public CardSlot pickCardAI(){
        CardSlot chosenCard = null;
        for(int i = 0; i < playerSlots.size(); i++){
            CardSlot slot = playerSlots.get(i);
            if(slot.getId().toString().contains(String.format("%s_HandSlot", this.id)) && slot.cardOnBoard()){
                chosenCard = enoughManaForCard(slot.getCard()) ? slot : null;
                if(chosenCard != null){
                    break;
                }
            }
        }
        return chosenCard;
    }
    /*
        Finds the strongest AI monster(with the highest attack) on the board who is going to attack.
     */
    private CardSlot strongestAttackerAI(){
        CardSlot strongest = null;
        for(int i = 0; i < playerSlots.size(); i++){
            CardSlot slot = playerSlots.get(i);
            if(slot.getId().toString().contains(String.format("%s_Slot", this.id)) && slot.cardOnBoard() && slot.getCard().getID() == ID.Monster){
                if(strongest == null || ((Monster)strongest.getCard()).getAttack() < ((Monster)slot.getCard()).getAttack()){
                    strongest = slot;
                }
            }
        }
        return strongest;
    }
    /*
        Finds the strongest opponent monster on the board which could be defeated by the strongest AI monster.
     */
    private CardSlot strongestOpponentPossibleToDefeatAI(){
        if(strongestAttackerAI() != null) { // If there are no AI monsters on the board.
            Monster strongestAttacker = (Monster)strongestAttackerAI().getCard();
            Player opponent = phase.getOpponent();
            CardSlot strongestOpp = null;
            for (int i = 0; i < opponent.playerSlots.size(); i++) {
                CardSlot slot = opponent.playerSlots.get(i);
                if(slot.getId().toString().contains(String.format("%s_Slot", opponent.getID())) && slot.cardOnBoard() && slot.getCard().getID() == ID.Monster){
                    Monster strongestOppMonster = null;
                    if(strongestOpp != null){
                        strongestOppMonster = ((Monster)strongestOpp.getCard());
                    }
                    Monster slotMonster = ((Monster)slot.getCard());
                    // If the strongest opponent monster is null or it's power(atk+def) is less than the monster's power on the board slot ->"slot",
                    // then the strongest opponent monster is on the board slot -> "slot".
                    if( ( strongestOpp == null || (strongestOppMonster.getAttack() + strongestOppMonster.getDef()) <
                                    (slotMonster.getAttack() + slotMonster.getDef()) ) && strongestAttacker.getAttack() >= slotMonster.getDef() ){
                        strongestOpp = slot;
                    }
                }
            }
            return strongestOpp;
        }else{
            return null;
        }
    }
    //--------------------------------
    // AI's attack logic.
    //--------------------------------
    public void attackAI(){
        CardSlot attacker = strongestAttackerAI();
        CardSlot defender = strongestOpponentPossibleToDefeatAI();
        if(attacker != null && defender != null){
            System.out.println("ATK: " + attacker.getCard().toString());
            System.out.println("DEF: " + defender.getCard().toString());
            if(((Monster)attacker.getCard()).getAttack() >= ((Monster)defender.getCard()).getDef() && ((Monster) attacker.getCard()).getStunTime() == 0) {
                phase.getOpponent().takeDamage( ((Monster)attacker.getCard()).getAttack() - ((Monster)defender.getCard()).getDef() );
                attacker.setAttackedThisTurn();
                defender.removeCard();
            }
        }
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
        if(id == ID.Player1){
            Font font = new Font("", Font.BOLD, (int)((Height + width) * 0.00933));
            g.setFont(font);
            g.drawString(String.format("%s", getMana()), (int)(width * 0.007), (int)(Height*0.605));
            g.drawString(String.format("%s", getHP()), (int)(width * 0.005), (int)(Height*0.505));
            g.drawString(String.format("%s", getManaStack()) + String.format("/%s", ManaStackCapacity), (int)(width * 0.054), (int)(Height*0.615));
            g.setFont(prevFont);
        }else{
            Font font = new Font("", Font.BOLD, (int)((Height + width) * 0.00933));
            g.setFont(font);
            g.drawString(String.format("%s", getMana()), (int)(width * 0.007), (int)(Height*0.199));
            g.drawString(String.format("%s", getHP()), (int)(width * 0.005), (int)(Height*0.3));
            g.drawString(String.format("%s", getManaStack()) + "/3", (int)(width * 0.054), (int)(Height*0.194));
            g.setFont(prevFont);
        }
    }
}
