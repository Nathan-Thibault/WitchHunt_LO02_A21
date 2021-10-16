package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.player.Player;

public abstract class Action {

    protected String description;

    public Action(String description){
        this.description = description;
    }

    public abstract boolean execute(Player caller, CardEffect effect);

    public abstract String cantExecute();
}
