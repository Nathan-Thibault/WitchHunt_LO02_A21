package fr.utt.lo02.witchhunt.card.effect;

import java.util.ArrayList;
import java.util.Objects;

public final class CardEffect {

    private final EffectType type;
    private final ArrayList<Action> actions;
    private final ArrayList<Condition> conditions;

    public CardEffect(EffectType type, ArrayList<Action> actions, ArrayList<Condition> conditions){
        this.type = Objects.requireNonNull(type, "CardEffect constructor: type can't be null.");

        int MIN_NUMBER_OF_ACTIONS = 1;
        int MAX_NUMBER_OF_ACTIONS = 2;
        if (actions == null){
            throw new NullPointerException("CardEffect constructor: actions can't be null");
        } else if (actions.size() > MAX_NUMBER_OF_ACTIONS || actions.size() < MIN_NUMBER_OF_ACTIONS){
            throw new IllegalArgumentException("CardEffect constructor: actions must contain at least " + MIN_NUMBER_OF_ACTIONS + " and at most " + MAX_NUMBER_OF_ACTIONS + " action(s).");
        } else {
            this.actions = actions;
        }

        int MIN_NUMBER_OF_CONDITIONS = 0;
        int MAX_NUMBER_OF_CONDITIONS = 1;
        if (conditions == null){
            throw new NullPointerException("CardEffect constructor: actions can't be null");
        } else if (conditions.size() > MAX_NUMBER_OF_CONDITIONS || conditions.size() < MIN_NUMBER_OF_CONDITIONS){
            throw new IllegalArgumentException("CardEffect constructor: conditions must contain at least " + MIN_NUMBER_OF_CONDITIONS + " and at most " + MAX_NUMBER_OF_CONDITIONS + " condition(s).");
        } else {
            this.conditions = conditions;
        }
    }

    public void play(){
        //TODO
    }

    public EffectType getType(){
        return this.type;
    }
}
