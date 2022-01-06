package fr.utt.lo02.witchhunt.card.effect.conditions;

import fr.utt.lo02.witchhunt.card.effect.Condition;
import fr.utt.lo02.witchhunt.player.Player;

/**
 * <b>RevealedARumourCard</b> represents the <i>"Only playable if you have
 * a revealed Rumour card."</i> condition on rumour cards.
 */
public final class RevealedARumourCard extends Condition {

    public RevealedARumourCard() {
        super("Only playable if you have\na revealed Rumour card.");
    }

    @Override
    public boolean verify(Player caller) {
        return caller.getOwnedCards().size() > caller.getHand().size();
    }
}
