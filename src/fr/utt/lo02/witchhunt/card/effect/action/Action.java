package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.player.Player;

import java.util.HashMap;

public abstract class Action {

    protected String description;

    public Action(String description) {
        this.description = description;
    }

    public abstract boolean execute(Player caller, HashMap<String, Object> args);

    public abstract String cantExecute();
}
