package fr.utt.lo02.witchhunt.card.effect.conditions;

import fr.utt.lo02.witchhunt.player.Player;

public abstract class Condition {
    /**
     * Description of the condition.
     * <p>
     * Define the condition exactly as it is written on the card.
     */
    protected String description;

    /**
     * Constructor for subclasses to call.
     *
     * @param description the description of the condition
     */
    public Condition(String description) {
        this.description = description;
    }

    /**
     * Get the condition's description.
     *
     * @return the description of the condition
     */
    public String getDescription() {
        return description;
    }

    /**
     * Verify if the condition is met.
     *
     * @param caller name of the player who posses the card containing this condition
     * @return <code>true</code> if the condition is met, <code>false</code> otherwise
     */
    public abstract boolean verify(Player caller);
}
