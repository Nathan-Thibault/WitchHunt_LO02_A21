package fr.utt.lo02.witchhunt.card;

import java.util.ArrayList;
import java.util.HashMap;

public final class CardManager {

    private static CardManager instance;
    private final HashMap<String, RumourCard> rumourCards = new HashMap<>();
    private final ArrayList<String> discardedCards = new ArrayList<>();

    private CardManager() {
        //TODO: create all rumour cards
//        rumourCards.put("", new RumourCard(
//                new CardEffect(EffectType.WITCH, new ArrayList<Action>(), null),
//                new CardEffect(EffectType.HUNT, new ArrayList<Action>(), null), null));
    }

    public static CardManager getInstance() {
        if (instance == null) {
            instance = new CardManager();
        }
        return instance;
    }

    public RumourCard getByName(String name) {
        return rumourCards.get(name);
    }

    public void discard(String name){
        discardedCards.add(name);
    }
}
