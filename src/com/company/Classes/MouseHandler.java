package com.company.Classes;

import com.company.Engine.Game;

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.ArrayList;

public class MouseHandler implements MouseListener, MouseMotionListener, Serializable {

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
                System.out.println("Start");
                game.gameState.isMenu = false;
                game.gameState.isLoading = true;
                //game.startGame();
                game.gameState.isLoading = false;
                game.gameState.isGame = true;
                game.gameState.startGame = true;
            }
            // Clicked on Multiplayer button
            else if(e.getX() >= 0 && e.getX() <= game.gameState.imgW && e.getY() <= game.gameState.imgH && e.getY() >= 0){
                System.out.println("MP button");
                //game.MP();
                game.TCP_MP();
            }
            // CLicked on Options button
            else if(e.getX() >= game.gameState.imgX && e.getX() <= game.gameState.imgX +  game.gameState.imgW && e.getY() <= game.gameState.imgYOffSet + 2*game.gameState.imgH + game.gameState.imgYButtonOffSet && e.getY() >= game.gameState.imgYOffSet + game.gameState.imgH + game.gameState.imgYButtonOffSet){
                game.gameState.isOptions = true;
                System.out.println("Options");
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
                    game.menuMusicClip = game.musicPlayer.playMusic(game.menuMusic);
                    game.musicPlayer.repeatMusic(game.menuMusicClip);
                }else if(!game.menuMusicClip.isRunning()){
                    game.musicPlayer.repeatMusic(game.menuMusicClip);
                }
            }
        }
        //===================================

        //===================================
        // Game
        //===================================

        if(game.gameState.isGame && game.phase.attackPhase()) {
            // Phase button
            if(e.getX() >= game.phase.GetEndTurnPosX() && e.getX() <= game.phase.GetEndTurnPosX() +  game.phase.GetEndTurnImgWidth() && e.getY() <= game.phase.GetEndTurnPosY() + game.phase.GetEndTurnImgHeight() && e.getY() >= game.phase.GetEndTurnPosY()){
//                if(game.ME.getID() == ID.Player2){
//                    game.phase.currentRound++;
//                }
                game.phase.nextPhase();
                if(game.phase.sendToOther) {
                    game.ME.tcpClient.sendUpdate("phase");
                    game.phase.sendToOther = false;
                }
                return;
            }
            //-------------------------------------------------------------
            // If it's not an attack phase, but attacker is still selected.
            //-------------------------------------------------------------
            if (!game.phase.attackPhase() && attacker != null) {
                attacker.attacking = false;
                attacker = null;
            }
            //---------------------------------------

            //---------------------------------------
            // Cancels attack with right mouse click
            //---------------------------------------
            if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
                if (attacker != null) {
                    attacker.attacking = false;
                    attacker = null;
                }
            }
            //---------------------------------------

            //---------------------------------------
            // Chooses a card which will be dragged
            //---------------------------------------
            for (CardSlot c : game.ME.playerHandSlots) {
                if ((e.getModifiers() == MouseEvent.BUTTON1_MASK) && e.getX() >= c.getX() && e.getX() <= c.getX() + c.getWidth() && e.getY() <= c.getY() + c.getHeight() && e.getY() >= c.getY()) {
                    System.out.println(c.getId());
                    if (c.getId() != ID.Player1_Deck) {
                        if (c.getCard() != null)
                            game.SlotClicked(c.getCard(), c);
                    }
                }
            }
            for (CardSlot c : game.ME.opponent.playerHandSlots) {
                if ((e.getModifiers() == MouseEvent.BUTTON1_MASK) && e.getX() >= c.getX() && e.getX() <= c.getX() + c.getWidth() && e.getY() <= c.getY() + c.getHeight() && e.getY() >= c.getY()) {
                    System.out.println(c.getId());
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
            for (CardSlot c : game.ME.playerBoardSlots) {
                index++;
                if ((e.getModifiers() == MouseEvent.BUTTON1_MASK) && e.getX() >= c.getX() && e.getX() <= c.getX() + c.getWidth() && e.getY() <= c.getY() + c.getHeight() && e.getY() >= c.getY()) {
                    System.out.println(c.getId());
                    if (c.cardOnBoard()) {
                        System.out.println(c.getId() + " " + c.getCard().getID());
                        if (game.phase.attackPhase()) {
                            if (c.getCard().getID() == ID.Monster && !c.attackedThisTurn() && ((Monster) c.getCard()).stunTime == 0) {
                                if (attacker == null) {
                                    attacker = c;
                                    attacker.attacking = true;
                                    selectedIndex = index;
                                    selectedOffsetX = c.getX();
                                    System.out.println("111111111111111");
                                    return;
                                } else {
                                    if(!game.currentPlayer.opponentHasMonsterOnTheBoard() && attacker.equals(c) && !c.attackedThisTurn()){
                                        game.attackOpponent(attacker, game.currentPlayer);
                                        attacker.setAttackedThisTurn();
                                        attacker.attacking = false;
                                        attacker = null;
                                        return;
                                    }else{
                                        attacker.attacking = false;
                                        attacker = c;
                                        attacker.attacking = true;
                                        System.out.println("22222222222222");
                                        selectedIndex = index;
                                        selectedOffsetX = c.getX();
                                        return;
                                        // TODO HERE
                                    }
                                }
                            } else {

                                if (attacker != null) {
                                    System.out.println("333333333333333");
                                    selectedIndex = index;
                                    selectedOffsetX = c.getX();

                                    attacker.attacking = false;
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
            for(CardSlot c : game.ME.opponent.playerBoardSlots){
                if ((e.getModifiers() == MouseEvent.BUTTON1_MASK) && e.getX() >= c.getX() && e.getX() <= c.getX() + c.getWidth() && e.getY() <= c.getY() + c.getHeight() && e.getY() >= c.getY()) {
                    System.out.println(c.getId());
                    if (c.cardOnBoard()) {
                        System.out.println(c.getCard().getName());
                        System.out.println(((Monster) c.getCard()).getStunTime());

                        if (game.phase.attackPhase() && attacker != null && c.getCard().getID() == ID.Monster) {

                            System.out.println("444444444444444444");
                            //game.setAttacking(selectedIndex - 1);
                            //game.targetX = c.getX() - selectedOffsetX;
                            game.currentPlayer.attack(attacker, c);
                            if(game.sound_attackEffectClip == null) {
                                game.sound_attackEffectClip = game.musicPlayer.playSound(game.sound_attackEffect);
                                game.musicPlayer.repeatSound(game.sound_attackEffectClip);
                            }else{
                                game.musicPlayer.repeatSound(game.sound_attackEffectClip);
                            }
                            attacker.attacking = false;
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
        /*
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
                deck.showCardsCount = true;
                System.out.println(deck.getDeck().getSize());
            }else{
                deck.showCardsCount = false;
            }
            CardSlot oppDeck = game.currentPlayer.opponent.deckSlot;
            if(e.getX() >= oppDeck.getX() && e.getX() <= oppDeck.getX() + oppDeck.getWidth() && e.getY() <= oppDeck.getY() + oppDeck.getHeight() && e.getY() >= oppDeck.getY()){
                oppDeck.showCardsCount = true;
                System.out.println(oppDeck.getDeck().getSize());
            }else{
                oppDeck.showCardsCount = false;
            }
        }

         */
    }
}
