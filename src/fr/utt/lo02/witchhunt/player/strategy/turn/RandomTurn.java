package fr.utt.lo02.witchhunt.player.strategy.turn;

import fr.utt.lo02.witchhunt.player.ArtificialPlayer;
import fr.utt.lo02.witchhunt.player.strategy.TurnStrategy;

import java.util.Random;

/**
 * An {@link fr.utt.lo02.witchhunt.player.ArtificialPlayer} with this strategy
 * will randomly try to play a hunt effect or accuse another player when it's his turn.
 *
 * @see PlayHunt#tryToPlayHunt(ArtificialPlayer)
 */
public final class RandomTurn implements TurnStrategy {
    @Override
    public void playTurn(ArtificialPlayer caller) {
        Random random = new Random();

        if (random.nextBoolean()) {
            caller.accuseSomeone();
        } else {
            PlayHunt.tryToPlayHunt(caller);
        }
    }
}
