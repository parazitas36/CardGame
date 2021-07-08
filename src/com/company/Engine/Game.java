package com.company.Engine;

import com.company.Classes.*;
import com.company.Database.DatabaseHandler;
import com.company.MultiPlayer.GameClient;
import com.company.MultiPlayer.GameServer;
import com.company.MultiPlayer.PlayerMP;
import com.company.TCPMP.TCPClient;
import com.company.TCPMP.TCPServer;
import com.company.Utils.CardReader;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.Serializable;
import java.util.ArrayList;


public class Game implements Runnable, Serializable {
    public Display display;
    private String title;
    private int width;
    private int height;
    private ImageIcon boardImg,
            backToMenu,
            backImg;
    public Image destroy;
    public Image stun;
    public Image buffimg;
    public Image curseimg;
    public Image bleedimg;
    public Image boosthpimg;

    public MusicPlayer musicPlayer;
    public AudioInputStream menuMusic;
    public AudioInputStream inGameMusic;
    public AudioInputStream winnerMusic;
    public AudioInputStream loserMusic;
    public AudioInputStream sound_drawEffect;
    public AudioInputStream sound_attackEffect;
    public AudioInputStream sound_destroyEffect;
    public AudioInputStream sound_placedEffect;
    public AudioInputStream sound_stunEffect;
    public AudioInputStream sound_buffEffect;
    public AudioInputStream sound_death;
    public AudioInputStream sound_hpBuff;
    public AudioInputStream sound_hpCurse;
    public Clip menuMusicClip;
    public Clip inGameMusicClip;
    public Clip winnerMusicClip;
    public Clip loserMusicClip;
    public Clip sound_drawEffectClip;
    public Clip sound_attackEffectClip;
    public Clip sound_destroyEffectClip;
    public Clip sound_placedEffectClip;
    public Clip sound_stunEffectClip;
    public Clip sound_buffEffectClip;
    public Clip sound_deathClip;
    public Clip sound_hpBuffClip;
    public Clip sound_hpCurseClip;

    public  DatabaseHandler dbHandler;

    public long TimeBefore = 0;

    private Thread thread;
    public boolean running;

    private ArrayList<CardSlot> player1_slots;
    private ArrayList<CardSlot> player2_slots;

    public GameClient socketClient;
    public GameServer server;
    public TCPServer tcpServer;
    public TCPClient tcpClient;

    public Player player1;
    public Player player2;
    public PlayerMP ME;
    public Player winner;

    private CardSlot draggingSlot;
    private Card draggingCard;
    private CardSlot chosenCardSlot;

    private ArrayList<Card> cards;
    public PlayerMP currentPlayer;
    public Phase phase;

    private boolean clean;

    private boolean mouseHolding;
    private Graphics g;
    private BufferStrategy buffer;
    private Handler handler;

    public GameState gameState;
    public WindowHandler windowHandler;

    private double deltaTime;
    private boolean startOfGame;
    private int slotClickedNr;
    private boolean reseted = false;

