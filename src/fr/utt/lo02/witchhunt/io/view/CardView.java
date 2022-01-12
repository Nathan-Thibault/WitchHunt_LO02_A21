package fr.utt.lo02.witchhunt.io.view;

import fr.utt.lo02.witchhunt.player.Identity;
import fr.utt.lo02.witchhunt.card.Card;
import fr.utt.lo02.witchhunt.card.IdentityCard;
import fr.utt.lo02.witchhunt.card.RumourCard;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

/**
 * A <b>CardView</b> is a graphical representation of a {@link Card}.
 * <p>
 * To stay updated when the {@link Card} it's representing changes,
 * it must be combined with a {@link fr.utt.lo02.witchhunt.io.listener.CardListener}.
 */
public final class CardView extends JButton {
    /**
     * Image for the back of a card.
     */
    private static BufferedImage imgBack;

    static {
        try {
            //load back image
            imgBack = ImageIO.read(Objects.requireNonNull(CardView.class.getResource("/resources/images/card_back_face.png")));
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Image for the front of the card.
     */
    private BufferedImage imgFront;
    /**
     * Icon representing the front of the card.
     */
    private ImageIcon frontFace;
    /**
     * Icon representing the back of the card.
     */
    private ImageIcon backFace;
    /**
     * Tells if the card is revealed or not.
     */
    private boolean revealed;

    /**
     * Constructor.
     *
     * @param card the card to make a graphical representation of
     */
    public CardView(Card card) {
        super();
        String cardName;
        int height;

        if (card instanceof IdentityCard) {
            cardName = "identity_".concat(((IdentityCard) card).getIdentity().equals(Identity.WITCH) ? "witch" : "villager");
            height = 50;
        } else {
            cardName = ((RumourCard) card).getName().toLowerCase(Locale.ROOT).replace(" ", "_");
            height = 100;
        }

        try {
            //load front image
            imgFront = ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/images/card_" + cardName + ".png")));

            //resize
            Image imgF = imgFront.getScaledInstance(imgFront.getWidth() * height / imgFront.getHeight(), height, Image.SCALE_SMOOTH);
            Image imgB = imgBack.getScaledInstance(imgBack.getWidth() * height / imgBack.getHeight(), height, Image.SCALE_SMOOTH);

            frontFace = new ImageIcon(imgF);
            backFace = new ImageIcon(imgB);
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }

        setMargin(new Insets(2, 2, 2, 2));

        //popup with card in bigger size when clicking on it
        addActionListener(e -> {
            BufferedImage img = revealed ? imgFront : imgBack;
            Image resizedImg = img.getScaledInstance(img.getWidth() * 600 / img.getHeight(), 600, Image.SCALE_SMOOTH);
            JOptionPane.showMessageDialog(null, new ImageIcon(resizedImg));
        });

        update(card.isRevealed());
    }

    /**
     * Copies an existing <b>CardView</b> but modify the height of the icons.
     *
     * @param model  the card view to copy
     * @param height the new height of the icons
     */
    public CardView(CardView model, int height) {
        this.imgFront = model.imgFront;
        this.revealed = model.revealed;

        Image imgF = imgFront.getScaledInstance(imgFront.getWidth() * height / imgFront.getHeight(), height, Image.SCALE_SMOOTH);
        Image imgB = imgBack.getScaledInstance(imgBack.getWidth() * height / imgBack.getHeight(), height, Image.SCALE_SMOOTH);

        frontFace = new ImageIcon(imgF);
        backFace = new ImageIcon(imgB);

        setMargin(new Insets(2, 2, 2, 2));

        //popup with card in bigger size when clicking on it
        JButton parent = this;
        addActionListener(e -> {
            BufferedImage img = revealed ? imgFront : imgBack;
            Image resizedImg = img.getScaledInstance(img.getWidth() * 600 / img.getHeight(), 600, Image.SCALE_SMOOTH);
            JOptionPane.showMessageDialog(parent, new ImageIcon(resizedImg));
        });
    }

    /**
     * Updates the view with the specified state.
     *
     * @param revealed <code>true</code> to show the front icon, <code>false</code> to show the back icon
     */
    public void update(boolean revealed) {
        this.revealed = revealed;
        setIcon(revealed ? frontFace : backFace);
    }
}
