package fr.utt.lo02.witchhunt.player.strategy.respond;

import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.player.ArtificialPlayer;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.ArrayList;

public final class NeverReveal implements RespondStrategy {
    @Override
    public void respondAccusation(ArtificialPlayer caller, String accuser) {
        tryNotToReveal(caller, accuser);
    }

    public static void tryNotToReveal(ArtificialPlayer caller, String accuser) {
        ArrayList<String> playableCards = caller.getPlayableCards(accuser);

        while (!playableCards.isEmpty()) {//player has cards to play
            String card = caller.chooseCardFrom(playableCards);

            CardManager.getInstance().getByName(card).playWitchEffect(PlayerManager.getInstance().getByPlayer(caller), accuser);
        }
        //no card can be played -> reveal
        caller.revealIdentity(accuser);
    }
}
