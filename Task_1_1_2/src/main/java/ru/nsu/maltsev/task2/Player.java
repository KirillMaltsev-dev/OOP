package ru.nsu.maltsev.task2;

import java.util.List;
import java.util.ArrayList;

public class Player {
    protected final List<Card> hand = new ArrayList<>();

    public void takeCard(Card card) {
        hand.add(card);
    }

    public int getscore() {
        int sum = 0;
        int acecount = 0;
        for (Card c : hand) {
            sum += c.GetValue();
            if (c.GetValue() == 11) acecount++;
        }
        while (sum > 21 && acecount > 0) {
            sum -= 10;
            acecount--;
        }
        return sum;
    }

    public List<Card> GetHand() {
        return hand;
    }

    public void showHand() {
        HandPrinter.showHand(hand, getscore());
    }
}
