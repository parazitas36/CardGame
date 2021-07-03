package com.company.Packets;

import com.company.MultiPlayer.GameClient;
import com.company.MultiPlayer.GameServer;

import java.io.Serializable;

public abstract class Packet  implements Serializable {
    public static enum PacketTypes{
        INVALID(-1), LOGIN(00), DISCONNECT(01), GAME(02);
        private int packetID;

        private PacketTypes(int packetID){
            this.packetID = packetID;
        }
        public int getID(){
            return packetID;
        }
    }

    public byte packetID;

    public Packet(int packetID){
        this.packetID = (byte)packetID;
    }

    public abstract  void writeData(GameClient client);
    public abstract  void writeData(GameServer server);

    public String readData(byte[] data){
        String message = new String(data).trim();
        return message.substring(2);
    }
    public abstract Object readObject(byte[] data);
    public static PacketTypes lookupPacket(String packetId){
        try{
            return lookupPacket(Integer.parseInt(packetId));
        }catch (NumberFormatException e){
            return PacketTypes.INVALID;
        }
    }
    public static PacketTypes lookupPacket(int id){
        for(PacketTypes p : PacketTypes.values()){
            if(p.getID() == id){
                return p;
            }
        }
        return PacketTypes.INVALID;
    }
}
