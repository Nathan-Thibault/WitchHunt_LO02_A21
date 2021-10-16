package fr.utt.lo02.witchhunt.card;

import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.card.effect.EffectType;
import fr.utt.lo02.witchhunt.player.Player;

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

    public boolean playEffect(EffectType type, Player caller) throws IllegalArgumentException{
        switch (type){
            case WITCH -> {
                return witchEffect.play(caller);
            }
            case HUNT -> {
                return huntEffect.play(caller);
            }
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
