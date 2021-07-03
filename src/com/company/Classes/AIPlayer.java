package com.company.Classes;

import com.company.Engine.Display;
import com.company.Engine.Game;

import java.io.Serializable;
import java.util.ArrayList;

public class AIPlayer extends Player  implements Serializable {
    public AIPlayer(ID _id, Deck _deck, ArrayList<CardSlot> slots, Display _display, Game _game) {
        super(_id, _deck, slots, _display, _game);
    }
    //===================================
    // Use only for AI opponent
    //===================================
    public boolean setCardOnBoardAI(CardSlot handSlot){
        Card card = handSlot.getCard();
        boolean cardPlaced = false;
        if(handSlot != null) {
            for (int i = 0; i < playerBoardSlots.size(); i++) {
                CardSlot slot = playerBoardSlots.get(i);
                if (!slot.cardOnBoard()) { // If its AI board slot which doesn't have a card, card is placed on this slot
                    decreaseMana(card.getManaCost());
                    slot.setCard(card);
                    if(!isSuper()) {
                        slot.setAttackedThisTurn();
                    }
                    handSlot.removeCard();
                    decreaseCardsInHandCount();
                    cardPlaced = true;
                    break;
                }
            }
        }
        return cardPlaced;
    }

    private CardSlot AIHealingCard(){
        CardSlot HP_Card = null;
        for(int i = 0; i < playerHandSlots.size(); i++){
            CardSlot slot = playerHandSlots.get(i);
            if(slot.cardOnBoard()){
                Card card = slot.getCard();
                if(card.getID() == ID.Buff && card.getName().contains("HP")){
                    HP_Card = slot;
                    break;
                }
            }
        }
        return HP_Card;
    }
    private CardSlot AIDestroyCurse(){
        CardSlot destroy = null;
        for(CardSlot slot : this.playerHandSlots){
            if(slot.cardOnBoard() && slot.getCard().getID() == ID.Curse){
                Curse curse = (Curse)slot.getCard();
                if(curse.getEffect().equals("destroy") && enoughManaForCard(curse)){
                    destroy = slot;
                    return destroy;
                }
            }
        }
        return destroy;
    }
    private CardSlot AIStunCurse(){
        CardSlot stun = null;
        for(CardSlot slot : this.playerHandSlots){
            if(slot.cardOnBoard() && slot.getCard().getID() == ID.Curse){
                Curse curse = (Curse)slot.getCard();
                if(curse.getEffect().equals("stun") && enoughManaForCard(curse)){
                    stun = slot;
                    return stun;
                }
            }
        }
        return stun;
    }
    private CardSlot HPCurseAI(){
        CardSlot hp_curse = null;
        for(CardSlot slot : this.playerHandSlots){
            if(slot.cardOnBoard() && slot.getCard().getID() == ID.Curse){
                Curse curse = (Curse)slot.getCard();
                if(curse.getEffect().equals("hp") && enoughManaForCard(curse)){
                    hp_curse = slot;
                    return hp_curse;
                }
            }
        }
        return hp_curse;
    }
    private int numberOfMonsters(Player player){
        int count = 0;
        for(CardSlot slot : player.playerBoardSlots){
            if(slot.cardOnBoard() && slot.getCard().getID() == ID.Monster){
                count++;
            }
        }
        return count;
    }
    private CardSlot strongestMonsterInHandAI(){
        CardSlot strongest = null;
        for(int i = 0; i < playerHandSlots.size(); i++){
            CardSlot slot = playerHandSlots.get(i);
            if(slot.cardOnBoard()){
                if(slot.getCard().getID() == ID.Monster){
                    Monster monster = (Monster)slot.getCard();
                    if(enoughManaForCard(monster)){
                        if(strongest == null){
                            strongest = slot;
                        }else{
                            Monster current = (Monster)strongest.getCard();
                            // If current strongest monster has less power than monster in the slot -> "slot"
                            if(current.getAttack() + current.getDef() < monster.getAttack() + monster.getDef()){
                                strongest = slot;
                            }
                        }
                    }
                }
            }
        }
        return strongest;
    }
    private boolean AIHasMonsterOnTheBoard(){
        for(CardSlot slot : this.playerBoardSlots){
            if(slot.cardOnBoard() && slot.getCard().getID() == ID.Monster){
                return true;
            }
        }
        return false;
    }
    public boolean opponentHasMonsterOnTheBoard(){
        for(CardSlot slot : this.opponent.playerBoardSlots){
            if(slot.cardOnBoard() && slot.getCard().getID() == ID.Monster){
                return true;
            }
        }
        return false;
    }
    private CardSlot strongestOppMonsterOnBoard(){
        CardSlot strongest = null;
        Monster monsterStrongest = null;
        for(CardSlot slot : this.opponent.playerBoardSlots){
            if(slot.cardOnBoard() && slot.getCard().getID() == ID.Monster && !((Monster)(slot.getCard())).getWasAttacked()){
                Monster monster = (Monster)slot.getCard();
                if(strongest == null){
                    strongest = slot;
                    monsterStrongest = (Monster)strongest.getCard();
                }else if(monster.getAttack() + monster.getDef() > monsterStrongest.getAttack() + monster.getDef()){
                    strongest = slot;
                    monsterStrongest = monster;
                }
            }
        }
        return strongest;
    }
    private CardSlot strongestDefenderInHandAI(){
        CardSlot defender = null;
        Monster bestDefender = null;
        for(CardSlot slot : this.playerHandSlots){
            if(slot.cardOnBoard() && slot.getCard().getID() == ID.Monster && enoughManaForCard(slot.getCard())){
                Monster monster = (Monster)slot.getCard();
                if(defender == null){
                    defender = slot;
                    bestDefender = monster;
                }else if(monster.getDef() > bestDefender.getDef()){
                    defender = slot;
                    bestDefender = monster;
                }
            }
        }
        return defender;
    }
    private CardSlot bestAttBuffAI(){
        CardSlot buff = null;
        for(CardSlot slot : playerHandSlots){
            if(slot.cardOnBoard() && slot.getCard().getID() == ID.Buff && enoughManaForCard(slot.getCard())){
                Buff buffCard = (Buff)slot.getCard();
                if(buffCard.getEffect().equals("atk")){
                    if(buff == null){
                        buff = slot;
                    }else{
                        if(((Buff)buff.getCard()).getEffectNum() < buffCard.getEffectNum()){
                            buff = slot;
                        }
                    }
                }
            }
        }
        return buff;
    }
    private CardSlot bestDefBuffAI(){
        CardSlot buff = null;
        for(CardSlot slot : playerHandSlots){
            if(slot.cardOnBoard() && slot.getCard().getID() == ID.Buff && enoughManaForCard(slot.getCard())){
                Buff buffCard = (Buff)slot.getCard();
                if(buffCard.getEffect().equals("def")){
                    if(buff == null){
                        buff = slot;
                    }else{
                        if(((Buff)buff.getCard()).getEffectNum() < buffCard.getEffectNum()){
                            buff = slot;
                        }
                    }
                }
            }
        }
        return buff;
    }
    /*
        Start phase logic how AI selects cards.
        1. Checks if he is low on hp. If true tries to find heal card and heal himself.
        2. Checks if opponent has monster on the board which cannot be killed with any monster on the board
           or in the hand(<- has enough mana for it) then it tries to curse him with stun or destroy.
        3. Places the strongest monster in the hand if it cannot be killed during next turn based by current situation
           otherwise places monster with the highest defense (to sustain damage to prevent direct damage to HP).
     */
    public void startPhaseSequenceAI(){
        possibleToDefeat = false; // Marks if it's possible to defeat the strongest opponent monster.
        boolean cursed = false; // We need to know if the strongest opponent monster was cursed.
        boolean cannotKillMonsterInHand = false;
        CardSlot strongestOppMonster = strongestOppMonsterOnBoard(); // The strongest monster on the opponent board side.
        CardSlot monsterInHand = strongestMonsterInHandAI();

        // Checks if AI is low hp, if true, looks for healing card then heals itself.
        if (AILowHP()) {
            CardSlot heal = AIHealingCard();
            if(heal != null && enoughManaForCard(heal.getCard())){
                Mana -= heal.getCard().getManaCost();
                HP += 3;
                heal.removeCard();
                cardsInHand--;
            }
        }

        // Updates possibleToDefeat.
        if(opponentHasMonsterOnTheBoard()) {
            Monster oppM = (Monster)strongestOppMonster.getCard();
            CardSlot strongestMonsterOnBoard = null; // The strongest AI's monster on the board.

            if(!possibleToDefeat){
                // Checks if AI's monster on the board can kill the strongest opponent monster.
                if(AIHasMonsterOnTheBoard()){
                    strongestMonsterOnBoard = strongestAttackerOnBoardAI();
                    if(strongestMonsterOnBoard != null) {
                        Monster onBoardM = (Monster) strongestMonsterOnBoard.getCard();
                        if (oppM.getDef() <= onBoardM.getAttack()) {
                            boolean worthToAttack; // If def==atk then it means, that both monsters die, so we need to check if it's worth.
                            if (oppM.getDef() < onBoardM.getAttack()) {
                                worthToAttack = true;
                            } else { // If opponent monster has more power then it's worth.
                                worthToAttack =
                                        oppM.getDef() + oppM.getAttack() > onBoardM.getAttack() + onBoardM.getDef() ? true : false;
                            }
                            possibleToDefeat = worthToAttack;
                        }
                    }
                }
                // Checks if the strongest monster in the hand(has enough mana for it) can kill the strongest opponent monster
                // or it cannot be killed by the strongest opponent monster.
                if(!possibleToDefeat && monsterInHand != null){
                    Monster inHandM = (Monster)monsterInHand.getCard();
                    if(oppM.getAttack() < inHandM.getDef()){
                        cannotKillMonsterInHand = true;
                    }
                    if(oppM.getDef() <= inHandM.getAttack()){
                        boolean worthToAttack; // Same as above.
                        if(oppM.getDef() < inHandM.getAttack()){ worthToAttack = true; }
                        else {
                            worthToAttack =
                                    oppM.getDef() + oppM.getAttack() > inHandM.getAttack() + inHandM.getDef() ? true : false;
                        }
                        possibleToDefeat = worthToAttack;
                    }
                }
            }
        }else{
            cannotKillMonsterInHand = true;
        }

        // If it's not possible to defeat the strongest opponent monster then AI tries to destroy or stun him.
        if(!possibleToDefeat){
            CardSlot destroy = null;
            CardSlot stun = null;
            if( (destroy = AIDestroyCurse()) != null){
                Curse curseCard = (Curse)destroy.getCard();
                if(strongestOppMonster != null) {
                    curseCard.curseLogic(strongestOppMonster, this, this.opponent);
                    destroy.removeCard();
                    cursed = true;
                }
            }else if( (stun = AIStunCurse()) != null){
                Curse curseCard = (Curse)stun.getCard();
                if(strongestOppMonster != null) {
                    curseCard.curseLogic(strongestOppMonster, this, this.opponent);
                    stun.removeCard();
                    cursed = true;
                }
            }
        }

        boolean continueLoop = true; // Need this one to prevent infinite loop when AI has mana but can't do anything,
        // it marks if at least one action was made the last time in the loop.


        // Loop which tries to spend as much mana as possible.
        while(this.Mana > 0 && continueLoop) {
            continueLoop = false;

            // Places the strongest monster in the hand if it won't be killed, otherwise tries to place the strongest defender
            // in the hand or if there is no monsters in the hand which could be placed when tries to use buffs/curses.
            CardSlot defender = strongestDefenderInHandAI();
            // (if) -> Strongest monster in the hand. (else if) -> Monster with the highest defense.
            if(monsterInHand != null && monsterInHand.cardOnBoard() && enoughManaForCard(monsterInHand.getCard()) && (cannotKillMonsterInHand || possibleToDefeat || cursed) && setCardOnBoardAI(monsterInHand)){
                continueLoop = true;
            }else if(defender != null && setCardOnBoardAI(defender)){
                continueLoop = true;
            }
            // Buffs and Curses
            else{
                CardSlot defBuff = bestDefBuffAI();
                CardSlot atkBuff = bestAttBuffAI();
                if(defBuff != null){
                    CardSlot bestAttacker = strongestAttackerOnBoardAI();
                    if(bestAttacker != null){
                        if(((Buff)defBuff.getCard()).buffLogic(bestAttacker, this)) {
                            continueLoop = true;
                            defBuff.removeCard();
                        }
                    }
                }else if(atkBuff != null){
                    CardSlot bestDefender = strongestDefenderOnBoardAI();
                    if(bestDefender != null){
                        if( ((Buff)atkBuff.getCard()).buffLogic(bestDefender, this) ){
                            continueLoop = true;
                            atkBuff.removeCard();
                        }
                    }
                }else{
                    CardSlot hp_curse = HPCurseAI();
                    if(hp_curse != null){
                        ((Curse)hp_curse.getCard()).hpCurseLogic(this, this.opponent, hp_curse);
                        continueLoop = true;
                    }
                }

            }

        }
    }

