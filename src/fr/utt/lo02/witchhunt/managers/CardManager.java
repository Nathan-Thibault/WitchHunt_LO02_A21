package fr.utt.lo02.witchhunt.managers;

import com.sun.jdi.ClassNotPreparedException;
import fr.utt.lo02.witchhunt.Utils;
import fr.utt.lo02.witchhunt.card.RumourCard;
import fr.utt.lo02.witchhunt.card.RumourCardBuilder;
import fr.utt.lo02.witchhunt.card.effect.actions.*;
import fr.utt.lo02.witchhunt.card.effect.conditions.Condition;
import fr.utt.lo02.witchhunt.card.effect.conditions.RevealedARumourCard;
import fr.utt.lo02.witchhunt.card.effect.conditions.RevealedAsVillager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * <b>CardManager</b> stores all the game cards and provides methods to manipulate them.
 * <p>
 * Cards ({@link RumourCard}) are stored in an HashMap with their name as key.
 * This allows to manipulate only strings for most of the operations affecting the cards
 * and only get the {@link RumourCard} when needed with its name.
 * <p>
 * Since all cards are created in the constructor of <b>CardManager</b>,
 * it is a singleton because they should not be duplicated.
 * Moreover, being a singleton allows access to it from anywhere in the project, which is needed.
 * <p>
 * <b>CardManager</b> provides, among others, methods to:
 * <ul>
 *     <li>get cards whether they are revealed or not, discarded or not, etc.</li>
 *     <li>discard and take discarded cards</li>
 *     <li>deal cards</li>
 * </ul>
 */
public final class CardManager {
    /**
     * Unique instance of <b>CardManager</b>
     */
    private static CardManager instance;

    /**
     * Map of all cards.
     * <p>
     * Has the name ({@link String}) as key of a {@link RumourCard} as value.
     */
    private final HashMap<String, RumourCard> allRumourCards = new HashMap<>();
    /**
     * Set containing the names of the discarded cards.
     */
    private HashSet<String> discardedCards;
    /**
     * Shuffled list containing the names of cards to be dealt.
     * <p>
     * It's first initialized in {@link CardManager#resetDealSystem()}
     * and then used in {@link CardManager#dealHand()}.
     */
    private ArrayList<String> cardsToDeal;
    /**
     * Number of cards to deal per player.
     * <p>
     * It's first initialized in {@link CardManager#resetDealSystem()}
     * and then used in {@link CardManager#dealHand()}.
     */
    private int numberOfCardsPerPlayer;
    /**
     * Indicates if the cards are created or not.
     * <p>
     * If <code>true</code>, they are created.
     */
    private boolean cardsCreated = false;

    /**
     * @see PropertyChangeSupport
     */
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Constructor to be called only once.
     */
    private CardManager() {
    }

    /**
     * Gets the unique instance of <b>CardManager</b>.
     * <p>
     * If the instance is null, this method creates it.
     *
     * @return the instance of <b>CardManager</b>
     */
    public static CardManager getInstance() {
        if (instance == null) {
            instance = new CardManager();
        }
        return instance;
    }

