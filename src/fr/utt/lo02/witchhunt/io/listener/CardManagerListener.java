package fr.utt.lo02.witchhunt.io.listener;

import fr.utt.lo02.witchhunt.io.GUI;

import javax.swing.SwingUtilities;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;

/**
 * The <b>CardManagerListener</b> class keeps a {@link GUI} updated
 * by listening to the {@link fr.utt.lo02.witchhunt.managers.CardManager}.
 *
 * @see PropertyChangeListener
 */
public final class CardManagerListener implements PropertyChangeListener {
    /**
     * GUI to keep updated.
     */
    private final GUI gui;

    /**
     * Constructor.
     *
     * @param gui gui to keep updated
     */
    public CardManagerListener(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        switch (propertyName) {
            case "cardsCreated" -> {
                if ((boolean) evt.getNewValue())
                    SwingUtilities.invokeLater(gui::createCardViews);
            }
            case "discardedCards" -> {
                @SuppressWarnings("unchecked")
                HashSet<String> discardedCards = (HashSet<String>) evt.getNewValue();

                SwingUtilities.invokeLater(() -> gui.updateDiscarded(discardedCards));
            }
        }
    }
}
