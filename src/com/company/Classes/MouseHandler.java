package com.company.Classes;

import com.company.Engine.Game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class MouseHandler implements MouseListener {

    private Canvas canvas;
    private Game game;

    ArrayList<CardSlot> cardSlots;
    public MouseHandler(Canvas canvas1, ArrayList<CardSlot> slots1, ArrayList<CardSlot> slots2, Game game){
        canvas = canvas1;
        canvas.addMouseListener(this);
        this.cardSlots = slots1;
        for(CardSlot s : slots2){
            cardSlots.add(s);
        }
        this.game = game;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        for(CardSlot c : cardSlots){
            if(e.getX() >= c.getX() && e.getX() <= c.getX()+c.getWidth() && e.getY() <= c.getY() + c.getHeight() && e.getY() >= c.getY()){
                // Draw card from deck
                if(c.getId() == ID.Player1_Deck){
                    Card card = c.getDeck().drawCard();
                    for(CardSlot slotas : cardSlots){
                        if(slotas.getId().toString().contains("Player1_HandSlot")){
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
                    if(c.getCard() != null && c.getId().toString().contains("Player1_HandSlot"))
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