    Board board;
    Deck deck;
    Deck opponentDeck;
    private int dragginCardOffsetX = -50, dragginCardOffsetY = -100;
    public boolean isGuest = false;
    public Game(String _title, int _width, int _height){
        title = _title;
        width = _width;
        height = _height;
        gameState = new GameState(_width, _height, this);
        clean = false;

        musicPlayer = new MusicPlayer();
        menuMusic = musicPlayer.getAudio("src/com/company/Assets/Muzika/InGameMusic.wav");
        inGameMusic = musicPlayer.getAudio("src/com/company/Assets/Muzika/InGameMusic.wav");
        winnerMusic = musicPlayer.getAudio("src/com/company/Assets/Muzika/WinnerMusic.wav");
        loserMusic = musicPlayer.getAudio("src/com/company/Assets/Muzika/LosingMusic.wav");
        sound_attackEffect = musicPlayer.getAudio("src/com/company/Assets/Muzika/AttackSound.wav");
        sound_destroyEffect = musicPlayer.getAudio("src/com/company/Assets/Muzika/DestroyCardSound.wav");
        sound_placedEffect = musicPlayer.getAudio("src/com/company/Assets/Muzika/PutACardForPlayer.wav");
        sound_drawEffect = musicPlayer.getAudio("src/com/company/Assets/Muzika/Maišymas.wav");
        sound_stunEffect = musicPlayer.getAudio("src/com/company/Assets/Muzika/stun.wav");
        sound_buffEffect = musicPlayer.getAudio("src/com/company/Assets/Muzika/buff.wav");
        sound_death = musicPlayer.getAudio("src/com/company/Assets/Muzika/death.wav");
        sound_hpBuff = musicPlayer.getAudio("src/com/company/Assets/Muzika/hpBuff.wav");
        sound_hpCurse = musicPlayer.getAudio("src/com/company/Assets/Muzika/hpCurse.wav");
        sound_deathClip = null;
        sound_drawEffectClip = null;
        sound_attackEffectClip = null;
        sound_destroyEffectClip = null;
        sound_placedEffectClip = null;
        winnerMusicClip = null;
        loserMusicClip = null;
        sound_stunEffectClip = null;
        sound_buffEffectClip = null;
        sound_hpBuffClip = null;
        sound_hpCurseClip = null;
       // menuMusicClip = musicPlayer.playMusic(menuMusic);
       // menuMusicClip.start();
    }
    public void SetDisplaySize(int width, int height){
        display.SetDisplaySize(width, height);
        this.height = height;
        this.width = width;
        gameState.setDisplaySize(width, height);
    }
    private void init(){
        display = new Display(title, width, height);
        backImg = null;
            new ImageIcon("src/com/company/Images/destroy.gif").getImage();
            destroy = new ImageIcon("src/com/company/Images/destroy.gif").getImage();
            stun = new ImageIcon("src/com/company/Images/stun.gif").getImage();
            buffimg = new ImageIcon("src/com/company/Images/Buff.gif").getImage();
            curseimg = new ImageIcon("src/com/company/Images/destroy.gif").getImage();
            boosthpimg = new ImageIcon("src/com/company/Images/Heal.gif").getImage();
            bleedimg = new ImageIcon("src/com/company/Images/bleed2.gif").getImage();

            backImg = new ImageIcon("src/com/company/Images/back.png");
            boardImg = new ImageIcon("src/com/company/Images/Board.png");
            backToMenu = new ImageIcon("src/com/company/Images/backtomenu.png");

        new MouseHandler(display.getCanvas(), this);
        new WindowHandler(this);
    }
    public void TCP_MP(){
        String host = JOptionPane.showInputDialog(this.display.getFrame(), "Enter host IP: ", "Connect to serverv");
        dbHandler = new DatabaseHandler(this.display.getFrame(), host);
        Object[] options = {"Create New Account", "Login", "Guest"};
        isGuest = false;
        reseted = false;
        while(!dbHandler.loggedIn) {
            int value = JOptionPane.showOptionDialog(this.display.getFrame(), "How would you like to join?", "LOGIN", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (value == 0) {
                dbHandler.createUser();
                dbHandler.login();
            } else if(value == 1){
                dbHandler.login();
            }else{
                isGuest = true;
                break;
            }
        }
//        if(JOptionPane.showConfirmDialog(this.display.getFrame(), "Host?") == 0){
//            tcpServer = new TCPServer();
//            System.out.println("Server created.");
//            tcpServer.acceptConnections();
//        }else{
            System.out.println("Client created.");
            tcpClient = new TCPClient(host, this);
            if(!isGuest) {
                tcpClient.sendOppUsername(dbHandler.getUser().username);
            }else{
                tcpClient.sendOppUsername("Guest");
            }
    }
    public void initMP(){
        ID id;
        ID oppID;
        id = tcpClient.playerNr == 1 ? ID.Player1 : ID.Player2;
        oppID = id == ID.Player1 ? ID.Player2 : ID.Player1;
        ME = new PlayerMP(id, null, null, display, this, null, -1);
        ME.opponent = new PlayerMP(oppID, null, null, display, this, null, -1);
        ME.setTCPClient(tcpClient);
        this.gameState.isMenu = false;
        this.gameState.isLoading = true;
        startGameMP(new ArrayList<>());
        this.gameState.isLoading = false;
        this.gameState.isGame = true;
        this.gameState.startGame = true;
    }
    /*
    public void MP(){
        String hostIP = null;
        if(JOptionPane.showConfirmDialog(this.display.getFrame(), "Host?") == 0){
            server = new GameServer(this);
            server.start();
        }else{
            hostIP = JOptionPane.showInputDialog(this.display.getFrame(), "Enter a host IP you want to join: ");
        }
        if(hostIP == null){
            socketClient = new GameClient(this, "localhost");
        }else{
            socketClient = new GameClient(this, hostIP);
        }
        socketClient.start();
        String username = JOptionPane.showInputDialog(this.display.getFrame(), "Enter username: ");
        System.out.println("Username: " + username);

        Packet00Login loginPacket = new Packet00Login(username);

        if(server != null){
            ME = new PlayerMP(ID.Player1, null, null, display, this, null, -1);
            ME.decreaseHP(2);
            ME.setUsername(username);
            System.out.println("Serveris");
            server.addConnection(ME, loginPacket);
        }else{
            ME = new PlayerMP(ID.Player2, null, null, display, this, null, -1);
            ME.setUsername(username);
        }

        loginPacket.writeData(socketClient);
        if(server == null)
        socketClient.sendData("Start".getBytes());
        //socketClient.sendData("ping".getBytes());
    }
     */
    /*
    public void startGame(){
        clean = false;
        board = new Board(display, this);
        handler = new Handler();
        draggingSlot = new CardSlot((Card) null, 0, 0, ID.Dragging_Slot, 0);
        draggingSlot.setWidth((int)(display.getWidth()*0.1));
        draggingSlot.setHeight((int)(display.getHeight()*0.2));

        // Creates card slots for both players
        player1_slots = board.getPlayer1_slots();
        for(CardSlot s: player1_slots){
            handler.addObject(s);
        }
        player2_slots = board.getPlayer2_slots();
        for(CardSlot s: player2_slots){
            handler.addObject(s);
        }

        cards = CardReader.Read("src/com/company/Assets/Cards_Data.txt"); // Reads cards data from file
        deck = new Deck(cards.size(), player1_slots.get(5).getX(), player1_slots.get(5).getY(), cards, backImg);
        player1_slots.get(5).setDeck(deck);
        deck.shuffle();
        cards = new ArrayList<>();
        cards = CardReader.Read("src/com/company/Assets/Cards_Data.txt");

        if(inGameMusicClip == null){
            System.out.println("dd");
            inGameMusicClip = musicPlayer.playSound(inGameMusic);
        }
        if(!inGameMusicClip.isRunning()){
            musicPlayer.repeatMusic(inGameMusicClip);
        }
        opponentDeck = new Deck(cards.size(), player2_slots.get(5).getX(), player2_slots.get(5).getY(), cards, backImg);
        player2_slots.get(5).setDeck(opponentDeck);
        opponentDeck.shuffle();

        player1 = new Player(ID.Player1, deck, player1_slots, display, this);
        player2 = new AIPlayer(ID.Player2, opponentDeck, player2_slots, display, this);

        player1.setOpponent(player2);
        player2.setOpponent(player1);

        phase = new Phase(width, height, player1, player2);
        handler.addObject(player1);
        handler.addObject(player2);

        handler.addObject(draggingSlot);
        currentPlayer = player1;
        gameState.startGame = false;
    }
     */

    public void startGameMP(ArrayList<PlayerMP> players){
        clean = false;
        startOfGame = true;
        board = new Board(display, this);
        handler = new Handler();
        draggingSlot = new CardSlot((Card) null, 0, 0, ID.Dragging_Slot, 0, this);
        draggingSlot.setWidth((int)(display.getWidth()*0.1));
        draggingSlot.setHeight((int)(display.getHeight()*0.2));

        // Creates card slots for both players
        player1_slots = board.getPlayer1_slots();
        for(CardSlot s: player1_slots){
            handler.addObject(s);
        }
        player2_slots = board.getPlayer2_slots();
        for(CardSlot s: player2_slots){
            handler.addObject(s);
        }

        //cards = CardReader.Read("src/com/company/Assets/Cards_Data.txt"); // Reads cards data from file
        cards = CardReader.Read(tcpClient.cardLines);
        deck = new Deck(cards.size(), player1_slots.get(5).getX(), player1_slots.get(5).getY(), cards, backImg);
        player1_slots.get(5).setDeck(deck);
        //deck.shuffle();
        cards = new ArrayList<>();
        //cards = CardReader.Read("src/com/company/Assets/Cards_Data.txt");
        cards = CardReader.Read(tcpClient.cardOppLines);

        if(inGameMusicClip == null){
            inGameMusicClip = musicPlayer.playSound(inGameMusic);
        }
        if(!inGameMusicClip.isRunning()){
            musicPlayer.repeatMusic(inGameMusicClip);
        }
        opponentDeck = new Deck(cards.size(), player2_slots.get(5).getX(), player2_slots.get(5).getY(), cards, backImg);
        player2_slots.get(5).setDeck(opponentDeck);
       // opponentDeck.shuffle();

        ME.setOpponent(ME.opponent);
        ME.opponent.setOpponent(ME);
        ME.deck = deck;
        ME.playerSlots = player1_slots;
        ME.display = display;
        ME.filterSlots();

        ME.opponent.deck = opponentDeck;
        ME.opponent.playerSlots = player2_slots;
        ME.opponent.display = display;
        ME.opponent.filterSlots();
        if(ME.getID() == ID.Player1) {
            currentPlayer = ME;
        }else{
            currentPlayer = ME.opponent;
        }

        phase = new Phase(width, height, currentPlayer, currentPlayer.opponent, this);
        if(ME.getID() == ID.Player1){
            phase.attack = true;
            phase.enemy = false;
        }else{
            phase.attack = false;
            phase.enemy = true;
        }
        handler.addObject(ME);
        handler.addObject(ME.opponent);
        handler.addObject(draggingSlot);
        gameState.startGame = false;
    }


    public double animTimer = 0;
    public boolean inAnimation = false;
    public ID animationCardID;
    double animX, animY;
    public int intID;
    private boolean isEnemyAttacking;
    public void setAttacking(int index, boolean _isEnemyAttacking){
        inAnimation = true;
//        animationCardID = id;
        intID = index;
        animTimer = 0;
        isEnemyAttacking = _isEnemyAttacking;
    }
    public int targetX = 0, targetY = 200;
    private void tick(){
        if(inAnimation && !isEnemyAttacking){
            animTimer += 1;
            // TODO move to animationHandler
            // swap animation direction if enemy
            if(animTimer < 17){
                animY -= (1 - (animTimer / 50)) * 5f;
                animX += targetX * (animTimer/(17*17/2));
            }else if(animTimer < 34){
                animY += (1 - ((animTimer - 50) / 50)) * 3f;
                animX -= targetX * ((animTimer-17)/(17*17/2));
            }else{
                inAnimation = false;
                animY = 0;
            }

            System.out.println("ANIM1: ");
            if(phase != null && !phase.weHaveAWinner()) {
                board.getPlayer1_slots().get(intID).SetAnimationOffsetX(animX);
                board.getPlayer1_slots().get(intID).SetAnimationOffsetY(animY);
            }
        }else if(inAnimation && isEnemyAttacking){
            animTimer += 1;
            // TODO move to animationHandler
            // swap animation direction if enemy
            if(animTimer < 17){
                animY += (1 - (animTimer / 50)) * 5f;
                animX += targetX * (animTimer/(17*17/2));
            }else if(animTimer < 34){
                animY -= (1 - ((animTimer - 50) / 50)) * 3f;
                animX -= targetX * ((animTimer-17)/(17*17/2));
            }else{
                inAnimation = false;
                animY = 0;
            }


            System.out.println("ANIM2: ");
            board.getPlayer2_slots().get(intID).SetAnimationOffsetX(animX);
            board.getPlayer2_slots().get(intID).SetAnimationOffsetY(animY);


        }

        if(gameState.isGame) {
            if(winnerMusicClip != null && winnerMusicClip.isRunning()) {
                winnerMusicClip.stop();
            }
            if(loserMusicClip != null && loserMusicClip.isRunning()){
                loserMusicClip.stop();
            }
            handler.tick();
            currentPlayer = phase.getCurrentPlayer();
            phase.updateTime();
            phase.checkTime();
            if(phase.weHaveAWinner()){
                gameState.isGame = false;
                gameState.celebrationWindow = true;
                if(currentPlayer.getHP() <= 0){
                    winner = currentPlayer.opponent;
                }else if(currentPlayer.opponent.getHP() <= 0) {
                    winner = currentPlayer;
                }
            }
        }else if(gameState.celebrationWindow){
            if(inGameMusicClip != null){
               inGameMusicClip.stop();
            }
            if(winner == player1 && winnerMusicClip == null){
                winnerMusicClip = musicPlayer.playSound(winnerMusic);
                musicPlayer.repeatSound(winnerMusicClip);
            }else if(winner == player1 && !winnerMusicClip.isRunning()){
                musicPlayer.repeatSound(winnerMusicClip);
            }
            if(winner == player2 && loserMusicClip == null){
                loserMusicClip = musicPlayer.playSound(loserMusic);
                musicPlayer.repeatSound(loserMusicClip);
            }else if(winner == player2 && !loserMusicClip.isRunning()){
                musicPlayer.repeatSound(loserMusicClip);
            }
            if(!clean) {
                player1 = null;
                player2 = null;
                if (cards != null) {
                    cards.clear();
                }
                cards = null;
                board = null;
                draggingSlot = null;
                draggingCard = null;
                handler = null;
                if (player1_slots != null) {
                    player1_slots.clear();
                }
                player1_slots = null;
                if (player2_slots != null) {
                    player2_slots.clear();
                }
                player2_slots = null;
                opponentDeck = null;
                phase = null;
                Runtime.getRuntime().gc();
                clean = true;
            }
        }
    }
    public void render(){
        buffer = display.getCanvas().getBufferStrategy();
        if(buffer == null){
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g = buffer.getDrawGraphics();
        // Drawing
        if(gameState.isMenu){
            //g.drawImage(menuBackgroundImg, 0, 0, width, height, null);
            gameState.render(g);
        }else if(gameState.isLoading){
            g.drawImage(display.loadingIMG.getImage(), 0, 0, width, height, null);
        }
        else if(gameState.isGame){
            g.drawImage(boardImg.getImage(), 0, 0, width, height, null);
            g.drawImage(phase.GetCurrentPhaseImage().getImage(), phase.GetEndTurnPosX(), phase.GetEndTurnPosY(), phase.GetEndTurnImgWidth(), phase.GetEndTurnImgHeight(), null);

            g.setColor(Color.WHITE);
            handler.render(g);
            DrawDraggingCard();
            Font prev = g.getFont();
            Font font = new Font(Font.SANS_SERIF, 3, (int)(height * 0.025));
            g.setFont(font);
            int roundTime = 35 - (int)phase.elapsedTime;
            String timer = "Time left: " + roundTime;
            Color color;
            if(roundTime <= 23 && roundTime >= 15){
                color = new Color(255, 224, 0);
            }else if (roundTime < 15 && roundTime >= 8){
                color = new Color(255, 159, 51);
            }else if (roundTime < 8){
                color = new Color(255, 0, 0);
            }else{
                color = new Color(255, 255, 255);
            }
            g.setColor(color);
            g.drawString(timer, (int)(width * 0.01), (int)(height * 0.41));
            g.setFont(prev);
            g.setColor(Color.WHITE);
        }else if(gameState.celebrationWindow){
            g.setColor(Color.black);
            g.fillRect(0, 0 , display.getWidth(), display.getHeight());
            g.setColor(Color.WHITE);
            Font prevFont = g.getFont();
            Font newFont = new Font(Font.SANS_SERIF, 3, 30);
            g.setFont(newFont);
            String winnerTitle = "";
            String uname =  null;
            if(!isGuest) {
                uname = dbHandler.getUser().username;
            }
            if(winner.getID() == ME.getID()){
                if(uname != null && !dbHandler.updated){
                    dbHandler.updateStats(true);
                    winnerTitle = dbHandler.getUser().username + " won!";
                }else if(isGuest){
                    winnerTitle = "Guest won!";
                }else{
                    winnerTitle = dbHandler.getUser().username + " won!";
                }
                this.gameState.ready = false;
            }else{
                if(uname != null && !dbHandler.updated){
                    dbHandler.updateStats(false);
                }
                winnerTitle = tcpClient.oppUsername + " won!";
                this.gameState.ready = false;
            }
            if(!reseted){
                this.tcpClient.reset();
                reseted = true;
            }
            g.drawImage(backToMenu.getImage(), (int)(width * 0.5 - width * 0.1), (int)(height * 0.5) + (int)(height * 0.1), (int)(width * 0.2), (int)(height * 0.1), null);
            g.drawString(winnerTitle, (int) (display.getWidth()*0.5 - winnerTitle.length() * 0.25 * 30), height/2);
            g.setFont(prevFont);

        }else if(gameState.isMP){
            gameState.render(g);
        }
        //-------------------------------
        buffer.show();
        g.dispose();
    }
    @Override
    public void run() {
        init();

        int fps = 60;
        double timePerTick = 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        int draws = 0;
        while(running){
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            lastTime = now;
            if(delta >= 1){
                tick();
                render();
                if(gameState.isGame) {
                    if (this.startOfGame && phase.getCurrentRound() == 1 && draws < 4) {
                        draws = startOfTheGame(draws);
                        phase.startTime = System.nanoTime();
                    }else{
                        startOfGame = false;
                    }
//                    if (phase.enemyTurn()) {
//                        enemyTurn();
//                    }
                }
                if(gameState.isMenu){ draws = 0; }
                delta--;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stop();
    }
    public synchronized void start(){
        if(running){
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }
    public synchronized void stop(){
        if(!running){
            return;
        }
        running = false;
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void exit(){
        System.exit(0);
    }
    public static void main(String[] args) {
        Game game = new Game("UbiHard Card Game", 1366, 768);
        game.start();
        // 1440x980
    }

    public void DrawDraggingCard(){

        // Drags a card
        if(mouseHolding && display.getFrame().getMousePosition() != null) {
            draggingSlot.setCard(draggingCard);
            draggingSlot.setX(display.getFrame().getMousePosition().x + dragginCardOffsetX);
            draggingSlot.setY(display.getFrame().getMousePosition().y + dragginCardOffsetY);
        }else{
            draggingSlot.setX(-1000);
            draggingSlot.setY(-1000);
        }

    }
    public void SlotClicked(Card card, CardSlot slot){
        if(card != null) {
            mouseHolding = true;
            draggingCard = card;
            this.slotClickedNr = ME.playerHandSlots.indexOf(slot);
            this.chosenCardSlot = slot;
            this.chosenCardSlot.removeCard();
            System.out.println("Selected card:\t" + draggingCard.getName());
        }
    }
    public void drawffect(int x, int y, Image image, int time, String c){
        if(TimeBefore < 500){
            TimeBefore = System.nanoTime();
        }
        if (c.equals("-hp")){
            while((System.nanoTime() - TimeBefore)/1000000000 <  time){
                g.drawImage(image, 0, 0, display.getWidth(), display.getHeight(), null);
            }
        }else if(c.equals("+hp")){
            while((System.nanoTime() - TimeBefore)/1000000000 <  time) {
                g.drawImage(image, (int) (display.getWidth() * 0.1), (int) (display.getHeight() - (int)(display.getHeight()*0.35)), (int) (display.getWidth()*0.8), (int) (display.getHeight() * 0.3), null);
            }
        }else{
            while((System.nanoTime() - TimeBefore)/1000000000 <  time){
                g.drawImage(image, x, y, (int)(display.getWidth()*0.1), (int)(display.getHeight()*0.20), null);
            }
        }
    }
    public void threadSleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // Places card on the board
    public void MouseReleased(){
        mouseHolding = false;
        for(CardSlot c : currentPlayer.playerBoardSlots){
            int targetCardIndex = currentPlayer.playerBoardSlots.indexOf(c);
            int x = c.getX(); int y = c.getY();
            if(display.getFrame().getMousePosition() != null && display.getFrame().getMousePosition().x >= x && display.getFrame().getMousePosition().x <= x+c.getWidth() && display.getFrame().getMousePosition().y <= y + c.getHeight() && display.getFrame().getMousePosition().y >= y){
                // Buff
                if(this.phase.attackPhase() && draggingCard != null && c.cardOnBoard() && c.getCard().getID() == ID.Monster && draggingCard.getID() == ID.Buff){
                    if(((Buff)(draggingCard)).buffLogic(c, phase.getCurrentPlayer())){
                        this.tcpClient.sendBuff(slotClickedNr, targetCardIndex);drawffect(x, y, buffimg, 2, "Buff");
                        TimeBefore = 0;
                        chosenCardSlot.removeCard();
                        chosenCardSlot = null;
                        if(sound_buffEffectClip == null){
                            sound_buffEffectClip = musicPlayer.playSound(sound_buffEffect);
                            musicPlayer.repeatSound(sound_buffEffectClip);
                        }else{
                            musicPlayer.repeatSound(sound_buffEffectClip);
                        }

                    }
                    else if(draggingCard != null){
                        this.chosenCardSlot.setCard(draggingCard);
                    }
                    break;
                }else if(this.phase.attackPhase() && draggingCard != null && c.getId() != ID.Player1_Deck && !c.cardOnBoard()){
                    // Monster
                    if(draggingCard.getID() == ID.Monster){
                        if(currentPlayer.placeCard(c, draggingCard)) {
                            int boardSlotIndex = currentPlayer.playerBoardSlots.indexOf(c);
                            this.tcpClient.sendCardPlaced(slotClickedNr, boardSlotIndex);
                            this.chosenCardSlot.removeCard();
                            this.chosenCardSlot = null;
                        }else if(draggingCard != null){
                            this.chosenCardSlot.setCard(draggingCard);
                        }
                    }
                }
                // Curse HP logic
            }else if(this.phase.attackPhase() && draggingCard != null && draggingCard.getID() == ID.Curse && ((Curse)(draggingCard)).getEffect().equals("hp")){
                Curse curse = ((Curse)(draggingCard));
                if(curse.getEffect().equals("hp") && display.getFrame().getMousePosition().y <= ((int)(display.getHeight()*0.7))){
                    if(curse.hpCurseLogic(phase.getCurrentPlayer(), phase.getOpponent(), chosenCardSlot)){
                        this.tcpClient.sendCurseHP(slotClickedNr);
                        if(sound_hpCurseClip == null){
                            sound_hpCurseClip = musicPlayer.playSound(sound_hpCurse);
                            musicPlayer.repeatSound(sound_hpCurseClip);
                        }else{
                            musicPlayer.repeatSound(sound_hpCurseClip);
                        }
                        drawffect(x, y, bleedimg, 1, "-hp");
                        threadSleep(500);
                        TimeBefore = 0;
                        chosenCardSlot.removeCard();
                        chosenCardSlot = null;
                    }else{
                        this.chosenCardSlot.setCard(draggingCard);
                    }break;
                }
                // Buff HP logic
            }else if(this.phase.attackPhase() &&draggingCard != null && draggingCard.getID() == ID.Buff && ((Buff)(draggingCard)).getEffect().equals("hp")){
                Buff buff = ((Buff)(draggingCard));
                if(buff.getEffect().equals("hp") && display.getFrame().getMousePosition().y >= ((int)(display.getHeight()*0.4)) && display.getFrame().getMousePosition().y <= ((int)(display.getHeight()*0.8))){
                    if(buff.hpBuffLogic(phase.getCurrentPlayer(), chosenCardSlot)){
                        this.tcpClient.sendBuffHP(slotClickedNr);
                        if(sound_hpBuffClip == null){
                            sound_hpBuffClip = musicPlayer.playSound(sound_hpBuff);
                            musicPlayer.repeatSound(sound_hpBuffClip);
                        }else{
                            musicPlayer.repeatSound(sound_hpBuffClip);
                        }
                        drawffect(x, y, boosthpimg, 1, "+hp");
                        TimeBefore = 0;
                        chosenCardSlot.removeCard();
                        chosenCardSlot = null;
                    }else {
                        this.chosenCardSlot.setCard(draggingCard);
                    }  break;
                }
            }
        }
        // Curses which affect opponent cards
        for(CardSlot c : currentPlayer.opponent.playerBoardSlots){
            int cursedCardIndex = currentPlayer.opponent.playerBoardSlots.indexOf(c);
            int x = c.getX(); int y = c.getY();
            if(display.getFrame().getMousePosition() != null && display.getFrame().getMousePosition().x >= x && display.getFrame().getMousePosition().x <= x+c.getWidth() && display.getFrame().getMousePosition().y <= y + c.getHeight() && display.getFrame().getMousePosition().y >= y) {
                if (this.phase.attackPhase() && draggingCard != null && c.cardOnBoard() && c.getCard().getID() == ID.Monster && draggingCard.getID() == ID.Curse) {
                    if (currentPlayer.enoughManaForCard(draggingCard) && ((Curse) (draggingCard)).curseLogic(c, currentPlayer, currentPlayer.opponent)) {
                        tcpClient.sendCurse(slotClickedNr, cursedCardIndex);
                        if( ((Curse)(draggingCard)).getEffect().contains("stun") ){
                            if(sound_stunEffectClip == null){
                                sound_stunEffectClip = musicPlayer.playSound(sound_stunEffect);
                                musicPlayer.repeatSound(sound_stunEffectClip);
                            }else{
                                musicPlayer.repeatSound(sound_stunEffectClip);
                            }
                            drawffect(x, y, stun, 1, "Curse");
                            TimeBefore = 0;
                        }else if( ((Curse)(draggingCard)).getEffect().contains("destroy") ){
                            if(sound_destroyEffectClip == null){
                                sound_destroyEffectClip = musicPlayer.playSound(sound_destroyEffect);
                                musicPlayer.repeatSound(sound_destroyEffectClip);
                            }else{
                                musicPlayer.repeatSound(sound_destroyEffectClip);
                            }
                            drawffect(x, y, destroy, 1, "Curse");
                            TimeBefore = 0;
                        }
                        chosenCardSlot.removeCard();
                        chosenCardSlot = null;
                    }
                    else if(draggingCard != null){
                        this.chosenCardSlot.setCard(draggingCard);
                    }
                    break;
                }

            }
        }

        // If card wasn't placed returns it into hand.
        if(draggingCard != null && this.chosenCardSlot != null){
            this.chosenCardSlot.setCard(draggingCard);
        }
        draggingCard = null;
    }
    //----------------------------------------------------------
    // Draws cards in the beginning of the game for each player,
    // 3 cards for the first player and 4 for the second.
    //----------------------------------------------------------
    public int startOfTheGame(int draws){
        if(draws < 3) {
            currentPlayer.drawCard();
        }
        currentPlayer.opponent.drawCard();
        draws++;
        try {
            Thread.sleep(500); // Draws a card and waits a little bit before drawing another one
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return draws;
    }
    //--------------------------------------------------------
    // Imitation of enemy turn, draws a card at the beginning
    // of the turn, places the first card in the hand on the board.
    //--------------------------------------------------------
    /*
    public void enemyTurn(){
        render();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        phase.nextPhase();
    }
    */
    public void attackOpponent(CardSlot attacker, PlayerMP currentplayer){
        currentplayer.opponent.decreaseHP(((Monster)(attacker.getCard())).getAttack());
    }
}
