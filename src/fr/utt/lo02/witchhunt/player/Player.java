package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.card.Card;
import fr.utt.lo02.witchhunt.managers.RoundManager;
import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.card.IdentityCard;
import fr.utt.lo02.witchhunt.card.RumourCard;
import fr.utt.lo02.witchhunt.card.effect.EffectType;
import fr.utt.lo02.witchhunt.io.IOController;
import fr.utt.lo02.witchhunt.managers.PlayerManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class represents a player in game.
 * <p>
 * Each player has a unique name by which he is identified.
 * A player can:
 * <ul>
 *     <li>choose a new {@link Identity} each round. This identity is represented by his {@link IdentityCard}.</li>
 *     <li>posses a set of {@link RumourCard}</li>
 *     <li>score points</li>
 *     <li>play a turn</li>
 *     <li>respond an accusation</li>
 * </ul>
 * <p>
 * This class defines the methods that should be implemented to make a working player.
 * And implements other methods that are common to all players.
 */
public abstract class Player {
    /**
     * Name of the <b>Player</b>.
     */
    protected final String name;
    /**
     * Represents the identity of the player.
     */
    protected IdentityCard identityCard;
    /**
     * Saves the points marked by the player.
     */
    private int score;
    /**
     * Set of card names the player has possession of.
     */
    private HashSet<String> ownedCards;
    /**
     * @see PropertyChangeSupport
     */
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Constructor for subclasses to call.
     *
     * @param name unique name representing the player
     * @throws NullPointerException if the name is <code>null</code>
     */
    public Player(String name) throws NullPointerException {
        this.name = Objects.requireNonNull(name, "Player constructor: name can't be null.");

        identityCard = null;
        score = 0;
    }

    /**
     * Makes the <b>Player</b> play his turn.
     */
    public abstract void playTurn();

    /**
     * Makes the <b>Player</b> respond the accusation by the specified accuser.
     *
     * @param accuser name of the player accusing this one
     */
    public abstract void respondAccusation(String accuser);

    /**
     * Makes the <b>Player</b> choose an {@link Identity}.
     */
    public abstract void chooseIdentity();

    /**
     * Makes the <b>Player</b> choose to reveal is identity or discard one of his cards.
     *
     * @return <code>true</code> to reveal and <code>false</code> to discard
     */
    public abstract boolean chooseToRevealOrDiscard();

    /**
     * Makes the <b>Player</b> choose a card from the given set.
     *
     * @param listOfCardNames set of card names to choose from
     * @return the name chosen
     */
    public abstract String chooseCardFrom(Set<String> listOfCardNames);

    /**
     * Makes the <b>Player</b> choose a player from the given set.
     *
     * @param listOfPlayerNames set of player names to choose from
     * @return the name chosen
     */
    public abstract String choosePlayerFrom(Set<String> listOfPlayerNames);

    /**
     * Reveals the {@link IdentityCard} of the <b>Player</b>
     * and shows a message to tell his {@link Identity} if specified.
     *
     * @param showMessage <code>true</code> to show the message, <code>false</code> to only reveal the identity card
     */
    public void revealIdentity(boolean showMessage) {
        identityCard.setRevealed(true);

        if (identityCard.getIdentity() == Identity.WITCH)
            PlayerManager.getInstance().eliminate(name);

        if (showMessage)
            IOController.getInstance().pause(name + " is a " + identityCard.getIdentity() + ".");
    }

    /**
     * Reveals the <b>Player</b> following an accusation by the specified accuser.
     * <p>
     * First reveals its {@link IdentityCard}.
     * Then shows a message, give points and makes the next player play
     * according to the {@link Identity} of the player.
     *
     * @param accuser name of the player who accused this one
     */
    public void revealIdentity(String accuser) {
        RoundManager rManager = RoundManager.getInstance();
        PlayerManager pManager = PlayerManager.getInstance();
        IOController io = IOController.getInstance();

        identityCard.setRevealed(true);

        StringBuilder sb = new StringBuilder();
        sb.append(name);

        if (identityCard.getIdentity() == Identity.WITCH) {
            pManager.getByName(accuser).addToScore(1);
            pManager.eliminate(name);

            sb.append(" was a Witch!\n");
            sb.append(name);
            sb.append(" is out of the game until the end of the round.\n");
            sb.append(accuser);
            sb.append(" gains one point and takes another turn.");

            rManager.setIndexAtPlayer(accuser);
        } else {
            sb.append(" is a Villager.\n");
            sb.append(accuser);
            sb.append(" gains no point and ");
            sb.append(name);
            sb.append(" takes next turn.");

            rManager.setIndexAtPlayer(name);
        }

        io.pause(sb.toString());
        rManager.next();
    }

    /**
     * Gets the non-revealed cards owned by the <b>Player</b>.
     *
     * @return a set of card names
     * @see Player#getRevealedCards(boolean)
     */
    public HashSet<String> getHand() {
        return getRevealedCards(false);
    }

    /**
     * Gets the revealed cards owned by the <b>Player</b>.
     *
     * @return a set of card names
     * @see Player#getRevealedCards(boolean)
     */
    public HashSet<String> getRevealedCards() {
        return getRevealedCards(true);
    }

