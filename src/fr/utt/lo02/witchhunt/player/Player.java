package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.card.IdentityCard;
import fr.utt.lo02.witchhunt.card.RumourCard;

import java.util.ArrayList;

public abstract class Player {

    protected int score;
    protected IdentityCard identityCard;
    protected final ArrayList<String> hand;

    public Player(ArrayList<String> hand){
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

    public IdentityCard getIdentityCard(){
        return identityCard;
    }

    public ArrayList<String> getHand(){
        return hand;
    }

    public void addToScore(int addend){
        score += addend;
    }

    public abstract void playTurn();

    public abstract void respondAccusation();

    public abstract void chooseIdentity();

    public abstract String chooseCardFrom(ArrayList<String> listOfCardNames);

    public abstract Player choosePlayer(boolean unrevealed);
}
