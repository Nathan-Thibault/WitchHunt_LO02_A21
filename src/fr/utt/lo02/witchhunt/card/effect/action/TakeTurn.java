package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.RoundManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.HashMap;

public final class TakeTurn extends Action {

    public TakeTurn() {
        super("Take next turn.");
    }

    @Override
    public boolean execute(Player caller, HashMap<String, Object> args) {
        RoundManager.getInstance().setIndexAtPlayer(PlayerManager.getInstance().getByPlayer(caller));
        return true;
    }

    @Override
    public String cantExecute() {
        return null;
    }
}
