package fr.utt.lo02.witchhunt.player.strategy.respond;

import fr.utt.lo02.witchhunt.player.ArtificialPlayer;
import fr.utt.lo02.witchhunt.player.strategy.Strategy;

public interface RespondStrategy extends Strategy {
    void respondAccusation(ArtificialPlayer caller, String accuser);
}
