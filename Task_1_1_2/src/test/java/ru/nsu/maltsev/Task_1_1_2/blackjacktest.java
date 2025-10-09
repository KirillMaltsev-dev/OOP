package ru.nsu.maltsev.Task_1_1_2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


class blackjacktest{
    @Test
    public void testCardValues() {
        Card king = new Card(Suit.Крести, Rank.Король);
        Card ace = new Card(Suit.Бубны, Rank.Туз);
        assertEquals(10, king.GetValue());
        assertEquals(11, ace.GetValue());
    }

    @Test
    public void testDealerGetScore() {
        Dealer dealer = new Dealer();
        dealer.takeCard(new Card(Suit.Пики, Rank.Десятка));
        dealer.takeCard(new Card(Suit.Бубны, Rank.Туз));
        assertEquals(21, dealer.getscore());
    }

    @Test
    public void testDealerAceAdjustment() {
        Dealer dealer = new Dealer();
        dealer.takeCard(new Card(Suit.Бубны, Rank.Туз));
        dealer.takeCard(new Card(Suit.Крести, Rank.Туз));
        dealer.takeCard(new Card(Suit.Пики, Rank.Десятка));
        assertEquals(12, dealer.getscore()); // 11 + 1 + 10
    }

    @Test
    public void testToStringFormat() {
        Card c = new Card(Suit.Крести, Rank.Восьмерка);
        String text = c.ToString();
        assertTrue(text.contains("Восьмерка") && text.contains("Крести"));
    }

    @Test
    public void testShowHandFormatting() {
        Dealer dealer = new Dealer();
        dealer.takeCard(new Card(Suit.Бубны, Rank.Пятерка));
        dealer.takeCard(new Card(Suit.Пики, Rank.Семерка));
        assertDoesNotThrow(dealer::showHand);
    }
}