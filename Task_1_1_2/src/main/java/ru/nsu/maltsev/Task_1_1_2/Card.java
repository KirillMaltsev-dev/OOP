package ru.nsu.maltsev.Task_1_1_2;

enum Suit {Червы, Пики, Бубны, Крести};
enum Rank {Двойка, Тройка, Четверка, Пятерка, Шестерка, Семерка, Восьмерка, Девятка, Десятка,
    Валет, Дама, Король, Туз};

class Card {
    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank){
        this.suit = suit;
        this.rank = rank;
    }

    public int GetValue(){
        return switch (rank){
            case Валет, Дама, Король -> 10;
            case Туз -> 11;
            default -> rank.ordinal() + 2;
        };
    }
    public String ToString(){
        return rank + " " + suit + " (" + GetValue() + ")";
    }
}