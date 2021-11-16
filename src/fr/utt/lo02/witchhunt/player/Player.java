package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.card.IdentityCard;

import java.util.ArrayList;

public abstract class Player {

    protected int score;
    protected IdentityCard identityCard;
    protected ArrayList<String> ownedCards;

    public Player() {
        identityCard = null;
        score = 0;
    }

    public void revealIdentity() {
        identityCard.setRevealed(true);
    }

    public void setIdentity(Identity identity) {
        identityCard = new IdentityCard(identity);
    }

    public IdentityCard getIdentityCard() {
        return identityCard;
    }

    public ArrayList<String> getHand() {
        return getRevealedCards(false);
    }

    public ArrayList<String> getOwnedCards() {
        return ownedCards;
    }

    public ArrayList<String> getRevealedCards() {
        return getRevealedCards(true);
    }

    private ArrayList<String> getRevealedCards(boolean revealed) {
        CardManager cManager = CardManager.getInstance();

        ArrayList<String> cards = new ArrayList<>();
        for (String card : ownedCards) {
            boolean cr = cManager.getByName(card).isRevealed();
            //card revealed or not depending on if we want it to be or not
            if (!(cr || revealed) || (cr && revealed))
                cards.add(card);
        }

        return cards;
    }

    public void reset() {
        identityCard = null;
        ownedCards = CardManager.getInstance().dealHand();
    }

    public void addToScore(int addend) {
        score += addend;
    }

    public int getScore() {
        return score;
    }

    public abstract void playTurn();

    public abstract void respondAccusation(String accuser);

    public abstract void chooseIdentity();

    /**
     * @return true to reveal and false to discard
     */
    public abstract boolean chooseToRevealOrDiscard();

    public abstract String chooseCardFrom(ArrayList<String> listOfCardNames);

    public abstract String choosePlayerFrom(ArrayList<String> listOfPlayerNames);
}
