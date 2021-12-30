package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.card.IdentityCard;
import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.player.Player;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

public final class PlayerView extends JPanel implements PropertyChangeListener {

    private final JLabel scoreLabel;
    private final JPanel identity;
    private final JPanel cards;

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

        player.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        switch (propertyName) {
            case "identityCard" -> {
                IdentityCard identityCard = (IdentityCard) e.getNewValue();
                identity.removeAll();
                if (identityCard != null) {
                    identity.add(new CardView(identityCard));
                } else {
                    JLabel identityNotSet = new JLabel("Identity not yet chosen.");
                    identity.add(identityNotSet);
                }
                identity.invalidate();
                identity.validate();
            }
            case "ownedCards" -> {
                CardManager cManager = CardManager.getInstance();
                @SuppressWarnings("unchecked")
                Set<String> ownedCards = (Set<String>) e.getNewValue();

                cards.removeAll();
                for (String cardName : ownedCards) {
                    cards.add(cManager.getByName(cardName).getCardView());
                }
                cards.invalidate();
                cards.validate();
            }
            case "score" -> scoreLabel.setText("Score: " + e.getNewValue());
        }
    }
}
