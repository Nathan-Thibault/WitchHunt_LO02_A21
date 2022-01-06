package fr.utt.lo02.witchhunt.player.strategy;

import fr.utt.lo02.witchhunt.player.ArtificialPlayer;

public interface RespondStrategy extends Strategy {
    void respondAccusation(ArtificialPlayer caller, String accuser);
}
