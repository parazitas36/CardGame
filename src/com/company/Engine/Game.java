package com.company.Engine;

import com.company.Classes.*;
import com.company.Utils.CardReader;
import com.company.Utils.DragCard;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class Game implements Runnable{
    private Display display;
    private String title;
    private int width;
    private int height;

    private Thread thread;
    private boolean running;

    private ArrayList<CardSlot> player1_slots;
    private ArrayList<CardSlot> player2_slots;

    private CardSlot draggingSlot;
    private Card draggingCard;

    private ArrayList<Card> cards;

    private boolean mouseHolding;
    private Graphics g;
    private BufferStrategy buffer;
    private Handler handler;

    Board board;
    Deck deck;
    private int dragginCardOffsetX = -50, dragginCardOffsetY = -100;

    public Game(String _title, int _width, int _height){
        title = _title;
        width = _width;
        height = _height;
    }
    private void init(){
        display = new Display(title, width, height);
        board = new Board(display);
        handler = new Handler();

        cards = CardReader.Read("src/com/company/Assets/Cards_Data.txt");

        draggingSlot = new CardSlot((Card) null, 0, 0, ID.Dragging_Slot);
        draggingSlot.setWidth((int)(display.getWidth()*0.1));
        draggingSlot.setHeight((int)(display.getHeight()*0.2));


        player1_slots = board.getPlayer1_slots();
        for(CardSlot s: player1_slots){
            handler.addObject(s);
        }
        BufferedImage backImg = null;
        try{
            backImg = ImageIO.read(new File("src/com/company/Images/back.jpg"));
        }catch (IOException e){
            e.printStackTrace();
        }
        deck = new Deck(cards.size(), player1_slots.get(11).getX(), player1_slots.get(11).getY(), cards, backImg);
        player2_slots = board.getPlayer2_slots();
        for(CardSlot s: player2_slots){
            handler.addObject(s);
        }
        player1_slots.get(11).setDeck(deck);
        handler.addObject(draggingSlot);
        deck.shuffle();
        System.out.println(deck.getDeck().size());
        new MouseHandler(display.getCanvas(), player1_slots, player2_slots, this);
    }
    private void tick(){
        handler.tick();
    }
    private void render(){
        buffer = display.getCanvas().getBufferStrategy();
        if(buffer == null){
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g = buffer.getDrawGraphics();
        // Piesiam
        g.setColor(Color.BLACK);
        g.fillRect(0,0, width, height);
        g.setColor(Color.WHITE);testImageDraw();testImageDraw();
        handler.render(g);
        testImageDraw();

        //
        buffer.show();
        g.dispose();
    }
    @Override
    public void run() {
        init();

        int fps = 60;
        double timePerTick = 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();

        while(running){
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            lastTime = now;
            if(delta >= 1){
                tick();
                render();
                delta--;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        stop();
    }
    public synchronized void start(){
        if(running){
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }
    public synchronized void stop(){
        if(!running){
            return;
        }
        running = false;
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Game game = new Game("Title", 800, 640);
        game.start();
    }

    public void testImageDraw(){
        BufferedImage img;

        // Tempia korta
        if(mouseHolding) {
            draggingSlot.setCard(draggingCard);
            draggingSlot.setX(display.getFrame().getMousePosition().x + dragginCardOffsetX);
            draggingSlot.setY(display.getFrame().getMousePosition().y + dragginCardOffsetY);
        }else{
            draggingSlot.setX(-1000);
            draggingSlot.setY(-1000);
        }

    }
    public void SlotClicked(Card card){
        mouseHolding = true;
        draggingCard = card;
        System.out.println("Selected card: " + card);
    }
    public Card getCardWithID(ID id){
        for(Card c: cards){
            if(c.getID() == id && id != ID.Player1_Deck){
                return c;
            }
        }
        return null;
    }

    // Padeda korta
    public void MouseReleased(){
        mouseHolding = false;

        for(CardSlot c : player1_slots){
            if(display.getFrame().getMousePosition().x >= c.getX() && display.getFrame().getMousePosition().x <= c.getX()+c.getWidth() && display.getFrame().getMousePosition().y <= c.getY() + c.getHeight() && display.getFrame().getMousePosition().y >= c.getY()){
                if(c.getId() != ID.Player1_Deck){
                    c.setCard(draggingCard);
                }
            }
        }
        draggingCard = null;
    }
}
