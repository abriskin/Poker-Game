package com.company;

public class PokerPlayer extends Player {

    public double chips;
    public int currentBet = 0;
    public boolean isPlaying = true;
    //TODO change variables to private

    PokerPlayer() {
        super();
        chips = 0;
    }

    PokerPlayer (String name, int max, int startingAmount) {
        super(name, max);
        chips = startingAmount;
    }

    public boolean canCoverBet (double amt) {
        return (chips >= currentBet + amt);
    }

    public double deduct () {
        chips -= currentBet;
        return chips;
    }

    public void increase (double amt) {
        chips += amt;
    }

    public double getChips() {
        return chips;
    }

    public void addToCurrentBet(int amt) {
        currentBet += amt;
    }

    public void setCurrentBet(int cB) {
        currentBet = cB;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void setPlaying(boolean p) {
        isPlaying = p;
    }

    public boolean getPlaying() {
        return isPlaying;
    }
}
