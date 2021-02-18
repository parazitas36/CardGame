package com.company.Engine;

import com.company.Classes.*;
import com.company.Utils.DragCard;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Game implements Runnable{
    private Display display;
    private String title;
    private int width;
    private int height;

    private Thread thread;
    private boolean running;

    private ArrayList<CardSlot> player1_slots;
    private ArrayList<CardSlot> player2_slots;
    private Graphics g;
    private BufferStrategy buffer;
    private Handler handler;
    Board board;
    MouseHandler m_handler;
    public Game(String _title, int _width, int _height){
        title = _title;
        width = _width;
        height = _height;
    }
    private void init(){
        display = new Display(title, width, height);
        board = new Board(display);
        handler = new Handler();
        player1_slots = board.getPlayer1_slots();
        for(CardSlot s: player1_slots){
            handler.addObject(s);
        }
        player2_slots = board.getPlayer2_slots();
        for(CardSlot s: player2_slots){
            handler.addObject(s);
        }
    }
    private void tick(){
        m_handler = new MouseHandler(display.getCanvas(), player1_slots);
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
        try {
            img = ImageIO.read(new File("src/com/company/Images/korta.png"));
            Card c = new Card("Player1 Monster", 7, ID.Buff, img);
            c.setX(player1_slots.get(3).getX());
            c.setY(player1_slots.get(3).getY());
            Card d = new Card("Player2 Monster", 7, ID.Buff, img);
            d.setX(player2_slots.get(2).getX());
            d.setY(player2_slots.get(2).getY());
            player1_slots.get(3).setCard(c);
            player2_slots.get(2).setCard(d);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
