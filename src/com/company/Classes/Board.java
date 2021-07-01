package com.company.Classes;

import com.company.Engine.*;

import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Board {
    private ArrayList<CardSlot> player1_slots;
    private ArrayList<CardSlot> player2_slots;
    private Display display;
    private BufferStrategy buffer;
    private int offsetYHand;
    private int offsetY;
    private Game game;

    public Board(Display _display, Game game){
        display = _display;
        player1_slots = new ArrayList<CardSlot>(5);
        player2_slots = new ArrayList<CardSlot>(5);
        offsetY = (int)(display.getHeight()*(-0.105));
        offsetYHand = (int)(display.getHeight()*(0.1));
        this.game = game;
        generateBoard();
    }
    private CardSlot generateSlot(double widthscale, double heightScale, int index, int firstId, int ii, int ij, int firstPos, int indexArray)
    {
        // firstId - represents which position is first in line number
        CardSlot slot;
        double widthS = (display.getWidth()*widthscale); //number represents display window procentege of width
        int spaceWidth = (int)(Math.ceil(display.getWidth()*0.05));
        int heightS = (int)(display.getHeight()*heightScale); // number represents display window height
        int heightHalf = display.getHeight()/2;
        if(game.ME.getID() == ID.Player1 || game.ME.getID() == ID.Player2) {
            int adjust = game.ME.getID() == ID.Player1 ? 0 : 5;
            if (index == firstId) // if card is first in that line
            {
                slot = new CardSlot((Card) null, (int)(widthS * 2), heightHalf + heightS + offsetY, ID.values()[index + adjust], indexArray);
            } else // if card is not first in that line
            {
                slot = new CardSlot((Card) null, (int)(firstPos + ((index - ii) * spaceWidth + (index - ij) * widthS * 2)), heightHalf + heightS + offsetY, ID.values()[index + adjust], indexArray);
            }

            return slot;
        }
        return null;
    }
    private CardSlot generateSlotOpp(double widthscale, double heightScale, int index, int firstId, int ii, int ij, int firstPos, int indexArray)
    {
        // firstId - represents which position is first in line number
        CardSlot slot;
        double widthS = (display.getWidth()*widthscale); //number represents display window procentege of width
        int spaceWidth = (int)(Math.ceil(display.getWidth()*0.05));
        int heightS = (int)(display.getHeight()*heightScale); // number represents display window height
        int heightHalf = display.getHeight()/2;
        if(game.ME.opponent.getID() == ID.Player1 || game.ME.opponent.getID() == ID.Player2)
        {
            int adjust = game.ME.opponent.getID() == ID.Player1 ? 5 : 0;
            if(index == firstId)
            {
                slot = new CardSlot((Card)null, (int)(widthS*2), heightHalf - heightS*2 - heightS/2 + offsetY, ID.values()[index - adjust], indexArray);
            }else{
                slot = new CardSlot((Card) null, (int)(firstPos + ((index-ii)*spaceWidth + (index-ij)*widthS*2)), heightHalf - heightS*2 - heightS/2 + offsetY, ID.values()[index-adjust], indexArray);
            }
            return slot;
        }
        return null;
    }
    public void generateBoard(){
        int firstPos = (int)(display.getWidth()*0.1)+(int)(display.getWidth()*0.1);
        for(int i = 5; i < 10; i++){
            CardSlot slot;
            slot = generateSlot(0.05,0.05,i,5,5,6, firstPos, i-5);
            slot.setWidth((int)(display.getWidth()*0.1));
            slot.setHeight((int)(display.getHeight()*0.2));
            player1_slots.add(slot);

            if(i == 9){
                slot = new CardSlot((Deck)null, firstPos + ((i-4)*(int)(Math.ceil(display.getWidth()*0.05)) + (i-5)*(int)(display.getWidth()*0.1)), display.getHeight()/2 + (int)(display.getHeight()*0.05) + offsetY, ID.Player1_Deck, i-5);
                slot.setWidth((int)(display.getWidth()*0.1));
                slot.setHeight((int)(display.getHeight()*0.2));
                player1_slots.add(slot);
            }
        }
        for(int i = 10; i < 15; i++){
            CardSlot slot;
            slot = generateSlotOpp(0.05,0.1,i,10,10,11, firstPos, i-10);
            slot.setWidth((int)(display.getWidth()*0.1));
            slot.setHeight((int)(display.getHeight()*0.2));
            player2_slots.add(slot);
            if(i == 14){
                slot = new CardSlot((Deck) null, firstPos + ((i-9)*(int)(int)(Math.ceil(display.getWidth()*0.05)) + (i-10)*(int)(display.getWidth()*0.1)), display.getHeight()/2 - (int)(display.getHeight() * 0.2) - (int)(display.getHeight()*0.05) + offsetY, ID.Player2_Deck, i - 10);
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
                        display.getHeight() / 2 + (int) (display.getHeight() * 0.2) + offsetYHand, ID.values()[15 + i], i);
                slot.setWidth((int)(display.getWidth()*0.1));
                slot.setHeight((int)(display.getHeight()*0.2));
                player1_slots.add(slot);
        }
        for(int i = 0; i < handSize; i++){
            CardSlot slot;
            slot = new CardSlot((Card) null, (int) display.getWidth() / 2 - handSize * (int) (display.getWidth() * 0.13) / 2 + (i) * (int) (display.getWidth() * 0.13),
                    display.getHeight()/2 - (int)(display.getHeight() * 0.2) - (int)(display.getHeight()*0.05) + (offsetY-(int)(display.getHeight()*0.153)) - offsetYHand, ID.values()[23 + i], i);
            slot.setWidth((int)(display.getWidth()*0.1));
            slot.setHeight((int)(display.getHeight()*0.2));
            player2_slots.add(slot);
        }
    }

    public ArrayList<CardSlot> getPlayer1_slots(){
        return this.player1_slots;
    }
    public ArrayList<CardSlot> getPlayer2_slots(){
        return this.player2_slots;
    }
}
