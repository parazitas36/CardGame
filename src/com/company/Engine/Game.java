package com.company.Engine;

import com.company.Classes.*;
import com.company.Utils.CardReader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Game implements Runnable{
    public Display display;
    private String title;
    private int width;
    private int height;
    private BufferedImage boardImg,
                            backToMenu,
                            backImg;
    private Image destroy;
    private Image stun;
    private Image buffimg;
    private Image curseimg;
    private Image bleedimg;
    private Image boosthpimg;

    long TimeBefore = 0;

    private Thread thread;
    public boolean running;

    private ArrayList<CardSlot> player1_slots;
    private ArrayList<CardSlot> player2_slots;
    private Player player1;
    private Player player2;
    private Player winner;

    private CardSlot draggingSlot;
    private Card draggingCard;
    private CardSlot chosenCardSlot;

    private ArrayList<Card> cards;
    public Player currentPlayer;
    public Phase phase;

    private boolean mouseHolding;
    private Graphics g;
    private BufferStrategy buffer;
    private Handler handler;

    public GameState gameState;

    private double deltaTime;

    Board board;
    Deck deck;
    Deck opponentDeck;
    private int dragginCardOffsetX = -50, dragginCardOffsetY = -100;

    public Game(String _title, int _width, int _height){
        title = _title;
        width = _width;
        height = _height;
        gameState = new GameState(_width, _height);

    }
    private void init(){
        display = new Display(title, width, height);


        backImg = null;
        try{
//            new ImageIcon("src/com/company/Images/destroy.gif").getImage();
            destroy = new ImageIcon("src/com/company/Images/destroy.gif").getImage();
            stun = new ImageIcon("src/com/company/Images/destroy.gif").getImage();
            buffimg = new ImageIcon("src/com/company/Images/Buff.gif").getImage();
            curseimg = new ImageIcon("src/com/company/Images/destroy.gif").getImage();
            boosthpimg = new ImageIcon("src/com/company/Images/Heal.gif").getImage();
            bleedimg = new ImageIcon("src/com/company/Images/bleed2.gif").getImage();

            backImg = ImageIO.read(new File("src/com/company/Images/back.png"));
            boardImg = ImageIO.read(new File("src/com/company/Images/Board.png"));
            backToMenu = ImageIO.read(new File("src/com/company/Images/backtomenu.png"));
        }catch (IOException e){
            e.printStackTrace();
        }


        new MouseHandler(display.getCanvas(), this);
    }
    public void startGame(){
        board = new Board(display);
        handler = new Handler();
        draggingSlot = new CardSlot((Card) null, 0, 0, ID.Dragging_Slot);
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

        opponentDeck = new Deck(cards.size(), player2_slots.get(5).getX(), player2_slots.get(5).getY(), cards, backImg);
        player2_slots.get(5).setDeck(opponentDeck);
        opponentDeck.shuffle();

        player1 = new Player(ID.Player1, deck, player1_slots, display);
        player2 = new Player(ID.Player2, opponentDeck, player2_slots, display);

        player1.setOpponent(player2);
        player2.setOpponent(player1);

        phase = new Phase(width, height, player1, player2);
        handler.addObject(player1);
        handler.addObject(player2);

        handler.addObject(draggingSlot);
        currentPlayer = player1;
        gameState.startGame = false;
    }
    public double animTimer = 0;
    public boolean inAnimation = false;
    public ID animationCardID;
    double animX, animY;
    public int intID;
    public void setAttacking(int index){
        inAnimation = true;
//        animationCardID = id;
        intID = index;
        animTimer = 0;
    }
    public int targetX = 0, targetY = 200;
    private void tick(){
        if(inAnimation){
            animTimer += 1;
            // TODO move to animationHandler
            // swap animation direction if enemy
            if(animTimer < 50){
                animY -= (1 - (animTimer / 50)) * 7f;
                animX += targetX * (animTimer/100);
            }else if(animTimer < 100){
                animY += (1 - ((animTimer - 50) / 50)) * 7f;
                animX -= targetX * ((animTimer-50)/100);
            }else{
                inAnimation = false;
                animY = 0;
            }

            if(!phase.enemyTurn()){
                System.out.println("ANIM1: ");
                board.getPlayer1_slots().get(intID).SetAnimationOffsetX(animX);
                board.getPlayer1_slots().get(intID).SetAnimationOffsetY(animY);
            }
            else{
                System.out.println("ANIM2: ");
                board.getPlayer2_slots().get(intID).SetAnimationOffsetX(animX);
                board.getPlayer2_slots().get(intID).SetAnimationOffsetY(animY);
            }

        }

        if(gameState.isGame) {
            handler.tick();
            currentPlayer = phase.getCurrentPlayer();
            phase.updateTime();
            phase.checkTime();
            if(phase.weHaveAWinner()){
                gameState.isGame = false;
                gameState.celebrationWindow = true;
                if(currentPlayer.getHP() <= 0){
                    System.out.println(currentPlayer.opponent.getID().toString() + " won!");
                    winner = currentPlayer.opponent;
                }else if(currentPlayer.opponent.getHP() <= 0) {
                    System.out.println(currentPlayer.getID().toString() + " won!");
                    winner = currentPlayer;
                }
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
            g.drawImage(display.loadingIMG, 0, 0, width, height, null);
        }
        else if(gameState.isGame){
            g.drawImage(boardImg, 0, 0, width, height, null);
            g.drawImage(phase.GetCurrentPhaseImage(), phase.GetEndTurnPosX(), phase.GetEndTurnPosY(), phase.GetEndTurnImgWidth(), phase.GetEndTurnImgHeight(), null);

            g.setColor(Color.WHITE);
            handler.render(g);
            DrawDraggingCard();
            Font prev = g.getFont();
            Font font = new Font(Font.SANS_SERIF, 3, (int)(height * 0.025));
            g.setFont(font);
            int roundTime = 35 - (int)phase.elapsedTime;
            String timer = "Time left: " + roundTime;
            if(roundTime <= 5){ g.setColor(Color.RED); }
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
            String winnerTitle = winner.getID().toString() + " won!";
            g.drawImage(backToMenu, (int)(width * 0.5 - width * 0.1), (int)(height * 0.5) + (int)(height * 0.1), (int)(width * 0.2), (int)(height * 0.1), null);
            g.drawString(winnerTitle, (int) (display.getWidth()*0.5 - winnerTitle.length() * 0.25 * 30), height/2);
            g.setFont(prevFont);

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
                    if (phase.startPhase() && phase.getCurrentRound() == 1 && draws < 4) {
                        draws = startOfTheGame(draws);
                        phase.startTime = System.nanoTime();
                    }
                    if (phase.enemyTurn()) {
                        enemyTurn();
                    }
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
        BufferedImage img;

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
            this.chosenCardSlot = slot;
            this.chosenCardSlot.removeCard();
            System.out.println("Selected card: " + card);
        }
    }
    public void drawffect(int x, int y, Image image, int time, String c){
        if(TimeBefore < 500){
            TimeBefore = System.nanoTime();
        }
        if (c.equals("-hp")){
            while((System.nanoTime() - TimeBefore)/1000000000 <  time){
                g.drawImage(image, 0, 0, display.getWidth(), (int)(display.getHeight()*0.2), null);
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
            int x = c.getX(); int y = c.getY();
            if(display.getFrame().getMousePosition() != null && display.getFrame().getMousePosition().x >= c.getX() && display.getFrame().getMousePosition().x <= c.getX()+c.getWidth() && display.getFrame().getMousePosition().y <= c.getY() + c.getHeight() && display.getFrame().getMousePosition().y >= c.getY()){
                if(draggingCard != null && c.cardOnBoard() && c.getCard().getID() == ID.Monster && draggingCard.getID() == ID.Buff){ // Buff
                    if(((Buff)(draggingCard)).buffLogic(c, phase.getCurrentPlayer())){
                        drawffect(x, y, buffimg, 1, "Buff");
                        threadSleep(500);
                        TimeBefore = 0;
                        chosenCardSlot.removeCard();
                        chosenCardSlot = null;
                    }
                }else if(draggingCard != null && c.getId() != ID.Player1_Deck && !c.cardOnBoard()){
                    if(draggingCard.getID() != ID.Buff && draggingCard.getID() != ID.Curse){ // Monster
                        if(currentPlayer.placeCard(c, draggingCard)) {
                            this.chosenCardSlot.removeCard();
                            this.chosenCardSlot = null;
                        }else{
                            this.chosenCardSlot.setCard(draggingCard);
                        }
                    }
                }
                //Curse hp logikia
            }else if(draggingCard != null && draggingCard.getID() == ID.Curse && ((Curse)(draggingCard)).getEffect().equals("hp")){
                Curse curse = ((Curse)(draggingCard));
                if(curse.getEffect().equals("hp") && display.getFrame().getMousePosition().y >= ((int)(display.getHeight()*0.3))){
                    drawffect(x, y, bleedimg, 1, "-hp");
                    threadSleep(500);
                    TimeBefore = 0;
                    curse.hpCurseLogic(phase.getCurrentPlayer(), phase.getOpponent(), chosenCardSlot);
                    chosenCardSlot.removeCard();
                    chosenCardSlot = null;
                    break;
                }
                //Buff hp logika
            }else if(draggingCard != null && draggingCard.getID() == ID.Buff && ((Buff)(draggingCard)).getEffect().equals("hp")){
                Buff buff = ((Buff)(draggingCard));
                if(buff.getEffect().equals("hp") && display.getFrame().getMousePosition().y >= ((int)(display.getHeight()*0.3))){
                    drawffect(x, y, boosthpimg, 1, "+hp");
                    threadSleep(500);
                    TimeBefore = 0;
                    buff.hpBuffLogic(c, phase.getCurrentPlayer(), chosenCardSlot);
                    chosenCardSlot.removeCard();
                    chosenCardSlot = null;
                    break;
                }
            }
            else if(draggingCard != null && this.chosenCardSlot != null){
                this.chosenCardSlot.setCard(draggingCard);
            }
        }
        for(CardSlot c : currentPlayer.opponent.playerBoardSlots){
            int x = c.getX(); int y = c.getY();
            if(display.getFrame().getMousePosition() != null && display.getFrame().getMousePosition().x >= c.getX() && display.getFrame().getMousePosition().x <= c.getX()+c.getWidth() && display.getFrame().getMousePosition().y <= c.getY() + c.getHeight() && display.getFrame().getMousePosition().y >= c.getY()) {
                if (draggingCard != null && c.cardOnBoard() && c.getCard().getID() == ID.Monster && draggingCard.getID() == ID.Curse) {
                    if (((Curse) (draggingCard)).curseLogic(c, currentPlayer, currentPlayer.opponent)) {
                        drawffect(x, y, destroy, 1, "Curse");
                        TimeBefore = 0;
                        threadSleep(500);
                        chosenCardSlot.removeCard();
                        chosenCardSlot = null;

                    }
                }
            }
        }
        if (display.getFrame().getMousePosition() != null && this.display.getFrame().getMousePosition().x >= phase.GetEndTurnPosX() && this.display.getFrame().getMousePosition().x <= phase.GetEndTurnPosX() + phase.GetEndTurnImgWidth() && this.display.getFrame().getMousePosition().y <= phase.GetEndTurnPosY() + phase.GetEndTurnImgHeight() && this.display.getFrame().getMousePosition().y >= phase.GetEndTurnPosY()) {
            phase.nextPhase();
            System.out.println("CLICKED END TURN");
        }

        draggingCard = null;
    }
    //----------------------------------------------------------
    // Draws cards in the beginning of the game for each player,
    // 3 cards for the first player and 4 for the second.
    //----------------------------------------------------------
    public int startOfTheGame(int draws){
        if(draws < 3) {
            player1.drawCard();
        }
        player2.drawCard();
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
    public void enemyTurn(){
        render();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        //player2.setCardOnBoard();
//        render();
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        phase.nextPhase();
        //player1.drawCard();
    }
}
