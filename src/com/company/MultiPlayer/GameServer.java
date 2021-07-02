package com.company.MultiPlayer;

import com.company.Classes.Deck;
import com.company.Classes.ID;
import com.company.Classes.Player;
import com.company.Engine.Game;
import com.company.Packets.*;
import com.company.Utils.CardReader;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class GameServer extends Thread{
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
            System.out.println(mpPlayers.size());
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
                sendDataToAllClients("ok".getBytes());
            }
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        System.out.println(message);
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
        System.out.println("size:" + mpPlayers.size());
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
