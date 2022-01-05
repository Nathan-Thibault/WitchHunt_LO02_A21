package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.managers.RoundManager;
import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.card.RumourCard;
import fr.utt.lo02.witchhunt.io.IOController;
import fr.utt.lo02.witchhunt.managers.PlayerManager;

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

        io.playerInfos(name, "It's your turn.");

        PlayerAction action = io.readFromSet(possibleActions, "Choose what to do from the options bellow:");

        switch (action) {
            case ACCUSE -> {
                Set<String> possibleTargets = PlayerManager.getInstance().getUnrevealedPlayers();
                possibleTargets.remove(name);//player can't choose himself

                io.playerInfos(name, "Choose a player to accuse.");

                String target = choosePlayerFrom(possibleTargets);
                RoundManager.getInstance().accuse(name, target);
            }
            case PLAY_HUNT -> {
                io.playerInfos(name, "Choose a card to play it's hunt effect.");

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

        if (playableCards.size() > 0) {//player has cards to play, he has a choice
            io.playerInfos(name, "You have been accused by " + accuser + ".");

            PlayerAction action = io.readFromSet(PlayerAction.getActions(true), "Choose what to do from the options bellow:");

            switch (action) {
                case PLAY_WITCH -> {
                    io.playerInfos(name, "Choose a card to play it's witch effect.");

                    String card = chooseCardFrom(playableCards);

                    cManager.getByName(card).playWitchEffect(name, accuser);
                }
                case REVEAL -> revealIdentity(accuser);
            }
        } else {//player has no cards, he is forced to reveal his identity
            io.playerInfos(name, "You are forced to reveal because you can't play any card.");

            revealIdentity(accuser);
        }
    }

    @Override
    public void chooseIdentity() {
        IOController io = IOController.getInstance();

        io.playerInfos(name, "Choose your identity.");

        Identity identity = io.readFromSet(EnumSet.allOf(Identity.class), name.concat(" choose your identity from the list bellow:"));
        setIdentity(identity);
    }

    @Override
    public boolean chooseToRevealOrDiscard() {
        IOController io = IOController.getInstance();

        io.playerInfos(name, "You are forced to reveal your identity or discard a card.");

        String action = io.readFromSet(Set.of("Discard", "Reveal"), name.concat(" you have to choose to reveal your identity or discard a card."));

        return action.equals("Reveal");
    }

    @Override
    public String chooseCardFrom(Set<String> setOfCardNames) {
        return IOController.getInstance().readFromSet(setOfCardNames, name.concat(" choose a card from the list bellow:"));
    }

    @Override
    public String choosePlayerFrom(Set<String> setOfPlayerNames) {
        return IOController.getInstance().readFromSet(setOfPlayerNames, name.concat(" choose a player from the list bellow:"));
    }

    public void appendCards(StringBuilder sb) {
        Set<String> ownedCards = getOwnedCards();

        if (!ownedCards.isEmpty()) {
            CardManager cardManager = CardManager.getInstance();

            sb.append("List of your cards and their effects:\n");

            for (String cardName : ownedCards) {
                RumourCard card = cardManager.getByName(cardName);
                sb.append("- ");
                sb.append(cardName);
                sb.append(" (");
                sb.append(card.isRevealed() ? "revealed" : "in hand");
                sb.append("):\n");
                sb.append(" WITCH:\n");
                sb.append(card.witchEffectDescription());
                sb.append(" HUNT:\n");
                sb.append(card.huntEffectDescription());
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
