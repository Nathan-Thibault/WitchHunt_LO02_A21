package fr.utt.lo02.witchhunt.card.effect.action;

import java.util.HashMap;

public abstract class Action {

    protected String description;

    public Action(String description) {
        this.description = description;
    }

    public abstract void execute(String callerName, HashMap<String, Object> args);

    public abstract boolean isExecutable(String callerName, HashMap<String, Object> args);
}
