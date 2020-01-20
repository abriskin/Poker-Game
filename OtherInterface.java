package com.company;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class OtherInterface {

    static Scanner reader = new Scanner(System.in);
    static PokerPlayer[] players = new PokerPlayer[2];
    static Deck myDeck = new Deck();
    static Kitty pot = new Kitty();
    static PokerPlayer dealer;
    static int ante = 1;

    public static void main(String[] args) throws InterruptedException {

        int maxCards = 5;
        int startAmount = 150;

        Strategy s = new Strategy();
        players[0] = new ComputerPokerPlayer("Computer", maxCards, startAmount, s);
        players[1] = new PokerPlayer("Thing Two", maxCards, startAmount);

        int ans = 0;
        do {
            playHand();
            System.out.println("Wanna play again? \n1: Yes \n2: No");
            ans = reader.nextInt();
        } while (ans == 1);
        for (int i = 0; i < players.length; i++) {
            System.out.println(players[i].getName() + " ends with a chip count of " + players[i].getChips());
        }
        System.out.println("Thank you for playing!");
    }

    //function for a round of the game
    private static void playHand() throws InterruptedException {
        pickDealerAndAntes();
        if (dealCardsAndBet()) {
            swapCardsAndBet();
        }
    }

    //pregame setup
    private static void pickDealerAndAntes() {
        pickDealer();
        getAntes();
    }

    //if no dealer, picks one; if current dealer, switches them
    public static void pickDealer() {
        if (dealer == null || (dealer.equals(players[1]))) dealer = players[0];
        else dealer = players[1];
    }

    //calculates antes, has players put them into pot, and subtracts from pot
    public static void getAntes() {
        ante += (int) (ante / 4);
        for (int i = 0; i < players.length; i++) {
            players[i].setCurrentBet(ante);
            System.out.println(players[i].getName() + " put " + ante + " into the pot.");
            pot.update(ante);
        }
    }

    //first half of a round
    private static boolean dealCardsAndBet() throws InterruptedException {
        dealCards();
        return bet(1);
    }

    //gives players a new hand
    private static void dealCards() {

        System.out.println("The ante is " + ante + ".");
        System.out.println("Both players put antes into the kitty. \n");

        myDeck.shuffle();
        for (int i = 0; i < players[0].getMAX_SIZE(); i++) {
            players[0].setCard(myDeck.deal());
            players[1].setCard(myDeck.deal());
        }

        System.out.println(players[1].getName() + ", here are your cards:");
        System.out.println(players[1]);
    }

    /*main part of game: allows players to continue until they have reached a consensus on a bet or
     *until one of them folds; if player folds or it's the end of round 2, calls endgame function
     */
    private static boolean bet(int g) throws InterruptedException {
        PokerPlayer curPlayer = players[0];
        PokerPlayer otherPlayer = dealer;

        if (otherPlayer == players[0])
            curPlayer = players[1];

        System.out.println(otherPlayer.getName() + " is the dealer");

        //main betting part
        do {

            System.out.println("The current pot value is " + pot.getTotal() + ".");
            int lastRaise = otherPlayer.getCurrentBet() - curPlayer.getCurrentBet();
            if (lastRaise > 0)
                System.out.println(otherPlayer.getName() + " just raised by " +
                        lastRaise + ".");
            int choice = getChoice(curPlayer,"What would you like to do? \n1: Fold \n2: Raise \n3: Call/Check", 1 );
            if (choice == 1) {
                curPlayer.setPlaying(false);
            } else if (choice == 2) {
                int newBet = getChoice(curPlayer,"How much would you like to raise by?", 2);
                if (curPlayer.canCoverBet(newBet)) {
                    pot.update(otherPlayer.getCurrentBet() + newBet - curPlayer.getCurrentBet() );
                    curPlayer.setCurrentBet(otherPlayer.getCurrentBet() + newBet);
                } else
                    System.out.println("ya can't raise by that much bro. ya poor");
            } else if (choice == 3) {
                int amt = otherPlayer.getCurrentBet() - curPlayer.getCurrentBet();
                if (amt == 0)
                    System.out.println("You checked! Very cool.");
                else {
                    if (curPlayer.canCoverBet(amt)) {
                        System.out.println("You put " + amt + " chips into the pot.");
                        curPlayer.addToCurrentBet(amt);
                        pot.update(amt);
                    } else System.out.println("Physically, you cannot call. Just, like, fold.");
                }
            } else {
                System.out.println("That's not, like, something you can pick.");
                System.out.println("I should add a loop here to make sure you pick an option that works, but I won't.");
                System.out.println("I'm sorry! But there's already like 5 nested while loops.");
            }

            if (curPlayer == players[0]) {
                curPlayer = players[1];
                otherPlayer = players[0];
            } else {
                curPlayer = players[0];
                otherPlayer = players[1];
            }

        } while (curPlayer.getPlaying() && otherPlayer.getPlaying()
                && curPlayer.getCurrentBet() != otherPlayer.getCurrentBet());

        if (g == 1 && curPlayer.getPlaying() && otherPlayer.getPlaying()) {
            System.out.println("The current pot value is " + pot.getTotal() + ".");
            return true;
        } else {
            findWinnerAndPayOut(g);
            for (int i = 0; i < players.length; i++) {
                System.out.println(players[i].getName() + ", your balance is " + players[i].getChips());
            }
            return false;
        }
    }

    //second half of a round: swapping cards & second round of betting
    private static boolean swapCardsAndBet() throws InterruptedException {
        System.out.println("Started swapping cards");
        swapCards();
        System.out.println("Started betting");
        return bet(2);
    }

    //allows players to pick how many cards to switch, gives them new cards, and returns discarded
    //cards to the deck
    public static void swapCards() {
        for (int playerIndex = 0; playerIndex < players.length; playerIndex++) {
            PokerPlayer thisPlayer = players[playerIndex];
            thisPlayer.sortHand();
            if (thisPlayer == players[1]) {
                System.out.println("\n" + thisPlayer.getName() + ", here are your cards: ");
                System.out.println(thisPlayer);
            }

            int numDiscard = getChoice(thisPlayer, "How many cards would you like to discard? ", 3);

            Card[] discardArray = new Card[numDiscard];
            for (int j = 0; j < numDiscard; j++) {
                int discardIndex = getChoice(thisPlayer,"Pick the number of a card you'd like to discard: ", 4);
                discardIndex--;
                Card discardCard = thisPlayer.showCard(discardIndex);

                boolean cardAlreadyInArray = false;
                for (int i = 0; i < j; i++) {
                    if (discardArray[i] == null)
                        break;
                    if (discardArray[i].equals(discardCard))
                        cardAlreadyInArray = true;
                }
                if (!cardAlreadyInArray) {
                    discardArray[j] = thisPlayer.discard(discardIndex);
                }
                else System.out.println("This number card has been picked already.");
            }

            thisPlayer.sortHand();
            int k;
            for (k = 0; k < numDiscard; k++) {
                if (discardArray[k] != null)
                    thisPlayer.setCard(myDeck.deal());
                else
                    break;
            }

            thisPlayer.sortHand();
            myDeck.returnToDeck(discardArray);
            System.out.println(thisPlayer.getName() + " swapped " + k + " cards.");
            if (thisPlayer == players[1]) {
                System.out.println("This is " + thisPlayer.getName() + "'s new hand:");
                System.out.println(thisPlayer);
            }
        }
    }

    //called when player folds or end of round 2: calculates winner, gives winner money, and resets
    //player hands, bets, and pot
    public static void findWinnerAndPayOut(int round) throws InterruptedException {
        int score0 = 0;
        int score1 = 0;
        int winner = -1;

        if (!players[0].getPlaying())
            score0 = -1;
        else if (!players[1].getPlaying())
            score1 = -1;
        else {
            score0 = Rules.scoreCards(players[0].getHandReference());
            score1 = Rules.scoreCards(players[1].getHandReference());
        }

        if (score0 > score1) {
            winner = 0;
        } else if (score0 < score1) {
            winner = 1;
        } else {
            winner = Rules.breakTie(players[0].getHandReference(),
                    players[1].getHandReference()) - 1;
        }

        if (round == 2 || score0 == -1 || score1 == -1) {
            players[0].setPlaying(true);
            players[1].setPlaying(true);
        }

        for (int i = 0; i < players.length; i++) {
            players[i].deduct();
        }

        for (int i = 0; i < players.length; i++) {
            players[i].setCurrentBet(0);
        }

        myDeck.returnToDeck(players[0].getHandReference());
        myDeck.returnToDeck(players[1].getHandReference());
        players[0].reset();
        players[1].reset();
        System.out.println("\n\nChecking cards...");
        TimeUnit.SECONDS.sleep(2);

        if (winner != -1) {
            players[winner].increase(pot.payout());
            System.out.println("\n" + players[winner].getName() + " is the winner!!!\n");
        } else {
            int halfPot;
            if (pot.getTotal() % 2 == 0) {
                halfPot = pot.getTotal();
                pot.setTotal(0);
            }
            else {
                halfPot = (pot.getTotal() - 1) / 2;
                pot.setTotal(1);
            }
            players[0].increase(halfPot);
            players[1].increase(halfPot);
        }
    }

    //allows player to make a choice
    public static int getChoice(PokerPlayer player, String prompt, int choiceType) {
        if (player == players[0]) {
            int choice = ((ComputerPokerPlayer)player).makeDecision(choiceType);
            System.out.println("The question for the computer was: \n" + prompt);
            System.out.println("The computer chose " + choice + "\n");
            return choice;
        }
        else {
            System.out.println(prompt);
            return reader.nextInt();
        }
    }

}
