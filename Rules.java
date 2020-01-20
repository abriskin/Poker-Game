package com.company;

import java.util.Arrays;

public class Rules {

    //returns 0 if h1 wins, 1 if h2 wins, -1 if tie
    public static int breakTie(Card[] hand1, Card[] hand2) {
        int combo = scoreCards(hand1);
        int tieNum1 = -1;
        int tieNum2 = -1;
        int[] checkArray;
        int winner;
        switch(combo) {
            case 10:
                checkArray = checkIfRoyalFlush(hand1);
                tieNum1 = checkArray[1];
                checkArray = checkIfRoyalFlush(hand2);
                tieNum2 = checkArray[1];
                break;
            case 9:
                checkArray = checkIfStraightFlush(hand1);
                tieNum1 = checkArray[1];
                checkArray = checkIfStraightFlush(hand2);
                tieNum2 = checkArray[1];
                break;
                //for flush & high run special method
            case 8:
                checkArray = checkIfFour(hand1);
                tieNum1 = checkArray[1];
                checkArray = checkIfFour(hand2);
                tieNum2 = checkArray[1];
                break;
            case 7:
                checkArray = checkIfFullHouse(hand1);
                tieNum1 = checkArray[1];
                checkArray = checkIfFullHouse(hand2);
                tieNum2 = checkArray[1];
                break;
            case 1: //since high card & flush judged the same way
            case 6:
                winner = getHigherFlushOrHigh(hand1, hand2);
                return winner;
            case 5:
                checkArray = checkIfStraight(hand1);
                tieNum1 = checkArray[1];
                checkArray = checkIfStraight(hand2);
                tieNum2 = checkArray[1];
                break;
            case 4:
                checkArray = checkIfThree(hand1);
                tieNum1 = checkArray[1];
                checkArray = checkIfThree(hand2);
                tieNum2 = checkArray[1];
                break;
            case 3:
                checkArray = checkIfTwoPair(hand1);
                tieNum1 = checkArray[1];
                checkArray = checkIfTwoPair(hand2);
                tieNum2 = checkArray[1];
                if (tieNum1 == tieNum2) {
                    tieNum2 = checkArray[2];
                    checkArray = checkIfTwoPair(hand1);
                    tieNum1 = checkArray[2];
                }
                //"wow adi, why is this code so ugly?"
                // "well ya see, i came up with it on the go & am too lazy to fix the earlier parts. sorry"
                if (tieNum1 == tieNum2) {
                    tieNum1 = checkArray[3];
                    checkArray = checkIfTwoPair(hand2);
                    tieNum2 = checkArray[3];
                }
                break;
            case 2:
                checkArray = checkIfPair(hand1);
                tieNum1 = checkArray[1];
                checkArray = checkIfPair(hand2);
                tieNum2 = checkArray[1];
                if (tieNum1 == tieNum2) {
                    winner = getHigherFlushOrHigh(hand1, hand2);
                    return winner;
                }
                break;
            //case 1 is above (near 6)
        }
        if (tieNum1 > tieNum2)
            return 1;
        else if (tieNum1 < tieNum2)
            return 2;
        else
            return 0;
    }