    /*
        Finds the strongest AI monster(with the highest attack) on the board who is going to attack
        or will be "buffed" with defense buff.
     */
    private CardSlot strongestAttackerOnBoardAI(){
        CardSlot strongest = null;
        for(int i = 0; i < playerBoardSlots.size(); i++){
            CardSlot slot = playerBoardSlots.get(i);
            if(slot.cardOnBoard() && slot.getCard().getID() == ID.Monster && ((Monster)slot.getCard()).stunTime == 0 && !slot.attackedThisTurn()){
                if(strongest == null || ((Monster)strongest.getCard()).getAttack() < ((Monster)slot.getCard()).getAttack()){
                    strongest = slot;
                }
            }
        }
        return strongest;
    }
    /*
        Finds a monster with the highest Defense on AI's board.
        (Using this to "buff" this monster with attack buff).
     */
    private CardSlot strongestDefenderOnBoardAI(){
        CardSlot strongest = null;
        for(int i = 0; i < playerBoardSlots.size(); i++){
            CardSlot slot = playerBoardSlots.get(i);
            if(slot.cardOnBoard() && slot.getCard().getID() == ID.Monster && ((Monster)slot.getCard()).stunTime == 0 && !slot.attackedThisTurn()){
                if(strongest == null || ((Monster)strongest.getCard()).getDef() < ((Monster)slot.getCard()).getDef()){
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
        if(strongestAttackerOnBoardAI() != null) { // If there are no AI monsters on the board.
            Monster strongestAttacker = (Monster) strongestAttackerOnBoardAI().getCard();
            Player opponent = phase.getOpponent();
            CardSlot strongestOpp = null;
            for (int i = 0; i < opponent.playerBoardSlots.size(); i++) {
                CardSlot slot = opponent.playerBoardSlots.get(i);
                if(slot.cardOnBoard() && slot.getCard().getID() == ID.Monster){
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

    //================================
    // AI's attack phase sequence logic.
    //================================
    public void attackAI(){
        /*
            If AI and Opponent have monsters on the board, this method goes through all opponent monsters and returns the strongest
            one which wasn't attacked this turn (check -> "strongestOppMonsterOnBoard" method) if the strongest AI monster can kill him
            or it's worth to attack him even if both of them will die then AI attacks opponent monster. If AI can't kill the strongest
            opponent monster or it's not worth to attack him, that monster is marked as "attacked" (look Monster class -> wasAttacked).
            After that while loop tries to find another strongest opponent monster which wasn't attacked.
         */
        if(AIHasMonsterOnTheBoard()){
            if(opponentHasMonsterOnTheBoard()){
                CardSlot strongestOppMonster;
                while((strongestOppMonster = strongestOppMonsterOnBoard()) != null){
                    CardSlot strongestAIMonster = strongestAttackerOnBoardAI();
                    if(strongestAIMonster != null){
                        Monster oppMonster = (Monster)strongestOppMonster.getCard();
                        Monster AIMonster = (Monster)strongestAIMonster.getCard();
                        int damage = AIMonster.getAttack() - oppMonster.getDef();
                        if(damage > 0){ // If AI monster can kill opponent monster without dying.
                            opponent.takeDamage(damage);
                            strongestAIMonster.setAttackedThisTurn();
                            strongestOppMonster.removeCard();
                        }else if(damage == 0){ // If both monsters will die we need to check if it's worth to sacrifice AI's monster.
                            boolean worthToAttack =
                                    (oppMonster.getAttack() + oppMonster.getDef() > AIMonster.getAttack() + AIMonster.getDef()) ? true : false;
                            // It's worth to sacrifice AI's monster if opponent monster has more total power or AI has more or equal
                            // number of monsters as opponent.
                            if(worthToAttack || (numberOfMonsters(this) >= numberOfMonsters(this.opponent))){
                                strongestAIMonster.removeCard();
                                strongestOppMonster.removeCard();
                            }else{ // If it's not worth it, mark an opponent monster as "attacked".
                                oppMonster.setWasAttacked(true);
                            }
                        }else{ // If AI cannot kill an opponent monster mark him as "attacked".
                            oppMonster.setWasAttacked(true);
                        }
                    }else{ // If AI has no monsters that can attack this turn then mark opponent monsters as "attacked".
                        ((Monster)strongestOppMonster.getCard()).setWasAttacked(true);
                    }
                }
            }
            // If opponent has no monsters on the board then AI does damage to opponent (HP).
            if(!opponentHasMonsterOnTheBoard()){
                CardSlot strongestAIAttacker;
                while((strongestAIAttacker = strongestAttackerOnBoardAI()) != null) {
                    Monster monster = (Monster) strongestAIAttacker.getCard();
                    opponent.takeDamage(monster.getAttack());
                    strongestAIAttacker.setAttackedThisTurn();
                }
            }
        }
    }

    public boolean AILowHP(){ return this.HP <= 10; }
    //=========================================================
    // The end of AI methods.
    //=========================================================
}
