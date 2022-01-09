package fr.utt.lo02.witchhunt.player.strategy.turn;

import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.player.ArtificialPlayer;
import fr.utt.lo02.witchhunt.player.strategy.TurnStrategy;

import java.util.Set;

/**
 * An {@link fr.utt.lo02.witchhunt.player.ArtificialPlayer} with this strategy will always try to play a hunt effect when it's his turn.
 */
public final class PlayHunt implements TurnStrategy {
    @Override
    public void playTurn(ArtificialPlayer caller) {
        tryToPlayHunt(caller);
    }

    /**
     * Makes the specified {@link ArtificialPlayer} play a hunt effect if possible.
     * <p>
     * This method first gets the list of playable cards of the artificial player.
     * If the list is not empty, the artificial player has to choose a card from this list to play its hunt effect.
     * Otherwise, the artificial player has to accuse someone.
     *
     * @param caller the artificial player whose turn to play
     */
    public static void tryToPlayHunt(ArtificialPlayer caller) {
        Set<String> playableCards = caller.getPlayableCards();

        while (!playableCards.isEmpty()) {//player has cards to play
            String card = caller.chooseCardFrom(playableCards);

            CardManager.getInstance().getByName(card).playHuntEffect(caller.getName());
        }
        //no card can be played -> accuse
        caller.accuseSomeone();
    }
}
