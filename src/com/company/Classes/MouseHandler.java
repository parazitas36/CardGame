package com.company.Classes;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MouseHandler implements MouseListener {


    private Canvas canvas;


    public MouseHandler(Canvas canvas1){
        canvas = canvas1;
        canvas.addMouseListener(this);
    }

//    public MouseHandler(Canvas canvas){
//        canvas.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                System.out.println("aaaaaa");
//            }
//        });
//    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Paspaude");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("Laiko");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("Atleido");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("Iejo");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println("Isejo");
    }
}
