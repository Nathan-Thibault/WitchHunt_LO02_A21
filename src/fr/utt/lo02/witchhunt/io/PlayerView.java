package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.card.IdentityCard;
import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.player.Player;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.border.LineBorder;
import java.util.HashSet;

public final class PlayerView extends JPanel {

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
    }

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

    public void updateOwnedCards(HashSet<String> ownedCards) {
        CardManager cManager = CardManager.getInstance();

        cards.removeAll();
        for (String cardName : ownedCards) {
            cards.add(cManager.getByName(cardName).getCardView());
        }
        cards.invalidate();
        cards.validate();
    }

    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }
}