    /**
     * Builds all the cards and put them in {@link CardManager#allRumourCards}.
     */
    public void createCards() {
        if (cardsCreated)
            return;

        Action chooseNext = new ChooseNextPlayer(null);
        Action mustAccuse = new MustAccuse();
        Action takeBack = new TakeBackRevealed();
        Action takeTurn = new TakeTurn();

        Condition revealed = new RevealedAsVillager();
        Condition revealedCard = new RevealedARumourCard();

        RumourCardBuilder angryMob = new RumourCardBuilder("Angry Mob");
        angryMob.addWitchAction(takeTurn);
        angryMob.addHuntAction(new RevealPlayer());
        angryMob.addHuntCondition(revealed);
        allRumourCards.put("Angry Mob", angryMob.build());

        RumourCardBuilder blackCat = new RumourCardBuilder("Black Cat");
        blackCat.addWitchAction(takeTurn);
        blackCat.addHuntAction(new SwitchWithDiscarded());
        blackCat.addHuntAction(takeTurn);
        allRumourCards.put("Black Cat", blackCat.build());

        RumourCardBuilder broomstick = new RumourCardBuilder("Broomstick");
        broomstick.addWitchAction(takeTurn);
        broomstick.addHuntAction(chooseNext);
        broomstick.addCantGetChosenBy("Angry Mob");
        allRumourCards.put("Broomstick", broomstick.build());

        RumourCardBuilder cauldron = new RumourCardBuilder("Cauldron");
        cauldron.addWitchAction(new MakeAccuserDiscard());
        cauldron.addWitchAction(takeTurn);
        cauldron.addHuntAction(new Reveal());
        allRumourCards.put("Cauldron", cauldron.build());

        RumourCardBuilder duckingStool = new RumourCardBuilder("Ducking Stool");
        duckingStool.addWitchAction(chooseNext);
        duckingStool.addHuntAction(new MakeRevealOrDiscard());
        allRumourCards.put("Ducking Stool", duckingStool.build());

        RumourCardBuilder evilEye = new RumourCardBuilder("Evil Eye");
        evilEye.addWitchAction(chooseNext);
        evilEye.addWitchAction(mustAccuse);
        evilEye.addHuntAction(chooseNext);
        evilEye.addHuntAction(mustAccuse);
        allRumourCards.put("Evil Eye", evilEye.build());

        RumourCardBuilder hookedNose = new RumourCardBuilder("Hooked Nose");
        hookedNose.addWitchAction(new TakeFromAccuser());
        hookedNose.addWitchAction(takeTurn);
        hookedNose.addHuntAction(new ChooseNextPlayer("cards"));
        hookedNose.addHuntAction(new RandomlyTakeCardFrom());
        allRumourCards.put("Hooked Nose", hookedNose.build());

        RumourCardBuilder petNewt = new RumourCardBuilder("Pet Newt");
        petNewt.addWitchAction(takeTurn);
        petNewt.addHuntAction(new TakeAnyRevealed());
        petNewt.addHuntAction(chooseNext);
        allRumourCards.put("Pet Newt", petNewt.build());

        RumourCardBuilder pointedHat = new RumourCardBuilder("Pointed Hat");
        pointedHat.addWitchAction(takeBack);
        pointedHat.addWitchAction(takeTurn);
        pointedHat.addWitchCondition(revealedCard);
        pointedHat.addHuntAction(takeBack);
        pointedHat.addHuntAction(chooseNext);
        pointedHat.addHuntCondition(revealedCard);
        allRumourCards.put("Pointed Hat", pointedHat.build());

        RumourCardBuilder theInquisition = new RumourCardBuilder("The Inquisition");
        theInquisition.addWitchAction(new Discard());
        theInquisition.addWitchAction(takeTurn);
        theInquisition.addHuntAction(new ChooseNextPlayer("unrevealed"));
        theInquisition.addHuntAction(new LookAtIdentity());
        theInquisition.addHuntCondition(revealed);
        allRumourCards.put("The Inquisition", theInquisition.build());

        RumourCardBuilder toad = new RumourCardBuilder("Toad");
        toad.addWitchAction(takeTurn);
        toad.addHuntAction(new Reveal());
        allRumourCards.put("Toad", toad.build());

        RumourCardBuilder wart = new RumourCardBuilder("Wart");
        wart.addWitchAction(takeTurn);
        wart.addHuntAction(chooseNext);
        wart.addCantGetChosenBy("Ducking Stool");
        allRumourCards.put("Wart", wart.build());

        cardsCreated = true;
        pcs.firePropertyChange("cardsCreated", false, true);
    }

    /**
     * Gets a {@link HashSet} containing all cards name.
     *
     * @return the set containing all cards name.
     */
    public HashSet<String> getAll() {
        return new HashSet<>(allRumourCards.keySet());
    }

    /**
     * Gets a {@link RumourCard} with its name.
     *
     * @param name the name of the card wanted
     * @return the card, or <code>null</code> if the name given does not exist
     */
    public RumourCard getByName(String name) {
        return allRumourCards.get(name);
    }

