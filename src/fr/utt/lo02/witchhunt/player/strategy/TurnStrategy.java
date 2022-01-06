package fr.utt.lo02.witchhunt.player.strategy;

import fr.utt.lo02.witchhunt.player.ArtificialPlayer;

/**
 * A strategy for an {@link ArtificialPlayer} to play a turn.
 */
public interface TurnStrategy extends Strategy {
    /**
     * Makes the {@link ArtificialPlayer} specified play his turn.
     * <p>
     * This method has to either make the artificial player accuse another player or make him play a hunt effect.
     *
     * @param caller the artificial player who has to play
     */
    void playTurn(ArtificialPlayer caller);
}
