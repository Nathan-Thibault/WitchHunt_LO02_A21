package fr.utt.lo02.witchhunt.player.strategy.respond;

import fr.utt.lo02.witchhunt.player.ArtificialPlayer;

public final class AlwaysReveal implements RespondStrategy {
    @Override
    public void respondAccusation(ArtificialPlayer caller, String accuser) {
        caller.revealIdentity(accuser);
    }
}