    /**
     * Sets a card as revealed and discarded.
     *
     * @param name the name of the card to discard
     */
    public void discard(String name) {
        HashSet<String> discardedCards = getDiscardedCards();
        discardedCards.add(name);
        setDiscardedCards(discardedCards);

        getByName(name).setRevealed(true);
    }

    /**
     * Sets a card as not revealed and not discarded.
     *
     * @param name the name of the card to un-discard
     */
    public void takeFromDiscarded(String name) {
        HashSet<String> discardedCards = getDiscardedCards();
        discardedCards.remove(name);
        setDiscardedCards(discardedCards);

        getByName(name).setRevealed(false);
    }

    /**
     * Gets a hand of cards to give to a {@link fr.utt.lo02.witchhunt.player.Player} at the start of a round.
     * <p>
     * Randomly gets the right amount of cards to deal from the list of not yet dealt cards.
     * Must not be called before {@link CardManager#resetDealSystem()} as the amount of cards to deal
     * and the list of cards not yet dealt are defined by it.
     *
     * @return a set of card names
     * @throws ClassNotPreparedException if called before {@link CardManager#resetDealSystem()}
     */
    public HashSet<String> dealHand() throws ClassNotPreparedException {
        if (cardsToDeal == null)
            throw new ClassNotPreparedException("CardManager dealHand: deal system not reset, cannot deal hand");

        HashSet<String> hand = new HashSet<>();

        //While there isn't enough cards in hand, take a random card from cardsToDeal and add it to the hand
        while (hand.size() < numberOfCardsPerPlayer) {
            String cardName = Utils.randomFromList(cardsToDeal);
            hand.add(cardName);
            cardsToDeal.remove(cardName);
        }

        //All cards have been dealt
        if (cardsToDeal.size() == 0) {
            cardsToDeal = null;
            numberOfCardsPerPlayer = 0;
        }

        return hand;
    }

    /**
     * Prepare the class to deal the hands of cards to players.
     * <p>
     * This method has to be called before {@link CardManager#dealHand()}.
     * <p>
     * It first sets the list cards not yet dealt. Then, discard some
     * cards to have an integer amount of cards to deal per player.
     * And finally, sets the amount of cards to deal per player.
     */
    public void resetDealSystem() {
        setDiscardedCards(new HashSet<>());//remove all discarded cards

        cardsToDeal = new ArrayList<>(allRumourCards.keySet());
        Collections.shuffle(cardsToDeal);

        for (String card : cardsToDeal)//un-reveal every card
            getByName(card).setRevealed(false);

        //Discard cards until there is an integer amount of cards to deal per player
        int numberOfPlayers = PlayerManager.getInstance().getAllPlayers().size();
        while (cardsToDeal.size() % numberOfPlayers != 0) {
            String cardName = Utils.randomFromList(cardsToDeal);
            discard(cardName);
            cardsToDeal.remove(cardName);
        }
        numberOfCardsPerPlayer = cardsToDeal.size() / numberOfPlayers;
    }

    /**
     * Gets the cards that are revealed but not discarded.
     *
     * @return a set of card names
     */
    public HashSet<String> getRevealedNonDiscardedCards() {
        HashSet<String> revealedCards = new HashSet<>();

        for (Map.Entry<String, RumourCard> entry : allRumourCards.entrySet()) {
            if (!discardedCards.contains(entry.getKey()) && entry.getValue().isRevealed())
                revealedCards.add(entry.getKey());
        }

        return revealedCards;
    }

    /**
     * Gets the discarded cards.
     *
     * @return a set of card names
     */
    public HashSet<String> getDiscardedCards() {
        return new HashSet<>(discardedCards);
    }

    /**
     * Sets the discarded cards set as the specified set.
     *
     * @param newDiscardedCards the new set containing the names of discarded cards
     */
    private void setDiscardedCards(HashSet<String> newDiscardedCards) {
        HashSet<String> oldDiscardedCards = discardedCards;
        discardedCards = newDiscardedCards;
        pcs.firePropertyChange("discardedCards", oldDiscardedCards, discardedCards);
    }

    /**
     * @see PropertyChangeSupport#addPropertyChangeListener(PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
}
