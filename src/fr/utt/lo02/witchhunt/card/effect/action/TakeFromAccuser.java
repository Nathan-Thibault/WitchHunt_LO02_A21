package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.HashMap;
import java.util.Objects;

public final class TakeFromAccuser extends Action {

    public TakeFromAccuser() {
        super("Take one card from the hand of\nthe player who accused you.");
    }

    @Override
    public boolean execute(String callerName, HashMap<String, Object> args) {
        PlayerManager pManager = PlayerManager.getInstance();
        Player caller = pManager.getByName(callerName);

        String accuserName = (String) Objects.requireNonNull(args.get("accuserName"), "TakeFromAccuser : missing argument accuserName");
        Player accuser = pManager.getByName(accuserName);

        if (accuser.getHand().isEmpty()) {
            return false;
        } else {
            String card = caller.chooseCardFrom(accuser.getHand());
            accuser.getOwnedCards().remove(card);
            caller.getOwnedCards().add(card);
            return true;
        }
    }

    @Override
    public String cantExecute() {
        return "Your accuser has not card in hand.";
    }
}
