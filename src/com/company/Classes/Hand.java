package com.company.Classes;

public class Hand {
    private int handSize;
    private ID playerID;
    public Hand(ID id){
        playerID = id;
        handSize = playerID == ID.Player1 ? 3 : 4;
    }
}
