package com.company.Utils;

import com.company.Classes.Card;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DragCard extends JPanel{
    BufferedImage image;

    {
        try {
            image = ImageIO.read(new File("src/com/company/Images/korta.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    final int WIDTH = image.getWidth();
    final int HEIGHT = image.getHeight();
    Point imageCorner;
    Point prevPt;

    public DragCard(int x, int y){
        imageCorner = new Point(x,y);
        ClickListener clickListener = new ClickListener();
        DragListener dragListener = new DragListener();
        this.addMouseListener(clickListener);
        this.addMouseMotionListener(dragListener);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(image, (int)imageCorner.getX(), (int)imageCorner.getY(), null);
    }
    private class ClickListener extends MouseAdapter{
        public void mousePressed(MouseEvent e){
            prevPt = e.getPoint();
        }
    }
    private class DragListener extends MouseMotionAdapter{
        public void mouseDragged(MouseEvent e){
            if(getMousePosition().getY() < image.getHeight() && getMousePosition().getX() < image.getWidth() && getMousePosition().getX() >= image.getMinX()){
                Point currentPt = e.getPoint();
                imageCorner.translate(
                        (int)(currentPt.getX()-prevPt.getX()),
                        (int)(currentPt.getY()-prevPt.getY())
                );
                prevPt = currentPt;
                repaint();
            }
        }
    }

}
