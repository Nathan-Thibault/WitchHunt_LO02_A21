package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.io.IOController;

import java.util.ArrayList;

public final class PhysicalPlayer extends Player {

    private final String name;

    public PhysicalPlayer(String name) {
        super();
        this.name = name;
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
        IOController io = IOController.getInstance();

        io.printInfo(name.concat(" choose your identity from the list bellow:"));
        setIdentity(io.readIdentity());
    }

    @Override
    public boolean chooseToRevealOrDiscard() {
        //TODO
        return false;
    }

    @Override
    public String chooseCardFrom(ArrayList<String> listOfCardNames) {
        IOController io = IOController.getInstance();

        io.printInfo(name.concat(" choose a card from the list bellow:"));
        return io.readFromList(listOfCardNames);
    }

    @Override
    public String choosePlayerFrom(ArrayList<String> listOfPlayerNames) {
        IOController io = IOController.getInstance();

        io.printInfo(name.concat(" choose a player from the list bellow:"));
        return io.readFromList(listOfPlayerNames);
    }
}
