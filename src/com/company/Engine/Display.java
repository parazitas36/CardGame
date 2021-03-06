package com.company.Engine;

import com.company.Classes.Buff;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;

public class Display {
    private JFrame frame;
    private Canvas canvas;
    private String title;
    private int width, height;
    public BufferedImage loadingIMG;

    public Display(String title, int width, int height){
        this.title = title;
        this.width = width;
        this.height = height;
        try{
            loadingIMG = ImageIO.read(new File("src/com/company/Images/Loading.png"));
        }catch (Exception e){
            e.printStackTrace();
        }
        createDisplay();
    }
    private void createDisplay(){
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));

        frame.add(canvas);
        frame.pack();
    }
    public Canvas getCanvas(){
        return canvas;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public JFrame getFrame(){
        return frame;
    }

}
