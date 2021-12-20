package fr.utt.lo02.witchhunt.card.effect.actions;

import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.managers.PlayerManager;

import java.util.HashMap;

/**
 * <b>Discard</b> represents the <i>"Discard a card from your hand."</i> action on rumour cards.
 */
public final class Discard extends Action {

    public Discard() {
        super("Discard a card from your hand.");
    }

    @Override
    public void execute(String callerName, HashMap<String, Object> args) {
        Player caller = PlayerManager.getInstance().getByName(callerName);

        String cardName = caller.chooseCardFrom(caller.getHand());
        caller.getOwnedCards().remove(cardName);
        CardManager.getInstance().discard(cardName);
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        return !PlayerManager.getInstance().getByName(callerName).getHand().isEmpty();
    }
}
