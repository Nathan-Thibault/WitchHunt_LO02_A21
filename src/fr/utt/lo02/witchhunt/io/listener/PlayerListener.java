package fr.utt.lo02.witchhunt.io.listener;

import fr.utt.lo02.witchhunt.card.IdentityCard;
import fr.utt.lo02.witchhunt.io.view.PlayerView;

import javax.swing.SwingUtilities;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;

/**
 * The <b>PlayerListener</b> class keeps it's {@link PlayerView} updated
 * by listening to the {@link fr.utt.lo02.witchhunt.player.Player} it represents.
 *
 * @see PropertyChangeListener
 */
public final class PlayerListener implements PropertyChangeListener {
    /**
     * View to keep updated.
     */
    private final PlayerView view;

    /**
     * Constructor.
     *
     * @param view view to keep updated
     */
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
