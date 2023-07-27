package com.company.Classes;

import com.company.Enums.BuffType;
import com.company.Enums.ID;

import java.awt.image.BufferedImage;

public class BuffCardFactory {
    public static BuffCard createBuffCard(String name, int manaCost, BufferedImage cardImage, String buffType, Integer amount) {
        BuffType buffTypeEnum = BuffType.GetBuffType(buffType);

        if (buffTypeEnum == BuffType.PlusHp) {
            return createHpBuffCard(name, manaCost, cardImage, amount);
        } else if (buffTypeEnum == BuffType.PlusAtk || buffTypeEnum == BuffType.PlusDef) {
            return createMonsterBuffCard(name, manaCost, cardImage, buffTypeEnum, amount);
        }

        return null;
    }

    private static HpBuffCard createHpBuffCard(String name, int manaCost, BufferedImage cardImage, Integer amount) {
        return new HpBuffCard(name, manaCost, cardImage, amount);
    }

    private static MonsterBuffCard createMonsterBuffCard(String name, int manaCost, BufferedImage cardImage, BuffType buffType, Integer amount) {
        return new MonsterBuffCard(name, manaCost, cardImage, buffType, amount);
    }
}
