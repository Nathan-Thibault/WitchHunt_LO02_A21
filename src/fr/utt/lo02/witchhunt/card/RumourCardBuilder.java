package fr.utt.lo02.witchhunt.card;

import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.card.effect.EffectType;
import fr.utt.lo02.witchhunt.card.effect.actions.Action;
import fr.utt.lo02.witchhunt.card.effect.conditions.Condition;

import java.util.ArrayList;
import java.util.Objects;

public final class RumourCardBuilder {

    private final String cardName;

    private final ArrayList<Action> witchActions = new ArrayList<>(2);
    private ArrayList<Condition> witchConditions = new ArrayList<>(1);

    private final ArrayList<Action> huntActions = new ArrayList<>(2);
    private ArrayList<Condition> huntConditions = new ArrayList<>(1);

    private String cantBeChosenBy = null;

    public RumourCardBuilder(String cardName) throws NullPointerException {
        this.cardName = Objects.requireNonNull(cardName);
    }

    public void addWitchAction(Action a) {
        witchActions.add(a);
    }

    public void addHuntAction(Action a) {
        huntActions.add(a);
    }

    public void addWitchCondition(Condition c) {
        witchConditions.add(c);
    }

    public void addHuntCondition(Condition c) {
        huntConditions.add(c);
    }

    public void addCantBeChosenBy(String otherCardName) {
        cantBeChosenBy = otherCardName;
    }

    public RumourCard build() throws IllegalStateException {
        if (witchActions.isEmpty())
            throw new IllegalStateException("RumourCardBuilder: witch effect has no action yet.");
        if (witchConditions.isEmpty()) witchConditions = null;

        if (huntActions.isEmpty()) throw new IllegalStateException("RumourCardBuilder: hunt effect has no action yet.");
        if (huntConditions.isEmpty()) huntConditions = null;

        return new RumourCard(cardName, new CardEffect(EffectType.WITCH, witchActions, witchConditions), new CardEffect(EffectType.HUNT, huntActions, huntConditions), cantBeChosenBy);
    }
}
