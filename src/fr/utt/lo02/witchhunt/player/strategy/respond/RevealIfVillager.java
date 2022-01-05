package fr.utt.lo02.witchhunt.player.strategy.respond;

import fr.utt.lo02.witchhunt.player.Identity;
import fr.utt.lo02.witchhunt.player.ArtificialPlayer;

public final class RevealIfVillager implements RespondStrategy {
    @Override
    public void respondAccusation(ArtificialPlayer caller, String accuser) {
        if (caller.getIdentityCard().getIdentity().equals(Identity.VILLAGER)) {
            caller.revealIdentity(accuser);
        } else {
            NeverReveal.tryNotToReveal(caller, accuser);
        }
    }
}
