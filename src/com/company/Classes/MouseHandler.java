package com.company.Classes;

import com.company.Engine.Game;
import com.company.Enums.ID;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MouseHandler implements MouseListener, MouseMotionListener {

    private Canvas canvas;
    private Game game;
    private CardSlot enteredSlot; // need this one for lifting up a card when mouse is on it

    ArrayList<CardSlot> cardSlots;
    CardSlot attacker; // card slot which has a monster that is going to attack
    public MouseHandler(Canvas canvas1, Game game){
        canvas = canvas1;
        canvas.addMouseListener(this);
        attacker = null;
        enteredSlot = null;
        this.game = game;
        canvas.addMouseMotionListener(this);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //===================================
        // MENU
        //===================================
        if(game.gameState.isMenu && !game.gameState.isOptions){
            // Clicked on Start button
            if(e.getX() >= game.gameState.imgX && e.getX() <= game.gameState.imgX +  game.gameState.imgW && e.getY() <= game.gameState.imgYOffSet + game.gameState.imgH && e.getY() >= game.gameState.imgYOffSet){
                if(game.menuMusicClip != null){
                    game.menuMusicClip.stop();
                    game.menuMusicClip.setFramePosition(0);
                }
                game.gameState.isMenu = false;
                game.gameState.isLoading = true;
                game.startGame();
                game.gameState.isLoading = false;
                game.gameState.isGame = true;
                game.gameState.startGame = true;
            }
            // CLicked on Options button
            else if(e.getX() >= game.gameState.imgX && e.getX() <= game.gameState.imgX +  game.gameState.imgW && e.getY() <= game.gameState.imgYOffSet + 2*game.gameState.imgH + game.gameState.imgYButtonOffSet && e.getY() >= game.gameState.imgYOffSet + game.gameState.imgH + game.gameState.imgYButtonOffSet){
                game.gameState.isOptions = true;
            }
            // Clicked on Exit button
            else if(e.getX() >= game.gameState.imgX && e.getX() <= game.gameState.imgX +  game.gameState.imgW && e.getY() <= game.gameState.imgYOffSet + 3*game.gameState.imgH + 2*game.gameState.imgYButtonOffSet && e.getY() >= game.gameState.imgYOffSet + 2*game.gameState.imgH + 2*game.gameState.imgYButtonOffSet){
                game.exit();
            }return;
        }
        if(game.gameState.isOptions){
            if(e.getX() >= game.gameState.imgX && e.getX() <= game.gameState.imgX +  game.gameState.imgW && e.getY() <= game.gameState.imgYOffSet + game.gameState.imgH && e.getY() >= game.gameState.imgYOffSet){
                game.SetDisplaySize(1366, 768);
            }
            // CLicked on Options button
            else if(e.getX() >= game.gameState.imgX && e.getX() <= game.gameState.imgX +  game.gameState.imgW && e.getY() <= game.gameState.imgYOffSet + 2*game.gameState.imgH + game.gameState.imgYButtonOffSet && e.getY() >= game.gameState.imgYOffSet + game.gameState.imgH + game.gameState.imgYButtonOffSet){
                game.SetDisplaySize(1600, 900);
            }
            // Clicked on Exit button
            else if(e.getX() >= game.gameState.imgX && e.getX() <= game.gameState.imgX +  game.gameState.imgW && e.getY() <= game.gameState.imgYOffSet + 3*game.gameState.imgH + 2*game.gameState.imgYButtonOffSet && e.getY() >= game.gameState.imgYOffSet + 2*game.gameState.imgH + 2*game.gameState.imgYButtonOffSet){
                game.SetDisplaySize(1920, 1080);
            }
            else if(e.getX() >= game.gameState.imgX && e.getX() <= game.gameState.imgX +  game.gameState.imgW && e.getY() <= game.gameState.imgYOffSet + 4*game.gameState.imgH + 3*game.gameState.imgYButtonOffSet && e.getY() >= game.gameState.imgYOffSet + 3*game.gameState.imgH + 3*game.gameState.imgYButtonOffSet){
                game.gameState.isOptions = false;
            }
        }
        //===================================

        //===================================
        // End Game Window
        //===================================
        if(game.gameState.celebrationWindow){
            if(e.getX()>=(int)(game.display.getWidth()*0.5-game.display.getWidth()*0.1) && e.getX() <= (int)(game.display.getWidth()*0.5-game.display.getWidth()*0.1)+(int)(game.display.getWidth()*0.2) &&
                    e.getY() >= (int)(game.display.getHeight()*0.5)+(int)(game.display.getHeight()*0.1) && e.getY() <= (int)(game.display.getHeight()*0.5)+(int)(game.display.getHeight()*0.1) + (int)(game.display.getHeight() * 0.1)){
                game.gameState.celebrationWindow = false;
                game.gameState.isMenu = true;
                if(game.menuMusicClip == null){
                    //game.menuMusicClip = game.musicPlayer.playMusic(game.menuMusic);
                    //game.musicPlayer.repeatMusic(game.menuMusicClip);
                }else if(!game.menuMusicClip.isRunning()){
                    //game.musicPlayer.repeatMusic(game.menuMusicClip);
                }
            }
        }
        //===================================

        //===================================
        // Game
        //===================================
        if(game.gameState.isGame) {
            // Phase button
            if(e.getX() >= game.phase.GetEndTurnPosX() && e.getX() <= game.phase.GetEndTurnPosX() +  game.phase.GetEndTurnImgWidth() && e.getY() <= game.phase.GetEndTurnPosY() + game.phase.GetEndTurnImgHeight() && e.getY() >= game.phase.GetEndTurnPosY()){
                game.phase.nextPhase();
                return;
            }
            //-------------------------------------------------------------
            // If it's not an attack phase, but attacker is still selected.
            //-------------------------------------------------------------
            if (!game.phase.attackPhase() && attacker != null) {
                attacker.isAttacking = false;
                attacker = null;
            }
            //---------------------------------------

            //---------------------------------------
            // Cancels attack with right mouse click
            //---------------------------------------
            if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
                if (attacker != null) {
                    attacker.isAttacking = false;
                    attacker = null;
                }
            }
            //---------------------------------------

            //---------------------------------------
            // Chooses a card which will be dragged
            //---------------------------------------
            for (CardSlot c : game.currentPlayer.playerHandSlots) {
                if ((e.getModifiers() == MouseEvent.BUTTON1_MASK) && e.getX() >= c.getX() && e.getX() <= c.getX() + c.getWidth() && e.getY() <= c.getY() + c.getHeight() && e.getY() >= c.getY()) {
                    if (c.getId() != ID.Player1_Deck) {
                        if (c.getCard() != null)
                            game.SlotClicked(c.getCard(), c);
                    }
                }
            }
            //---------------------------------------

            //---------------------------------------
            // Chooses a monster which is going to attack.
            //---------------------------------------
            int index = 0;
            for (CardSlot c : game.currentPlayer.playerBoardSlots) {
                index++;
                if ((e.getModifiers() == MouseEvent.BUTTON1_MASK) && e.getX() >= c.getX() && e.getX() <= c.getX() + c.getWidth() && e.getY() <= c.getY() + c.getHeight() && e.getY() >= c.getY()) {
                    if (c.isCardOnBoard()) {
                        if (game.phase.attackPhase()) {
                            if (c.getCard().getID() == ID.Monster && !c.didAttackThisTurn() && ((MonsterCard) c.getCard()).stunTime == 0) {
                                if (attacker == null) {
                                    attacker = c;
                                    attacker.isAttacking = true;
                                    selectedIndex = index;
                                    selectedOffsetX = c.getX();
                                    return;
                                } else {
                                    if(!game.currentPlayer.opponentHasMonsterOnTheBoard() && attacker.equals(c) && !c.didAttackThisTurn()){
                                        game.attackOpponent(attacker, game.currentPlayer);
                                        attacker.setAttackedThisTurn();
                                        attacker.isAttacking = false;
                                        attacker = null;
                                        return;
                                    }else{
                                        attacker.isAttacking = false;
                                        attacker = c;
                                        attacker.isAttacking = true;
                                        selectedIndex = index;
                                        selectedOffsetX = c.getX();
                                        return;
                                        // TODO HERE
                                    }
                                }
                            } else {

                                if (attacker != null) {
                                    selectedIndex = index;
                                    selectedOffsetX = c.getX();

                                    attacker.isAttacking = false;
                                    attacker = null;
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            //---------------------------------------

            //---------------------------------------
            // Selects a monster which will be attacked.
            //---------------------------------------
            for(CardSlot c : game.currentPlayer.opponent.playerBoardSlots){
                if ((e.getModifiers() == MouseEvent.BUTTON1_MASK) && e.getX() >= c.getX() && e.getX() <= c.getX() + c.getWidth() && e.getY() <= c.getY() + c.getHeight() && e.getY() >= c.getY()) {
                    if (c.isCardOnBoard()) {
                        if (game.phase.attackPhase() && attacker != null && c.getCard().getID() == ID.Monster) {
                            //game.setAttacking(selectedIndex - 1);
                            //game.targetX = c.getX() - selectedOffsetX;
                            game.currentPlayer.attack(attacker, c);
                            if(game.sound_attackEffectClip == null) {
                                game.sound_attackEffectClip = game.musicPlayer.playSound(game.sound_attackEffect);
                                game.musicPlayer.repeatSound(game.sound_attackEffectClip);
                            }else{
                                game.musicPlayer.repeatSound(game.sound_attackEffectClip);
                            }
                            attacker.isAttacking = false;
                            attacker = null;
                            return;
                        }
                    }
                }
            }
            //---------------------------------------
        }
        //===================================
    }
    public int selectedIndex, selectedOffsetX;
    @Override
    public void mouseReleased(MouseEvent e) {
        if(game.gameState.isMenu){

            return;
        }
        if(game.gameState.isGame) {
            game.MouseReleased();
        }
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(game.gameState.isMenu){
            if(e.getX() >= game.gameState.imgX && e.getX() <= game.gameState.imgX +  game.gameState.imgW && e.getY() <= game.gameState.imgYOffSet + game.gameState.imgH && e.getY() >= game.gameState.imgYOffSet){
                game.gameState.overStartButton = true;
            }else{
                game.gameState.overStartButton = false;
            }
            if(e.getX() >= game.gameState.imgX && e.getX() <= game.gameState.imgX +  game.gameState.imgW && e.getY() <= game.gameState.imgYOffSet + 2*game.gameState.imgH + game.gameState.imgYButtonOffSet && e.getY() >= game.gameState.imgYOffSet + game.gameState.imgH + game.gameState.imgYButtonOffSet){
                game.gameState.overOptionsButton = true;
            }else{
                game.gameState.overOptionsButton = false;
            }
            if(e.getX() >= game.gameState.imgX && e.getX() <= game.gameState.imgX +  game.gameState.imgW && e.getY() <= game.gameState.imgYOffSet + 3*game.gameState.imgH + 2*game.gameState.imgYButtonOffSet && e.getY() >= game.gameState.imgYOffSet + 2*game.gameState.imgH + 2*game.gameState.imgYButtonOffSet){
                game.gameState.overExitButton = true;
            }else{
                game.gameState.overExitButton = false;
            }
            return;
        }
        if(game.gameState.isGame) {
            for (CardSlot c : game.currentPlayer.playerHandSlots) {
                if (e.getX() >= c.getX() && e.getX() <= c.getX() + c.getWidth() && e.getY() <= c.getY() + c.getHeight() && e.getY() >= c.getY()) {
                    //--------------------------------------------------------
                    // When mouse is moved in/on the card that card is lifted.
                    //--------------------------------------------------------
                    if (enteredSlot == null) {
                        enteredSlot = c;
                        enteredSlot.setY(enteredSlot.getY() - (int) (enteredSlot.getHeight() * 0.4));
                    }
                    //--------------------------------------------------------
                }
            }
            //---------------------------------------------------------------------------------------
            // When mouse exits the card that card is lifted, it comes back to it's initial position.
            //---------------------------------------------------------------------------------------
            if (enteredSlot != null && (e.getX() < enteredSlot.getX() || e.getX() > enteredSlot.getX() + enteredSlot.getWidth() || e.getY() > enteredSlot.getY() + enteredSlot.getHeight() || e.getY() < enteredSlot.getY())) {
                enteredSlot.setY(enteredSlot.getY() + (int) (enteredSlot.getHeight() * 0.4));
                enteredSlot = null;
            }
            //---------------------------------------------------------------------------------------
            CardSlot deck = game.currentPlayer.deckSlot;
            if(e.getX() >= deck.getX() && e.getX() <= deck.getX() + deck.getWidth() && e.getY() <= deck.getY() + deck.getHeight() && e.getY() >= deck.getY()){
                deck.shouldShowCardsCount = true;
            }else{
                deck.shouldShowCardsCount = false;
            }
            CardSlot oppDeck = game.currentPlayer.opponent.deckSlot;
            if(e.getX() >= oppDeck.getX() && e.getX() <= oppDeck.getX() + oppDeck.getWidth() && e.getY() <= oppDeck.getY() + oppDeck.getHeight() && e.getY() >= oppDeck.getY()){
                oppDeck.shouldShowCardsCount = true;
            }else{
                oppDeck.shouldShowCardsCount = false;
            }
        }
    }
}
