package com.company.Classes;

import com.company.Utils.CardReader;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public class Deck extends GameObject  implements Serializable {
    Stack<Card> deck;
    private int size;
    private ImageIcon image;
    public Deck(int _size, int x, int y, ArrayList<Card> _card, ImageIcon img) {
        super();
        this.image = img;
        this.size = _size;
        this.deck = new Stack<Card>();
        this.transfer(_card);
    }

    public void transfer(ArrayList<Card> card) {
        for (int i=0 ; i < card.size(); i++) {
            deck.add(card.get(i));
        }
    }

    @Override
    public void tick() {

    }
    public Card drawCard(){
        if(getSize() != 0){
            return deck.pop();
        }else{
            System.out.println("Deck is empty!");
            return null;
        }
    }

     public void shuffle() {
        ArrayList<Card> temp = new ArrayList<>();
        while(!deck.isEmpty()) {
            temp.add(deck.pop());
        }

        Collections.shuffle(temp);
        for (int i = 0; i < temp.size(); i++) {
            deck.push(temp.get(i));
        }
     }

    @Override
    public void render(Graphics g) {

    }
    public Image getImage(){
        return image.getImage();
    }
    public Stack getDeck(){
        return deck;
    }
    public int getSize(){
        size = deck.size();
        return size;
    }
}

