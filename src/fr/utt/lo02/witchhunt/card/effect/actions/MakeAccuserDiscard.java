package fr.utt.lo02.witchhunt.card.effect.actions;

import fr.utt.lo02.witchhunt.Utils;
import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.managers.PlayerManager;

import java.util.HashMap;
import java.util.Objects;

/**
 * <b>MakeAccuserDiscard</b> represents the <i>"The player who accused you discards
 * a random card from their hand."</i> action on rumour cards.
 */
public final class MakeAccuserDiscard extends Action {

    public MakeAccuserDiscard() {
        super("The player who accused you discards\na random card from their hand.");
    }

    @Override
    public void execute(String callerName, HashMap<String, Object> args) {
        String accuserName = (String) Objects.requireNonNull(args.get("accuserName"), "MakeDiscard: missing accuserName argument");
        Player accuser = PlayerManager.getInstance().getByName(accuserName);

        //discard random card from accuser's hand
        String cardName = Utils.randomFromSet(accuser.getHand());
        accuser.removeFromOwnedCards(cardName);
        CardManager.getInstance().discard(cardName);
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        String accuserName = (String) Objects.requireNonNull(args.get("accuserName"), "MakeAccuserDiscard: missing accuserName argument");
        Player accuser = PlayerManager.getInstance().getByName(accuserName);

        return !accuser.getHand().isEmpty();
    }
}
