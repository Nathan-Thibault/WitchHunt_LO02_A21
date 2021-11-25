package fr.utt.lo02.witchhunt.card;

/**
 * Extending this class allows a common behavior to all cards. A card is either hidden (face down) or revealed.
 */
public abstract class Card {
    /**
     * State of the card.
     *
     * <code>true</code> the card is revealed, <code>false</code> it's hidden
     */
    protected boolean revealed;

    /**
     * Constructor for subclasses to call.
     */
    public Card() {
        revealed = false;
    }

    /**
     * Gets the state (revealed or not) of the card.
     *
     * @return <code>true</code> if the card is revealed, <code>false</code> otherwise
     */
    public boolean isRevealed() {
        return revealed;
    }

    /**
     * Sets the state (revealed or not) of the card.
     *
     * @param revealed <code>true</code> to reveal the card, <code>false</code> to un-reveal it
     */
    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }
}
