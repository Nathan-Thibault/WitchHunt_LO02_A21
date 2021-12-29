package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.card.Card;
import fr.utt.lo02.witchhunt.card.IdentityCard;
import fr.utt.lo02.witchhunt.card.RumourCard;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public final class CardView extends JButton implements PropertyChangeListener {
    private ImageIcon frontFace;
    private ImageIcon backFace;

    public CardView(Card card) {
        super();
        String cardName;
        int height;

        if (card instanceof IdentityCard) {
            cardName = "identity_".concat(((IdentityCard) card).getIdentity().equals(Identity.WITCH) ? "witch" : "villager");
            height = 100;
        } else {
            cardName = ((RumourCard) card).getName().toLowerCase(Locale.ROOT).replace(" ", "_");
            height = 200;
        }

        try {
            //load images
            BufferedImage buffImgF = ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/images/card_" + cardName + ".png")));
            BufferedImage buffImgB = ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/images/card_back_face.png")));

            //resize
            Image imgF = buffImgF.getScaledInstance(buffImgF.getWidth() * height / buffImgF.getHeight(), height, Image.SCALE_SMOOTH);
            Image imgB = buffImgB.getScaledInstance(buffImgB.getWidth() * height / buffImgB.getHeight(), height, Image.SCALE_SMOOTH);

            frontFace = new ImageIcon(imgF);
            backFace = new ImageIcon(imgB);
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }

        this.setIcon(backFace);

        card.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals("revealed"))
            this.setIcon((boolean) e.getNewValue() ? frontFace : backFace);
    }
}
