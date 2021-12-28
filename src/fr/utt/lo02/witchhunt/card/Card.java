package fr.utt.lo02.witchhunt.card;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

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

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

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
     * @param newRevealed <code>true</code> to reveal the card, <code>false</code> to un-reveal it
     */
    public void setRevealed(boolean newRevealed) {
        boolean oldRevealed = revealed;
        revealed = newRevealed;
        pcs.firePropertyChange("revealed", oldRevealed, revealed);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
