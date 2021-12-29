package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.card.Card;
import fr.utt.lo02.witchhunt.card.IdentityCard;
import fr.utt.lo02.witchhunt.card.RumourCard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public final class CardView extends JButton implements PropertyChangeListener {
    private static BufferedImage imgBack;
    static {
        try {
            //load back image
            imgBack = ImageIO.read(Objects.requireNonNull(CardView.class.getResource("/resources/images/card_back_face.png")));
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }
    private BufferedImage imgFront;
    private ImageIcon frontFace;
    private ImageIcon backFace;

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

        setIcon(backFace);
        setMargin(new Insets(3, 3, 3, 3));

        //popup with card in bigger size when clicking on it
        JButton parent = this;
        addActionListener(e -> {
            BufferedImage img = card.isRevealed() ? imgFront : imgBack;
            Image resizedImg = img.getScaledInstance(img.getWidth() * 600 / img.getHeight(), 600, Image.SCALE_SMOOTH);
            JOptionPane.showMessageDialog(parent, new ImageIcon(resizedImg));
        });

        card.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals("revealed"))
            setIcon((boolean) e.getNewValue() ? frontFace : backFace);
    }
}
