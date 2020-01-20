package com.company;

public class ComputerPokerPlayer extends PokerPlayer{

    private Strategy s;

    ComputerPokerPlayer(String n, int max, int startingAmount, Strategy strat) {
        super(n, max, startingAmount);
        s = strat;
    }

    //player logic: interact with strategy
    public int makeDecision(int choiceType) {
        switch (choiceType) {
            case 1: //pick whether to bet or not
                return s.pickPlay();
            case 2: //pick amount to bet
                return s.pickBetAmount(getChips(), getCurrentBet());
            case 3: //pick amount to swap
                return s.pickAmountToSwap();
            case 4: //pick card to swap
                return s.pickCardToSwap();
            default:
                return -1;
        }
        /*if (options == -1) //special case: pick bet amount
            return (int) (Math.random() * (this.getChips()-this.getCurrentBet())/2);
        else //normal case
            return 1 + (int)(Math.random() * options);*/
    }
}
