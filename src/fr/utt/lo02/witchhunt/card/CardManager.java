package fr.utt.lo02.witchhunt.card;

import com.sun.jdi.ClassNotPreparedException;
import fr.utt.lo02.witchhunt.Utils;
import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.card.effect.EffectType;
import fr.utt.lo02.witchhunt.card.effect.action.*;
import fr.utt.lo02.witchhunt.card.effect.condition.RevealedARumourCard;
import fr.utt.lo02.witchhunt.card.effect.condition.RevealedAsVillager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class CardManager {

    private static CardManager instance;

    private final HashMap<String, RumourCard> allRumourCards = new HashMap<>();
    private final ArrayList<String> discardedCards = new ArrayList<>();
    private ArrayList<String> cardsToDeal;
    private int numberOfCardsPerPlayer;

    private CardManager() {
        allRumourCards.put("Angry Mob", new RumourCard(
                new CardEffect(EffectType.WITCH, new TakeTurn()),
                new CardEffect(EffectType.HUNT, new RevealPlayer(), new RevealedAsVillager()), null));

        allRumourCards.put("Black Cat", new RumourCard(
                new CardEffect(EffectType.WITCH, new TakeTurn()),
                new CardEffect(EffectType.HUNT, new SwitchWithDiscarded(), new TakeTurn()), null));

        allRumourCards.put("Broomstick", new RumourCard(
                new CardEffect(EffectType.WITCH, new TakeTurn()),
                new CardEffect(EffectType.HUNT, new ChooseNextPlayer(null)), "Angry Mob"));

        allRumourCards.put("Cauldron", new RumourCard(
                new CardEffect(EffectType.WITCH, new MakeAccuserDiscard(), new TakeTurn()),
                new CardEffect(EffectType.HUNT, new Reveal()), null));

        allRumourCards.put("Ducking Stool", new RumourCard(
                new CardEffect(EffectType.WITCH, new ChooseNextPlayer(null)),
                new CardEffect(EffectType.HUNT, new MakeRevealOrDiscard()), null));

        allRumourCards.put("Evil Eye", new RumourCard(
                new CardEffect(EffectType.WITCH, new ChooseNextPlayer(null), new MustAccuse()),
                new CardEffect(EffectType.HUNT, new ChooseNextPlayer(null), new MustAccuse()), null));

        allRumourCards.put("Hooked Nose", new RumourCard(
                new CardEffect(EffectType.WITCH, new TakeFromAccuser(), new TakeTurn()),
                new CardEffect(EffectType.HUNT, new ChooseNextPlayer("cards"), new RandomlyTakeCardFrom()), null));

        allRumourCards.put("Pet Newt", new RumourCard(
                new CardEffect(EffectType.WITCH, new TakeTurn()),
                new CardEffect(EffectType.HUNT, new TakeAnyRevealed(), new ChooseNextPlayer(null)), null));

        allRumourCards.put("Pointed Hat", new RumourCard(
                new CardEffect(EffectType.WITCH, new TakeBackRevealed(), new TakeTurn(), new RevealedARumourCard()),
                new CardEffect(EffectType.HUNT, new TakeBackRevealed(), new ChooseNextPlayer(null), new RevealedARumourCard()), null));

        allRumourCards.put("The Inquisition", new RumourCard(
                new CardEffect(EffectType.WITCH, new Discard(), new TakeTurn()),
                new CardEffect(EffectType.HUNT, new ChooseNextPlayer("unrevealed"), new LookAtIdentity()), null));

        allRumourCards.put("Toad", new RumourCard(
                new CardEffect(EffectType.WITCH, new TakeTurn()),
                new CardEffect(EffectType.HUNT, new Reveal()), null));

        allRumourCards.put("Wart", new RumourCard(
                new CardEffect(EffectType.WITCH, new TakeTurn()),
                new CardEffect(EffectType.HUNT, new ChooseNextPlayer(null)), "Ducking Stool"));
    }

    public static CardManager getInstance() {
        if (instance == null) {
            instance = new CardManager();
        }
        return instance;
    }

    public RumourCard getByName(String name) {
        return allRumourCards.get(name);
    }

    public void discard(String name) {
        discardedCards.add(name);
        getByName(name).setRevealed(true);
    }

    public void takeFromDiscarded(String name) {
        discardedCards.remove(name);
        getByName(name).setRevealed(false);
    }

    public ArrayList<String> dealHand() {
        if (cardsToDeal == null)
            throw new ClassNotPreparedException("CardManager dealHand: deal system not reset, cannot deal hand");

        ArrayList<String> hand = new ArrayList<>();

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

    public void resetDealSystem() {
        cardsToDeal = new ArrayList<>(allRumourCards.keySet());

        //Discard cards until there is an integer amount of cards to deal per player
        int numberOfPlayers = 6;//TODO: change numberOfPlayers when RoundManager will be created
        while (cardsToDeal.size() % numberOfPlayers != 0) {
            String cardName = Utils.randomFromList(cardsToDeal);
            discard(cardName);
            cardsToDeal.remove(cardName);
        }
        numberOfCardsPerPlayer = cardsToDeal.size() / numberOfPlayers;
    }

    public ArrayList<String> getRevealedNonDiscardedCards() {
        ArrayList<String> revealedCards = new ArrayList<>();

        for (Map.Entry<String, RumourCard> entry : allRumourCards.entrySet()) {
            if (!discardedCards.contains(entry.getKey()) && entry.getValue().isRevealed())
                revealedCards.add(entry.getKey());
        }

        return revealedCards;
    }

    public ArrayList<String> getDiscardedCards() {
        return discardedCards;
    }
}
