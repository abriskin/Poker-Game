package com.company;

public class Card {

    private final String suit;
    private final String rank;
    private final int value; //since point values can be different in different games

    Card (String s, String r, int v) {
        suit = s;
        rank = r;
        value = v;
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return rank + " of " + suit;
    }
}
