package com.company.MultiPlayer;

import com.company.Classes.CardSlot;
import com.company.Classes.Deck;
import com.company.Classes.ID;
import com.company.Classes.Player;
import com.company.Engine.Display;
import com.company.Engine.Game;

import java.net.InetAddress;
import java.util.ArrayList;

public class PlayerMP extends Player {
    public InetAddress ip;
    public int port;
    public String username;
    public PlayerMP opponent;

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
}