    //returns score for hand; only works for 5 cards but otherwise it would've been more work so :/
    //assumes cards are previously sorted
    public static int scoreCards(Card[] h) {
        int[] checkArray;

        //yooo are you ready for,,,, if statements!! so many if statements!! let's go!!
        checkArray = checkIfRoyalFlush(h);
        if (checkArray[0] == 1) return 10;
        else {
            checkArray = checkIfStraightFlush(h);
            if (checkArray[0] == 1) return 9;
            else {
                checkArray = checkIfFour(h);
                if (checkArray[0] == 1) return 8;
                else {
                    checkArray = checkIfFullHouse(h);
                    if (checkArray[0] == 1) return 7;
                    else {
                        checkArray = checkIfFlush(h);
                        if (checkArray[0] == 1) return 6;
                        else {
                            checkArray = checkIfStraight(h);
                            if (checkArray[0] == 1) return 5;
                            else {
                                checkArray = checkIfThree(h);
                                if (checkArray[0] == 1) return 4;
                                else {
                                    checkArray = checkIfTwoPair(h);
                                    if (checkArray[0] == 1) return 3;
                                    else {
                                        checkArray = checkIfPair(h);
                                        if (checkArray[0] == 1) return 2;
                                        else {
                                            return 1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static int[] checkIfRoyalFlush(Card[] hand) {
        int[] returnArray = new int[2];
        returnArray[0] = 1;

        int[] checkStraightFlushArray = checkIfStraightFlush(hand);

        if (checkStraightFlushArray[0] == 0)
            returnArray[0] = 0;
        else {
            if (hand[0].getValue() != 10) returnArray[0] = 0;
        }
        returnArray[1] = 10;
        return returnArray;
    }

    private static int[] checkIfStraightFlush(Card[] hand) {
        int[] returnArray = new int[2];
        returnArray[0] = 0;

        int[] checkStraightArray = checkIfStraight(hand);
        int[] checkFlushArray = checkIfFlush(hand);

        if (checkStraightArray[0] == 1 && checkFlushArray[0] == 1) {
            returnArray[0] = 1;
            returnArray[1] = checkStraightArray[1];
        }
        return returnArray;
    }

    private static int[] checkIfFour(Card[] hand) {
        int[] returnArray = new int[2];
        Arrays.toString(hand);
        for (int i = 0; i < 2; i++) {
            returnArray[0] = 1;
            for (int j = i+1; j < i + 4; j++) {
                if (hand[i] != null && hand[j] != null) {
                    if (hand[i].getValue() != (hand[j].getValue())) returnArray[0] = 0;
                }
            }
        }
        returnArray[1] = hand[2].getValue(); //because a four of a kind necessarily passes through there
        return returnArray;
    }

    private static int[] checkIfFullHouse(Card[] hand) {
        int[] returnArray = new int[3];
        returnArray[0] = 0;
        int[] threeArray = checkIfThree(hand);
        int threeStartsHere = threeArray[2];
        switch (threeStartsHere) {
            case (27): //since otherwise like. it goes through this even if it isn't an actual fullHouse bc 0 = false
                if (hand[3].getValue() == hand[4].getValue())
                    returnArray[0] = 1;
                break;
            case (2):
                if (hand[0].getValue() == hand[1].getValue())
                    returnArray[0] = 1;
                break;
        }
        returnArray[1] = hand[2].getValue(); //since that's where part of the 3 is going to be for sure
        return returnArray;
    }

    private static int[] checkIfFlush(Card[] hand) {
        int[] returnArray = new int[1]; //i know this is stupid but i have a bunch of arrays. i like that.
        returnArray[0] = 1;
        for (int i = 0; i < 4; i++) {
            if (hand[i] != null && hand[i+1] != null)
                if (!(hand[i].getSuit().equals(hand[i + 1].getSuit()))) returnArray[0] = 0;
        }
        return returnArray;
    }

    private static int[] checkIfStraight(Card[] hand) {
        int[] returnArray = new int[2];
        returnArray[0] = 1;
        for (int i = 0; i < 4; i++) {
            if (hand[i] != null && hand[i+1] != null) {
                if (hand[i].getValue() + 1 != hand[i + 1].getValue()) returnArray[0] = 0;
            }
        }
        if (hand[0] != null)
            returnArray[1] = hand[0].getValue();
        return returnArray;
    }

    private static int[] checkIfThree(Card[] hand) {
        //requirements: third element of returnArray is index where three starts
        int[] returnArray = new int[3];
        for (int i = 0; i < 3; i++) {
            returnArray[0] = 1;
            for (int j = i; j < i + 3; j++) {
                if (hand[i] != null && hand[j] != null) {
                    if (!(hand[i].getRank().equals(hand[j].getRank()))) returnArray[0] = 0;
                }
            }
            if (returnArray[0] == 1) {
                returnArray[1] = hand[i].getValue();
                if (i == 0)
                    returnArray[2] = 27;
                else
                    returnArray[2] = 2; //cards sorted so 3 in a row can only start at 0, 1, or 2
            }
        }
        return returnArray;
    }

    private static int[] checkIfTwoPair(Card[] hand) {
        //make sure to return array of 3: t/f, bigger pair, smaller pair, high card <-- order matters
        int[] returnArray = new int[4];
        Card[] hand2 = new Card[hand.length];
        for (int i = 0; i < hand.length; i++) {
            hand2[i] = hand[i];
        }

        int[] checkArray = checkIfPair(hand);
        if (checkArray[0] == 1) { //if there isn't a single pair there aren't gonna be two, adi. numbers.

            returnArray[1] = checkArray[1];
            hand[checkArray[2]] = null;
            hand[checkArray[2] + 1] = null;

            checkArray = checkIfPair(hand);
            if (checkArray[0] == 1) {
                returnArray[0] = 1;
                returnArray[2] = checkArray[1];
                if (checkArray[2] == 0)
                    returnArray[3] = 2;
                else
                    returnArray[3] = 0;
            }
        }
        for (int i = 0; i < hand2.length; i++) {
            hand[i] = hand2[i];
        }
        return returnArray;
    }

    private static int[] checkIfPair(Card[] hand) {
        //pair (i initialized it to false because proving that something isn't a pair is fairly difficult)
        //0: t/f, 1: higher pair value, 3: index of first card in pair
        int[] returnArray = new int[3];

        for (int i = 0; i < 4; i++) {
            if (hand[i] != null && hand[i+1] != null) {
                if (hand[i].getValue() == hand[i+1].getValue()) {
                    returnArray[0] = 1;
                    returnArray[1] = hand[i].getValue();
                    returnArray[2] = i;
                }
            }
        }
        return returnArray;
    }

    //flush & high card calculated the same way
    private static int getHigherFlushOrHigh(Card[] hand1, Card[] hand2) {
        for (int i = 4; i >= 0; i--) {
            if (hand1[i] != null && hand2[i] != null) {
                if (hand1[i].getValue() > hand2[i].getValue())
                    return 1;
                else if (hand1[i].getValue() < hand2[i].getValue())
                    return 2;
            }
        }
        return 0;
    }
}
