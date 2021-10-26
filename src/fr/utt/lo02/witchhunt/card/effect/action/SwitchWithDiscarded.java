package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.HashMap;

public final class SwitchWithDiscarded extends Action {

    public SwitchWithDiscarded() {
        super("Add one discarded card to your\nhand, and then discard this card.");
    }

    @Override
    public boolean execute(String callerName, HashMap<String, Object> args) {
        CardManager cManager = CardManager.getInstance();
        Player caller = PlayerManager.getInstance().getByName(callerName);

        if (cManager.getDiscardedCards().isEmpty())
            return false;

        String card = caller.chooseCardFrom(cManager.getDiscardedCards());

        cManager.takeFromDiscarded(card);
        caller.getHand().add(card);
        //only Back Cat card has this action, hence the following code
        caller.getHand().remove("Black Cat");
        cManager.discard("Black Cat");

        return true;
    }

    @Override
    public String cantExecute() {
        return "There is no discarded cards.";
    }
}
