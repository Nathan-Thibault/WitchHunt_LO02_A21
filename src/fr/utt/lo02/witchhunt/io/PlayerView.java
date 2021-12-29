package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.card.IdentityCard;
import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.player.Player;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.border.LineBorder;
import java.awt.Component;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

public final class PlayerView extends JPanel implements PropertyChangeListener {

    private final JLabel scoreLabel;
    private final JPanel identityPanel;
    private final JPanel cardsPanel;

    public PlayerView(Player player) {
        super();
        identityPanel = new JPanel();
        scoreLabel = new JLabel();
        cardsPanel = new JPanel();

        //name
        JLabel nameLabel = new JLabel();
        nameLabel.setText(player.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(30F));//set font size

        //score
        scoreLabel.setText("Score: 0");

        //identity
        JLabel identityNotSet = new JLabel("Identity not yet chosen.");
        identityPanel.add(identityNotSet);

        //hand
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.X_AXIS));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(nameLabel);
        add(scoreLabel);
        add(identityPanel);
        add(cardsPanel);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setBorder(new LineBorder(new Color(125, 125, 125)));

        player.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        switch (propertyName) {
            case "identityCard" -> {
                IdentityCard identityCard = (IdentityCard) e.getNewValue();
                identityPanel.removeAll();
                if (identityCard != null) {
                    identityPanel.add(new CardView(identityCard));
                } else {
                    JLabel identityNotSet = new JLabel("Identity not yet chosen.");
                    identityPanel.add(identityNotSet);
                }
                identityPanel.invalidate();
                identityPanel.validate();
            }
            case "ownedCards" -> {
                CardManager cManager = CardManager.getInstance();
                @SuppressWarnings("unchecked")
                Set<String> ownedCards = (Set<String>) e.getNewValue();

                cardsPanel.removeAll();
                for (String cardName : ownedCards) {
                    cardsPanel.add(cManager.getByName(cardName).getCardView());
                }
                cardsPanel.invalidate();
                cardsPanel.validate();
            }
            case "score" -> scoreLabel.setText("Score: " + e.getNewValue());
        }
    }
}
