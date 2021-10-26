package fr.utt.lo02.witchhunt.player;

import java.util.ArrayList;

public final class PhysicalPlayer extends Player{

    public PhysicalPlayer(){
        super();
    }

    @Override
    public void playTurn() {
        //TODO
    }

    @Override
    public void respondAccusation(String accuser) {
        //TODO
    }

    @Override
    public void chooseIdentity() {
        //TODO
    }

    @Override
    public boolean chooseToRevealOrDiscard() {
        //TODO
        return false;
    }

    @Override
    public String chooseCardFrom(ArrayList<String> listOfCardNames) {
        //TODO
        return null;
    }

    @Override
    public String choosePlayerFrom(ArrayList<String> listOfPlayerNames) {
        //TODO
        return null;
    }
}
