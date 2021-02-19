package com.company.Engine;

import com.company.Classes.*;
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
    private ArrayList<Card> cards;
    private Card draggingCard;
    private boolean mouseHolding;
    private ID selectedCard;
    private Graphics g;
    private BufferStrategy buffer;
    private Handler handler;
    Board board;
    Deck deck;
    MouseHandler m_handler;
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

        cards = new ArrayList<Card>();
        ArrayList<Card> cardsCreate = new ArrayList<Card>();
        try{
            cardsCreate.add(new Card("Player1 Monster_1", 1, ID.Monster_1, ImageIO.read(new File("src/com/company/Images/korta.png"))));
            cardsCreate.add(new Card("Player1 Monster_2", 2, ID.Monster_2, ImageIO.read(new File("src/com/company/Images/korta2.png"))));
            cardsCreate.add(new Card("Player1 Monster_3", 3, ID.Monster_3, ImageIO.read(new File("src/com/company/Images/korta3.jpg"))));
            cardsCreate.add(new Card("Player1 Monster_4", 4, ID.Monster_4, ImageIO.read(new File("src/com/company/Images/korta4.png"))));


            cardsCreate.add(new Card("Player1 Monster_5", 5, ID.Monster_5, ImageIO.read(new File("src/com/company/Images/korta5.png"))));
            cardsCreate.add(new Card("Player1 Monster_6", 6, ID.Monster_6, ImageIO.read(new File("src/com/company/Images/korta6.jpg"))));
            for (Card c: cardsCreate) {
                cards.add(c);
            }

            draggingSlot = new CardSlot((Card) null, 0, 0, ID.values()[0]);
            draggingSlot.setWidth((int)(display.getWidth()*0.1));
            draggingSlot.setHeight((int)(display.getHeight()*0.2));
            //draggingCard = new Card("sdsad", 7, ID.Buff, ImageIO.read(new File("src/com/company/Images/back.jpg")));

        } catch (IOException e) {
             e.printStackTrace();
        }

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
        deck = new Deck(cardsCreate.size(), player1_slots.get(11).getX(), player1_slots.get(11).getY(), cardsCreate, backImg);
        player2_slots = board.getPlayer2_slots();
        for(CardSlot s: player2_slots){
            handler.addObject(s);
        }
        handler.addObject(draggingSlot);

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


        player1_slots.get(5).setCard(cards.get(0));
        player1_slots.get(6).setCard(cards.get(1));
        player1_slots.get(7).setCard(cards.get(2));
        player1_slots.get(8).setCard(cards.get(3));
        player1_slots.get(9).setCard(cards.get(4));
        player1_slots.get(10).setCard(cards.get(5));
        player1_slots.set(11, new CardSlot(deck, player1_slots.get(11).getX(), player1_slots.get(11).getY(),ID.Player1_Deck));
        // Tempia korta
        if(mouseHolding) {
            draggingSlot.setCard(getCardWithID(selectedCard));
            draggingSlot.setX(display.getFrame().getMousePosition().x + dragginCardOffsetX);
            draggingSlot.setY(display.getFrame().getMousePosition().y + dragginCardOffsetY);
        }else{
            draggingSlot.setX(-1000);
            draggingSlot.setY(-1000);
        }

    }
    public void SlotClicked(ID id){
        mouseHolding = true;
        selectedCard = id;
        draggingCard = getCardWithID(selectedCard);
        System.out.println("Selected card: " + id);
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

    }
}
