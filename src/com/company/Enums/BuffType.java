package com.company.Enums;

public enum BuffType {
    PlusAtk("atk"),
    PlusDef("def"),
    PlusHp("hp");

    public final String type;

    BuffType(String type) {
        this.type = type;
    }

    public static BuffType GetBuffType(String type) {
        for (var buffType : BuffType.values()) {
            if (buffType.type.equalsIgnoreCase(type)) {
                return buffType;
            }
        }

        return null;
    }
}
