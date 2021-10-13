package fr.utt.lo02.witchhunt.card.effect.condition;

import fr.utt.lo02.witchhunt.player.Player;

public abstract class Condition {

    protected String description;

    public Condition(String description){
        this.description = description;
    }

    public abstract boolean verify(Player caller);
}
