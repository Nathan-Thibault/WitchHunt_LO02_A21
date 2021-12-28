package fr.utt.lo02.witchhunt.io;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public final class CardListener implements PropertyChangeListener {
    private final CardView view;

    public CardListener(CardView view) {
        this.view = view;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals("revealed"))
            view.update((boolean) e.getNewValue());
    }
}
