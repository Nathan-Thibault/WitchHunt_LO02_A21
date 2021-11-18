package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.card.CardManager;
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
        IOController io = IOController.getInstance();

        io.playerTurn(name);
        io.displayGameInfos();
        PlayerAction action = io.readFromList(PlayerAction.getTurnActions());

        switch (action) {
            case ACCUSE -> {
                //TODO choose player
            }
            case PLAY_HUNT -> {
                //TODO
            }
        }
    }

    @Override
    public void respondAccusation(String accuser) {
        IOController io = IOController.getInstance();
        CardManager cManager = CardManager.getInstance();

        ArrayList<String> playableCards = getPlayableCards(accuser);
        if (playableCards.size() > 0) {//player has cards to play, he has a choice
            io.printInfo(name.concat(" you've been accused by ").concat(accuser).concat(". Choose what to do."));
            PlayerAction action = io.readFromList(PlayerAction.getRespondActions());

            switch (action) {
                case PLAY_WITCH -> {
                    io.printInfo(name.concat(" choose a card to play it's witch effect."));
                    String card = chooseCardFrom(playableCards);

                    cManager.getByName(card).playWitchEffect(name, accuser);
                }
                case REVEAL -> revealIdentity(accuser);
            }
        } else {//player has no cards, he is forced to reveal his identity
            revealIdentity();
        }
    }

    @Override
    public void chooseIdentity() {
        IOController io = IOController.getInstance();

        io.printInfo(name.concat(" choose your identity from the list bellow:"));
        setIdentity(io.readIdentity());
    }

    @Override
    public boolean chooseToRevealOrDiscard() {
        IOController io = IOController.getInstance();

        io.printInfo(name.concat(" you have to choose to reveal your identity or discard a card."));
        String action = io.readFromList(Arrays.asList("Discard", "Reveal"));

        return action.equals("Reveal");
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
