package com.company.Packets;

import com.company.MultiPlayer.GameClient;
import com.company.MultiPlayer.GameServer;

import java.io.Serializable;

public class Packet00Login extends Packet  implements Serializable {
    private String username;
    public Packet00Login(byte[] data){
        super(00);
        this.username = readData(data);
    }
    public Packet00Login(String username){
        super(00);
        this.username = username;
    }
    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public Object readObject(byte[] data) {
        return null;
    }

    public byte[] getData(){
        return ("00" + this.username).getBytes();
    }

    public String getUsername() {
        return this.username;
    }
}
