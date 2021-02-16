package com.company.Classes;

import java.awt.*;
import java.util.Collections;
import java.util.Stack;

public class Deck extends GameObject{
    private Stack deck1;
    Stack<Card> deck;
    private int size;
    public Deck(int _size, int x, int y) {
        super(x, y);
        this.deck = new Stack<Card>();
        this.size = _size;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

    }
}

