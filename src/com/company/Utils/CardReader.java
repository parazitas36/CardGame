package com.company.Utils;

import com.company.Classes.*;
import com.company.Enums.ID;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class CardReader {

    private static BufferedImage stunnedImg;

    public static ArrayList<Card> ReadCards(String path) {
        ArrayList<Card> cards = new ArrayList<>();
        Scanner scan = null;

        try {
            scan = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        stunnedImg = tryReadStunnedImage();

        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] splittedValues = line.split(";");
            Card card = createCard(splittedValues);

            if (card != null) {
                cards.add(card);
            }
        }

        Collections.shuffle(cards);
        return cards;
    }

    private static Card createCard(String[] splittedValues) {
        try {
            String cardType = splittedValues[0];
            return createCardBasedOnType(splittedValues, cardType);
        } catch (Exception e) {
            return null;
        }
    }

    private static Card createCardBasedOnType(String[] splittedValues, String cardType) {
        String cardImagePath = splittedValues[3];
        var basicCardData =  new BasicCardData(splittedValues[1], Integer.parseInt(splittedValues[2]), tryReadCardImage(cardImagePath));

        switch (cardType) {
            case "M":
                return createMonsterCard(splittedValues, basicCardData);

            case "B":
                return createBuffCard(splittedValues, basicCardData);

            case "C":
                return createCurseCard(splittedValues, basicCardData);

            default:
                return null;
        }
    }

    private static MonsterCard createMonsterCard(String[] splittedValues, BasicCardData basicCardData) {
        int atk = Integer.parseInt(splittedValues[4]);
        int def = Integer.parseInt(splittedValues[5]);

        MonsterCard monsterCard = new MonsterCard(basicCardData.getName(), basicCardData.getManaCost(), atk, def, basicCardData.getCardImage());
        monsterCard.setStunnedImg(stunnedImg);

        return monsterCard;
    }

    private static BuffCard createBuffCard(String[] splittedValues, BasicCardData basicCardData) {
        String buffType = splittedValues[4];
        int amount = Integer.parseInt(splittedValues[5]);

        return BuffCardFactory.createBuffCard(basicCardData.getName(), basicCardData.getManaCost(), basicCardData.getCardImage(), buffType, amount);
    }

    private static CurseCard createCurseCard(String[] splittedValues, BasicCardData basicCardData) {
        String curseType = splittedValues[4];

        try {
            int amount = Integer.parseInt(splittedValues[5]);
            return CurseCardFactory.createCurseCard(basicCardData.getName(), basicCardData.getManaCost(), basicCardData.getCardImage(), curseType, amount);
        } catch (Exception e) {
            return CurseCardFactory.createCurseCard(basicCardData.getName(), basicCardData.getManaCost(), basicCardData.getCardImage(), curseType, null);
        }
    }

    private static BufferedImage tryReadCardImage(String cardImagePath) {
        try {
            return ImageIO.read(new File(cardImagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BufferedImage tryReadStunnedImage() {
        try {
            return ImageIO.read(new File("src/com/company/Images/stunnedoverlay.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}

