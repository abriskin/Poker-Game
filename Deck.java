package com.company;

import java.util.Arrays;

public class Deck {

    private Card[] cards;
    public final int MAX_SIZE;
    private int size;

    Deck () {

        MAX_SIZE = 52;

        cards = new Card[MAX_SIZE];

        String[] suits = new String[]{"Diamonds", "Clubs", "Hearts", "Spades"};

        String[] intsToWords = new String[]{"Two", "Three", "Four", "Five", "Six", "Seven",
                "Eight", "Nine", "Ten", "Jack", "Queen", "King", "Ace"};

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                cards[i * 13 + j] = new Card(suits[i], intsToWords[j], j + 2);
            }
        }

        size = MAX_SIZE;
    }

    public Card deal() {

        Card c = cards[size-1];

        cards[size-1] = null;

        size--;

        return c;
    }

    boolean returnToDeck (Card c) {

        if (size == 52) return false;
        else {
            if (c != null) {
                for (int i = size-1; i >= 0; i--) {
                    cards[i+1] = cards[i];
                }
                cards[0] = c;
                size++;
            }
        }
        return true;
    }

    boolean returnToDeck (Card[] ca) {

        if (size + ca.length > 52) return false;

        else {
            for (int i = 0; i < ca.length; i++) {
                if (ca[i] != null) {
                    returnToDeck(ca[i]);
                    ca[i] = null;
                }
            }
        }

        return true;
    }

    public void shuffle() {

        for (int i = 0; i < size; i++) {

            int rand = (int) (Math.random() * size);

            Card temp = cards[i];

            cards[i] = cards[rand];

            cards[rand] = temp;
        }
    }

    public int getSize() {
        return size;
    }

    public String toString() {

        String returnString = "";
        for (int i = 0; i < size; i++) {
            if (cards[i] != null)
                returnString += "\n" + cards[i].toString();
        }
        return returnString;
    }

    public static void sortCards (Card[] hand) { //sorts cards passed in by faceValue

        fixCards(hand);

        Card temp;

        for (int i = 0; i < hand.length; i++) {
            for (int j = i; j < hand.length; j++) {
                if (hand[j] != null && hand[i] != null) {
                    if (hand[j].getValue() < hand[i].getValue()) {
                        temp = hand[j];
                        hand[j] = hand[i];
                        hand[i] = temp;
                    }
                }
            }
        }
    }

    public static void fixCards (Card[] hand) {

        for (int i = 0; i < hand.length; i++) {
            if (hand[i] == null) {
                for (int j = i+1; j < hand.length; j++) {
                    Card temp = hand[i];
                    hand[i] = hand[j];
                    hand[j] = temp;
                    if (hand[i] != null)
                        break;
                }
            }
        }
    }

    public boolean checkDeck() {

        int spadesCnt=0, diamondsCnt=0, clubsCnt=0, heartsCnt=0;
        int totalValue=0;

        Card[] spades = new Card[20];
        Card[] diamonds = new Card[20];
        Card[] hearts = new Card[20];
        Card[] clubs = new Card[20];

        System.out.println("*******Checking Deck **********/");

        for (int i = 0; i < MAX_SIZE; i++) {

            if (cards[i]!=null && cards[i].getSuit().equals("clubs")) {
                clubs[clubsCnt]=cards[i];
                clubsCnt++;
            }

            else if (cards[i]!=null && cards[i].getSuit().equals("diamonds")) {
                diamonds[diamondsCnt]=cards[i];
                diamondsCnt++;
            }

            else if (cards[i]!=null && cards[i].getSuit().equals("hearts")) {
                hearts[heartsCnt]=cards[i];
                heartsCnt++;
            }

            else if(cards[i]!=null && cards[i].getSuit().equals("spades")) {
                spades[spadesCnt]=cards[i];
                spadesCnt++;
            }

            if (cards[i]!=null )
                totalValue+=cards[i].getValue();

        }

        for (int i=0; i<clubsCnt; i++) {
            if (clubs[i]!=null)
                System.out.println(clubs[i]);
        }

        for (int i=0; i < diamondsCnt; i++) {
            if(diamonds[i]!=null)
                System.out.println(diamonds[i]);
        }

        for (int i=0; i<spadesCnt; i++) {
            if(spades[i]!=null)
                System.out.println(spades[i]);
        }

        for (int i=0; i<heartsCnt; i++) {
            if(hearts[i]!=null)
                System.out.println(hearts[i]);
        }


        System.out.println("Clubs: " + clubsCnt + " Spades: " + spadesCnt + " Diamonds: " + diamondsCnt + " Hearts: " + heartsCnt);
        System.out.println("Total: " + totalValue);

        if (clubsCnt==13 && spadesCnt == 13 && diamondsCnt==13 && heartsCnt==13 && totalValue==416)
            return true;
        return false;

    }

}
