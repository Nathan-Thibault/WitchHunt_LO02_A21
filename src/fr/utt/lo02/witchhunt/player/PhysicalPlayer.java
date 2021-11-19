package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.RoundManager;
import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.card.RumourCard;
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

        io.printInfo(name.concat(" it's your turn. Choose what to do from the options bellow:"));
        io.displayGameInfos();

        List<PlayerAction> possibleActions = PlayerAction.getTurnActions();
        ArrayList<String> playableCards = getPlayableCards();

        if (playableCards.isEmpty())//no playable cards, remove play hunt option from user
            possibleActions.remove(PlayerAction.PLAY_HUNT);

        PlayerAction action = io.readFromList(PlayerAction.getTurnActions());

        switch (action) {
            case ACCUSE -> {
                ArrayList<String> possibleTargets = PlayerManager.getInstance().getUnrevealedPlayers();
                possibleTargets.remove(name);//player can't choose himself

                String target = choosePlayerFrom(possibleTargets);
                RoundManager.getInstance().accuse(name, target);
            }
            case PLAY_HUNT -> {
                String cardName = chooseCardFrom(playableCards);
                RumourCard card = CardManager.getInstance().getByName(cardName);

                card.canPlayHuntEffect(name);
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
        Identity identity = io.readFromList(Arrays.stream(Identity.values()).toList());
        setIdentity(identity);
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
