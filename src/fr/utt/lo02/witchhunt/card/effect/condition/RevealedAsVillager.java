package fr.utt.lo02.witchhunt.card.effect.condition;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.player.Player;

public final class RevealedAsVillager extends Condition {

    public RevealedAsVillager() {
        super("Only playable if you have\nbeen revealed as a Villager.");
    }

    @Override
    public boolean verify(Player caller) {
        return caller.getIdentityCard().getIdentity().equals(Identity.VILLAGER) && caller.getIdentityCard().isRevealed();
    }
}
