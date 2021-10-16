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
        CardManager cManager = CardManager.getInstance();

        ArrayList<String> hand = new ArrayList<>();
        for (String card : ownedCards) {
            if (!cManager.getByName(card).isRevealed())
                hand.add(card);
        }

        return hand;
    }

    public ArrayList<String> getOwnedCards() {
        return ownedCards;
    }

    public void removeFromOwned(String cardName){
        ownedCards.remove(cardName);
    }

    public void resetHand() {
        ownedCards = CardManager.getInstance().dealHand();
    }

    public void addToScore(int addend) {
        score += addend;
    }

    public void resetScore() {
        score = 0;
    }

    public abstract void playTurn();

    public abstract void respondAccusation();

    public abstract void chooseIdentity();

    public abstract String chooseCardFrom(ArrayList<String> listOfCardNames);

    public abstract String choosePlayerFrom(ArrayList<String> listOfPlayerNames);
}
