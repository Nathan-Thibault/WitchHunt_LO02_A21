package fr.utt.lo02.witchhunt.card;

import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.card.effect.EffectType;
import fr.utt.lo02.witchhunt.io.IOController;

public final class RumourCard extends Card {

    private final String name;
    private final CardEffect witchEffect;
    private final CardEffect huntEffect;
    private final String cantBeChosenBy;

    public RumourCard(String name, CardEffect witchEffect, CardEffect huntEffect, String cantBeChosenBy) {
        super(false);

        this.name = name;

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

        this.cantBeChosenBy = cantBeChosenBy;
    }

    public void playWitchEffect(String callerName, String accuser) {
        IOController.getInstance().printInfo(callerName.concat(" played ").concat(name).concat(":\n").concat(witchEffect.getDescription()));
        setRevealed(true);
        witchEffect.play(callerName, accuser);
    }

    public void playHuntEffect(String callerName) {
        IOController.getInstance().printInfo(callerName.concat(" played ").concat(name).concat(":\n").concat(huntEffect.getDescription()));
        setRevealed(true);
        huntEffect.play(callerName);
    }

    public boolean canPlayWitchEffect(String callerName, String accuser) {
        return witchEffect.isPlayable(callerName, accuser);
    }

    public boolean canPlayHuntEffect(String callerName) {
        return huntEffect.isPlayable(callerName);
    }

    public String getCantBeChosenBy() {
        //TODO: create verification using this when playing cards
        if (revealed) {
            return cantBeChosenBy;
        }
        return null;
    }
}
