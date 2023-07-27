package com.company.Enums;

public enum CurseType {
    Destroy("destroy"),
    MinusHp("hp"),
    Stun("stun");

    public final String type;

    CurseType(String type) {
        this.type = type;
    }

    public static CurseType GetCurseType(String type) {
        for (var curseType : CurseType.values()) {
            if (curseType.type.equalsIgnoreCase(type)) {
                return curseType;
            }
        }

        return null;
    }
}
