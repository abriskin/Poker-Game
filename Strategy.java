package com.company;

public class Strategy {

    public int pickPlay() {
        return pickRandomOption(3);
    }

    public int pickBetAmount(double chips, int currentBet) {
        return (int) (Math.random() * (chips-currentBet)/2);
    }

    public int pickAmountToSwap() {
        return pickRandomOption(5);
    }

    public int pickCardToSwap() {
        return pickRandomOption(5);
    }

    private int pickRandomOption (int c) {
        return 1 + (int)(Math.random() * c);
    }
}
