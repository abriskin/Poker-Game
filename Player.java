package com.company;

public class Player {
    private Card[] hand;
    private final int MAX_SIZE;
    private int currentSize;
    private String name;

    Player () {
        name = "lake";
        MAX_SIZE = 7;
        hand = new Card[MAX_SIZE];
    }

    Player (String n, int max) {
        name = n;
        MAX_SIZE = max;
        hand = new Card[MAX_SIZE];
    }

    public boolean setCard (Card c) {
        if (currentSize >= MAX_SIZE)
            return false;
        hand[currentSize] = c;
        currentSize++;
        return true;
    }

    public Card discard (int i) {
        if (hand[i] != null) {
            currentSize--;
            Card temp = hand[i];
            hand[i] = null;
            return temp;
        }
        else {
            return hand[i];
        }
    }

    public String showHand() {
        String returnString = "";
        for (int i = 0; i < currentSize; i++) {
            returnString += hand[i] + "\n";
        }
        return returnString;
    }

    public Card showCard (int i) {
        return hand[i];
    }

    public void sortHand() {
        Deck.sortCards(hand);
    }

    @Override
    public String toString() {
        String returnString = "";
        for (int i = 0; i < currentSize; i++) {
            if (hand[i] != null)
                returnString += ((i+1) + ": " + hand[i] + "\n");
        }
        return returnString;
    }

    public String getName() {
        return name;
    }

    public int getMAX_SIZE() {
        return MAX_SIZE;
    }

    public Card[] getHandReference() {
        return hand;
    }

    //empties hand
    public void reset() {

        for (int i = 0; i < currentSize; i++) {
            hand[i] = null;
        }
        currentSize = 0;
    }
}