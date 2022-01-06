package fr.utt.lo02.witchhunt.player.strategy;

import fr.utt.lo02.witchhunt.player.ArtificialPlayer;

public interface TurnStrategy extends Strategy {
    void playTurn(ArtificialPlayer caller);
}
