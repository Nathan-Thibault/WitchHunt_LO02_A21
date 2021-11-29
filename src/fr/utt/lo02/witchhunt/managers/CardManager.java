package fr.utt.lo02.witchhunt.managers;

import com.sun.jdi.ClassNotPreparedException;
import fr.utt.lo02.witchhunt.Utils;
import fr.utt.lo02.witchhunt.card.RumourCard;
import fr.utt.lo02.witchhunt.card.RumourCardBuilder;
import fr.utt.lo02.witchhunt.card.effect.actions.*;
import fr.utt.lo02.witchhunt.card.effect.conditions.Condition;
import fr.utt.lo02.witchhunt.card.effect.conditions.RevealedARumourCard;
import fr.utt.lo02.witchhunt.card.effect.conditions.RevealedAsVillager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;

public final class CardManager {

    private static CardManager instance;

    private final HashMap<String, RumourCard> allRumourCards = new HashMap<>();
    private final HashSet<String> discardedCards = new HashSet<>();
    private ArrayList<String> cardsToDeal;
    private int numberOfCardsPerPlayer;

    private CardManager() {
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

    public HashSet<String> dealHand() {
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

    public void resetDealSystem() {
        discardedCards.clear();//remove all discarded cards

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

    public HashSet<String> getRevealedNonDiscardedCards() {
        HashSet<String> revealedCards = new HashSet<>();

        for (Map.Entry<String, RumourCard> entry : allRumourCards.entrySet()) {
            if (!discardedCards.contains(entry.getKey()) && entry.getValue().isRevealed())
                revealedCards.add(entry.getKey());
        }

        return revealedCards;
    }

    public HashSet<String> getDiscardedCards() {
        return discardedCards;
    }
}
