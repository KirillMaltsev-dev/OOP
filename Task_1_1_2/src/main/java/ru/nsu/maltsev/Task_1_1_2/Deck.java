package ru.nsu.maltsev.Task_1_1_2;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class Deck {
    private final List<Card> cards = new ArrayList<>();

    public Deck(){
        for (Suit s : Suit.values())
            for (Rank r : Rank.values())
                cards.add(new Card(s, r));
        Collections.shuffle(cards);
    }

    public Card draw(){
        return cards.remove(cards.size() - 1);
    }
}
