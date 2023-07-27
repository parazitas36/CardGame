package com.company.Classes;

import com.company.Enums.CurseType;

import java.awt.image.BufferedImage;

public class CurseCardFactory {
    public static CurseCard createCurseCard(String name, int manaCost, BufferedImage cardImage, String curseType, Integer amount) {
        CurseType curseTypeEnum = CurseType.GetCurseType(curseType);

        if (curseTypeEnum == CurseType.MinusHp) {
            return createHpCurseCard(name, manaCost, cardImage, amount);
        } else if (curseTypeEnum == CurseType.Stun) {
            return createStunCard(name, manaCost, cardImage);
        } else if (curseTypeEnum == CurseType.Destroy) {
            return createDestroyCard(name, manaCost, cardImage);
        }

        return null;
    }

    private static HpCurseCard createHpCurseCard(String name, int manaCost, BufferedImage cardImage, Integer amount) {
        return new HpCurseCard(name, manaCost, cardImage, amount);
    }

    private static StunCard createStunCard(String name, int manaCost, BufferedImage cardImage) {
        return new StunCard(name, manaCost, cardImage);
    }

    private static DestroyCard createDestroyCard(String name, int manaCost, BufferedImage cardImage) {
        return new DestroyCard(name, manaCost, cardImage);
    }
}
