package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.io.IOController;

import java.util.ArrayList;

public final class PhysicalPlayer extends Player {

    public PhysicalPlayer() {
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
        setIdentity(IOController.getInstance().readIdentity());
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
