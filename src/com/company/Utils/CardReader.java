package com.company.Utils;

import com.company.Classes.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class CardReader  implements Serializable {
    public static String[] ReadLines(String path){
        ArrayList<String> linesArr = new ArrayList<>();
        Scanner scan = null;
        try{
            scan = new Scanner(new File(path));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        while(scan.hasNextLine()){
            String line = scan.nextLine();
            linesArr.add(line);
        }
        String[] lines =  new String[linesArr.size()];
        Collections.shuffle(linesArr);
        for(int i = 0; i < linesArr.size(); i++){
            lines[i] = linesArr.get(i);
        }
        return lines;
    }
    public static ArrayList<Card> Read(String path) {
        ArrayList<Card> cards = new ArrayList<>();
        Scanner scan = null;
        try {
            scan = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageIcon stunnedImg = null;

        stunnedImg = new ImageIcon("src/com/company/Images/stunnedoverlay.png");

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

            ImageIcon img = null;
            img = new ImageIcon(imgPath);

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
                    ((Monster)monster).setStunnedImg(stunnedImg);
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

    public static ArrayList<Card> Read(String[] lines) {
        ArrayList<Card> cards = new ArrayList<>();
        ImageIcon stunnedImg = null;

        stunnedImg = new ImageIcon("src/com/company/Images/stunnedoverlay.png");

        for(int i = 0; i < lines.length; i++){
            //--------------------------------------------
            // Reads the data every card has
            //--------------------------------------------
            String line = lines[i];
            String[] values = line.split(";");

            String type = values[0];
            String name = values[1];
            int manaCost = Integer.parseInt(values[2]);
            String imgPath = values[3];

            ImageIcon img = null;
            img = new ImageIcon(imgPath);

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
                    ((Monster)monster).setStunnedImg(stunnedImg);
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
        //Collections.shuffle(cards);
        return cards;
    }

}

