package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.player.Player;

import java.util.HashMap;

public final class Discard extends Action {

    public Discard() {
        super("Discard a card from your hand.");
    }

    @Override
    public boolean execute(Player caller, HashMap<String, Object> args) {
        if (caller.getHand().isEmpty()) {
            return false;
        } else {
            String cardName = caller.chooseCardFrom(caller.getHand());
            caller.getOwnedCards().remove(cardName);
            CardManager.getInstance().discard(cardName);
            return true;
        }
    }

    @Override
    public String cantExecute() {
        return "You have no card in hand.";
    }
}
