package fr.utt.lo02.witchhunt.card.effect.condition;

import fr.utt.lo02.witchhunt.card.Card;
import fr.utt.lo02.witchhunt.player.Player;

public final class RevealedARumourCard extends Condition{

    public RevealedARumourCard(){
        super("Only playable if you have\na revealed Rumour card.");
    }

    @Override
    public boolean verify(Player caller) {
        for (Card card: caller.getHand()) {
            if (card.isRevealed()) return true;
        }
        return false;
    }
}