    /**
     * Gets the cards either revealed or not owned by the <b>Player</b>.
     *
     * @param revealed <code>true</code> to get revealed cards, <code>false</code> to get non-revealed cards
     * @return a set of card names
     * @see Card#isRevealed()
     */
    private HashSet<String> getRevealedCards(boolean revealed) {
        CardManager cManager = CardManager.getInstance();

        HashSet<String> cards = new HashSet<>();
        for (String card : ownedCards) {
            boolean cr = cManager.getByName(card).isRevealed();
            //card revealed or not depending on if we want it to be or not
            if (!(cr || revealed) || (cr && revealed))
                cards.add(card);
        }

        return cards;
    }

    /**
     * Resets the <b>Player</b> for a new round.
     * <p>
     * Sets his identity card to <code>null</code> and gets him new hand.
     *
     * @see CardManager#dealHand()
     */
    public void reset() {
        setIdentityCard(null);
        setOwnedCards(CardManager.getInstance().dealHand());
    }

    /**
     * Gets the cards of the hand of the <b>Player</b> that can be played for his turn.
     *
     * @return a set of card names whose hunt effect can be played
     */
    public HashSet<String> getPlayableCards() {
        return getPlayableCards(EffectType.HUNT, null);
    }

    /**
     * Gets the cards of the hand of the <b>Player</b> that can be played after being accused by the specified accuser.
     *
     * @param accuser name of the player accusing this one
     * @return a set of card names whose which effect can be played
     */
    public HashSet<String> getPlayableCards(String accuser) {
        return getPlayableCards(EffectType.WITCH, accuser);
    }

    /**
     * Gets the cards of the hand of the <b>Player</b> that can be played at the moment this method is called.
     *
     * @param type    type of the effect to use
     * @param accuser name of the player who is accusing this one, if there is one (it may be <code>null</code>)
     * @return a set of card names
     * @see Player#getHand()
     * @see fr.utt.lo02.witchhunt.card.effect.CardEffect#isPlayable(String)
     * @see fr.utt.lo02.witchhunt.card.effect.CardEffect#isPlayable(String, String)
     */
    private HashSet<String> getPlayableCards(EffectType type, String accuser) {
        CardManager cManager = CardManager.getInstance();
        HashSet<String> cards = new HashSet<>();

        for (String cardName : getHand()) {
            RumourCard card = cManager.getByName(cardName);

            boolean playable = switch (type) {
                case HUNT -> card.canPlayHuntEffect(name);
                case WITCH -> card.canPlayWitchEffect(name, accuser);
            };

            if (playable) cards.add(cardName);
        }

        return cards;
    }

    /**
     * Removes a card from the set of owned cards of this <b>Player</b>.
     *
     * @param cardName name of the card to remove
     */
    public void removeFromOwnedCards(String cardName) {
        HashSet<String> newOwnedCards = getOwnedCards();
        newOwnedCards.remove(cardName);
        setOwnedCards(newOwnedCards);
    }

    /**
     * Adds a card from the set of owned cards of this <b>Player</b>.
     *
     * @param cardName name of the card to add
     */
    public void addToOwnedCards(String cardName) {
        HashSet<String> newOwnedCards = getOwnedCards();
        newOwnedCards.add(cardName);
        setOwnedCards(newOwnedCards);
    }

    /**
     * Gets the cards owned by the <b>Player</b>.
     *
     * @return the set of card names owned by the player
     */
    public HashSet<String> getOwnedCards() {
        return new HashSet<>(ownedCards);
    }

    /**
     * Sets the cards owned by the <b>Player</b>.
     *
     * @param newOwnedCards the new set of card names owned by the player
     */
    private void setOwnedCards(HashSet<String> newOwnedCards) {
        HashSet<String> oldOwnedCards = ownedCards;
        ownedCards = newOwnedCards;
        pcs.firePropertyChange("ownedCards", oldOwnedCards, ownedCards);
    }

    /**
     * Adds the specified amount to the <b>Player</b>'s score.
     *
     * @param addend the amount to add to the score
     */
    public void addToScore(int addend) {
        setScore(score + addend);
    }

    /**
     * Gets the score of the <b>Player</b>.
     *
     * @return the player's score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score of the <b>Player</b>.
     *
     * @param newScore the player's new score
     */
    private void setScore(int newScore) {
        int oldScore = score;
        score = newScore;
        pcs.firePropertyChange("score", oldScore, score);
    }

    /**
     * Gets the {@link IdentityCard} of the <b>Player</b>.
     *
     * @return the player's identity card or <code>null</code> if the player hasn't chosen his identity yet
     */
    public IdentityCard getIdentityCard() {
        return identityCard;
    }

    /**
     * Sets the {@link IdentityCard} of the <b>Player</b>.
     *
     * @param newIdentityCard the player's new identity card
     */
    private void setIdentityCard(IdentityCard newIdentityCard) {
        IdentityCard oldIdentityCard = identityCard;
        identityCard = newIdentityCard;
        pcs.firePropertyChange("identityCard", oldIdentityCard, identityCard);
    }

    /**
     * Sets the {@link Identity} of the <b>Player</b>.
     *
     * @param identity the player's new identity
     */
    public void setIdentity(Identity identity) {
        setIdentityCard(new IdentityCard(identity));
    }

    /**
     * Gets the name of the <b>Player</b>.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * @see PropertyChangeSupport#addPropertyChangeListener(PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
}
