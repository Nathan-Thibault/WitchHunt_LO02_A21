package fr.utt.lo02.witchhunt.card;

import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.card.effect.EffectType;
import fr.utt.lo02.witchhunt.card.effect.actions.Action;
import fr.utt.lo02.witchhunt.card.effect.conditions.Condition;

import java.util.ArrayList;
import java.util.Objects;

/**
 * <b>RumourCardBuilder</b> is a temporary class allowing to build a {@link RumourCard}.
 */
public final class RumourCardBuilder {
    /**
     * Name of the {@link RumourCard} to be built.
     */
    private final String cardName;

    /**
     * List of actions to constitute the witch effect of the {@link RumourCard} to be built.
     */
    private final ArrayList<Action> witchActions = new ArrayList<>(2);
    /**
     * List of conditions to constitute the witch effect of the {@link RumourCard} to be built.
     */
    private ArrayList<Condition> witchConditions = new ArrayList<>(1);

    /**
     * List of actions to constitute the hunt effect of the {@link RumourCard} to be built.
     */
    private final ArrayList<Action> huntActions = new ArrayList<>(2);
    /**
     * List of conditions to constitute the hunt effect of the {@link RumourCard} to be built.
     */
    private ArrayList<Condition> huntConditions = new ArrayList<>(1);

    /**
     * Name of another {@link RumourCard} that will not be able to choose the one to be built when it will be revealed.
     */
    private String cantGetChosenBy = null;

    /**
     * Constructor.
     *
     * @param cardName name of the {@link RumourCard} to be built.
     * @throws NullPointerException if cardName is <code>null</code>.
     */
    public RumourCardBuilder(String cardName) throws NullPointerException {
        this.cardName = Objects.requireNonNull(cardName);
    }

    /**
     * Adds an action to the list of actions to constitute the witch effect of the {@link RumourCard} to be built.
     *
     * @param a the {@link Action} to be added
     */
    public void addWitchAction(Action a) {
        witchActions.add(a);
    }

    /**
     * Adds an action to the list of actions to constitute the hunt effect of the {@link RumourCard} to be built.
     *
     * @param a the {@link Action} to be added
     */
    public void addHuntAction(Action a) {
        huntActions.add(a);
    }

    /**
     * Adds a condition to the list of conditions to constitute the witch effect of the {@link RumourCard} to be built.
     *
     * @param c the {@link Condition} to be added
     */
    public void addWitchCondition(Condition c) {
        witchConditions.add(c);
    }

    /**
     * Adds a condition to the list of conditions to constitute the hunt effect of the {@link RumourCard} to be built.
     *
     * @param c the {@link Condition} to be added
     */
    public void addHuntCondition(Condition c) {
        huntConditions.add(c);
    }

    /**
     * Sets the name of another {@link RumourCard} that will not be able to
     * choose the one to be built when it will be revealed.
     *
     * @param otherCardName name of the other card
     */
    public void addCantGetChosenBy(String otherCardName) {
        cantGetChosenBy = otherCardName;
    }

    /**
     * Builds the {@link RumourCard} with the previously set properties.
     *
     * @return the {@link RumourCard}
     * @throws IllegalStateException if there is none action set for the witch or hunt effect
     */
    public RumourCard build() throws IllegalStateException {
        if (witchActions.isEmpty())
            throw new IllegalStateException("RumourCardBuilder: witch effect has no action yet.");
        if (witchConditions.isEmpty()) witchConditions = null;

        if (huntActions.isEmpty()) throw new IllegalStateException("RumourCardBuilder: hunt effect has no action yet.");
        if (huntConditions.isEmpty()) huntConditions = null;

        return new RumourCard(cardName, new CardEffect(EffectType.WITCH, witchActions, witchConditions, cardName), new CardEffect(EffectType.HUNT, huntActions, huntConditions, cardName), cantGetChosenBy);
    }
}
