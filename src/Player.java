import java.util.ArrayList;
import java.util.List;

public class Player {

    private final List<Card> hand;
    static public int chips;

    public Player() {
        this.hand = new ArrayList<>();
    }

    public List<Card> getHand() {
        return hand;
    }

    public int calcHand() {
        int total = 0;
        for (Card card : this.hand) {
            int value = Integer.parseInt(String.valueOf(card.getOrderedRank(card.getRank())));
            total += value;
            if(total > 21 && value == 11) {
                total -= 10;
            }
        }
        return total;
    }


    public int getChips() {
        return chips;
    }

    public void addChips(int chipWager) {
        chips += chipWager;
    }

    public void subtractChips(int chipwager) {
        chips -= chipwager;
    }

    public void takeCard(Card card) {
        hand.add(card);
        sortHand();
    }


    private void sortHand() {
        hand.sort((a, b) -> {
            if (Card.getOrderedRank(a.getRank()) == Card.getOrderedRank(b.getRank())) {
                return Card.getOrderedSuit(a.getSuit()) - Card.getOrderedSuit(b.getSuit());     // order by suit if
            }                                                                                   // ranks are the same

            return Card.getOrderedRank(a.getRank()) - Card.getOrderedRank(b.getRank());         // otherwise, by rank
        });
    }

}
