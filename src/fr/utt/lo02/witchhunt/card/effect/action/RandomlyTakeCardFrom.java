package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.Utils;
import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.HashMap;
import java.util.Objects;

public final class RandomlyTakeCardFrom extends Action {

    public RandomlyTakeCardFrom() {
        super("Before their turn, take a random card\nfrom their hand and add it to your hand.");
    }

    @Override
    public boolean execute(Player caller, HashMap<String, Object> args) {
        CardEffect effect = (CardEffect) Objects.requireNonNull(args.get("effect"), "RandomlyTakeCardFrom : missing argument effect");
        Player target = PlayerManager.getInstance().getByName(effect.getTarget());

        String card = Utils.randomFromList(target.getHand());
        target.getOwnedCards().remove(card);
        caller.getOwnedCards().add(card);

        return true;
    }

    @Override
    public String cantExecute() {
        return null;
    }
}
