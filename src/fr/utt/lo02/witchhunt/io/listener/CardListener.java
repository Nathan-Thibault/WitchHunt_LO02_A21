package fr.utt.lo02.witchhunt.io.listener;

import fr.utt.lo02.witchhunt.io.view.CardView;

import javax.swing.SwingUtilities;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public final class CardListener implements PropertyChangeListener {
    private final CardView view;

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
