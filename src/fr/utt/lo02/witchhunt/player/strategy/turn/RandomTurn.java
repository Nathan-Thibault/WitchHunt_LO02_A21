package fr.utt.lo02.witchhunt.player.strategy.turn;

import fr.utt.lo02.witchhunt.player.ArtificialPlayer;
import fr.utt.lo02.witchhunt.player.strategy.TurnStrategy;

import java.util.Random;

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
