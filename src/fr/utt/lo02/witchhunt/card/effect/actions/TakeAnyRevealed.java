package fr.utt.lo02.witchhunt.card.effect.actions;

import fr.utt.lo02.witchhunt.card.effect.Action;
import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.managers.PlayerManager;

import java.util.HashMap;
import java.util.Set;
import java.util.Objects;

/**
 * <b>TakeAnyRevealed</b> represents the <i>"Take a revealed Rumour card from
 * any other player into your hand."</i> action on rumour cards.
 */
public final class TakeAnyRevealed extends Action {

    public TakeAnyRevealed() {
        super("Take a revealed Rumour card from\nany other player into your hand.");
    }

    @Override
    public void execute(String callerName, HashMap<String, Object> args) {
        Player caller = PlayerManager.getInstance().getByName(callerName);
        Set<String> revealedCards = CardManager.getInstance().getRevealedNonDiscardedCards();

        //remove caller cards since he has to chose from another player
        for (String cardName : caller.getOwnedCards()) {
            revealedCards.remove(cardName);
        }

        String card = caller.chooseCardFrom(revealedCards);
        Player owner = Objects.requireNonNull(PlayerManager.getInstance().getOwnerOf(card));

        owner.removeFromOwnedCards(card);
        CardManager.getInstance().getByName(card).setRevealed(false);
        caller.addToOwnedCards(card);
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        Player caller = PlayerManager.getInstance().getByName(callerName);
        Set<String> revealedCards = CardManager.getInstance().getRevealedNonDiscardedCards();

        //remove caller cards since he has to chose from another player
        for (String card : caller.getOwnedCards()) {
            revealedCards.remove(card);
        }

        return !revealedCards.isEmpty();
    }
}
