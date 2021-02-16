package com.company.Engine;

import com.company.Classes.*;

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
    Board board;
    MouseHandler handler;
    public Game(String _title, int _width, int _height){
        title = _title;
        width = _width;
        height = _height;
    }
    private void init(){
        display = new Display(title, width, height);
        board = new Board(display);
        player1_slots = board.getPlayer1_slots();
        player2_slots = board.getPlayer2_slots();
    }
    private void tick(){
        handler = new MouseHandler(display.getCanvas());
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
        board.render(g);
        testImageDraw();

        //
        buffer.show();
        g.dispose();
    }
    @Override
    public void run() {
        init();

        while(running){
            tick();
            render();
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
            player1_slots.get(3).setCard(new Card("sdsad", 7, player1_slots.get(3).getX(), player1_slots.get(3).getY(), img));
            player2_slots.get(3).setCard(new Card("sdsad", 7, player2_slots.get(3).getX(), player2_slots.get(3).getY(), img));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
