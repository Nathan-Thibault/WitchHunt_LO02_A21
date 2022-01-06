package fr.utt.lo02.witchhunt.card.effect.actions;

import fr.utt.lo02.witchhunt.Utils;
import fr.utt.lo02.witchhunt.card.effect.Action;
import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.managers.PlayerManager;

import java.util.HashMap;
import java.util.Objects;

/**
 * <b>RandomlyTakeCardFrom</b> represents the <i>"Before their turn, take a random card
 * from their hand and add it to your hand."</i> action on rumour cards.
 */
public final class RandomlyTakeCardFrom extends Action {

    public RandomlyTakeCardFrom() {
        super("Before their turn, take a random card\nfrom their hand and add it to your hand.");
    }

    @Override
    public void execute(String callerName, HashMap<String, Object> args) {
        PlayerManager pManager = PlayerManager.getInstance();
        Player caller = pManager.getByName(callerName);

        CardEffect effect = (CardEffect) Objects.requireNonNull(args.get("effect"), "RandomlyTakeCardFrom : missing argument effect");
        Player target = pManager.getByName(Objects.requireNonNull(effect.getTarget(), "RandomlyTakeCardFrom : target is can't be null"));

        String cardName = Utils.randomFromSet(target.getHand());
        target.removeFromOwnedCards(cardName);
        caller.addToOwnedCards(cardName);
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        return true;
    }
}
