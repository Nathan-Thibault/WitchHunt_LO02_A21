package fr.utt.lo02.witchhunt.card.effect.conditions;

import fr.utt.lo02.witchhunt.player.Identity;
import fr.utt.lo02.witchhunt.player.Player;

/**
 * <b>RevealedAsVillager</b> represents the <i>"Only playable if you have
 * been revealed as a Villager."</i> condition on rumour cards.
 */
public final class RevealedAsVillager extends Condition {

    public RevealedAsVillager() {
        super("Only playable if you have\nbeen revealed as a Villager.");
    }

    @Override
    public boolean verify(Player caller) {
        return caller.getIdentityCard().getIdentity().equals(Identity.VILLAGER) && caller.getIdentityCard().isRevealed();
    }
}
