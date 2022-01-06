package fr.utt.lo02.witchhunt.player.strategy.respond;

import fr.utt.lo02.witchhunt.player.Identity;
import fr.utt.lo02.witchhunt.player.ArtificialPlayer;
import fr.utt.lo02.witchhunt.player.strategy.RespondStrategy;

/**
 * An {@link fr.utt.lo02.witchhunt.player.ArtificialPlayer} with this strategy will reveal his identity if he is a {@link Identity#VILLAGER}.
 * <p>
 * If the artificial player is a {@link Identity#WITCH} he will try to not reveal his identity.
 * This means that he will play a witch effect if possible (see {@link NeverReveal#tryNotToReveal(ArtificialPlayer, String)}).
 * If not, he will reveal his identity.
 */
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
