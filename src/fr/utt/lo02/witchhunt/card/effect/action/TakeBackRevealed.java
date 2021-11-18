package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.ArrayList;
import java.util.HashMap;

public final class TakeBackRevealed extends Action {

    public TakeBackRevealed() {
        super("Take one of your own revealed\nRumour cards into your hand.");
    }

    @Override
    public void execute(String callerName, HashMap<String, Object> args) {
        CardManager cManager = CardManager.getInstance();
        Player caller = PlayerManager.getInstance().getByName(callerName);

        ArrayList<String> revealedCards = caller.getOwnedCards();
        revealedCards.removeAll(caller.getHand());

        String card = caller.chooseCardFrom(revealedCards);
        cManager.getByName(card).setRevealed(false);
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        Player caller = PlayerManager.getInstance().getByName(callerName);

        ArrayList<String> revealedCards = caller.getOwnedCards();
        revealedCards.removeAll(caller.getHand());

        return !revealedCards.isEmpty();
    }
}
