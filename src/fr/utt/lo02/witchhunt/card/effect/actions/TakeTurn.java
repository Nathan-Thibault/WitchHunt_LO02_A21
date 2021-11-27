package fr.utt.lo02.witchhunt.card.effect.actions;

import fr.utt.lo02.witchhunt.managers.RoundManager;

import java.util.HashMap;

public final class TakeTurn extends Action {

    public TakeTurn() {
        super("Take next turn.");
    }

    @Override
    public void execute(String callerName, HashMap<String, Object> args) {
        RoundManager.getInstance().setIndexAtPlayer(callerName);
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        return true;
    }
}
