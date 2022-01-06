package fr.utt.lo02.witchhunt.player.strategy;

import fr.utt.lo02.witchhunt.player.ArtificialPlayer;

/**
 * A strategy for an {@link ArtificialPlayer} to respond an accusation.
 */
public interface RespondStrategy extends Strategy {
    /**
     * Makes the {@link ArtificialPlayer} specified respond to an accusation.
     * <p>
     * This method has to either reveal the artificial player identity or make him play a witch effect.
     *
     * @param caller  the artificial player accused
     * @param accuser name of the player accusing the caller
     */
    void respondAccusation(ArtificialPlayer caller, String accuser);
}
