package ru.nsu.maltsev.task2;

import java.util.List;

public class HandPrinter {

    public static void showHand(List<Card> hand, int score) {
        System.out.printf("[");
        int i = 0;
        for (Card c : hand) {
            System.out.print(c.ToString());
            if (i < hand.size() - 1) {
                System.out.print(", ");
            }
            i++;
        }
        System.out.printf("]");
        System.out.printf(" => " + score + "\n");
    }

    public static void showOneCard(List<Card> hand) {
        if (!hand.isEmpty()) {
            System.out.println("[" + hand.get(0).ToString() + ", <Закрытая карта>]");
        }
    }

    public static void revealHiddenCard(List<Card> hand) {
        if (hand.size() >= 2) {
            System.out.println("Дилер открывает закрытую карту: " + hand.get(1).ToString());
        }
    }
}
