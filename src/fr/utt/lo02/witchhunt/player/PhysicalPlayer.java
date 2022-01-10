package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.managers.RoundManager;
import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.card.RumourCard;
import fr.utt.lo02.witchhunt.io.IOController;
import fr.utt.lo02.witchhunt.managers.PlayerManager;

import java.util.EnumSet;
import java.util.Set;

/**
 * A <b>PhysicalPlayer</b> is a {@link Player} that acts based on user input.
 * <p>
 * This class basically only just implements abstract methods of {@link Player}.
 * Each works approximately the same way, which is,
 * building a list of actions the player can do and make the user choose one.
 */
public final class PhysicalPlayer extends Player {
    /**
     * Constructor.
     *
     * @param name unique name representing the player
     */
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

    /**
     * Appends to the specified {@link StringBuilder} the list of the cards
     * and their full description owned by the <b>PhysicalPlayer</b>.
     *
     * @param sb the string builder to append to
     */
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

    /**
     * Actions the user must choose to take during a turn or during responding an accusation.
     * <p>
     * A <b>PlayerAction</b> as a description and is either a turn action or a response action.
     * So this enumeration provides a method to get all the turn or response actions.
     */
    private enum PlayerAction {
        ACCUSE("Accuse another player", false),
        PLAY_HUNT("Play a hunt effect", false),
        PLAY_WITCH("Play a witch effect", true),
        REVEAL("Reveal your identity", true);

        private final String description;
        private final boolean responseAction;

        PlayerAction(String description, boolean responseAction) {
            this.description = description;
            this.responseAction = responseAction;
        }

        public static Set<PlayerAction> getActions(boolean respondAction) {
            EnumSet<PlayerAction> actions = EnumSet.allOf(PlayerAction.class);

            actions.removeIf(pa -> respondAction != pa.isResponseAction());
            return actions;
        }

        public boolean isResponseAction() {
            return responseAction;
        }

        @Override
        public String toString() {
            return description;
        }
    }
}
