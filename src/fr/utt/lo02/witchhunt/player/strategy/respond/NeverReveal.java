package fr.utt.lo02.witchhunt.player.strategy.respond;

import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.player.ArtificialPlayer;
import fr.utt.lo02.witchhunt.player.strategy.RespondStrategy;

import java.util.Set;

/**
 * An {@link fr.utt.lo02.witchhunt.player.ArtificialPlayer} with this strategy will try not to reveal his identity when accused.
 */
public final class NeverReveal implements RespondStrategy {
    @Override
    public void respondAccusation(ArtificialPlayer caller, String accuser) {
        tryNotToReveal(caller, accuser);
    }

    /**
     * Makes the specified {@link ArtificialPlayer} play a witch effect if possible.
     * <p>
     * This method first gets the list of playable cards of the artificial player.
     * If the list is not empty, the artificial player has to choose a card from this list to play its witch effect.
     * Otherwise, the identity of the artificial player is revealed.
     *
     * @param caller  the artificial player accused
     * @param accuser name of the player accusing the caller
     */
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
