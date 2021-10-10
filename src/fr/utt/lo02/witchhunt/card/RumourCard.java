package fr.utt.lo02.witchhunt.card;

import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.card.effect.EffectType;

public final class RumourCard extends Card {

    private final CardEffect witchEffect;
    private final CardEffect huntEffect;
    private final String cantBeChosenBy;

    public RumourCard(CardEffect witchEffect, CardEffect huntEffect, String cantBeChosenBy) {
        super(false);

        if (witchEffect == null) {
            throw new NullPointerException("RumourCard constructor: witchEffect can't be null.");
        } else if(witchEffect.getType() != EffectType.WITCH) {
            throw new IllegalArgumentException("RumourCard constructor: witchEffect type isn't WITCH.");
        } else {
            this.witchEffect = witchEffect;
        }

        if (huntEffect == null) {
            throw new NullPointerException("RumourCard constructor: huntEffect can't be null.");
        } else if(huntEffect.getType() != EffectType.HUNT) {
            throw new IllegalArgumentException("RumourCard constructor: huntEffect type isn't HUNT.");
        } else {
            this.huntEffect = huntEffect;
        }

        this.cantBeChosenBy = cantBeChosenBy;
    }

    @Override
    public void reveal() {
        revealed = true;
    }

    public void playEffect(EffectType type){
        switch (type){
            case WITCH -> witchEffect.play();
            case HUNT -> huntEffect.play();
            default -> throw new IllegalArgumentException("RumourCard playEffect: type unknown");
        }
    }

    public String getCantBeChosenBy(){
        if(revealed) {
            return cantBeChosenBy;
        }
        return null;
    }
}
