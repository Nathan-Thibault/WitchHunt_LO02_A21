package fr.utt.lo02.witchhunt.player.strategy.respond;

import fr.utt.lo02.witchhunt.player.ArtificialPlayer;

public final class NeverReveal implements RespondStrategy{
    @Override
    public void respondAccusation(ArtificialPlayer caller) {
        tryNotToReveal(caller);
    }

    public static void tryNotToReveal(ArtificialPlayer caller){
        //TODO play witch effect if possible otherwise reveal
    }
}
