package ru.nsu.maltsev.Task_1_1_2;

import java.util.Scanner;

public class Main {
    //статистика
    static int rounds = 1;
    static int player_win = 0;
    static int dealer_win = 0;

    //ход игрока
    public static void player_turn(Player player, Deck deck, Dealer dealer){
        Card card = deck.draw();
        player.takeCard(card);
        System.out.println("Вы открыли карту: " + card.ToString());
        System.out.println("Ваши карты: ");
        player.showHand();
        System.out.println("Карты дилера: ");
        dealer.showOneCard();
    }

    //ход дилера
    public static void dealer_turn(Player player, Dealer dealer, Deck deck){
        dealer.revealHiddenCard();
        System.out.println("Ваши карты: ");
        player.showHand();
        System.out.println("Карты дилера: ");
        dealer.showHand();
        while (dealer.getscore() < 17){
            Card card = deck.draw();
            dealer.takeCard(card);
            System.out.println("Дилер открывает карту: " + card.ToString());
            System.out.println("Ваши карты: ");
            player.showHand();
            System.out.println("Карты дилера: ");
            dealer.showHand();
        }
    }

    public static void playround() {
        System.out.printf("Раунд %d\n", rounds);
        Deck deck = new Deck();
        Player player = new Player();
        Dealer dealer = new Dealer();
        System.out.println("Дилер раздал карты");
        //игроки берут карты
        player.takeCard(deck.draw());
        player.takeCard(deck.draw());
        dealer.takeCard(deck.draw());
        dealer.takeCard(deck.draw());
        System.out.printf("\tВаши карты: ");
        player.showHand();
        System.out.printf("\tКарты дилера: ");
        dealer.showOneCard();
        System.out.println("Ваш ход");
        System.out.println("-------");
        System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться...");
        Scanner in = new Scanner(System.in);
        int choice = in.nextInt();
        while (choice != 0){
            if (choice != 1){
                System.out.println("Введите корректное число");
                choice = in.nextInt();
            }
            else{
                player_turn(player, deck, dealer);
                if (player.getscore() > 21){
                    System.out.println("Сумма привысила 21 - вы проиграли");
                    dealer_win++;
                    rounds++;
                    System.out.println(player_win + ":" + dealer_win);
                    return;
                }
                System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться...");
                choice = in.nextInt();
            }
        }
        System.out.println("Ход дилера");
        System.out.println("-------");
        dealer_turn(player, dealer, deck);
        if (dealer.getscore() > 21){
            System.out.println("Дилер набрал больше 21 - вы победили!");
            player_win++;
            rounds++;
            System.out.println(player_win + ":" + dealer_win);
            return;
        }
        if (player.getscore() > dealer.getscore()){
            System.out.println("Вы набрали больше очков, чем дилер, поздравляю!");
            player_win++;
            rounds++;
            System.out.println(player_win + ":" + dealer_win);
            return;
        }
        else if (player.getscore() < dealer.getscore()){
            System.out.println("К сожалению, дилер набрал больше очков - вы проиграли");
            dealer_win++;
            rounds++;
            System.out.println(player_win + ":" + dealer_win);
            return;
        }
        else{
            System.out.println("Количество набранных очков одинаково - это ничья");
            rounds++;
            System.out.println(player_win + ":" + dealer_win);
            return;
        }
    }

    public static void main(String[] args) {
        System.out.println("Добро пожаловать в консольный Блэкджек!");
        Scanner in = new Scanner(System.in);
        boolean play = true;
        while (play) {
            playround();
            System.out.println("Сыграем следующий раунд? Введите '1' чтобы продолжить, '0' чтобы выйти");
            int next = in.nextInt();
            play = (next == 1);
        }
        in.close();
    }
}
