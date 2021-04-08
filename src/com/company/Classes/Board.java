package com.company.Classes;

import com.company.Engine.*;

import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Board {
    private ArrayList<CardSlot> player1_slots;
    private ArrayList<CardSlot> player2_slots;
    private Display display;
    private BufferStrategy buffer;
    private int offsetYHand = 100;
    private int offsetY = -100;

    public Board(Display _display){
        display = _display;
        player1_slots = new ArrayList<CardSlot>(5);
        player2_slots = new ArrayList<CardSlot>(5);
        generateBoard();
    }
    private CardSlot generateAndScale(double widthscale, double heightScale, int idValue, int firstId)
    {
        // firstPos - represents which positiion is first in line number/
        CardSlot slot;
        int width = display.getWidth(); //number represents display window width
        int height = display.getHeight(); // number represents display window height
        if(idValue == firstId) // if card is first in that line
        {
            slot = new CardSlot((Card)null, (int)(width*widthscale), height/2 + (int)(height*heightScale) + offsetY, ID.values()[idValue]);
        }else // if card is not first in that line
        {
            slot = new CardSlot((Card)null, firstPos + ((idValue-5)*(int)(width*0.1) + (idValue-6)*(int)(width*0.1)), height/2 + (int)(height*0.05) + offsetY, ID.values()[idValue]);
        }

        return slot;
    }
    public void generateBoard(){
        int firstPos = 0;
        for(int i = 5; i < 10; i++){
            CardSlot slot;
            if(i == 5){
                slot = new CardSlot((Card)null, (int)(display.getWidth()*0.1), display.getHeight()/2 + (int)(display.getHeight()*0.05) + offsetY, ID.values()[i]);
                firstPos = (int)(display.getWidth()*0.1)+(int)(display.getWidth()*0.1);
            }else{
                slot = new CardSlot((Card)null, firstPos + ((i-5)*(int)(display.getWidth()*0.05) + (i-6)*(int)(display.getWidth()*0.1)), display.getHeight()/2 + (int)(display.getHeight()*0.05) + offsetY, ID.values()[i]);
            }
            slot.setWidth((int)(display.getWidth()*0.1));
            slot.setHeight((int)(display.getHeight()*0.2));
            player1_slots.add(slot);
            if(i == 9){
                slot = new CardSlot((Card)null, firstPos + ((i-4)*(int)(display.getWidth()*0.05) + (i-5)*(int)(display.getWidth()*0.1)), display.getHeight()/2 + (int)(display.getHeight()*0.05) + offsetY, ID.Player1_Deck);
                slot.setWidth((int)(display.getWidth()*0.1));
                slot.setHeight((int)(display.getHeight()*0.2));
                player1_slots.add(slot);
            }
        }
        for(int i = 10; i < 15; i++){
            CardSlot slot;
            if(i == 10){
                slot = new CardSlot((Card)null, (int)(display.getWidth()*0.1), display.getHeight()/2 - (int)(display.getHeight() * 0.2) - (int)(display.getHeight()*0.05) + offsetY, ID.values()[i]);
                firstPos = (int)(display.getWidth()*0.1)+(int)(display.getWidth()*0.1);
            }else{
                slot = new CardSlot((Card) null, firstPos + ((i-10)*(int)(display.getWidth()*0.05) + (i-11)*(int)(display.getWidth()*0.1)), display.getHeight()/2 - (int)(display.getHeight() * 0.2) - (int)(display.getHeight()*0.05) + offsetY, ID.values()[i]);
            }
            slot.setWidth((int)(display.getWidth()*0.1));
            slot.setHeight((int)(display.getHeight()*0.2));
            player2_slots.add(slot);
            if(i == 14){
                slot = new CardSlot((Card) null, firstPos + ((i-9)*(int)(display.getWidth()*0.05) + (i-10)*(int)(display.getWidth()*0.1)), display.getHeight()/2 - (int)(display.getHeight() * 0.2) - (int)(display.getHeight()*0.05) + offsetY, ID.Player2_Deck);
                slot.setWidth((int)(display.getWidth()*0.1));
                slot.setHeight((int)(display.getHeight()*0.2));
                player2_slots.add(slot);
            }
        }

        // Take these variables from somewhere else in the code later:
        int handSize = 7;
        int handSizeLimit = 8;
        // Generating hand slots
        for(int i = 0; i < handSize; i++){
            CardSlot slot;
            slot = new CardSlot((Card) null, (int) display.getWidth() / 2 - handSize * (int) (display.getWidth() * 0.13) / 2 + (i) * (int) (display.getWidth() * 0.13),
                        display.getHeight() / 2 + (int) (display.getHeight() * 0.2) + offsetYHand, ID.values()[15 + i]);
                slot.setWidth((int)(display.getWidth()*0.1));
                slot.setHeight((int)(display.getHeight()*0.2));
                player1_slots.add(slot);
        }
    }

//    public void render(Graphics g){
//        g.setColor(Color.WHITE);
//        for(CardSlot slot : player1_slots){
//            slot.render(g);
//        }
//        for(CardSlot slot : player2_slots){
//            slot.render(g);
//        }
//    }
    public ArrayList<CardSlot> getPlayer1_slots(){
        return this.player1_slots;
    }
    public ArrayList<CardSlot> getPlayer2_slots(){
        return this.player2_slots;
    }
}
