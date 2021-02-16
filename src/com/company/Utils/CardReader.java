package com.company.Utils;

import com.company.Classes.Buff;
import com.company.Classes.Card;
import com.company.Classes.Curse;
import com.company.Classes.Monster;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CardReader {

    public static void Read(String path) {
        ArrayList<Card> cards = new ArrayList<Card>();
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
            switch (type) {
                case "M" :
                    Integer attack = Integer.getInteger(values[3]);
                    Integer defence = Integer.getInteger(values[4]);

                    Monster monster = new Monster(name, manaCost, attack, defence);



                case "B":
                    Integer _attack = Integer.getInteger(values[3]);
                    Integer _defence = Integer.getInteger(values[4]);
                    Integer hp = Integer.getInteger(values[5]);

                   // Buff buff = new Buff(name, manaCost, _attack, _defence, hp);

                case "C":
                    Integer __attack = Integer.getInteger(values[3]);
                    Integer __defence = Integer.getInteger(values[4]);
                    Integer __stun = Integer.getInteger(values[5]);

                   // Curse curse = new Curse(name, manaCost, __attack, __defence, __stun);


            }


        }
    }

}

