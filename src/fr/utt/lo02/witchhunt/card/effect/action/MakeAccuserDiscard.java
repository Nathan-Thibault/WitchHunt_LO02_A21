package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.Utils;
import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.HashMap;
import java.util.Objects;

public final class MakeAccuserDiscard extends Action {

    public MakeAccuserDiscard() {
        super("The player who accused you discards\na random card from their hand.");
    }

    @Override
    public void execute(String callerName, HashMap<String, Object> args) {
        String accuserName = (String) Objects.requireNonNull(args.get("accuserName"), "MakeDiscard: missing accuserName argument");
        Player accuser = PlayerManager.getInstance().getByName(accuserName);

        //discard random card from accuser's hand
        String card = Utils.randomFromSet(accuser.getHand());
        accuser.getOwnedCards().remove(card);
        CardManager.getInstance().discard(card);
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        String accuserName = (String) Objects.requireNonNull(args.get("accuserName"), "MakeAccuserDiscard: missing accuserName argument");
        Player accuser = PlayerManager.getInstance().getByName(accuserName);

        return !accuser.getHand().isEmpty();
    }
}
