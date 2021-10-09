package fr.utt.lo02.witchhunt.card.effect;

public abstract class Action {

    protected String description;

    public Action(String description){
        this.description = description;
    }

    public abstract void execute();
}
