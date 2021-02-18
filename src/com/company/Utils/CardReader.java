package com.company.Utils;

import com.company.Classes.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        while(scan != null ) {
            String line = scan.nextLine();
            String[] values = line.split(";");
            String type = values[0];
            String name = values[1];
            int manaCost = Integer.getInteger(values[2]);
            int x = Integer.getInteger(values[3]);
            int y = Integer.getInteger(values[4]);
            switch (type) {
                case "M" :
                    int attack = Integer.getInteger(values[5]);
                    int defence = Integer.getInteger(values[6]);

                //    Monster monster = new Monster(name, manaCost, x, y, attack, defence);

                case "B":
                    int _attack = Integer.getInteger(values[3]);
                    int _defence = Integer.getInteger(values[4]);
                    int hp = Integer.getInteger(values[5]);

                     // Buff buff = new Buff(name, manaCost, _attack, _defence, hp);

                case "C":
                    int __attack = Integer.getInteger(values[3]);
                    int __defence = Integer.getInteger(values[4]);
                    int __stun = Integer.getInteger(values[5]);

                   // Curse curse = new Curse(name, manaCost, __attack, __defence, __stun);
            }
        }
        Collections.shuffle(cards);
        return cards;
    }



}

