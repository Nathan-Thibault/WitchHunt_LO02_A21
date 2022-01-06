package fr.utt.lo02.witchhunt.player.strategy.turn;

import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.player.ArtificialPlayer;
import fr.utt.lo02.witchhunt.player.strategy.TurnStrategy;

import java.util.Set;

public final class PlayHunt implements TurnStrategy {
    @Override
    public void playTurn(ArtificialPlayer caller) {
        tryToPlayHunt(caller);
    }

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
