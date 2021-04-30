package com.company.Classes;

import com.company.Engine.Game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MouseHandler implements MouseListener, MouseMotionListener {

    private Canvas canvas;
    private Game game;
    private CardSlot enteredSlot; // need this one for lifting up a card when mouse is on it

    ArrayList<CardSlot> cardSlots;
    CardSlot attacker; // card slot which has a monster that is going to attack
    public MouseHandler(Canvas canvas1, ArrayList<CardSlot> slots1, ArrayList<CardSlot> slots2, Game game){
        canvas = canvas1;
        canvas.addMouseListener(this);
        attacker = null;
        enteredSlot = null;
        this.cardSlots = slots1;
        for(CardSlot s : slots2){
            cardSlots.add(s);
        }
        this.game = game;
        canvas.addMouseMotionListener(this);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(game.gameState.isMenu){

            if (game.display.getFrame().getMousePosition() != null && this.game.display.getFrame().getMousePosition().x >= 0 && this.game.display.getFrame().getMousePosition().x <= 1000 && this.game.display.getFrame().getMousePosition().y <= 1000 && this.game.display.getFrame().getMousePosition().y >= 0) {
                // gameState.isMenu = false;
                System.out.println("EXITING GAME");
                game.running = false;
                //System.out.println("STARTING GAME");
            }

            return;
        }
        //-------------------------------------------------------------
        // If it's not an attack phase, but attacker is still selected.
        //-------------------------------------------------------------
        if(!game.phase.attackPhase() && attacker != null){
            attacker.attacking = false;
            attacker = null;
        }
        //---------------------------------------

        //---------------------------------------
        // Cancels attack with right mouse click
        //---------------------------------------
        if(e.getModifiers() == MouseEvent.BUTTON3_MASK){
            if(attacker != null){
                attacker.attacking = false;
                attacker = null;
            }
            for(int ia = 0; ia < 26; ia++){
                if(cardSlots.get(ia) != null && cardSlots.get(ia).getCard() != null && cardSlots.get(ia).getCard().getID() != null) {
                    System.out.println("NOT NULL " + ia);
                    //game.setAttacking(cardSlots.get(ia).getCard().getID(), 0);
                }
            }
            game.setAttacking(6);


        }
        //---------------------------------------

        int index = 0;
        for(CardSlot c : cardSlots){
            index++;
            if((e.getModifiers() == MouseEvent.BUTTON1_MASK) && e.getX() >= c.getX() && e.getX() <= c.getX()+c.getWidth() && e.getY() <= c.getY() + c.getHeight() && e.getY() >= c.getY()){
                if(c.cardOnBoard()){
                    System.out.println(c.getCard().getName());
                    System.out.println(((Monster)c.getCard()).getStunTime());
                    //---------------------------------------
                    // Chooses a monster which is going to attack.
                    //---------------------------------------
                    if(game.phase.attackPhase() && c.getId().toString().contains(String.format("%s_Slot", game.currentPlayer.getID().toString()))){
                        if(c.getCard().getID() == ID.Monster && !c.attackedThisTurn()){
                            if(attacker == null){
                                attacker = c;
                                attacker.attacking = true;
                            }else{
                                attacker.attacking = false;
                                attacker = c;
                                attacker.attacking = true;
                                game.setAttacking(index-1);
                                // TODO HERE
                            }
                        }else{
                            if(attacker != null) {
                                attacker.attacking = false;
                                attacker = null;
                            }
                        }
                    }
                    //---------------------------------------

                    //---------------------------------------
                    // Selects a monster which will be attacked.
                    //---------------------------------------
                    else if(game.phase.attackPhase() && attacker != null && c.getCard().getID() == ID.Monster && c.getId().toString().contains(String.format("%s_Slot", ID.Player2.toString()))){
                        game.currentPlayer.attack(attacker, c);
                        attacker.attacking = false;
                        attacker = null;
                    }
                    //---------------------------------------
                }else {
                    System.out.println("Click: " + c.getId());
                }
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(game.gameState.isMenu){

            return;
        }
        for(CardSlot c : cardSlots){
            if((e.getModifiers() == MouseEvent.BUTTON1_MASK) && e.getX() >= c.getX() && e.getX() <= c.getX()+c.getWidth() && e.getY() <= c.getY() + c.getHeight() && e.getY() >= c.getY()){
                System.out.println("Pressed: " + c.getId());
                //---------------------------------------
                // Chooses a card which will be dragged
                //---------------------------------------
                if(c.getId() != ID.Player1_Deck){
                    if(c.getCard() != null && c.getId().toString().contains(String.format("%s_HandSlot", game.currentPlayer.getID().toString())))
                    game.SlotClicked(c.getCard(), c);
                }
                //---------------------------------------
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(game.gameState.isMenu){

            return;
        }
        game.MouseReleased();
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

            return;
        }

        for(CardSlot c : cardSlots){
            if(e.getX() >= c.getX() && e.getX() <= c.getX()+c.getWidth() && e.getY() <= c.getY() + c.getHeight() && e.getY() >= c.getY()){
                //--------------------------------------------------------
                // When mouse is moved in/on the card that card is lifted.
                //--------------------------------------------------------
                if(c.getId().toString().contains(String.format("%s_HandSlot", ID.Player1.toString())) && enteredSlot == null){
                    enteredSlot = c;
                    enteredSlot.setY(enteredSlot.getY() - (int)(enteredSlot.getHeight()*0.4));
                }
                //--------------------------------------------------------
            }
        }
        //---------------------------------------------------------------------------------------
        // When mouse exits the card that card is lifted, it comes back to it's initial position.
        //---------------------------------------------------------------------------------------
        if(enteredSlot != null && (e.getX() < enteredSlot.getX() || e.getX() > enteredSlot.getX()+enteredSlot.getWidth() || e.getY() > enteredSlot.getY() + enteredSlot.getHeight() || e.getY() < enteredSlot.getY())){
            enteredSlot.setY(enteredSlot.getY() + (int)(enteredSlot.getHeight()*0.4));
            enteredSlot = null;
        }
        //---------------------------------------------------------------------------------------
    }

}
