package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.card.IdentityCard;
import fr.utt.lo02.witchhunt.card.RumourCard;

import java.util.ArrayList;

public abstract class Player {

    protected int score;
    protected IdentityCard identityCard;
    protected final ArrayList<RumourCard> hand;

    public Player(ArrayList<RumourCard> hand){
        if(hand == null){
            throw new NullPointerException("Player constructor: hand can't be null");
        } else if (hand.isEmpty()){
            throw new IllegalArgumentException("Player constructor: hand can't be empty");
        } else {
            this.hand = hand;
        }
        identityCard = null;
        score = 0;
    }

    public void revealIdentity(){
        identityCard.reveal();
    }

    public void setIdentity(Identity identity){
        identityCard = new IdentityCard(identity);
    }

    public Identity getIdentity(){
        return identityCard.getIdentity();
    }

    public ArrayList<RumourCard> getHand(){
        return hand;
    }

    public abstract void playTurn();

    public abstract void respondAccusation();

    public abstract void chooseIdentity();
}
