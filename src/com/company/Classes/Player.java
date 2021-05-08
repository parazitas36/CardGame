package com.company.Classes;

import com.company.Engine.Display;
import com.company.Engine.Game;

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
    public ArrayList<CardSlot> playerHandSlots;
    public ArrayList<CardSlot> playerBoardSlots;
    public CardSlot deckSlot;
    private int handSizeLimit, cardsInHand;
    private Phase phase;
    private Display display;
    public Player opponent;
    private boolean possibleToDefeat;
    private Game game;
    public Player(ID _id, Deck _deck, ArrayList<CardSlot> slots, Display _display, Game _game){
        HP = 10;
        if(_id == ID.Player2){ // Just for testing reasons
            HP = 20;
        }
        Mana = 1;
        ManaCapacity = 1;
        ManaStack = 0;
        ManaStackCapacity = 3;
        id = _id;
        deck = _deck;
        playerSlots = slots;
        filterSlots();
        handSizeLimit = 7;
        cardsInHand = 0;
        this.display = _display;
        possibleToDefeat = false;
        game = _game;
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
    private void filterSlots(){
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
        for(int i = 0; i < playerHandSlots.size(); i++){
            CardSlot slot = playerHandSlots.get(i);
            if(!slot.cardOnBoard() && handSizeLimit > cardsInHand){
                slot.setCard(card);
                cardsInHand++;
                break;
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
        HP = HP - amount;
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
        int damage = attMonster.getAttack() - defMonster.getDef();
        if(damage > 0){

            System.out.println("555555555555555");
            game.setAttacking(attacker.getIndex(), false);
            game.targetX = defender.getX() - attacker.getX();
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            phase.getOpponent().takeDamage(damage);
                            attacker.setAttackedThisTurn();
                            defender.removeCard();
                        }
                    },
                    500
            );
            return true;
        }else if (damage == 0){
            System.out.println("66666666666666666666");
            game.setAttacking(attacker.getIndex(), false);
            game.targetX = defender.getX() - attacker.getX();
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            attacker.setAttackedThisTurn();
                            attacker.removeCard();
                            defender.removeCard();
                        }
                    },
                    500
            );

            return true;
        }else if(damage < 0){
            System.out.println("77777777777777777");
            game.setAttacking(attacker.getIndex(), false);
            game.targetX = defender.getX() - attacker.getX();
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            attacker.setAttackedThisTurn();
                            attacker.removeCard();
                        }
                    },
                    500
            );

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
                    Mana-=card.getManaCost();
                    slot.setCard(card);
                    handSlot.removeCard();
                    cardsInHand--;
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
    private boolean opponentHasMonsterOnTheBoard(){
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
            if(slot.cardOnBoard() && slot.getCard().getID() == ID.Monster){
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

        boolean continueLoop = true; // Need this one to prevent infinite loop when player has mana but can't do anything.
        while(this.Mana > 0 && continueLoop) {
            continueLoop = false;

            CardSlot defender = strongestDefenderInHandAI();
            if(monsterInHand != null && monsterInHand.cardOnBoard() && enoughManaForCard(monsterInHand.getCard()) && (cannotKillMonsterInHand || possibleToDefeat || cursed) && setCardOnBoardAI(monsterInHand)){
                continueLoop = true;
            }else if(defender != null && setCardOnBoardAI(defender)){
                continueLoop = true;
            }
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
        Finds the strongest AI monster(with the highest attack) on the board who is going to attack.
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

    /*
        Finds the second strongest opponent monster on the board which could be defeated by the strongest AI monster.
     */
    private CardSlot secondStrongestOpponentPossibleToDefeatAI(){
        if(strongestAttackerOnBoardAI() != null) { // If there are no AI monsters on the board.
            Monster strongestAttacker = (Monster) strongestAttackerOnBoardAI().getCard();
            Player opponent = phase.getOpponent();
            CardSlot strongestOpp = strongestOpponentPossibleToDefeatAI();
            CardSlot secondStrongest = null;
            Monster secondStrongestMonster = null;
            for (int i = 0; i < opponent.playerBoardSlots.size(); i++) {
                CardSlot slot = opponent.playerBoardSlots.get(i);
                if(slot.cardOnBoard() && slot.getCard().getID() == ID.Monster){
                    Monster slotMonster = ((Monster)slot.getCard());
                    // If the strongest opponent monster is null or it's power(atk+def) is less than the monster's power on the board slot ->"slot",
                    // then the strongest opponent monster is on the board slot -> "slot".
                    if(slot.getId() != strongestOpp.getId() && ( secondStrongest == null || (secondStrongestMonster.getAttack() + secondStrongestMonster.getDef()) <
                            (slotMonster.getAttack() + slotMonster.getDef()) ) && strongestAttacker.getAttack() >= slotMonster.getDef() ){
                        secondStrongest = slot;
                        secondStrongestMonster = (Monster)secondStrongest.getCard();
                    }
                }
            }
            return secondStrongest;
        }else{
            return null;
        }
    }

    //--------------------------------
    // AI's attack logic.
    //--------------------------------
    public void attackAI(){
        CardSlot attacker = strongestAttackerOnBoardAI();
        CardSlot defender = strongestOpponentPossibleToDefeatAI();
        if(attacker != null && defender != null){
            System.out.println("ATK: " + attacker.getCard().toString());
            System.out.println("DEF: " + defender.getCard().toString());
            int damage = ((Monster)attacker.getCard()).getAttack() - ((Monster)defender.getCard()).getDef();
            if(damage > 0) {
                // TODO DD
                game.setAttacking(attacker.getIndex(), true);
                game.targetX = defender.getX() - attacker.getX();
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                phase.getOpponent().takeDamage(damage);
                                attacker.setAttackedThisTurn();
                                defender.removeCard();
                            }
                        },
                        500
                );

            }else if(damage == 0){
                int attackerPower = ((Monster)attacker.getCard()).getAttack() + ((Monster)attacker.getCard()).getDef();
                int defenderPower = ((Monster)defender.getCard()).getAttack() + ((Monster)defender.getCard()).getDef();
                boolean worthToAttack = defenderPower > attackerPower ? true : false;
                if(worthToAttack) {
                    game.setAttacking(attacker.getIndex(), true);
                    game.targetX = defender.getX() - attacker.getX();
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    attacker.setAttackedThisTurn();
                                    defender.removeCard();
                                    attacker.removeCard();
                                }
                            },
                            500
                    );

                }else{
                    attacker.setAttackedThisTurn();
                    attackAI();
//                    defender = secondStrongestOpponentPossibleToDefeatAI();
//                    if(defender != null){
//                        damage = ((Monster)attacker.getCard()).getAttack() - ((Monster)defender.getCard()).getDef();
//                        if(damage > 0){
//                            attacker.setAttackedThisTurn();
//                            defender.removeCard();
//                        }
//                    }
                }
            }
        }
    }

    public boolean AILowHP(){ return this.HP <= 10; }
    //=========================================================
    // The end of AI methods.
    //=========================================================
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
            Font font = new Font( Font.SANS_SERIF, 3, (int)((Height + width) * 0.00933));
            g.setFont(font);
            g.drawString(String.format("%s", getMana()), (int)(width * 0.007), (int)(Height*0.605));
            g.drawString(String.format("%s", getHP()), (int)(width * 0.005), (int)(Height*0.505));
            g.drawString(String.format("%s", getManaStack()) + String.format("/%s", ManaStackCapacity), (int)(width * 0.054), (int)(Height*0.615));
            g.setFont(prevFont);
        }else{
            Font font = new Font( Font.SANS_SERIF, 3, (int)((Height + width) * 0.00933));
            g.setFont(font);
            g.drawString(String.format("%s", getMana()), (int)(width * 0.007), (int)(Height*0.199));
            g.drawString(String.format("%s", getHP()), (int)(width * 0.005), (int)(Height*0.3));
            g.drawString(String.format("%s", getManaStack()) + "/3", (int)(width * 0.054), (int)(Height*0.194));
            g.setFont(prevFont);
        }
    }
}
