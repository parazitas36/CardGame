package com.company.Engine;

import com.company.Classes.Card;
import com.company.Classes.CardSlot;
import com.company.Classes.ID;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Game implements Runnable{
    private Display display;
    private String title;
    private int width;
    private int height;

    private Thread thread;
    private boolean running;

    private Graphics g;
    private BufferStrategy buffer;
    public Game(String _title, int _width, int _height){
        title = _title;
        width = _width;
        height = _height;
    }
    private void init(){
        display = new Display(title, width, height);
    }
    private void tick(){

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
        g.setColor(Color.WHITE);
        int firstPos = 0;
        for(int i = 0; i < 5; i++){
            if(i == 0){
                g.fillRect((int)(width*0.05), height/2 + (int)(height*0.05), (int)(width*0.1), (int)(height * 0.2));
                firstPos = (int)(width*0.05)+(int)(width*0.1);
            }else{
                g.fillRect(firstPos + (i*(int)(width*0.1) + (i-1)*(int)(width*0.1)), height/2 + (int)(height*0.05), (int)(width*0.1), (int)(height * 0.2));
            }
        }
        firstPos = 0;
        for(int i = 0; i < 5; i++){
            if(i == 0){
                g.fillRect((int)(width*0.05), height/2 - (int)(height * 0.2) - (int)(height*0.05), (int)(width*0.1), (int)(height * 0.2));
                firstPos = (int)(width*0.05)+(int)(width*0.1);
            }else{
                g.fillRect(firstPos + (i*(int)(width*0.1) + (i-1)*(int)(width*0.1)), height/2 - (int)(height * 0.2) - (int)(height*0.05), (int)(width*0.1), (int)(height * 0.2));
            }
        }
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
            g.drawImage(img, (int)(width*0.05), height/2 + (int)(height*0.05), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
