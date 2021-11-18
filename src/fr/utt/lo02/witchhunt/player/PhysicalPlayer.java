package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.io.IOController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PhysicalPlayer extends Player {
    public PhysicalPlayer(String name) {
        super(name);
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

    enum PlayerAction {
        ACCUSE("Accuse another player", false),
        PLAY_HUNT("Play a hunt effect", false),
        PLAY_WITCH("Play a witch effect", true),
        REVEAL("Reveal your identity", true);

        private final String description;
        private final boolean respondAction;

        PlayerAction(String description, boolean respondAction) {
            this.description = description;
            this.respondAction = respondAction;
        }

        public static List<PlayerAction> getTurnActions() {
            return Arrays.stream(PlayerAction.values()).filter(pa -> !pa.isRespondAction()).toList();
        }

        public static List<PlayerAction> getRespondActions() {
            return Arrays.stream(PlayerAction.values()).filter(PlayerAction::isRespondAction).toList();
        }


        public boolean isRespondAction() {
            return respondAction;
        }

        @Override
        public String toString() {
            return description;
        }
    }
}
