package com.company.Engine;

import com.company.Classes.*;
import com.company.Enums.ID;
import com.company.Utils.CardReader;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Game implements Runnable {
    Board board;
    long TimeBefore = 0;
    private ArrayList<Card> cards;
    private BufferStrategy buffer;
    private BufferedImage boardImg, backToMenu, backImg;
    private Card draggingCard;
    private CardSlot chosenCardSlot;
    private CardSlot draggingSlot;
    private Graphics g;
    private Handler handler;
    private Image bleedimg;
    private Image boosthpimg;
    private Image buffimg;
    private Image curseimg;
    private Image destroy;
    private Image stun;
    private Player player1;
    private Player player2;
    private Player winner;
    private Thread thread;
    private boolean clean;
    private boolean mouseHolding;
    private int dragginCardOffsetX = -50, dragginCardOffsetY = -100;
    public AudioInputStream inGameMusic;
    public AudioInputStream loserMusic;
    public AudioInputStream menuMusic;
    public AudioInputStream sound_attackEffect;
    public AudioInputStream sound_buffEffect;
    public AudioInputStream sound_death;
    public AudioInputStream sound_destroyEffect;
    public AudioInputStream sound_drawEffect;
    public AudioInputStream sound_hpBuff;
    public AudioInputStream sound_hpCurse;
    public AudioInputStream sound_placedEffect;
    public AudioInputStream sound_stunEffect;
    public AudioInputStream winnerMusic;
    public Clip inGameMusicClip;
    public Clip loserMusicClip;
    public Clip menuMusicClip;
    public Clip sound_attackEffectClip;
    public Clip sound_buffEffectClip;
    public Clip sound_deathClip;
    public Clip sound_destroyEffectClip;
    public Clip sound_drawEffectClip;
    public Clip sound_hpBuffClip;
    public Clip sound_hpCurseClip;
    public Clip sound_placedEffectClip;
    public Clip sound_stunEffectClip;
    public Clip winnerMusicClip;
    public Display display;
    public GameState gameState;
    public MusicPlayer musicPlayer;
    public Phase phase;
    public Player currentPlayer;
    public boolean running;
    private final double FORBIDDEN_AREA_TO_PLACE_CARD_HEIGHT_MULTIPLIER = 0.3;
    public static final int DECK_SLOT_INDEX = 5;

    public Game(String _title, int _width, int _height) {
        display = new Display(_title, _width, _height);
        gameState = new GameState(_width, _height);
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
        //menuMusicClip = musicPlayer.playMusic(menuMusic);
        //menuMusicClip.start();
    }

    public void SetDisplaySize(int width, int height) {
        display.SetDisplaySize(width, height);
        gameState.setDisplaySize(width, height);
    }

    private void init() {
        cards = CardReader.ReadCards("src/com/company/Assets/Cards_Data.txt"); // Reads cards data from file
        tryReadRequiredImages();
        new MouseHandler(display.getCanvas(), this);
    }

    private void tryReadRequiredImages() {
        try {
            destroy = new ImageIcon("src/com/company/Images/destroy.gif").getImage();
            stun = new ImageIcon("src/com/company/Images/stun.gif").getImage();
            buffimg = new ImageIcon("src/com/company/Images/Buff.gif").getImage();
            curseimg = new ImageIcon("src/com/company/Images/destroy.gif").getImage();
            boosthpimg = new ImageIcon("src/com/company/Images/Heal.gif").getImage();
            bleedimg = new ImageIcon("src/com/company/Images/bleed2.gif").getImage();

            backImg = ImageIO.read(new File("src/com/company/Images/back.png"));
            boardImg = ImageIO.read(new File("src/com/company/Images/Board.png"));
            backToMenu = ImageIO.read(new File("src/com/company/Images/backtomenu.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        clean = false;
        initializeGameObjects();
        createCardSlotsForPlayers();
        createPlayers();
        createPhase();
        handler.addObject(draggingSlot);
        currentPlayer = player1;
        gameState.startGame = false;
    }

    private void initializeGameObjects() {
        board = new Board(display);
        handler = new Handler();
        createDraggingSlot();
    }

    private void createDraggingSlot() {
        draggingSlot = new CardSlot((Card) null, 0, 0, ID.Dragging_Slot, 0);
        draggingSlot.setWidth((int) (display.getWidth() * 0.1));
        draggingSlot.setHeight((int) (display.getHeight() * 0.2));
    }

    private void createCardSlotsForPlayers() {
        addPlayerCardSlotsToHandler(board.getPlayer1_slots());
        addPlayerCardSlotsToHandler(board.getPlayer2_slots());
    }

    private void addPlayerCardSlotsToHandler(ArrayList<CardSlot> cardSlots) {
        for (CardSlot s : cardSlots) {
            handler.addObject(s);
        }
    }

    private void createPlayers() {
        player1 = createPlayer(board.getPlayer1_slots(), ID.Player1);
        player2 = createPlayer(board.getPlayer2_slots(), ID.Player2);
        setPlayersAsOpponents();
        addPlayersToHandler();
    }

    private Player createPlayer(ArrayList<CardSlot> playerSlots, ID playerId) {
        Deck playerDeck = createPlayerDeck(getDeckSlot(playerSlots));
        return new Player(playerId, playerDeck, playerSlots, display, this);
    }

    private void setPlayersAsOpponents() {
        player1.setOpponent(player2);
        player2.setOpponent(player1);
    }

    private void addPlayersToHandler() {
        handler.addObject(player1);
        handler.addObject(player2);
    }

    private Deck createPlayerDeck(CardSlot deckSlot) {
        var deck = new Deck(cards.size(), deckSlot.getX(), deckSlot.getY(), cards, backImg);
        deckSlot.setDeck(deck);
        deck.shuffle();

        return deck;
    }

    private CardSlot getDeckSlot(ArrayList<CardSlot> cardSlots) {
        return cardSlots.get(DECK_SLOT_INDEX);
    }

    private void createPhase() {
        phase = new Phase(display.getWidth(), display.getHeight(), player1, player2);
    }

    public double animTimer = 0;
    public boolean inAnimation = false;
    public ID animationCardID;
    double animX, animY;
    public int intID;
    private boolean isEnemyAttacking;

    public void setAttacking(int index, boolean _isEnemyAttacking) {
        inAnimation = true;
//        animationCardID = id;
        intID = index;
        animTimer = 0;
        isEnemyAttacking = _isEnemyAttacking;
    }

    public int targetX = 0, targetY = 200;

    private void tick() {
        if (inAnimation && !isEnemyAttacking) {
            animTimer += 1;
            // TODO move to animationHandler
            // swap animation direction if enemy
            if (animTimer < 17) {
                animY -= (1 - (animTimer / 50)) * 5f;
                animX += targetX * (animTimer / (17 * 17 / 2));
            } else if (animTimer < 34) {
                animY += (1 - ((animTimer - 50) / 50)) * 3f;
                animX -= targetX * ((animTimer - 17) / (17 * 17 / 2));
            } else {
                inAnimation = false;
                animY = 0;
            }

            if (phase != null && !phase.weHaveAWinner()) {
                board.getPlayer1_slots().get(intID).SetAnimationOffsetX(animX);
                board.getPlayer1_slots().get(intID).SetAnimationOffsetY(animY);
            }
        } else if (inAnimation && isEnemyAttacking) {
            animTimer += 1;
            // TODO move to animationHandler
            // swap animation direction if enemy
            if (animTimer < 17) {
                animY += (1 - (animTimer / 50)) * 5f;
                animX += targetX * (animTimer / (17 * 17 / 2));
            } else if (animTimer < 34) {
                animY -= (1 - ((animTimer - 50) / 50)) * 3f;
                animX -= targetX * ((animTimer - 17) / (17 * 17 / 2));
            } else {
                inAnimation = false;
                animY = 0;
            }

            board.getPlayer2_slots().get(intID).SetAnimationOffsetX(animX);
            board.getPlayer2_slots().get(intID).SetAnimationOffsetY(animY);
        }

        if (gameState.isGame) {
            if (winnerMusicClip != null && winnerMusicClip.isRunning()) {
                winnerMusicClip.stop();
            }
            if (loserMusicClip != null && loserMusicClip.isRunning()) {
                loserMusicClip.stop();
            }
            handler.tick();
            currentPlayer = phase.getCurrentPlayer();
            phase.updateTime();
            phase.checkTime();
            if (phase.weHaveAWinner()) {
                gameState.isGame = false;
                gameState.celebrationWindow = true;
                if (currentPlayer.getHP() <= 0) {
                    winner = currentPlayer.opponent;
                } else if (currentPlayer.opponent.getHP() <= 0) {
                    winner = currentPlayer;
                }
            }
        } else if (gameState.celebrationWindow) {
            if (inGameMusicClip != null) {
                inGameMusicClip.stop();
            }
            if (winner == player1 && winnerMusicClip == null) {
                winnerMusicClip = musicPlayer.playSound(winnerMusic);
                musicPlayer.repeatSound(winnerMusicClip);
            } else if (winner == player1 && !winnerMusicClip.isRunning()) {
                musicPlayer.repeatSound(winnerMusicClip);
            }
            if (winner == player2 && loserMusicClip == null) {
                loserMusicClip = musicPlayer.playSound(loserMusic);
                musicPlayer.repeatSound(loserMusicClip);
            } else if (winner == player2 && !loserMusicClip.isRunning()) {
                musicPlayer.repeatSound(loserMusicClip);
            }
            if (!clean) {
                player1 = null;
                player2 = null;
                draggingSlot = null;
                draggingCard = null;
                handler = null;
                if (board.getPlayer1_slots() != null) {
                    board.getPlayer1_slots().clear();
                }
                if (board.getPlayer2_slots() != null) {
                    board.getPlayer1_slots().clear();
                }
                board = null;
                phase = null;
                Runtime.getRuntime().gc();
                clean = true;
            }
        }
    }

    public void render() {
        buffer = display.getCanvas().getBufferStrategy();
        if (buffer == null) {
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g = buffer.getDrawGraphics();
        // Drawing
        if (gameState.isMenu) {
            //g.drawImage(menuBackgroundImg, 0, 0, width, height, null);
            gameState.render(g);
        } else if (gameState.isLoading) {
            g.drawImage(display.loadingIMG, 0, 0, display.getWidth(), display.getHeight(), null);
        } else if (gameState.isGame) {
            g.drawImage(boardImg, 0, 0, display.getWidth(), display.getHeight(), null);
            g.drawImage(phase.GetCurrentPhaseImage(), phase.GetEndTurnPosX(), phase.GetEndTurnPosY(), phase.GetEndTurnImgWidth(), phase.GetEndTurnImgHeight(), null);

            g.setColor(Color.WHITE);
            handler.render(g);
            DrawDraggingCard();
            Font prevFont = g.getFont();
            Font font = new Font(Font.SANS_SERIF, 3, (int) (display.getHeight() * 0.025));
            g.setFont(font);
            int roundTime = 35 - (int) phase.elapsedTime;
            String timer = "Time left: " + roundTime;
            Color color;
            if (roundTime <= 23 && roundTime >= 15) {
                color = new Color(255, 224, 0);
            } else if (roundTime < 15 && roundTime >= 8) {
                color = new Color(255, 159, 51);
            } else if (roundTime < 8) {
                color = new Color(255, 0, 0);
            } else {
                color = new Color(255, 255, 255);
            }
            g.setColor(color);
            g.drawString(timer, (int) (display.getWidth() * 0.01), (int) (display.getHeight() * 0.41));
            g.setFont(prevFont);
            g.setColor(Color.WHITE);
        } else if (gameState.celebrationWindow) {
            g.setColor(Color.black);
            g.fillRect(0, 0, display.getWidth(), display.getHeight());
            g.setColor(Color.WHITE);
            Font prevFont = g.getFont();
            Font newFont = new Font(Font.SANS_SERIF, 3, 30);
            g.setFont(newFont);
            String winnerTitle = winner.getID().toString() + " won!";
            g.drawImage(backToMenu, (int) (display.getWidth() * 0.5 - display.getWidth() * 0.1), (int) (display.getHeight() * 0.5) + (int) (display.getHeight() * 0.1), (int) (display.getWidth() * 0.2), (int) (display.getHeight() * 0.1), null);
            g.drawString(winnerTitle, (int) (display.getWidth() * 0.5 - winnerTitle.length() * 0.25 * 30), display.getHeight() / 2);
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
        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            lastTime = now;
            if (delta >= 1) {
                tick();
                render();
                if (gameState.isGame) {
                    if (phase.startPhase() && phase.getCurrentRound() == 1 && draws < 4) {
                        draws = startOfTheGame(draws);
                        phase.startTime = System.nanoTime();
                    }
                    if (phase.enemyTurn()) {
                        enemyTurn();
                    }
                }
                if (gameState.isMenu) {
                    draws = 0;
                }
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

    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        System.exit(0);
    }

    public static void main(String[] args) {
        Game game = new Game("UbiHard Card Game", 1366, 768);
        game.start();
    }

    public void DrawDraggingCard() {
        if (mouseHolding && display.getFrame().getMousePosition() != null) {
            draggingSlot.setCard(draggingCard);
            draggingSlot.setX(display.getFrame().getMousePosition().x + dragginCardOffsetX);
            draggingSlot.setY(display.getFrame().getMousePosition().y + dragginCardOffsetY);
        } else {
            draggingSlot.setX(-1000);
            draggingSlot.setY(-1000);
        }

    }

    public void SlotClicked(Card card, CardSlot slot) {
        if (card != null) {
            mouseHolding = true;
            draggingCard = card;
            this.chosenCardSlot = slot;
            this.chosenCardSlot.removeCard();
        }
    }

    public void drawEffect(int x, int y, Image image, int time, String effectType) {
        if (TimeBefore < 500) {
            TimeBefore = System.nanoTime();
        }

        while ((System.nanoTime() - TimeBefore) / 1000000000 < time) {
            this.drawEffectBasedOnEffectType(x, y, image, effectType);
        }

        TimeBefore = 0;
    }

    private void drawEffectBasedOnEffectType(int x, int y, Image image, String effectType) {
        if (effectType.equals("-hp")) {
            g.drawImage(image, 0, 0, display.getWidth(), display.getHeight(), null);
        } else if (effectType.equals("+hp")) {
            g.drawImage(image, (int) (display.getWidth() * 0.1), (int) (display.getHeight() - (int) (display.getHeight() * 0.35)), (int) (display.getWidth() * 0.8), (int) (display.getHeight() * 0.3), null);
        } else {
            g.drawImage(image, x, y, (int) (display.getWidth() * 0.1), (int) (display.getHeight() * 0.20), null);
        }
    }

    public void threadSleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void MouseReleased() {
        mouseHolding = false;
        boolean wasCardPlayed = this.wasCardPlayed();
        this.removeOrReturnDraggedCard(wasCardPlayed);
    }

    private boolean wasCardPlayed() {
        if(wasCardPlayedOnOwnSide()) {
            return true;
        }

        return wasCardPlacedOnOpponentSide();
    }

    private void removeOrReturnDraggedCard(boolean wasCardPlayed) {
        if(!wasCardPlayed && draggingCard != null && chosenCardSlot != null) {
            this.returnCardToTheHand();
        } else if (wasCardPlayed) {
            this.removeCardFromTheHand();
        }
    }

    private void removeCardFromTheHand() {
        chosenCardSlot.removeCard();
        draggingCard = null;
    }

    private void returnCardToTheHand() {
        chosenCardSlot.setCard(draggingCard);
        draggingCard = null;
    }

    private boolean wasCardPlayedOnOwnSide() {
        for (CardSlot cardSlot : currentPlayer.playerBoardSlots) {
            if (tryToPlayCard(cardSlot, currentPlayer, currentPlayer.opponent)) {
                return true;
            }
        }
        return false;
    }

    private boolean wasCardPlacedOnOpponentSide() {
        for (CardSlot cardSlot : currentPlayer.opponent.playerBoardSlots) {
            if (tryToPlayCard(cardSlot, currentPlayer, currentPlayer.opponent)) {
                return true;
            }
        }
        return false;
    }

    private boolean tryToPlayCard(CardSlot targetSlot, Player currentPlayer, Player opponent) {
        if (!this.canCardBePlayed(targetSlot, currentPlayer)) {
            return false;
        }

        return this.tryToPlayCardAccordingItsType(targetSlot, currentPlayer, opponent);
    }

    private boolean tryToPlayCardAccordingItsType(CardSlot targetSlot, Player currentPlayer, Player opponent) {
        if (draggingCard.isBuffCard()) {
            var buffCard = (BuffCard)draggingCard;
            return buffCard.playCard(targetSlot, currentPlayer, chosenCardSlot);
        } else if (draggingCard.isCurseCard()) {
            var curseCard = (CurseCard)draggingCard;
            return curseCard.playCard(currentPlayer, opponent, chosenCardSlot, targetSlot);
        } else if (draggingCard.isMonsterCard()) {
            var monsterCard = (MonsterCard)draggingCard;
            return currentPlayer.placeCard(targetSlot, monsterCard);
        }

        return false;
    }

    private boolean canCardBePlayed(CardSlot targetSlot, Player currentPlayer) {
        if (draggingCard == null) {
            return false;
        }

        if (draggingCard.mustHaveTarget() && this.isMouseOverCardSlot(targetSlot)) {
            return draggingCard.canCardBePlayed(currentPlayer, targetSlot);
        }

        return this.isMouseOverBoard(display.getFrame())
               && draggingCard.canCardBePlayed(currentPlayer, null);
    }

    public int startOfTheGame(int draws) {
        if (draws < 3) {
            player1.drawCard();
        }
        player2.drawCard();
        draws++;
        threadSleep(500);
        return draws;
    }

    public void enemyTurn() {
        render();
        threadSleep(500);
        phase.nextPhase();
    }

    public void attackOpponent(CardSlot attacker, Player currentplayer) {
        currentplayer.opponent.decreaseHP(((MonsterCard) (attacker.getCard())).getAttack());
    }

    private boolean isMouseOverCardSlot(CardSlot cardSlot) {
        return this.isMouseInTheFrame(display.getFrame()) && cardSlot.isMouseOverCardSlot(this.getMousePositionFromFrame(display.getFrame()));
    }

    private boolean isMouseInTheFrame(JFrame frame) {
        return frame.getMousePosition() != null;
    }

    private Point getMousePositionFromFrame(JFrame frame) {
        return frame.getMousePosition();
    }

    private boolean isMouseOverBoard(JFrame frame) {
        if (!this.isMouseInTheFrame(frame)) {
            return false;
        }

        int topBoundary = (int)(display.getHeight() * FORBIDDEN_AREA_TO_PLACE_CARD_HEIGHT_MULTIPLIER);
        int bottomBoundary = display.getHeight() - topBoundary;

        return getMousePositionFromFrame(frame).y >= topBoundary && getMousePositionFromFrame(frame).y <= bottomBoundary;
    }
}
