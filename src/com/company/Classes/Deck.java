package com.company.Classes;

import com.company.Utils.CardReader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public class Deck extends GameObject{
     ArrayList <Card> cards;
    Stack<Card> deck;
    private int size;
    public Deck(int _size, int x, int y) {
        super();
        this.deck = new Stack<Card>();
        this.size = _size;
    }
    public Deck(int _size, int x, int y, ArrayList<Card> _card) {
        super();
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
    public void add(Card card) { deck.add(card); }

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

    public Card drawRandomCard() {
        Random generator = new Random();
        int index = generator.nextInt(deck.size());
        return deck.remove(index);
    }

    @Override
    public void render(Graphics g) {

    }
}

