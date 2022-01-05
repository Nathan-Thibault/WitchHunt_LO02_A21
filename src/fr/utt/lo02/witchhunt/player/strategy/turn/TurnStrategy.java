package fr.utt.lo02.witchhunt.player.strategy.turn;

import fr.utt.lo02.witchhunt.player.ArtificialPlayer;
import fr.utt.lo02.witchhunt.player.strategy.Strategy;

public interface TurnStrategy extends Strategy {
    void playTurn(ArtificialPlayer caller);
}
