package fr.utt.lo02.witchhunt.io.listener;

import fr.utt.lo02.witchhunt.io.GUI;

import javax.swing.SwingUtilities;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;

public final class CardManagerListener implements PropertyChangeListener {
    private final GUI gui;

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
