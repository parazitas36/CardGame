package com.company.Classes;

import com.company.Engine.Display;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Board {
    private ArrayList<CardSlot> player1_slots;
    private ArrayList<CardSlot> player2_slots;
    private Display display;
    private Graphics g;
    private BufferStrategy buffer;
    public Board(Display _display, Graphics _g){
        display = _display;
        g = _g;
        player1_slots = new ArrayList<>(5);
        player2_slots = new ArrayList<>(5);
        generateBoard();
    }
    public void generateBoard(){
        int firstPos = 0;
        for(int i = 5; i < 10; i++){
            CardSlot slot;
            if(i == 5){
                slot = new CardSlot(null, (int)(display.getWidth()*0.05), display.getHeight()/2 + (int)(display.getHeight()*0.05), ID.values()[i]);
                firstPos = (int)(display.getWidth()*0.05)+(int)(display.getWidth()*0.1);
            }else{
                slot = new CardSlot(null, firstPos + ((i-5)*(int)(display.getWidth()*0.1) + (i-6)*(int)(display.getWidth()*0.1)), display.getHeight()/2 + (int)(display.getHeight()*0.05), ID.values()[i]);
            }
            slot.setWidth((int)(display.getWidth()*0.1));
            slot.setHeight((int)(display.getHeight()*0.2));
            player1_slots.add(slot);
            System.out.println(ID.values()[i]);
        }
        for(int i = 10; i < 15; i++){
            CardSlot slot;
            if(i == 10){
                slot = new CardSlot(null, (int)(display.getWidth()*0.05), display.getHeight()/2 - (int)(display.getHeight() * 0.2) - (int)(display.getHeight()*0.05), ID.values()[i]);
                firstPos = (int)(display.getWidth()*0.05)+(int)(display.getWidth()*0.1);
            }else{
                slot = new CardSlot(null, firstPos + ((i-10)*(int)(display.getWidth()*0.1) + (i-11)*(int)(display.getWidth()*0.1)), display.getHeight()/2 - (int)(display.getHeight() * 0.2) - (int)(display.getHeight()*0.05), ID.values()[i]);
            }
            slot.setWidth((int)(display.getWidth()*0.1));
            slot.setHeight((int)(display.getHeight()*0.2));
            player2_slots.add(slot);
            System.out.println(ID.values()[i]);
        }
    }

    public void render(){
        buffer = display.getCanvas().getBufferStrategy();
        if(buffer == null){
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g = buffer.getDrawGraphics();
        g.setColor(Color.WHITE);
        for(CardSlot slot : player1_slots){
            if(slot.cardOnBoard()) {
                System.out.println("Padeta korta");
            }else{
                g.fillRect(slot.getX(), slot.getY(), slot.getWidth(), slot.getHeight());
            }
        }
        for(CardSlot slot : player2_slots){
            if(slot.cardOnBoard()) {
                System.out.println("Padeta korta");
            }else{
                g.fillRect(slot.getX(), slot.getY(), slot.getWidth(), slot.getHeight());
            }
        }
    }
}