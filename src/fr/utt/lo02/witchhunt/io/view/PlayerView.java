package fr.utt.lo02.witchhunt.io.view;

import fr.utt.lo02.witchhunt.card.IdentityCard;
import fr.utt.lo02.witchhunt.io.listener.CardListener;
import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.player.Player;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.border.LineBorder;
import java.util.HashSet;

/**
 * A <b>PlayerView</b> is a graphical representation of a {@link Player}.
 * <p>
 * To stay updated when the {@link Player} it's representing changes,
 * it must be combined with a {@link fr.utt.lo02.witchhunt.io.listener.PlayerListener}.
 */
public final class PlayerView extends JPanel {
    /**
     * Label to display the score.
     */
    private final JLabel scoreLabel;
    /**
     * Panel containing the representation of the identity card.
     */
    private final JPanel identity;
    /**
     * Panel containing the representation of the owned cards.
     */
    private final JPanel cards;

    /**
     * Constructor.
     *
     * @param player the player to make a graphical representation of
     */
    public PlayerView(Player player) {
        super();
        identity = new JPanel();
        scoreLabel = new JLabel();
        cards = new JPanel();

        //name
        JLabel nameLabel = new JLabel();
        nameLabel.setText(player.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(30F));//set font size

        //score
        scoreLabel.setText("Score: 0");

        //identity
        JLabel identityNotSet = new JLabel("Identity not yet chosen.");
        identity.add(identityNotSet);

        //hand
        cards.setLayout(new FlowLayout());

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(nameLabel);
        top.add(scoreLabel);

        BorderLayout layout = new BorderLayout();
        layout.setHgap(20);
        setLayout(layout);
        add(identity, BorderLayout.WEST);
        add(top, BorderLayout.NORTH);
        add(cards, BorderLayout.CENTER);
        setBorder(new LineBorder(new Color(125, 125, 125)));
    }

    /**
     * Updates the representation of the identity card of the <b>PlayerView</b>.
     *
     * @param identityCard new identity card
     */
    public void updateIdentityCard(IdentityCard identityCard) {
        identity.removeAll();
        if (identityCard != null) {
            CardView cardView = new CardView(identityCard);
            identityCard.addPropertyChangeListener(new CardListener(cardView));

            identity.add(cardView);
        } else {
            JLabel identityNotSet = new JLabel("Identity not yet chosen.");
            identity.add(identityNotSet);
        }
        identity.invalidate();
        identity.validate();
    }

    /**
     * Updates the representation of the owned cards of the <b>PlayerView</b>.
     *
     * @param ownedCards new set of the names of owned cards
     */
    public void updateOwnedCards(HashSet<String> ownedCards) {
        CardManager cManager = CardManager.getInstance();

        cards.removeAll();
        for (String cardName : ownedCards) {
            cards.add(cManager.getByName(cardName).getCardView());
        }
        cards.invalidate();
        cards.validate();
    }

    /**
     * Updates the display for the score of the <b>PlayerView</b>.
     *
     * @param score new score
     */
    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }
}
