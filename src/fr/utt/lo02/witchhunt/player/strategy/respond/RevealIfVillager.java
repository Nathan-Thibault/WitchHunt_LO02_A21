package fr.utt.lo02.witchhunt.player.strategy.respond;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.player.ArtificialPlayer;

public class RevealIfVillager implements RespondStrategy{
    @Override
    public void respondAccusation(ArtificialPlayer caller) {
        if (caller.getIdentity() == Identity.VILLAGER){
            caller.revealIdentity();
        } else {
            NeverReveal.tryNotToReveal(caller);
        }
    }
}
