package com.company.Engine;

import com.company.MultiPlayer.PlayerMP;
import com.company.Packets.Packet01Disconnect;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowHandler implements WindowListener {
    private final Game game;
    public WindowHandler(Game game){
        this.game = game;
        this.game.display.getFrame().addWindowListener(this);
    }
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        Packet01Disconnect packet = new Packet01Disconnect(((PlayerMP)(this.game.ME)).getUsername());
        System.out.println(" Me:\t" + ((PlayerMP)game.ME).getUsername() + "[ " + game.ME.getID() +" ]");
        System.out.println(" My opponent:\t" + ((PlayerMP)game.ME.opponent).getUsername()  + "[ " + game.ME.opponent.getID() +" ]");
        packet.writeData(this.game.socketClient);
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
