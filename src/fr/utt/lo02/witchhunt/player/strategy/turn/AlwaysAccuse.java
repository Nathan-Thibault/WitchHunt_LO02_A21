package fr.utt.lo02.witchhunt.player.strategy.turn;

import fr.utt.lo02.witchhunt.player.ArtificialPlayer;

public final class AlwaysAccuse implements TurnStrategy {
    @Override
    public void playTurn(ArtificialPlayer caller) {
        caller.accuseSomeone();
    }
}
