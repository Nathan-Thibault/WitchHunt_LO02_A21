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
    private final JPanel topLeft;
    private final JPanel topRight;
    private final JPanel top;
    private final JPanel cards;

    public PlayerView(Player player) {
        super();
        topLeft = new JPanel();
        topRight = new JPanel();
        top = new JPanel();
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
        topLeft.add(identityNotSet);

        //hand
        cards.setLayout(new FlowLayout());

        topRight.setLayout(new BoxLayout(topRight, BoxLayout.Y_AXIS));
        topRight.add(nameLabel);
        topRight.add(scoreLabel);

        top.setLayout(new FlowLayout());
        top.add(topLeft);
        top.add(topRight);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(top);
        add(cards);
        setBorder(new LineBorder(new Color(125, 125, 125)));

        player.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        switch (propertyName) {
            case "identityCard" -> {
                IdentityCard identityCard = (IdentityCard) e.getNewValue();
                topLeft.removeAll();
                if (identityCard != null) {
                    topLeft.add(new CardView(identityCard));
                } else {
                    JLabel identityNotSet = new JLabel("Identity not yet chosen.");
                    topLeft.add(identityNotSet);
                }
                topLeft.invalidate();
                topLeft.validate();
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
