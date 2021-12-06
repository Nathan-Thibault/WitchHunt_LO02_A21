package fr.utt.lo02.witchhunt.card.effect.actions;

import fr.utt.lo02.witchhunt.io.IOController;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.managers.PlayerManager;

import java.util.HashMap;
import java.util.Objects;

public final class TakeFromAccuser extends Action {

    public TakeFromAccuser() {
        super("Take one card from the hand of\nthe player who accused you.");
    }

    @Override
    public void execute(String callerName, HashMap<String, Object> args) {
        PlayerManager pManager = PlayerManager.getInstance();

        Player caller = pManager.getByName(callerName);
        String accuserName = (String) Objects.requireNonNull(args.get("accuserName"), "TakeFromAccuser : missing argument accuserName");
        Player accuser = pManager.getByName(accuserName);

        IOController.getInstance().printInfo(callerName + " choose a card from the hand of " + accuserName + ".");
        String card = caller.chooseCardFrom(accuser.getHand());
        accuser.getOwnedCards().remove(card);
        caller.getOwnedCards().add(card);
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        String accuserName = (String) Objects.requireNonNull(args.get("accuserName"), "TakeFromAccuser : missing argument accuserName");
        Player accuser = PlayerManager.getInstance().getByName(accuserName);

        return !accuser.getHand().isEmpty();
    }
}
