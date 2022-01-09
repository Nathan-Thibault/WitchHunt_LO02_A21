package fr.utt.lo02.witchhunt.player.strategy.turn;

import fr.utt.lo02.witchhunt.player.ArtificialPlayer;
import fr.utt.lo02.witchhunt.player.strategy.TurnStrategy;

/**
 * An {@link fr.utt.lo02.witchhunt.player.ArtificialPlayer} with this strategy will always accuse another player when it's his turn.
 */
public final class AlwaysAccuse implements TurnStrategy {
    @Override
    public void playTurn(ArtificialPlayer caller) {
        caller.accuseSomeone();
    }
}
