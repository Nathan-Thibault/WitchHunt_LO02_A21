package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.card.RumourCard;

import java.util.ArrayList;

public class PhysicalPlayer extends Player{

    public PhysicalPlayer(ArrayList<RumourCard> hand){
        super(hand);
    }

    @Override
    public void playTurn() {
        //TODO
    }

    @Override
    public void respondAccusation() {
        //TODO
    }

    @Override
    public void chooseIdentity() {
        //TODO
    }

    @Override
    public String chooseCardFrom(ArrayList<String> listOfCardNames) {
        //TODO
        return null;
    }

    @Override
    public Player choosePlayer(boolean unreaveled) {
        //TODO
        return null;
    }
}
