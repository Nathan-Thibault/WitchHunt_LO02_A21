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
        CardManager cManager = CardManager.getInstance();

        ArrayList<String> playableCards = caller.getHand();

        //try to play witch effect of every card in hand until there's one that works
        while (!playableCards.isEmpty()) {
            String card = caller.chooseCardFrom(playableCards);
            if (cManager.getByName(card).playWitchEffect(PlayerManager.getInstance().getByPlayer(caller), accuser)) {
                return;
            } else {
                playableCards.remove(card);
            }
        }
        //no card could have been played -> reveal
        caller.revealIdentity(accuser);
    }
}
