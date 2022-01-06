package fr.utt.lo02.witchhunt.player.strategy.respond;

import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.player.ArtificialPlayer;
import fr.utt.lo02.witchhunt.player.strategy.RespondStrategy;

import java.util.Set;

public final class NeverReveal implements RespondStrategy {
    @Override
    public void respondAccusation(ArtificialPlayer caller, String accuser) {
        tryNotToReveal(caller, accuser);
    }

    public static void tryNotToReveal(ArtificialPlayer caller, String accuser) {
        Set<String> playableCards = caller.getPlayableCards(accuser);

        while (!playableCards.isEmpty()) {//player has cards to play
            String card = caller.chooseCardFrom(playableCards);

            CardManager.getInstance().getByName(card).playWitchEffect(caller.getName(), accuser);
        }
        //no card can be played -> reveal
        caller.revealIdentity(accuser);
    }
}
