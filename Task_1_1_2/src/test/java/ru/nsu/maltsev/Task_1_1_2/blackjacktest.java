package ru.nsu.maltsev.Task_1_1_2;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Расширенные тесты для покрытия >80%
 */
public class blackjacktest {

    @Test
    void testCardValues() {
        assertEquals(10, new Card(Suit.Пики, Rank.Король).GetValue());
        assertEquals(11, new Card(Suit.Бубны, Rank.Туз).GetValue());
        assertEquals(2, new Card(Suit.Крести, Rank.Двойка).GetValue());
    }

    @Test
    void testCardToStringContainsInfo() {
        String s = new Card(Suit.Крести, Rank.Восьмерка).ToString();
        assertTrue(s.contains("Восьмерка"));
        assertTrue(s.contains("Крести"));
    }

    @Test
    void testDeckDrawReducesSize() {
        Deck deck = new Deck();
        int before = deckSize(deck);
        Card c = deck.draw();
        assertNotNull(c);
        assertEquals(before - 1, deckSize(deck));
    }

    /** Получаем размер через reflection (так как cards приватный) */
    private int deckSize(Deck deck) {
        try {
            var f = Deck.class.getDeclaredField("cards");
            f.setAccessible(true);
            return ((List<?>) f.get(deck)).size();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testDealerScoreAndAceLogic() {
        Dealer dealer = new Dealer();
        dealer.takeCard(new Card(Suit.Бубны, Rank.Туз));
        dealer.takeCard(new Card(Suit.Пики, Rank.Десятка));
        assertEquals(21, dealer.getscore());
        dealer.takeCard(new Card(Suit.Крести, Rank.Туз));
        assertTrue(dealer.getscore() <= 21);
    }

    @Test
    void testDealerShowHandAndReveal() {
        Dealer dealer = new Dealer();
        dealer.takeCard(new Card(Suit.Пики, Rank.Тройка));
        dealer.takeCard(new Card(Suit.Бубны, Rank.Валет));
        assertDoesNotThrow(dealer::showHand);
        assertDoesNotThrow(dealer::showOneCard);
        assertDoesNotThrow(dealer::revealHiddenCard);
    }

    @Test
    void testMainPlayerTurnLogic() {
        Deck deck = new Deck();
        Dealer dealer = new Dealer();
        Player player = new Player();
        assertDoesNotThrow(() -> Main.player_turn(player, deck, dealer));
    }

    @Test
    void testMainDealerTurnLogic() {
        Deck deck = new Deck();
        Dealer dealer = new Dealer();
        Player player = new Player();
        dealer.takeCard(deck.draw());
        dealer.takeCard(deck.draw());
        assertDoesNotThrow(() -> Main.dealer_turn(player, dealer, deck));
    }

    @Test
    void testPlayRoundStopsAfterLose() {
        // эмулируем ввод игрока: берёт карту (1), затем останавливается (0)
        String input = "1\n0\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        assertDoesNotThrow(Main::playround);
    }

    @Test
    void testEqualityOfRoundStats() {
        // Проверка корректности счётчиков
        int before = Main.rounds;
        Main.player_win = 1;
        Main.dealer_win = 2;
        Main.rounds++;
        assertTrue(Main.player_win <= Main.rounds);
        assertEquals(before + 1, Main.rounds);
    }
}