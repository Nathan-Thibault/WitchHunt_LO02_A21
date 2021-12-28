package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.card.IdentityCard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;

public final class PlayerListener implements PropertyChangeListener {
    private final PlayerView view;

    public PlayerListener(PlayerView view) {
        this.view = view;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        switch (propertyName) {
            case "identityCard" -> view.updateIdentityCard((IdentityCard) e.getNewValue());
            case "ownedCards" -> view.updateOwnedCards((HashSet<String>) e.getNewValue());
            case "score" -> view.updateScore((int) e.getNewValue());
        }
    }
}
