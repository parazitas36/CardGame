package com.company.MultiPlayer;

import com.company.Classes.CardSlot;
import com.company.Classes.Deck;
import com.company.Classes.ID;
import com.company.Classes.Player;
import com.company.Engine.Display;
import com.company.Engine.Game;
import com.company.TCPMP.TCPClient;

import java.awt.*;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

public class PlayerMP extends Player  implements Serializable {
    public InetAddress ip;
    public int port;
    public String username;
    public PlayerMP opponent;
    public TCPClient tcpClient;
    public PlayerMP(ID _id, Deck _deck, ArrayList<CardSlot> slots, Display _display, Game _game, InetAddress ip, int port) {
        super(_id, _deck, slots, _display, _game);
        this.ip = ip;
        this.port = port;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }
    @Override
    public void tick() {
        super.tick();
    }

    public PlayerMP getOpponent(){ return this.opponent; }
    public void setOpponent(PlayerMP opponent) {
        this.opponent = opponent;
    }
    public void setTCPClient(TCPClient tcpClient){ this.tcpClient = tcpClient; }
    public void placeCardMP(int handSlotIndex, int boardSlotIndex){
        CardSlot handSlot = this.opponent.playerHandSlots.get(handSlotIndex);
        System.out.println("Padejo korta:" + handSlot.getCard().getName());
        CardSlot boardSlot = this.opponent.playerBoardSlots.get(boardSlotIndex);
        this.opponent.placeCard(boardSlot, handSlot.getCard());
        handSlot.removeCard();
    }
    @Override
    public boolean opponentHasMonsterOnTheBoard(){
        for(CardSlot slot : this.opponent.playerBoardSlots){
            if(slot.cardOnBoard() && slot.getCard().getID() == ID.Monster){
                return true;
            }
        }
        return false;
    }
    @Override
    public void render(Graphics g) {
        int width = display.getWidth();
        int Height = display.getHeight();
        // draw mana in board
        Font prevFont = g.getFont();
        Font font1 = new Font( Font.SANS_SERIF, 3, (int)((Height + width) * 0.02));
        Color clr = Color.MAGENTA;
        Color clrop = Color.GRAY;
        Color clrprev = Color.WHITE;
        if(id == this.game.ME.getID()){
            Font font = new Font( Font.SANS_SERIF, 3, (int)((Height + width) * 0.00933));

            g.setFont(font);
            g.drawString(String.format("%s", this.game.ME.getMana()), (int)(width * 0.007), (int)(Height*0.605));
            g.drawString(String.format("%s", this.game.ME.getHP()), (int)(width * 0.005), (int)(Height*0.505));
            g.drawString(String.format("%s", this.game.ME.getManaStack()) + String.format("/%s", ManaStackCapacity), (int)(width * 0.054), (int)(Height*0.615));
            if(this.isSuper()){
                g.setColor(clr);
                g.setFont(font1);
                g.drawString(String.format("%s", "S"), (int)(width * 0.053), (int)(Height*0.515));
            }
            g.setColor(clrprev);
            g.setFont(prevFont);
        }else{
            Font font = new Font( Font.SANS_SERIF, 3, (int)((Height + width) * 0.00933));
            g.setFont(font);
            g.drawString(String.format("%s", this.game.ME.opponent.getMana()), (int)(width * 0.007), (int)(Height*0.199));
            g.drawString(String.format("%s", this.game.ME.opponent.getHP()), (int)(width * 0.005), (int)(Height*0.3));
            g.drawString(String.format("%s", this.game.ME.opponent.getManaStack()) + "/3", (int)(width * 0.054), (int)(Height*0.194));
            g.setFont(font1);
            if(this.isSuper()){
                g.setColor(clrop);
                g.setFont(font1);
                g.drawString(String.format("%s", "S"), (int)(width * 0.0545), (int)(Height*0.31));
            }
            g.setColor(clrprev);
            g.setFont(prevFont);
        }
    }
}
