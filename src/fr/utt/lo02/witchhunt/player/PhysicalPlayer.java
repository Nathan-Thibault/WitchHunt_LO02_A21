package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.RoundManager;
import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.card.RumourCard;
import fr.utt.lo02.witchhunt.io.IOController;

import java.util.EnumSet;
import java.util.Set;

public final class PhysicalPlayer extends Player {
    public PhysicalPlayer(String name) {
        super(name);
    }

    @Override
    public void playTurn() {
        IOController io = IOController.getInstance();

        Set<PlayerAction> possibleActions = PlayerAction.getActions(false);
        Set<String> playableCards = getPlayableCards();

        if (playableCards.isEmpty())//no playable cards, remove play hunt option from user
            possibleActions.remove(PlayerAction.PLAY_HUNT);

        io.displayGameInfos();

        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" it's your turn.\n");

        //display list of his cards if hand is not null
        buildSetOfCards(sb, false);

        sb.append("\nChoose what to do from the options bellow:");
        io.printInfo(sb.toString());

        PlayerAction action = io.readFromSet(possibleActions);

        switch (action) {
            case ACCUSE -> {
                Set<String> possibleTargets = PlayerManager.getInstance().getUnrevealedPlayers();
                possibleTargets.remove(name);//player can't choose himself

                String target = choosePlayerFrom(possibleTargets);
                RoundManager.getInstance().accuse(name, target);
            }
            case PLAY_HUNT -> {
                io.printInfo(name.concat(" choose a card to play it's hunt effect from the list bellow."));
                String cardName = chooseCardFrom(playableCards);
                RumourCard card = CardManager.getInstance().getByName(cardName);

                card.playHuntEffect(name);
            }
        }
    }

    @Override
    public void respondAccusation(String accuser) {
        IOController io = IOController.getInstance();
        CardManager cManager = CardManager.getInstance();

        Set<String> playableCards = getPlayableCards(accuser);

        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" you've been accused by ");
        sb.append(accuser);
        sb.append(".\n");

        //display list of his cards if hand is not null
        buildSetOfCards(sb, true);


        if (playableCards.size() > 0) {//player has cards to play, he has a choice
            sb.append("\nChoose what to do from the options bellow:");
            io.printInfo(sb.toString());

            PlayerAction action = io.readFromSet(PlayerAction.getActions(true));

            switch (action) {
                case PLAY_WITCH -> {
                    io.printInfo(name.concat(" choose a card to play it's witch effect from the list bellow."));
                    String card = chooseCardFrom(playableCards);

                    cManager.getByName(card).playWitchEffect(name, accuser);
                }
                case REVEAL -> revealIdentity(accuser);
            }
        } else {//player has no cards, he is forced to reveal his identity
            sb.append("\nYou are forced to reveal because you can't play any card:");
            io.printInfo(sb.toString());

            revealIdentity(accuser);
        }
    }

    @Override
    public void chooseIdentity() {
        IOController io = IOController.getInstance();

        io.printInfo(name.concat(" choose your identity from the list bellow:"));
        Identity identity = io.readFromSet(EnumSet.allOf(Identity.class));
        setIdentity(identity);
    }

    @Override
    public boolean chooseToRevealOrDiscard() {
        IOController io = IOController.getInstance();

        io.printInfo(name.concat(" you have to choose to reveal your identity or discard a card."));
        String action = io.readFromSet(Set.of("Discard", "Reveal"));

        return action.equals("Reveal");
    }

    @Override
    public String chooseCardFrom(Set<String> listOfCardNames) {
        IOController io = IOController.getInstance();

        return io.readFromSet(listOfCardNames);
    }

    @Override
    public String choosePlayerFrom(Set<String> listOfPlayerNames) {
        IOController io = IOController.getInstance();

        io.printInfo(name.concat(" choose a player from the list bellow:"));
        return io.readFromSet(listOfPlayerNames);
    }

    private void buildSetOfCards(StringBuilder sb, boolean witchEffect) {
        Set<String> hand = getHand();

        if (!hand.isEmpty()) {
            CardManager cardManager = CardManager.getInstance();

            sb.append("List of your cards and their ");
            sb.append(witchEffect ? "witch" : "hunt");
            sb.append(" effect:\n");

            for (String cardName : hand) {
                sb.append("-");
                sb.append(cardName);
                sb.append(":\n");

                String lines;
                if (witchEffect)
                    lines = cardManager.getByName(cardName).witchEffectDescription();
                else
                    lines = cardManager.getByName(cardName).huntEffectDescription();

                for (String line : lines.split("\n")) {
                    sb.append("    ");
                    sb.append(line);
                    sb.append("\n");
                }
            }

            sb.append("\n");
        }
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

        public static Set<PlayerAction> getActions(boolean respondAction) {
            EnumSet<PlayerAction> actions = EnumSet.allOf(PlayerAction.class);

            actions.removeIf(pa -> respondAction != pa.isRespondAction());
            return actions;
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
