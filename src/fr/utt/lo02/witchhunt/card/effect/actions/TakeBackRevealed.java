package fr.utt.lo02.witchhunt.card.effect.actions;

import fr.utt.lo02.witchhunt.card.effect.Action;
import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.managers.PlayerManager;

import java.util.Set;
import java.util.HashMap;

/**
 * <b>TakeBackRevealed</b> represents the <i>"Take one of your own revealed
 * Rumour cards into your hand."</i> action on rumour cards.
 */
public final class TakeBackRevealed extends Action {

    public TakeBackRevealed() {
        super("Take one of your own revealed\nRumour cards into your hand.");
    }

    @Override
    public void execute(String callerName, HashMap<String, Object> args) {
        CardManager cManager = CardManager.getInstance();
        Player caller = PlayerManager.getInstance().getByName(callerName);

        Set<String> revealedCards = caller.getOwnedCards();
        revealedCards.removeAll(caller.getHand());

        String card = caller.chooseCardFrom(revealedCards);
        cManager.getByName(card).setRevealed(false);
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        Player caller = PlayerManager.getInstance().getByName(callerName);

        Set<String> revealedCards = caller.getOwnedCards();
        revealedCards.removeAll(caller.getHand());

        return !revealedCards.isEmpty();
    }
}
