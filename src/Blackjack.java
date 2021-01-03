import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Blackjack {

    private final String[] SUITS = { "C", "D", "H", "S" };
    private final String[] RANKS = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K" };

    private char whoseTurn;
    private final Player player;
    private final Player computer;
    private List<Card> deck;
    private final Scanner in;
    private boolean playerStay = false;
    private boolean computerStay = false;
    String winMessage = "Congratulations, you win! You had a hand with a value of ";
    String loseMessage = "Maybe next time. You lose. You had a hand with a value of ";
    String tieMessage = "Looks like it's a tie with a value of ";
    String extra = " and the CPU had a hand with a value of ";



    public Blackjack() {
        this.whoseTurn = 'P';  // player goes first
        this.player = new Player();
        this.computer = new Player();
        this.in = new Scanner(System.in);
    }

    public void play() {
        // play the game until someone wins
        System.out.println("How many chips would you like to wager?");
        int chipWager = in.nextInt();
        in.nextLine();
        while (chipWager < 1 || chipWager > 25) {
            System.out.println("How many chips would you like to wager?");
            chipWager = in.nextInt();
           // in.nextLine();
        }

        shuffleAndDeal();
        //TimeUnit.SECONDS.sleep(1);
        if (player.calcHand() == 21) {
            player.addChips(chipWager * 3);
            String blackjackMessage = "Congratulations, you got a blackjack!";
            System.out.println("\n" + blackjackMessage);
            System.out.println("You gained " + (chipWager * 3) + " chips, for a total of " + player.getChips() + " chips.");
        }

        while (true) {
            // The game doesn't end until one of the players gets a hand worth 21.

            if (computer.calcHand() > 21 && player.calcHand() <= 21) {
                player.addChips(chipWager);
                System.out.println("\n" + winMessage + player.calcHand() + extra + computer.calcHand() + ".");
                System.out.println("You gained " + chipWager + " chips, for a total of " + player.getChips() + " chips.");
                break;
            } else if (player.calcHand() > 21 && computer.calcHand() <= 21) {
                player.subtractChips(chipWager);
                System.out.println("\n" + loseMessage + player.calcHand() + extra + computer.calcHand() + ".");
                System.out.println("You lost " + chipWager + " chips, for a total of " + player.getChips() + " chips.");
                break;
            }

            if (playerStay && computerStay) {
                if (computer.calcHand() > player.calcHand()) {
                    player.subtractChips(chipWager);
                    System.out.println("\n" + loseMessage + player.calcHand() + extra + computer.calcHand() + ".");
                    System.out.println("You lost " + chipWager + " chips, for a total of " + player.getChips() + ".");
                    break;
                } else if (player.calcHand() > computer.calcHand()) {
                    player.addChips(chipWager);
                    System.out.println("\n" + winMessage + player.calcHand() + extra + computer.calcHand() + ".");
                    System.out.println("You gained " + chipWager + " chips, for a total of " + player.getChips() + " chips.");
                    break;
                } else if (player.calcHand() == computer.calcHand()) {
                    System.out.println("\n" + tieMessage + player.calcHand() + ".");
                    System.out.println("You have a total of " + player.getChips() + " chips.");
                    break;
                }
            }
            else if (whoseTurn == 'P') {
                if (playerStay) {
                    whoseTurn = 'C';
                }
                else {
                    whoseTurn = takeTurn(false);
                }
            } else if (whoseTurn == 'C') {
                if (computerStay) {
                    whoseTurn = 'P';
                }
                else {
                    whoseTurn = takeTurn(true);
                }
            }
        }
    }



    public void shuffleAndDeal() {
        if (deck == null) {
            initializeDeck();
        }
        Collections.shuffle(deck);  // shuffles the deck

        while (player.getHand().size() < 2) {
            player.takeCard(deck.remove(0));    // deal 2 cards to the
            computer.takeCard(deck.remove(0));  // player and the computer
        }
    }

    ////////// PRIVATE METHODS /////////////////////////////////////////////////////

    private void initializeDeck() {
        deck = new ArrayList<>(52);

        for (String suit : SUITS) {
            for (String rank : RANKS) {
                deck.add(new Card(rank, suit));     // adds 52 cards to the deck (13 ranks, 4 suits)
            }
        }
    }

    private char takeTurn(boolean cpu) {
        showHand(cpu);


        if (!cpu) {
            // print value
            String answer;
            System.out.println("PLAYER hand value: " + player.calcHand() + "." );

            System.out.println("Would you like to hit? y/n");
            answer = in.nextLine().trim().toLowerCase();
           // in.nextLine();

            if(answer.equals("y")) {
                player.takeCard(deck.remove(0));
            }
            else if(answer.equals("n")) {
                System.out.println("PLAYER: Your turn!");
                playerStay = true;
            }
            return 'C';

        } else {
            if(computer.calcHand() >= 17) {
                System.out.println("CPU: Your turn!");
                computerStay = true;
            }
            else {
                System.out.println("CPU: I'll hit!");
                computer.takeCard(deck.remove(0));
            }
            return 'P';
        }
    }


    private void showHand(boolean cpu) {
        if (!cpu) {
            String playerHand = "";
            for (int i=0; i < player.getHand().size(); i++) {
                playerHand += player.getHand().get(i) + " ";
            }
            System.out.println("PLAYER hand: " + playerHand);   // only show player's hand
        }
        else {
            String maskedHand = "";
            for (int i=0; i < computer.getHand().size(); i++) {
                maskedHand += computer.getHand().get(i) + " ";
            }
            System.out.println("\nCPU hand: **" + maskedHand.substring(2));
            //System.out.println("\n CPU hand: " + computer.getHand());
        }
    }


    ////////// MAIN METHOD /////////////////////////////////////////////////////////

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("##############################################################");
        System.out.println("#   Blackjack                                                #");
        System.out.println("#                                                            #");
        System.out.println("#   A human v. CPU rendition of the classic card game        #");
        System.out.println("#   Blackjack. Play the game!                                #");
        System.out.println("#                                                            #");
        System.out.println("##############################################################");

        new Blackjack().play();

        String playerContinue;
        do {
            System.out.println("Would you like to play again? y/n");
            playerContinue = in.nextLine().trim().toLowerCase();
                if (playerContinue.equals("n")) {
                    break;
                }
                else {
                    new Blackjack().play();
                }
        } while (playerContinue.equals("y"));
    }
}