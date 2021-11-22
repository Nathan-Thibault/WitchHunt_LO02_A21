package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.HashMap;
import java.util.Set;
import java.util.Objects;

public final class TakeAnyRevealed extends Action {

    public TakeAnyRevealed() {
        super("Take a revealed Rumour card from\nany other player into your hand.");
    }

    @Override
    public void execute(String callerName, HashMap<String, Object> args) {
        Player caller = PlayerManager.getInstance().getByName(callerName);
        Set<String> revealedCards = CardManager.getInstance().getRevealedNonDiscardedCards();

        //remove caller cards since he has to chose from another player
        for (String card : caller.getOwnedCards()) {
            revealedCards.remove(card);
        }

        String card = caller.chooseCardFrom(revealedCards);
        Player owner = Objects.requireNonNull(PlayerManager.getInstance().getOwnerOf(card));

        owner.getOwnedCards().remove(card);
        caller.getOwnedCards().add(card);
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
