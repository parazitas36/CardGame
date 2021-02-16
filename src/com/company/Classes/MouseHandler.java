package com.company.Classes;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class MouseHandler implements MouseListener {


    private Canvas canvas;

    ArrayList<CardSlot> cardSlots;
    public MouseHandler(Canvas canvas1, ArrayList<CardSlot> slots){
        canvas = canvas1;
        canvas.addMouseListener(this);
        this.cardSlots = slots;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        for(CardSlot c : cardSlots){
            if(e.getX() >= c.getX() && e.getX() <= c.getX()+c.getWidth() && e.getY() <= c.getY() + c.getHeight() && e.getY() >= c.getY()){
                System.out.println("Click: " + c.getId());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for(CardSlot c : cardSlots){
            if(e.getX() >= c.getX() && e.getX() <= c.getX()+c.getWidth() && e.getY() <= c.getY() + c.getHeight() && e.getY() >= c.getY()){
                System.out.println("Pressed: " + c.getId());
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
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
