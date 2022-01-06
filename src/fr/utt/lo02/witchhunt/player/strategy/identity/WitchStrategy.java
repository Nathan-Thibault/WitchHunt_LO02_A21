package fr.utt.lo02.witchhunt.player.strategy.identity;

import fr.utt.lo02.witchhunt.player.Identity;
import fr.utt.lo02.witchhunt.player.strategy.IdentityStrategy;

/**
 * An {@link fr.utt.lo02.witchhunt.player.ArtificialPlayer} with this strategy will choose to be a {@link Identity#WITCH}.
 */
public final class WitchStrategy implements IdentityStrategy {
    @Override
    public Identity chooseIdentity() {
        return Identity.WITCH;
    }
}
