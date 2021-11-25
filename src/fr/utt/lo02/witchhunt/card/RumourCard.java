package fr.utt.lo02.witchhunt.card;

import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.card.effect.EffectType;
import fr.utt.lo02.witchhunt.io.IOController;

import java.util.Objects;

/**
 * This class represents a playing card of the game.
 * <p>
 * A <b>RumourCard</b> is composed of a unique name and a {@link CardEffect} of each {@link EffectType}.
 * The class offers methods to use its effects.
 */
public final class RumourCard extends Card {

    private final String name;
    private final CardEffect witchEffect;
    private final CardEffect huntEffect;
    private final String cantChoose;

    /**
     * Constructs a new <b>RumourCard</b>.
     *
     * @param name        the unique name used to identify the card
     * @param witchEffect the {@link CardEffect} of type {@link EffectType#WITCH}
     * @param huntEffect  the {@link CardEffect} of type {@link EffectType#HUNT}
     * @param cantChoose
     * @throws NullPointerException     if the name, the witchEffect or the huntEffect is <code>null</code>
     * @throws IllegalArgumentException if the {@link EffectType} isn't corresponding for witchEffect or huntEffect
     */
    public RumourCard(String name, CardEffect witchEffect, CardEffect huntEffect, String cantChoose) throws NullPointerException, IllegalArgumentException {
        super();

        this.name = Objects.requireNonNull(name, "RumourCard constructor: name can't be null");

        if (witchEffect == null) {
            throw new NullPointerException("RumourCard constructor: witchEffect can't be null.");
        } else if (!witchEffect.getType().equals(EffectType.WITCH)) {
            throw new IllegalArgumentException("RumourCard constructor: witchEffect type isn't WITCH.");
        } else {
            this.witchEffect = witchEffect;
        }

        if (huntEffect == null) {
            throw new NullPointerException("RumourCard constructor: huntEffect can't be null.");
        } else if (!huntEffect.getType().equals(EffectType.HUNT)) {
            throw new IllegalArgumentException("RumourCard constructor: huntEffect type isn't HUNT.");
        } else {
            this.huntEffect = huntEffect;
        }

        this.cantChoose = cantChoose;
    }

    /**
     * Plays the {@link CardEffect} of type {@link EffectType#WITCH} of the <b>RumourCard</b>.
     * <p>
     * This method also reveal the card and displays a message that it has been played.
     *
     * @param callerName name of the player who posses the card
     * @param accuser    name of the player who accused the possessor of the card
     */
    public void playWitchEffect(String callerName, String accuser) {
        IOController.getInstance().printInfo(callerName.concat(" played ").concat(name).concat(":\n").concat(witchEffectDescription()));
        setRevealed(true);
        witchEffect.play(callerName, accuser);
    }

    /**
     * Plays the {@link CardEffect} of type {@link EffectType#HUNT} of the <b>RumourCard</b>.
     * <p>
     * This method also reveal the card and displays a message that it has been played.
     *
     * @param callerName name of the player who posses the card
     */
    public void playHuntEffect(String callerName) {
        IOController.getInstance().printInfo(callerName.concat(" played ").concat(name).concat(":\n").concat(huntEffectDescription()));
        setRevealed(true);
        huntEffect.play(callerName);
    }

    /**
     * Checks if the {@link CardEffect} of type {@link EffectType#WITCH} of the <b>RumourCard</b> can be played.
     *
     * @param callerName name of the player who posses the card
     * @param accuser    name of the player who accused the possessor of the card
     * @return <code>true</code> if it can be played, <code>false</code> otherwise
     */
    public boolean canPlayWitchEffect(String callerName, String accuser) {
        return witchEffect.isPlayable(callerName, accuser);
    }

    /**
     * Checks if the {@link CardEffect} of type {@link EffectType#HUNT} of the <b>RumourCard</b> can be played.
     *
     * @param callerName name of the player who posses the card
     * @return <code>true</code> if it can be played, <code>false</code> otherwise
     */
    public boolean canPlayHuntEffect(String callerName) {
        return huntEffect.isPlayable(callerName);
    }

    /**
     * Gets the description of the witch effect of the <b>RumourCard</b>
     *
     * @return the description of the {@link CardEffect} of type {@link EffectType#WITCH}
     */
    public String witchEffectDescription() {
        return witchEffect.getDescription();
    }

    /**
     * Gets the description of the hunt effect of the <b>RumourCard</b>
     *
     * @return the description of the {@link CardEffect} of type {@link EffectType#HUNT}
     */
    public String huntEffectDescription() {
        return huntEffect.getDescription();
    }

    /**
     * @return
     */
    public String getCantChose() {
        //TODO: create verification using this when playing card
        if (revealed) {
            return cantChoose;
        }
        return null;
    }
}
