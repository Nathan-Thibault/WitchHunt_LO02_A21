package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.Utils;
import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public final class MakeDiscard extends Action {

    public MakeDiscard() {
        super("The player who accused you discards\na random card from their hand.");
    }

    @Override
    public boolean execute(Player caller, HashMap<String, Object> args) {
        String accuserName = (String) Objects.requireNonNull(args.get("accuserName"), "CardEffect play: accuserName cannot be null to execute action ");
        Player accuser = PlayerManager.getInstance().getByName(accuserName);

        //discard random card from accuser's hand
        String card = Utils.randomFromList(accuser.getHand());
        accuser.getOwnedCards().remove(card);
        CardManager.getInstance().discard(card);
        return true;
    }

    @Override
    public String cantExecute() {
        return "There is no players with cards in hand to choose.";
    }
}
