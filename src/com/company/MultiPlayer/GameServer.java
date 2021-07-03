package com.company.MultiPlayer;

import com.company.Classes.Card;
import com.company.Classes.Deck;
import com.company.Classes.ID;
import com.company.Classes.Player;
import com.company.Engine.Game;
import com.company.Packets.*;
import com.company.Utils.CardReader;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class GameServer extends Thread implements Serializable {
    private DatagramSocket socket;
    private Game game;
    public int count ;
    private ArrayList<PlayerMP> mpPlayers;
    public GameServer(Game game){
        this.game = game;
        mpPlayers = new ArrayList<PlayerMP>();
        try {
            this.socket = new DatagramSocket(1331);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.count = 0;
    }
    public void run(){
        while(true){
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
            String message = new String(packet.getData());
//            System.out.println("CLIENT > " +new String(packet.getData()).trim() + " ip address: " + packet.getAddress().toString());
//            if(message.trim().equalsIgnoreCase("ping")){
//                sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
//            }
            if(message.trim().equalsIgnoreCase("start")){

                //sendData("ok".getBytes(), packet.getAddress(), packet.getPort());
                if(game.menuMusicClip != null){
                    game.menuMusicClip.stop();
                    game.menuMusicClip.setFramePosition(0);
                }
                game.gameState.isMenu = false;
                game.gameState.isLoading = true;
                game.startGameMP(mpPlayers);
                game.gameState.isLoading = false;
                game.gameState.isGame = true;
                game.gameState.startGame = true;
                sendData("ok".getBytes(), packet.getAddress(), packet.getPort());
            }
            if(message.trim().equalsIgnoreCase("update")){
                System.out.println("Updating...");
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream(16);
                ObjectOutputStream os = null;
                try {
                     os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
                     os.flush();
                     Card card = game.ME.playerHandSlots.get(0).getCard();
                    System.out.println("Sending card: " + card.getName());
                     os.writeObject(card);
                     os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] sendBuf = byteStream.toByteArray();
                String code = "02";
                System.out.println("Uzima baitu:" + code.getBytes().length);
                byte[] newBuf = new byte[sendBuf.length + 2];
                byte[] codeBytes = code.getBytes();
                newBuf[0] = codeBytes[0];
                newBuf[1] = codeBytes[1];
                for(int i = 2; i < sendBuf.length; i++){
                    newBuf[i-2] = sendBuf[i];
                }
                System.out.println("Packet size: " + newBuf.length);
                sendData(newBuf, packet.getAddress(), packet.getPort());
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;
        switch (type){
            default:
                break;
            case INVALID:
                break;
            case LOGIN:
                packet = new Packet00Login(data);
                System.out.println("["+address.getHostAddress()+":"+port+"]" + ((Packet00Login)packet).getUsername() + " has connected...");
                if(game.ME == null) {
                    game.ME = new PlayerMP(ID.Player1, null, null, null, game, address, port);
                    game.ME.setUsername(((Packet00Login) packet).getUsername());
                    this.addConnection(game.ME, (Packet00Login) packet);
                }else{
                    game.ME.opponent = new PlayerMP(ID.Player2, null, null, null, game, address, port);
                    game.ME.opponent.setUsername(((Packet00Login) packet).getUsername());
                    this.addConnection(game.ME.opponent, (Packet00Login) packet);
                }
                break;
            case DISCONNECT:
                packet = new Packet01Disconnect(data);
                System.out.println("["+address.getHostAddress()+":"+port+"]" + ((Packet01Disconnect)packet).getUsername() + " has disconnected...");
                this.removeConnection((Packet01Disconnect)packet);
                break;
        }
    }

    public void removeConnection(Packet01Disconnect packet) {
        this.mpPlayers.remove(getPlayerMPIndex(packet.getUsername()));
        packet.writeData(this);
    }

    public int getPlayerMPIndex(String username){
        int index = 0;
        for(PlayerMP p : mpPlayers){
            if(p.getUsername().equals(username)){
                break;
            }
            index++;
        }
        return index;
    }

    public void addConnection(PlayerMP player, Packet00Login packet) {
        boolean alreadyConnected = false;
        for(PlayerMP p : this.mpPlayers){
            if(player.getUsername().equalsIgnoreCase(p.getUsername())){
                if(p.ip == null){
                    p.ip = player.ip;
                }

                if(p.port == -1){
                    p.port = player.port;;
                }
                alreadyConnected = true;
            }else{
                sendData(packet.getData(), p.ip, p.port);

                packet = new Packet00Login(p.getUsername());
                sendData(packet.getData(), player.ip, player.port);
            }
        }
        if(!alreadyConnected){
            this.mpPlayers.add(player);
        }
        System.out.println("Players count:\t" + mpPlayers.size());
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port){
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendDataToAllClients(byte[] data){
        for(PlayerMP p : mpPlayers){
            if(p.ip != null && p.port >= 0)
            sendData(data, p.ip, p.port);
        }
    }
}
