package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.RoundManager;

import java.util.HashMap;

public final class TakeTurn extends Action {

    public TakeTurn() {
        super("Take next turn.");
    }

    @Override
    public boolean execute(String callerName, HashMap<String, Object> args) {
        RoundManager.getInstance().setIndexAtPlayer(callerName);
        return true;
    }

    @Override
    public String cantExecute() {
        return null;
    }
}
