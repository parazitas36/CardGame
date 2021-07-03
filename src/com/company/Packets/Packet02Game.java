package com.company.Packets;

import com.company.Engine.Game;
import com.company.MultiPlayer.GameClient;
import com.company.MultiPlayer.GameServer;

import java.io.Serializable;

public class Packet02Game extends Packet  implements Serializable {
    private Game game;
    public Packet02Game(byte[] data){
        super(02);
        this.game = readObject(data);
    }
    public Packet02Game(Game game){
        super(02);
        this.game = game;
    }
    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }
    public byte[] getData(){
        return ("02" + this.game).getBytes();
    }

    @Override
    public Game readObject(byte[] data) {
        return null;
    }

    /*public String getUsername() {
        return this.username;
    }*/
}
