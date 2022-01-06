package fr.utt.lo02.witchhunt.card.effect;

import java.util.HashMap;

/**
 * The <b>Action</b> class is a generic representation of a non-divisible, part of a {@link fr.utt.lo02.witchhunt.card.effect.CardEffect} action.
 */
public abstract class Action {
    /**
     * Description of the action.
     * <p>
     * Says what it does exactly as it is written on the card.
     */
    protected String description;

    /**
     * Constructor for subclasses to call.
     *
     * @param description the description of the action
     */
    public Action(String description) {
        this.description = description;
    }

    /**
     * Execute the action.
     * <p>
     * This method contains the logic necessary to the execution of the action.
     * It can use lots of other parts of the program, but mainly {@link fr.utt.lo02.witchhunt.managers} classes.
     *
     * @param callerName name of the player who plays the action
     * @param args       contains arguments depending on the needs of the action
     */
    public abstract void execute(String callerName, HashMap<String, Object> args);

    /**
     * Checks if the action can be executed.
     * <p>
     * For example, an action may need at least one player with cards in hand,
     * this method will verify that the requirement is met without modifying anything in the game.
     *
     * @param callerName name of the player who posses the card containing this action
     * @param args       contains arguments depending on the needs of the action
     * @return <code>true</code> if all requirements are met for the action to be played, <code>false</code> otherwise
     */
    public abstract boolean isExecutable(String callerName, HashMap<String, Object> args);

    /**
     * Get the action's description.
     *
     * @return the description of the action
     */
    public String getDescription() {
        return description;
    }
}
