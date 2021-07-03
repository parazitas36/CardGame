package com.company.MultiPlayer;
import com.company.Classes.Card;
import com.company.Classes.ID;
import com.company.Classes.Phase;
import com.company.Engine.Display;
import com.company.Engine.Game;
import com.company.Packets.Packet;
import com.company.Packets.Packet00Login;
import com.company.Packets.Packet01Disconnect;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class GameClient extends Thread  implements Serializable {
    private InetAddress ipAddress;
    private DatagramSocket socket;
    private Game game;

    public GameClient(Game game, String ipAddress){
        this.game = game;
        try {
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
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
            if(message.trim().equalsIgnoreCase("ok")){
                System.out.println("\tGame started...");

                ArrayList<PlayerMP> playerMPS = new ArrayList<>();
                playerMPS.add(game.ME);
                playerMPS.add(game.ME.opponent);
                if(game.menuMusicClip != null){
                    game.menuMusicClip.stop();
                    game.menuMusicClip.setFramePosition(0);
                }
                game.gameState.isMenu = false;
                game.gameState.isLoading = true;
                game.startGameMP(playerMPS);
                game.gameState.isLoading = false;
                game.gameState.isGame = true;
                game.gameState.startGame = true;
                sendData("update".getBytes());
            }
            byte[] messageBuf = packet.getData();
            byte[] code = new byte[2];
            code[0] = messageBuf[0]; code[1] = messageBuf[1];
            if(new String(code).trim().equalsIgnoreCase("02")){
                System.out.println("Got update...");
                byte[] buf = new byte[messageBuf.length - 2];
                for(int i = 2; i < messageBuf.length; i++){
                    buf[i-2] = messageBuf[i];
                }
                ByteArrayInputStream byteStream = new ByteArrayInputStream(buf);
                try {
                    ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));
                    Object o = is.readObject();
                    Card card = (Card)o;
                    if(card != null) {
                        System.out.println("Got a card: " + card.getName());
                    }else{
                        System.out.println("Didn't get a card...");
                    }
                    game.ME.opponent.playerHandSlots.get(0).setCard(card);
                    is.close();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            //System.out.println("SERVER > " + new String(packet.getData()).trim() + " ip address: " + packet.getAddress().toString());
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        byte[] message = new byte[2];
        message[0] = data[0]; message[1] = data[1];
        Packet.PacketTypes type = Packet.lookupPacket(new String(message).substring(0, 2));
        Packet packet = null;
        switch (type){
            default:
                break;
            case INVALID:
                break;
            case LOGIN:
                packet = new Packet00Login(data);
                System.out.println("["+address.getHostAddress()+":"+port+"] " + ((Packet00Login)packet).getUsername() + " has joined the game...");
                ID tempID = game.ME.getID() == ID.Player1 ? ID.Player2 : ID.Player1;
                game.ME.opponent  = new PlayerMP(tempID, null, null, null, game, address, port);
                game.ME.opponent .decreaseHP(5);
                ((PlayerMP) (game.ME.opponent )).setUsername(((Packet00Login) packet).getUsername());
                break;
            case DISCONNECT:
                packet = new Packet01Disconnect(data);
                System.out.println("["+address.getHostAddress()+":"+port+"]" + ((Packet01Disconnect)packet).getUsername() + " has left a game...");
                break;
        }
    }

    public void sendData(byte[] data){
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
