package com.company.Classes;

import com.company.Enums.ID;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CardSlot extends GameObject {
    private Card card;
    private Deck deck;
    private ID id;
    private boolean didAttackThisTurn;
    private boolean hasCard = false;
    private double animationOffsetX, animationOffsetY;
    private int height;
    private int index;
    private int width;
    public BufferedImage backimg;
    public boolean isAttacking;
    public boolean shouldShowCardsCount;

    public void readImages() {
        try {
            backimg = ImageIO.read(new File("src/com/company/Images/back.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CardSlot(Card _card, int x, int y, ID slotID, int _index) {
        super();
        setX(x);
        setY(y);
        card = _card;
        id = slotID;
        hasCard = card == null ? false : true;
        isAttacking = false;
        didAttackThisTurn = false;
        index = _index;
        readImages();
    }

    public CardSlot(Deck _deck, int x, int y, ID slotID, int _index) {
        super();
        deck = _deck;
        setX(x);
        setY(y);
        id = slotID;
        shouldShowCardsCount = false;
        index = _index;
        readImages();
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics g) {
        Font prevfont = g.getFont();
        Font newfont = new Font("", Font.BOLD, (int) ((height + width) * 0.039));
        Font deckFont = new Font("", Font.BOLD, (int) ((height + width) * 0.045));

        if (this.isCardOnBoard()) {
            Card card = this.getCard();
            if (this.id.toString().contains("Player1_HandSlot") || this.id.toString().contains("Dragging_Slot")) {
                g.drawImage(card.getImage(), (int) (this.getX() + animationOffsetX), (int) (this.getY() + animationOffsetY), this.getWidth(), this.getHeight(), null);
                if (card.getID() == ID.Monster) {
                    g.setFont(newfont);
                    g.drawString(String.format("%s", ((MonsterCard) card).getAttack()), (int) (this.getX() + (int) (this.getWidth() * 0.885)), this.getY() + (int) (this.getHeight() * 0.325));
                    g.drawString(String.format("%s", ((MonsterCard) card).getDef()), this.getX() + (int) (this.getWidth() * 0.885), this.getY() + (int) (this.getHeight() * 0.525));
                    g.drawString(String.format("%s", ((MonsterCard) card).getManaCost()), this.getX() + (int) (this.getWidth() * 0.885), this.getY() + (int) (this.getHeight() * 0.095));
                    g.setFont(prevfont);
                } else if (card.getID() == ID.Curse || card.getID() == ID.Buff) {
                    g.setFont(newfont);
                    g.drawString(String.format("%s", card.getManaCost()), this.getX() + (int) (this.getWidth() * 0.885), this.getY() + (int) (this.getHeight() * 0.095));
                    g.setFont(prevfont);
                }
            } else if (card.getID().toString() == ID.Monster.toString() && this.id.toString().contains("Player1_Slot")) {
                g.setFont(newfont);
                g.drawImage(card.getImage(), (int) (this.getX() + animationOffsetX), (int) (this.getY() + animationOffsetY), this.getWidth(), this.getHeight(), null);
                g.drawString(String.format("%s", ((MonsterCard) card).getAttack()), (int) (this.getX() + (int) (this.getWidth() * 0.885)), this.getY() + (int) (this.getHeight() * 0.325));
                g.drawString(String.format("%s", ((MonsterCard) card).getDef()), this.getX() + (int) (this.getWidth() * 0.885), this.getY() + (int) (this.getHeight() * 0.525));
                g.drawString(String.format("%s", ((MonsterCard) card).getManaCost()), this.getX() + (int) (this.getWidth() * 0.885), this.getY() + (int) (this.getHeight() * 0.095));
                g.setFont(prevfont);
                if (((MonsterCard) card).stunTime > 0) {
                    g.drawImage(((MonsterCard) card).stunnedImg, this.getX(), this.getY(), this.getWidth(), this.getHeight(), null);
                }
                if (isAttacking == true) {
                    Color c = g.getColor();
                    g.setColor(Color.RED);
                    g.drawRect(this.getX() - 10, this.getY() - 10, this.getWidth() + 20, this.getHeight() + 20);
                    g.setColor(c);
                }
            } else if (card.getID().toString() != ID.Monster.toString() && this.id.toString().contains("Player1_Slot")) {
                g.drawImage(card.getImage(), (int) (this.getX() + animationOffsetX), (int) (this.getY() + animationOffsetY), this.getWidth(), this.getHeight(), null);
            } else {
                g.drawImage(backimg, (int) (this.getX() + animationOffsetX), (int) (this.getY() + animationOffsetY) + this.getHeight(), this.getWidth(), -this.getHeight(), null);
                if (card.getID() == ID.Monster && this.getId().toString().contains("Player2_Slot")) {
                    g.drawImage(card.getImage(), (int) (this.getX() + animationOffsetX), (int) (this.getY() + animationOffsetY) + this.getHeight(), this.getWidth(), -this.getHeight(), null);
                    g.setFont(newfont);
                    g.drawString(String.format("%s", ((MonsterCard) card).getAttack()), this.getX() + (int) (this.getWidth() * 0.885), this.getY() + (int) (this.getHeight() * 0.74));
                    g.drawString(String.format("%s", ((MonsterCard) card).getDef()), this.getX() + (int) (this.getWidth() * 0.885), this.getY() + (int) (this.getHeight() * 0.52));
                    g.drawString(String.format("%s", ((MonsterCard) card).getManaCost()), this.getX() + (int) (this.getWidth() * 0.885), this.getY() + (int) (this.getHeight() * 0.96));
                    g.setFont(prevfont);
                    if (((MonsterCard) card).stunTime > 0) {
                        g.drawImage(((MonsterCard) card).stunnedImg, this.getX(), this.getY(), this.getWidth(), this.getHeight(), null);
                    }
                }
            }
        } else if (deck != null) {
            if (id.toString().contains("Player1")) {
                g.drawImage(deck.getImage(), (int) (this.getX() + animationOffsetX), (int) (this.getY() + animationOffsetY), this.getWidth(), this.getHeight(), null);
                if (shouldShowCardsCount) {
                    g.setFont(deckFont);
                    g.drawString("Cards: " + getDeck().getSize(), this.getX() + (int) (this.getWidth() * 0.3), this.getY() + this.getHeight() / 2);
                    g.setFont(prevfont);
                }
            } else {
                g.drawImage(deck.getImage(), (int) (this.getX() + animationOffsetX), (int) (this.getY() + animationOffsetY) + this.getHeight(), this.getWidth(), -this.getHeight(), null);
                if (shouldShowCardsCount) {
                    g.setFont(deckFont);
                    g.drawString("Cards: " + getDeck().getSize(), this.getX() + (int) (this.getWidth() * 0.3), this.getY() + this.getHeight() / 2);
                    g.setFont(prevfont);
                }
            }
        }
    }

    public void setCard(Card _card) {
        this.card = _card;
        if (_card != null) {
            this.hasCard = true;
        }
    }

    public void setDeck(Deck _deck) {
        this.deck = _deck;
    }

    public void removeCard() {
        this.card = null;
        hasCard = false;
    }

    public ID getId() {
        return this.id;
    }

    public void setWidth(int _width) {
        this.width = _width;
    }

    public void setHeight(int _height) {
        this.height = _height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean isCardOnBoard() {
        return this.hasCard = this.card != null;
    }

    public boolean isDeckSlot() {
        return this.deck != null;
    }

    public Card getCard() {
        return this.card;
    }

    public Deck getDeck() {
        return this.deck;
    }

    public int getIndex() {
        return index;
    }

    public void setAttackedThisTurn() {
        didAttackThisTurn = true;
    }

    public void resetAttackedThisTurn() {
        didAttackThisTurn = false;
    }

    public boolean didAttackThisTurn() {
        return didAttackThisTurn;
    }

    public void SetAnimationOffsetX(double value) {
        animationOffsetX = value;
    }

    public void SetAnimationOffsetY(double value) {
        animationOffsetY = value;
    }

    public boolean isMouseOverCardSlot(Point mousePosition) {
        return this.isMouseOverCardSlotHorizontally(mousePosition)
               && this.isMouseOverCardSlotVertically(mousePosition);
    }

    private boolean isMouseOverCardSlotHorizontally(Point mousePosition) {
        return this.getCardSlotTopLeftCorner().x <= mousePosition.x
               && mousePosition.x <= this.getCardSlotBottomRightCorner().x;
    }

    private boolean isMouseOverCardSlotVertically(Point mousePosition) {
        return this.getCardSlotTopLeftCorner().y <= mousePosition.y
               && mousePosition.y <= this.getCardSlotBottomRightCorner().y;
    }

    private Point getCardSlotTopLeftCorner() {
        return new Point(this.getX(), this.getY());
    }

    private Point getCardSlotBottomRightCorner() {
        int rightEdgePositionX = this.getX() + this.width;
        int bottomEdgePositionY = this.getY() + this.getY();

        return new Point(rightEdgePositionX, bottomEdgePositionY);
    }
}
