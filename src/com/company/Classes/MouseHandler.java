package com.company.Classes;

import com.company.Engine.Game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MouseHandler implements MouseListener {

    private Canvas canvas;
    private Game game;

    ArrayList<CardSlot> cardSlots;
    CardSlot attacker;
    public MouseHandler(Canvas canvas1, ArrayList<CardSlot> slots1, ArrayList<CardSlot> slots2, Game game){
        canvas = canvas1;
        canvas.addMouseListener(this);
        attacker = null;
        this.cardSlots = slots1;
        for(CardSlot s : slots2){
            cardSlots.add(s);
        }
        this.game = game;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getModifiers() == MouseEvent.BUTTON3_MASK && e.getClickCount() == 1){ // Cancels attack with right mouse click
            if(attacker != null){
                attacker.attacking = false;
                attacker = null;
            }
        }
        for(CardSlot c : cardSlots){
            if(e.getX() >= c.getX() && e.getX() <= c.getX()+c.getWidth() && e.getY() <= c.getY() + c.getHeight() && e.getY() >= c.getY()){
                // Draw card from deck
                if(c.getId() == ID.Player1_Deck){
                    Card card = c.getDeck().drawCard();
                    for(CardSlot slotas : cardSlots){
                        if(slotas.getId().toString().contains(String.format("%s_HandSlot", game.currentPlayer.getID().toString()))){
                            if(slotas.getCard() == null && card != null){
                                slotas.setCard(card);
                                break;
                            }
                        }
                    }
                }
                //-------------------------
                if(c.cardOnBoard()){
                    System.out.println(c.getCard().getName());
                    if(c.getId().toString().contains(String.format("%s_Slot", game.currentPlayer.getID().toString()))){
                        if(c.getCard().getID() == ID.Monster){
                            if(attacker == null){
                                attacker = c;
                                attacker.attacking = true;
                            }else{
                                attacker.attacking = false;
                                attacker = c;
                                attacker.attacking = true;
                            }
                        }else{
                            attacker.attacking = false;
                            attacker = null;
                        }
                    }
                    else if(attacker != null && c.getCard().getID() == ID.Monster && c.getId().toString().contains(String.format("%s_Slot", ID.Player2.toString()))){
                        if(game.currentPlayer.attack((Monster)attacker.getCard(), (Monster)c.getCard())){
                            c.removeCard();
                        }
                        attacker.attacking = false;
                        attacker = null;
                    }
                }else {
                    System.out.println("Click: " + c.getId());
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for(CardSlot c : cardSlots){
            if(e.getX() >= c.getX() && e.getX() <= c.getX()+c.getWidth() && e.getY() <= c.getY() + c.getHeight() && e.getY() >= c.getY()){
                System.out.println("Pressed: " + c.getId());
                if(c.getId() != ID.Player1_Deck){
                    if(c.getCard() != null && c.getId().toString().contains(String.format("%s_HandSlot", game.currentPlayer.getID().toString())))
                    game.SlotClicked(c.getCard(), c);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        game.MouseReleased();
    }
    boolean temp = false;
    @Override
    public void mouseEntered(MouseEvent e) {
        for(CardSlot c : cardSlots){
            if(e.getX() >= c.getX() && e.getX() <= c.getX()+c.getWidth() && e.getY() <= c.getY() + c.getHeight() && e.getY() >= c.getY()){
                System.out.println(c.getId());
            }
        }

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
