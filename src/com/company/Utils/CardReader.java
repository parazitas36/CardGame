package com.company.Utils;

import com.company.Classes.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class CardReader {
    public static ArrayList<Card> Read(String path) {
        ArrayList<Card> cards = new ArrayList<>();
        Scanner scan = null;
        try {
            scan = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while(scan.hasNextLine()) {
            //--------------------------------------------
            // Reads the data every card has
            //--------------------------------------------
            String line = scan.nextLine();
            String[] values = line.split(";");

            String type = values[0];
            String name = values[1];
            int manaCost = Integer.parseInt(values[2]);
            String imgPath = values[3];

            BufferedImage img = null;
            try {
                img = ImageIO.read(new File(imgPath));
            }catch (IOException e){
                e.printStackTrace();
            }
            //--------------------------------------------
            // Reads additional data for each type of card
            //--------------------------------------------
            switch (type) {
                //--------------------------------------------
                // Reads monster card data
                //--------------------------------------------
                case "M":
                    int atk = Integer.parseInt(values[4]);
                    int def = Integer.parseInt(values[5]);
                    Card monster = new Monster(name, manaCost, ID.Monster, atk, def, img);
                    cards.add(monster);
                    break;
                //--------------------------------------------
                // Reads buff card data
                //--------------------------------------------
                case "B":
                    String effect = values[4];
                    if(!effect.equals("atk") && !effect.equals("def") && !effect.equals("hp")){
                       Card buff = new Buff(name, manaCost, ID.Buff, img, effect);
                       cards.add(buff);
                    }else{
                        int amount = Integer.parseInt(values[5]);
                        Card buff = new Buff(name, manaCost, ID.Buff, img, effect, amount);
                        cards.add(buff);
                    }
                    break;
                //--------------------------------------------
                // Reads curse card data
                //--------------------------------------------
                case "C":
                    String eff = values[4];
                    if(!eff.equals("atk") && !eff.equals("def") && !eff.equals("hp")){
                        Card curse = new Curse(name, manaCost, ID.Curse, img, eff);
                        cards.add(curse);
                    } else{
                        int amount = Integer.parseInt(values[5]);
                        Card curse = new Curse(name, manaCost, ID.Curse, img, eff, amount);
                        cards.add(curse);
                    }
                    break;
                //--------------------------------------------
                default:
                    break;
            }
        }
        Collections.shuffle(cards);
        return cards;
    }



}

