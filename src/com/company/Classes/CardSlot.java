package com.company.Classes;

public class CardSlot extends GameObject{
    private Card card;
    public CardSlot(Card _card, int x, int y){
        super(x, y);
        card = _card;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render() {
        super.render();
    }
}
