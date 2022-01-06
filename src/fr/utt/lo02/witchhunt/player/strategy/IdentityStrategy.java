package fr.utt.lo02.witchhunt.player.strategy;

import fr.utt.lo02.witchhunt.player.Identity;

/**
 * A strategy for an {@link fr.utt.lo02.witchhunt.player.ArtificialPlayer} to choose is {@link Identity}.
 */
public interface IdentityStrategy extends Strategy {
    /**
     * Chooses an {@link Identity}.
     *
     * @return the identity chosen
     */
    Identity chooseIdentity();
}
