package fr.utt.lo02.witchhunt.card.effect.actions;

import fr.utt.lo02.witchhunt.card.effect.Action;
import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.managers.PlayerManager;

import java.util.HashMap;

/**
 * <b>SwitchWithDiscarded</b> represents the <i>"Add one discarded card to your
 * hand, and then discard this card."</i> action on rumour cards.
 */
public final class SwitchWithDiscarded extends Action {

    public SwitchWithDiscarded() {
        super("Add one discarded card to your\nhand, and then discard this card.");
    }

    @Override
    public void execute(String callerName, HashMap<String, Object> args) {
        CardManager cManager = CardManager.getInstance();
        Player caller = PlayerManager.getInstance().getByName(callerName);

        String cardName = caller.chooseCardFrom(cManager.getDiscardedCards());

        cManager.takeFromDiscarded(cardName);
        caller.addToOwnedCards(cardName);
        //only Back Cat card has this action, hence the following code
        caller.removeFromOwnedCards("Black Cat");
        cManager.discard("Black Cat");
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        return !CardManager.getInstance().getDiscardedCards().isEmpty();
    }
}
