package fr.utt.lo02.witchhunt.io.listener;

import fr.utt.lo02.witchhunt.io.view.CardView;

import javax.swing.SwingUtilities;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The <b>CardListener</b> class keeps it's {@link CardView} updated
 * by listening to the {@link fr.utt.lo02.witchhunt.card.Card} it represents.
 *
 * @see PropertyChangeListener
 */
public final class CardListener implements PropertyChangeListener {
    /**
     * View to keep updated.
     */
    private final CardView view;

    /**
     * Constructor.
     *
     * @param view view to keep updated
     */
    public CardListener(CardView view) {
        this.view = view;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals("revealed")) {
            boolean revealed = (boolean) e.getNewValue();

            SwingUtilities.invokeLater(() -> view.update(revealed));
        }
    }
}
