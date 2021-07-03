package com.company.Packets;

import com.company.MultiPlayer.GameClient;
import com.company.MultiPlayer.GameServer;

import java.io.Serializable;

public class Packet01Disconnect extends Packet  implements Serializable {
    private String username;
    public Packet01Disconnect(byte[] data){
        super(01);
        this.username = readData(data);
    }
    public Packet01Disconnect(String username){
        super(01);
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
        return ("01" + this.username).getBytes();
    }

    public String getUsername() {
        return this.username;
    }
}
