package com.company.Classes;

public class CardSlot extends GameObject{
    private Card card;
    private ID id; //
    public CardSlot(Card _card, int x, int y, ID slotID){
        super(x, y);
        card = _card;
        id = slotID;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render() {
        super.render();
    }

    public ID getId() {
        return id;
    }
}
