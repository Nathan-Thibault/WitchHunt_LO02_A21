package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.card.IdentityCard;

import javax.swing.SwingUtilities;
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
            case "identityCard" -> {
                IdentityCard identityCard = (IdentityCard) e.getNewValue();

                SwingUtilities.invokeLater(() -> view.updateIdentityCard(identityCard));
            }
            case "ownedCards" -> {
                @SuppressWarnings("unchecked")
                HashSet<String> ownedCards = (HashSet<String>) e.getNewValue();

                SwingUtilities.invokeLater(() -> view.updateOwnedCards(ownedCards));
            }
            case "score" -> {
                int score = (int) e.getNewValue();

                SwingUtilities.invokeLater(() -> view.updateScore(score));
            }
        }
    }
}
